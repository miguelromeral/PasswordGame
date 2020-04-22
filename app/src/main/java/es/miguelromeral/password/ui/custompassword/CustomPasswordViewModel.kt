package es.miguelromeral.password.ui.custompassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabaseDao
import es.miguelromeral.password.ui.game.GameFactory
import kotlinx.coroutines.*

class CustomPasswordViewModel(
    val database: PasswordDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)



    fun addPassword(password: Password){
        uiScope.launch {
            createNewPassword(password)
        }
    }

    private suspend fun createNewPassword(password: Password){
        return withContext(Dispatchers.IO){
            database.insert(password)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
