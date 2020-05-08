package es.miguelromeral.password.ui.finishedgame

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import java.lang.IllegalArgumentException

class FinishedGameFactory (
        private val database: PasswordDatabaseDao,
        private val application: Application,
        private val success: Int,
        private val fails: Int,
        private val passwords: Array<Password>
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(FinishedGameViewModel::class.java)){
            return FinishedGameViewModel(database, application, success, fails, passwords) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}