package es.miguelromeral.password.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.classes.Password
import java.util.HashMap
import kotlin.random.Random

class GameViewModel(
    val category: String,
    val level: String,
    val language: String) : ViewModel() {

    private val _text = MutableLiveData<String>("Password!")
    val text: LiveData<String> = _text

    private val _listOfWords = MutableLiveData<List<Password>>()
    val listOfWords = _listOfWords

    private val _currentIndex = MutableLiveData<Int>(DEFAULT_INDEX)
    val currentIndex = _currentIndex

    private val _nFails = MutableLiveData<Int>(0)
    val nFails = _nFails

    private val _nSuccess = MutableLiveData<Int>(0)
    val nSuccess = _nSuccess

    private var _countdownInt = VALUE_NOT_STARTED
    private var _countdown = MutableLiveData<Int>()
    val countdown = _countdown

    private val _gameFinished = MutableLiveData<Boolean>(false)
    val gameFinished = _gameFinished

    private lateinit var timer: CountDownTimer

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var mFirestore: FirebaseFirestore

    private var timestamp: Long = 0

    init{
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        val ref = mFirestore.collection(COLL_PASSWORD)
        Log.i(TAG, "cat: $category, lev: $level, lan: $language")

        /*
        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912
        )

        // Add a new document with a generated ID
        mFirestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
*/

        var listRetrieved = mutableListOf<Password>()

        var query: Query? = null

        if(!level.equals(Options.DEFAULT_LEVEL, true)){
            query = (query ?: ref).whereEqualTo(FIELD_LEVEL, level.toLowerCase())
        }
        if(!category.equals(Options.DEFAULT_CATEGORY, true)){
            query = (query ?: ref).whereEqualTo(FIELD_CATEGORY, category.toLowerCase())
        }
        if(query == null)
            query = ref

        query
            .limit(DEFAULT_MAX_WORDS)
            .get()
            .addOnSuccessListener {documents ->
                try {
                    if (documents != null) {
                        for (document in documents) {
                            var obj = document.toObject(Password::class.java)
                            listRetrieved.add(obj)
                        }
                        Log.d(TAG, "DocumentSnapshot read successfully!")

                        _listOfWords.postValue(listRetrieved.shuffled())
                        _currentIndex.postValue(0)
                        initTimer()

                    } else {
                        Log.d(TAG, "No such document!")

                        _currentIndex.postValue(DEFAULT_INDEX)
                    }
                }catch (ex: Exception){
                    Log.e(TAG, ex.message)
                }
            }

    }

    /*fun initSettings(){
        initTimer()
    }*/


    private fun initTimer(){
        _countdownInt = (DEFAULT_WAIT / ONE_SECOND).toInt()
        _countdown.postValue(_countdownInt)
        timer = object : CountDownTimer(DEFAULT_WAIT, ONE_SECOND){
            override fun onTick(milisUtilFinished: Long){
                _countdownInt -= 1
                _countdown.postValue(_countdownInt)
            }

            override fun onFinish() {
                _countdownInt = VALUE_FINISHED
                _countdown.postValue(0)
                finishGame()
            }
        }
        timestamp = System.currentTimeMillis()
        timer.start()
    }


    private fun finishGame(){
        _gameFinished.postValue(true)
    }
    fun finishGameOK(){
        _gameFinished.postValue(false)
    }


    fun nextWord(success: Boolean) {
        try {
            val end = System.currentTimeMillis()
            listOfWords?.value?.let { list ->
                var listSize = list.size
                var index = _currentIndex.value!!

                if(index < listSize){
                    val pwd = getCurrentPassword()
                    if(pwd != null){
                        pwd.time = end - timestamp
                        if(success){
                            pwd.solved = true
                            _nSuccess.postValue(_nSuccess.value!! + 1)
                        }else{
                            pwd.failed = true
                            _nFails.postValue(_nFails.value!! + 1)
                        }

                        _currentIndex.postValue(_currentIndex.value!! + 1)
                    }
                    timestamp = end
                    if(index == listSize - 1){
                        finishGame()
                    }
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message)
        }
    }

    fun checkHintsFromMicrophone(sentence: String){
        try {
            val words = sentence.split(SEPARATOR_VOICE)
            getCurrentPassword()?.let { pwd ->
                for (word in words) {
                    if (pwd.saidHint(word) || pwd.word.equals(word)){
                        Log.i(TAG, "MATCH! User said a hint: $word")
                        nextWord(false)
                        return
                    }
                }
            }
        }catch (e: Exception){
            Log.i(TAG, "exception in checkHintsFromMicrophone: "+e.message)
        }
    }

    private fun getCurrentPassword(): Password? {
        try {
            return listOfWords?.value?.get(_currentIndex.value!!)
        }catch (e: Exception){
            Log.e(TAG, e.message)
            return null
        }
    }

    companion object {
        const val TAG = "GameViewModel"
        const val SEPARATOR_VOICE = " "


        val DEFAULT_WAIT = 61000L
        val ONE_SECOND = 1000L

        val DEFAULT_MAX_WORDS = 20L

        val VALUE_NOT_STARTED = -1
        val VALUE_FINISHED = -2

        val COLL_PASSWORD = "passwords"
        val FIELD_CATEGORY = "category"
        val FIELD_LEVEL = "level"
        val DEFAULT_INDEX = -1
    }
}