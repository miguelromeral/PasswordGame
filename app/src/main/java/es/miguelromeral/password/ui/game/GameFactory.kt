package es.miguelromeral.password.ui.game

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import java.lang.IllegalArgumentException

class GameFactory (
        private val database: PasswordDatabaseDao,
        private val application: Application,
        private val category: String,
        private val level: String,
        private val language: String,
        private val source: Int,
        private val gameTime: Long,
        private val countWords: Int
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java)){
            return GameViewModel(database, application, category, level, language, source, gameTime, countWords) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}