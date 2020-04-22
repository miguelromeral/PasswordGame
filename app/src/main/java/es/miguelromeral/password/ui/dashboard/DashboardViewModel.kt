package es.miguelromeral.password.ui.dashboard

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.miguelromeral.password.classes.CustomPassword
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabaseDao
import kotlinx.coroutines.*

class DashboardViewModel(
    val database: PasswordDatabaseDao,
    application: Application
) : AndroidViewModel(application) {


    val passwords = database.getAllPasswords()

    val filteredList: MutableLiveData<List<CustomPassword>> = MutableLiveData()

    private var _dataChanged = MutableLiveData<Boolean>()
    val dataChanged = _dataChanged

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)



    fun clearAllPasswords(){
        uiScope.launch {
            clearDatabaseIO()
        }
    }


    private suspend fun clearDatabaseIO(){
        return withContext(Dispatchers.IO){
            database.clear()
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}