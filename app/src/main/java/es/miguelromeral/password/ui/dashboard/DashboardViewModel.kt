package es.miguelromeral.password.ui.dashboard

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import es.miguelromeral.password.ui.utils.importSecretsFU
import kotlinx.coroutines.*

class DashboardViewModel(
        val database: PasswordDatabaseDao,
        application: Application
) : AndroidViewModel(application) {


    lateinit var passwords: LiveData<List<Password>>

    val filteredList: MutableLiveData<List<Password>> = MutableLiveData()

    private var _dataChanged = MutableLiveData<Boolean>()
    val dataChanged = _dataChanged


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    init {
        updateAllPasswords()
    }


    fun updateAllPasswords(){
        passwords = database.getAllPasswords(true)
        _dataChanged.postValue(true)
    }

    fun finalDataChanged(){
        _dataChanged.postValue(false)
    }


    fun filterPasswords(criteria: String?): Boolean {
        if(criteria == null || criteria.isEmpty()){
            filteredList.postValue(passwords.value)
        }else {
            val tmp = mutableListOf<Password>()
            val input = criteria.toLowerCase()

            passwords.value?.let {
                for (s in it) {
                    if (s.word.toLowerCase().contains(criteria) || (s.hints?.toLowerCase() ?: "").contains(criteria)){
                        tmp.add(s)
                    }
                }
            }
            filteredList.postValue(tmp.sortedBy { it.word })
        }
        return true
    }


    fun importSecrets(activity: Activity) = uiScope.launch {

        val task = async(Dispatchers.IO) {
            importSecretsFU(activity, database)
        }

        when(task.await()){
            true -> {
                updateAllPasswords()
            }
        }


    }


    fun clearAllPasswords(){
        uiScope.launch {
            clearDatabaseIO()
        }
    }

    fun deletePassword(pwd: Password){
        uiScope.launch {
            deletePasswordIO(pwd)
        }
    }

    private suspend fun clearDatabaseIO(){
        return withContext(Dispatchers.IO){
            database.clear(custom = true)
        }
    }

    private suspend fun deletePasswordIO(pwd: Password){
        return withContext(Dispatchers.IO){
            database.delete(pwd.word)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}