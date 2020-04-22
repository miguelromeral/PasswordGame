package es.miguelromeral.password.ui.custompassword

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabaseDao
import es.miguelromeral.password.ui.game.GameFactory
import kotlinx.coroutines.*
import kotlin.random.Random

class CustomPasswordViewModel(
    val database: PasswordDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _warning = MutableLiveData<String?>()
    val warning = _warning

    fun addPassword(password: Password?){
        password?.let {
            uiScope.launch {
                createNewPassword(password)
            }
        }
    }

    private suspend fun createNewPassword(password: Password){
        return withContext(Dispatchers.IO){
            password.random = Random.nextLong()
            password.language = "english"
            password.level = "easy"
            password.category = "custom"
            database.insert(password)
            _warning.postValue("Added successfully!")
        }
    }

    fun clearWarning(){
        _warning.postValue(null)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    companion object {
        const val TAG = "CustomPasswordViewModel"
    }
}
