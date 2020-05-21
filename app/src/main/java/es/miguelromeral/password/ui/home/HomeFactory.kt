package es.miguelromeral.password.ui.home

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import es.miguelromeral.password.ui.game.GameViewModel
import java.lang.IllegalArgumentException

class HomeFactory (
    private val activity: Activity,
    private val database: PasswordDatabaseDao,
    private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(activity, database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}