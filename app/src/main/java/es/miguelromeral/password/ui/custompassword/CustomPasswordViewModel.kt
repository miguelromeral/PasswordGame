package es.miguelromeral.password.ui.custompassword

import android.app.Application
import android.content.res.Resources
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
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

    private val _hints = MutableLiveData<List<String>>(listOf())
    val hints = _hints


    fun setPassword(pwd: Password){
        _hints.postValue(pwd.hintsSplit())
    }


    fun addPassword(resources: Resources, password: Password?){
        password?.let {
            uiScope.launch {
                createNewPassword(resources, password)
            }
        }
    }

    fun addHint(hint: String){
        _hints.value?.let{ list ->
            var nueva = list.toMutableList()
            nueva.add(hint)
            _hints.postValue(nueva)
        }
    }

    fun removeHint(hint: String){
        hints.value?.let { list ->
            var nueva = list.toMutableList()
            var index = list.indexOf(hint)
            nueva.removeAt(index)
            _hints.postValue(nueva)
        }
    }

    fun getHintsFormatted(): String{
        var text = ""
        hints.value?.let { list ->
            for (hint in list) {
                text += "${hint}${Password.SEPARATOR}"
            }
            if(list.size > 0)
                text = text.substring(0, text.length - 1)
        }
        return text
    }

    private suspend fun createNewPassword(resources: Resources, password: Password){
        return withContext(Dispatchers.IO){
            password.random = Random.nextLong()
            password.custom = true
            /*password.language = password.language
            password.level = password.level
            password.category = password.category*/
            database.insert(password)
            _warning.postValue(resources.getString(R.string.cpf_warning_added_successfully, password.word))
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
