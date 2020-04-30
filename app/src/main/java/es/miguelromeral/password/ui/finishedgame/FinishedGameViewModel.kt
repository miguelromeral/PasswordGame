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
            //ans.score = ScoreBoard.getScore(ans.time, ans.solved, ans.failed, ans.level ?: Options.DEFAULT_LEVEL)
            points += ans.score
        }
        if(points < 0)
            points = 0

        return points
    }




}
