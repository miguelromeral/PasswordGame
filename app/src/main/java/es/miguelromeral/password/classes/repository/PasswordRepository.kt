package es.miguelromeral.password.classes.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import es.miguelromeral.password.classes.*
import es.miguelromeral.password.classes.options.Categories
import es.miguelromeral.password.classes.options.FirestoreConfig
import es.miguelromeral.password.classes.options.Levels
import es.miguelromeral.password.classes.options.Options
import es.miguelromeral.password.classes.database.PasswordDatabaseDao
import es.miguelromeral.password.ui.game.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository(private val database: PasswordDatabaseDao) {


    private var mFirestore: FirebaseFirestore

    val retrieved = MutableLiveData(listOf<Password>())

    val localWords = database.getAllPasswords()

    init {
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }

    fun retrieveLocalWords(category: String, level: String, language: String): List<Password> {
        var myWords = listOf<Password>()

        val tmp = database.getAllPasswordsSync()

        tmp?.let{ list ->
            myWords = list

            myWords = myWords.filter { it.language.equals(language) }

            if(!Levels.isDefault(level))
                myWords = myWords.filter { it.level.equals(level) }

            if(!Categories.isDefault(category))
                myWords = myWords.filter { it.category.equals(category) }

            myWords.forEach { it.custom = true }
        }

        return myWords
    }


    suspend fun retrieveWords(category: String, level: String, language: String, source: Int, inter: IRepository) {
        return withContext(Dispatchers.IO) {
            var listRetrieved = mutableListOf<Password>()

            var localWords =
                if(source == Options.SOURCE_BOTH || source == Options.SOURCE_CUSTOM)
                    retrieveLocalWords(category, level, language)
                else
                    emptyList()

            if(source == Options.SOURCE_CUSTOM){
                retrieved.postValue(localWords)
                inter.recieveWords(localWords)
                return@withContext Unit
            }else {
                val ref = mFirestore.collection(FirestoreConfig.COLL_PASSWORD)
                Log.i(TAG, "cat: $category, lev: $level, lan: $language, source: $source")


                var query: Query = ref.whereEqualTo(FirestoreConfig.FIELD_LANGUAGE, language)

                if (!Levels.isDefault(level)) {
                    query = query.whereEqualTo(FirestoreConfig.FIELD_LEVEL, level)
                }
                if (!Categories.isDefault(category)) {
                    query = query.whereEqualTo(FirestoreConfig.FIELD_CATEGORY, category)
                }

/*
                query.limit(GameViewModel.DEFAULT_MAX_WORDS)
                    .get()
                    .addOn
  */

                query
                    .limit(GameViewModel.DEFAULT_MAX_WORDS)
                    .get()
                    .addOnSuccessListener { documents ->
                        try {
                            if (documents != null) {
                                for (document in documents) {
                                    var obj = document.toObject(Password::class.java)
                                    listRetrieved.add(obj)
                                }

                                if(source == Options.SOURCE_BOTH)
                                    listRetrieved.addAll(localWords)

                                retrieved.postValue(listRetrieved.toList())
                                inter.recieveWords(listRetrieved.toList())
                                Log.d(TAG, "DocumentSnapshot read successfully: ${listRetrieved.size}!")

                            } else {
                                Log.d(TAG, "No such document!")
                            }

                        } catch (e: Exception) {
                            Log.i(TAG, "Exception trying to retrieve words: " + e.message)
                        }

                    }

                /*Log.i("TEST", "End of retrieving")*/
            }
            Unit
        }
    }

    

    companion object {
        const val TAG = "PasswordRepository"
    }

}