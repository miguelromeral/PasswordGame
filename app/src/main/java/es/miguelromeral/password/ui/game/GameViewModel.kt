package es.miguelromeral.password.ui.game

import android.app.Application
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
import es.miguelromeral.password.classes.IRepository
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabaseDao
import es.miguelromeral.password.classes.repository.PasswordRepository
import kotlinx.coroutines.launch
import java.util.HashMap
import kotlin.random.Random

class GameViewModel(
    private val database: PasswordDatabaseDao,
    val application: Application,
    val category: String,
    val level: String,
    val language: String,
    val useLocalDB: Boolean) : ViewModel(), IRepository {

    private val _text = MutableLiveData<String>("Password!")
    val text: LiveData<String> = _text

    private val repository = PasswordRepository(database)

    private val _listOfWords = MutableLiveData<List<Password>>()
    val listOfWords = _listOfWords

    private val _currentIndex = MutableLiveData<Int>(DEFAULT_INDEX)
    val currentIndex = _currentIndex

    private val _nFails = MutableLiveData(0)
    val nFails = _nFails

    private val _nSuccess = MutableLiveData(0)
    val nSuccess = _nSuccess

    private var _countdownInt = VALUE_NOT_STARTED
    private var _countdown = MutableLiveData<Int>()
    val countdown = _countdown

    private val _gameFinished = MutableLiveData<Boolean>(false)
    val gameFinished = _gameFinished

    private lateinit var timer: CountDownTimer

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var timestamp: Long = 0

    init{
        initGame()
    }

    fun initGame(){
        val t = this
        uiScope.launch {
            repository.retrieveWords(category, level, language, useLocalDB, t)
        }
    }

    override fun retrievedWords(list: List<Password>) {
        if(list.isNotEmpty()) {
            _listOfWords.postValue(list?.shuffled())
            initTimer()
            Log.i("TEST", "timer initiated")
        }
        Log.i("TEST", "End of retrievedWords")
    }

    fun initTimer(){
        _currentIndex.postValue(0)
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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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