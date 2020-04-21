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

    private lateinit var timer: CountDownTimer

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var mFirestore: FirebaseFirestore

    init{
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

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

        mFirestore
            .collection(COLL_PASSWORD)
            //.whereEqualTo("level","medium")
            //.limit(2)
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

                    } else {
                        Log.d(TAG, "No such document!")

                        _currentIndex.postValue(DEFAULT_INDEX)
                    }
                }catch (ex: Exception){
                    Log.e(TAG, ex.message)
                }
            }

    }

    fun initSettings(){
        initTimer()
    }


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
            }
        }
        timer.start()
    }


    fun successWord(){
        try{
            getCurrentPassword()?.let{ pwd ->
                pwd.solved = true
                _nSuccess.postValue(_nSuccess.value!! + 1)
            }
        }catch(e: Exception){
            Log.e(TAG, e.message)
        }
    }

    fun failWord(){
        try{
            getCurrentPassword()?.let{ pwd ->
                pwd.failed = true
                _nFails.postValue(_nFails.value!! + 1)
            }
        }catch(e: Exception){
            Log.e(TAG, e.message)
        }
    }

    fun nextWord(): Boolean {
        try {
            listOfWords?.value?.let { list ->
                var listSize = list.size - 1
                var index = _currentIndex.value!!

                if (index < listSize){
                    _currentIndex.postValue(index + 1)
                    return true
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message)
        }
        return false
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
        val TAG = "GameViewModel"

        val DEFAULT_WAIT = 61000L
        val ONE_SECOND = 1000L

        val VALUE_NOT_STARTED = -1
        val VALUE_FINISHED = -2

        val COLL_PASSWORD = "passwords"
        val DEFAULT_INDEX = -1
    }
}