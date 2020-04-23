package es.miguelromeral.password.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.classes.PasswordDatabaseDao
import es.miguelromeral.password.ui.home.HomeViewModel
import java.lang.IllegalArgumentException

class GameFactory (
    private val database: PasswordDatabaseDao,
    private val application: Application,
    private val category: String,
    private val level: String,
    private val language: String,
    private val useLocalDB: Boolean
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(database, application, category, level, language, useLocalDB) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}