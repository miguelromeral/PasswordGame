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
        val answers = passwords.filter { it.time != 0L }
        _score.postValue(calculateScore(answers))
        _listOfWords.postValue(answers)
    }

    fun calculateScore(answers: List<Password>): Int{
        return 0
    }
}
