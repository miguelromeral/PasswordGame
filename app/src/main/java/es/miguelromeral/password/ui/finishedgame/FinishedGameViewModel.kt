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


    init {
        _listOfWords.postValue(passwords.toList())
    }
}
