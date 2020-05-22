package es.miguelromeral.password.ui.home

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import es.miguelromeral.password.classes.options.FirestoreConfig
import es.miguelromeral.password.classes.repository.Repository
import kotlinx.coroutines.*
import timber.log.Timber

class HomeViewModel(
    private val activity: Activity,
    private val database: PasswordDatabaseDao,
    val application: Application) : ViewModel() {

    private val _filterLevel = MutableLiveData<Boolean>(false)
    val filterLevel = _filterLevel

    private val _filterCategory = MutableLiveData<Boolean>(false)
    val filterCategory  = _filterCategory

    private val repository = Repository(application, database)

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    private val _updatedCache = MutableLiveData<Boolean>(false)
    val updatedCache = _updatedCache


    init {

        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val cacheVersionLocal = sharedPref.getString(FirestoreConfig.CONFIG_CACHE, FirestoreConfig.CONFIG_CACHE_DEFAULT_VALUE) ?: FirestoreConfig.CONFIG_CACHE_DEFAULT_VALUE
        Timber.i("Local Cache Version: $cacheVersionLocal")
        repository.refreshCache(uiScope, activity, cacheVersionLocal, _updatedCache)
    }

    fun endUpdatedCache(){
        _updatedCache.postValue(false)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}