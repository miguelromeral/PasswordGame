package es.miguelromeral.password.classes.repository

import android.app.Activity
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import es.miguelromeral.password.BuildConfig
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import es.miguelromeral.password.classes.options.Categories
import es.miguelromeral.password.classes.options.FirestoreConfig
import es.miguelromeral.password.classes.options.FirestoreConfig.Companion.CONFIG_CACHE
import es.miguelromeral.password.classes.options.FirestoreConfig.Companion.CONFIG_CACHE_DEFAULT_VALUE
import es.miguelromeral.password.classes.options.Levels
import es.miguelromeral.password.classes.options.Options
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber


class Repository(
    private val application: Application,
    private val database: PasswordDatabaseDao
) {

    private var remoteConfig: FirebaseRemoteConfig

    init {
        remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setDeveloperModeEnabled(BuildConfig.DEBUG)
            .setMinimumFetchIntervalInSeconds(4200)
            .build()
        remoteConfig.setConfigSettings(configSettings)
    }

    suspend fun retrieveWords(category: String, level: String, language: String, source: Int, countWords: Int, inter: IRepository) {
        withContext(Dispatchers.IO) {

            when(source){
                Options.SOURCE_BOTH -> {
                    var listRetrieved = mutableListOf<Password>()
                    listRetrieved.addAll(retrieveLocalWords(category, level, language, true))
                    listRetrieved.addAll(retrieveLocalWords(category, level, language, false))
                    sendResults(countWords, listRetrieved, inter)
                }
                Options.SOURCE_CUSTOM -> {
                    sendResults(countWords, retrieveLocalWords(category, level, language, true), inter)
                }
                Options.SOURCE_DEFAULT -> {
                    sendResults(countWords, retrieveLocalWords(category, level, language, false), inter)
                }
                else -> {
                    sendResults(countWords, emptyList(), inter)
                }
            }
        }
    }


    private fun retrieveLocalWords(category: String, level: String, language: String, custom: Boolean): List<Password> {
        var myWords = listOf<Password>()

        val tmp = database.getAllPasswordsSync(custom)

        tmp?.let{ list ->

            myWords = list

            myWords = myWords.filter { it.language.equals(language) }

            if(!Levels.isDefault(level))
                myWords = myWords.filter { it.level.equals(level) }

            if(!Categories.isDefault(category))
                myWords = myWords.filter { it.category.equals(category) }
        }

        return myWords
    }

    private fun sendResults(count: Int, list: List<Password>, inter: IRepository){

        val toReturn = if(list.size > count){
            list.shuffled().subList(0, count)
        }else{
            list
        }

        inter.recieveWords(toReturn)
    }


    fun refreshCache(scope: CoroutineScope, activity: Activity, cacheVersionLocal: String, flag: MutableLiveData<Boolean>) {

        Timber.i("Refreshing cache")

            remoteConfig.fetchAndActivate()
                .addOnSuccessListener {
                    Timber.i("Remote Config fetched and activated")

                    var cacheVersionServer: String = remoteConfig.getString(CONFIG_CACHE)
                    Timber.i("cacheVersionServer: $cacheVersionServer | cacheVersionLocal: $cacheVersionLocal")

                    if(cacheVersionServer.isEmpty() || cacheVersionServer != cacheVersionLocal || cacheVersionServer == CONFIG_CACHE_DEFAULT_VALUE){
                        Timber.i("Updating cache")
                        var mFirestore = FirebaseFirestore.getInstance()
                        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
                        mFirestore.collection(FirestoreConfig.COLL_PASSWORD)
                            .get()
                            .addOnSuccessListener { documents ->
                                Timber.i("Passwords retrieved from firestore: ${documents.size()}")
                                scope.launch {
                                    insertAllData(documents)

                                    activity.getPreferences(Context.MODE_PRIVATE)?.let {
                                        with(it.edit()) {
                                            putString(CONFIG_CACHE, cacheVersionServer)
                                            commit()
                                        }
                                    }

                                    flag.postValue(true)

                                    Timber.i("Updated the local cache version")
                                }
                            }
                    }
                }

    }


    private suspend fun insertAllData(documents: QuerySnapshot){
        withContext(Dispatchers.IO) {
            try {
                Timber.i("Inserting all passwords retrieved")
                if (documents != null) {
                    var listObjects = mutableListOf<Password>()
                    for (document in documents) {
                        var obj = document.toObject(Password::class.java)
                        obj.id = document.id
                        listObjects.add(obj)
                    }
                    database.insertCache(listObjects)
                    Timber.i("Finished after ${listObjects.size}!"
                    )
                } else {
                    Timber.i("No such document!")
                }


            } catch (e: Exception) {
                Timber.i("Exception trying to insert words: " + e.message)
            }
        }
    }

}
