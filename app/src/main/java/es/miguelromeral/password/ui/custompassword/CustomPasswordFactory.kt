package es.miguelromeral.password.ui.custompassword

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import java.lang.IllegalArgumentException

class CustomPasswordFactory (
        private val database: PasswordDatabaseDao,
        private val application: Application
) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override  fun <T: ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(CustomPasswordViewModel::class.java)){
            return CustomPasswordViewModel(database, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}