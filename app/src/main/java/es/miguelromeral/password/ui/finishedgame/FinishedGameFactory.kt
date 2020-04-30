package es.miguelromeral.password.ui.finishedgame

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.Password
import java.lang.IllegalArgumentException

class FinishedGameFactory (
    private val success: Int,
    private val fails: Int,
    private val passwords: Array<Password>
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinishedGameViewModel::class.java)){
            return FinishedGameViewModel(success, fails, passwords) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}