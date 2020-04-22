package es.miguelromeral.password.ui.finishedgame

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.ui.game.GameViewModel
import es.miguelromeral.password.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class FinishedGameFactory (
    private val success: Int
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinishedGameViewModel::class.java)){
            return FinishedGameViewModel(success) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}