package es.miguelromeral.password.ui.finishedgame

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.classes.Password

class FinishedGameViewModel (
    private val success: Int,
    private val fails: Int,
    private val passwords: Array<Password>
) : ViewModel() {

    private val _listOfWords = MutableLiveData<List<Password>>()
    val listOfWords = _listOfWords

    private val _score = MutableLiveData<Int>()
    val score = _score


    init {
        val answers = passwords.filter { it.solved || it.failed }
        _score.postValue(calculateScore(answers))
        _listOfWords.postValue(answers)
    }

    private fun calculateScore(answers: List<Password>): Int{
        var points = 0
        for(ans in answers){
            ans.score =
                    if(ans.solved){
                        SCORE_HIT + getBonus(ans.time)
                    }else if(ans.failed){
                        SCORE_MISS
                    }else{
                        0
                    }
            points += ans.score
        }
        if(points < 0)
            points = 0

        return points
    }

    private fun getBonus(time: Long): Int =
        if(time > SCORE_MAX_TIME)
            SCORE_MAX_TIME_VALUE
        else if(time < SCORE_MIN_TIME)
            SCORE_MIN_TIME_VALUE
        else{
            val maxtime = SCORE_MAX_TIME - SCORE_MIN_TIME
            val midtime = time - SCORE_MIN_TIME
            val maxscore = SCORE_MIN_TIME_VALUE - SCORE_MAX_TIME_VALUE
            val pct = (midtime.toDouble() / maxtime.toDouble())
            val part = pct * maxscore
            (maxscore - part).toInt()
        }


    companion object {
        const val SCORE_HIT = 200
        const val SCORE_MISS = -100

        const val SCORE_MAX_TIME_VALUE = 50
        const val SCORE_MAX_TIME = 30000L

        const val SCORE_MIN_TIME_VALUE = 2000
        const val SCORE_MIN_TIME = 5000L
    }
}
