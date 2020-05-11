package es.miguelromeral.password.ui.finishedgame

import android.app.Application
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import kotlinx.coroutines.*

class FinishedGameViewModel (
        private val database: PasswordDatabaseDao,
        private val application: Application,
        private val success: Int,
        private val fails: Int,
        private val passwords: Array<Password>
) : ViewModel() {

    private val _listOfWords = MutableLiveData<List<Password>>()
    val listOfWords = _listOfWords


    private val _hits = MutableLiveData<Int>(success)
    val hits = _hits

    private val _mistakes = MutableLiveData<Int>(fails)
    val mistakes = _mistakes

    private val _score = MutableLiveData<Int>()
    private var _scoreInt = 0
    val score = _score

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    private val _added = MutableLiveData<String>("")
    val added = _added

    init {
        val answers = setFastest(passwords.filter { it.solved ?: false || it.failed ?: false })
        _score.postValue(calculateScore(answers))
        _listOfWords.postValue(answers)
    }

    private fun setFastest(list: List<Password>): List<Password> {
        var fastestTime: Long? = null
        var fastestPwd: Password? = null

        var mostPointsScore: Int? = null
        var mostPointsPwd: Password? = null

        for (pwd in list){
            if(pwd.solved == true){
                if(fastestTime == null || pwd.time?.compareTo(fastestTime) == -1){
                    if (fastestPwd != null) {
                        fastestPwd.fastest = false
                    }
                    fastestTime = pwd.time
                    pwd.fastest = true
                    fastestPwd = pwd
                }
                if(mostPointsScore == null || pwd.score?.compareTo(mostPointsScore) == 1){
                    if(mostPointsPwd != null){
                        mostPointsPwd.mostPoints = false
                    }
                    mostPointsScore = pwd.score
                    pwd.mostPoints = true
                    mostPointsPwd = pwd
                }
            }
        }
        return list
    }

    private fun calculateScore(answers: List<Password>): Int{
        var points = 0
        for(ans in answers){
            points += ans.score ?: 0
        }
        if(points < 0)
            points = 0

        _scoreInt = points

        return points
    }

    fun shareScore(context: Context){

        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,
                    context.resources.getString(R.string.fg_share_text, _scoreInt.toString(), success.toString(), fails.toString()))
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun saveWord(pwd: Password){
        uiScope.launch {
            saveWordThread(pwd)
        }
    }

    private suspend fun saveWordThread(pwd: Password){
        return withContext(Dispatchers.IO){
            pwd.time = 0
            pwd.score = 0
            pwd.solved = false
            pwd.failed = false
            database.insert(pwd)
            _added.postValue(pwd.word)
        }
    }

    fun endAdd(){
        _added.postValue("")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}
