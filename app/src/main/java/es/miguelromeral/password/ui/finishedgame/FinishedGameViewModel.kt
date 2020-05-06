package es.miguelromeral.password.ui.finishedgame

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password

class FinishedGameViewModel (
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


    init {
        val answers = passwords.filter { it.solved ?: false || it.failed ?: false }
        _score.postValue(calculateScore(answers))
        _listOfWords.postValue(answers)
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

}
