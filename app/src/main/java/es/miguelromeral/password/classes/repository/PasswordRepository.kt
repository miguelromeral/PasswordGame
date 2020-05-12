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
import kotlin.random.Random

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

    private fun sendResults(count: Int, list: List<Password>, inter: IRepository){

        val toReturn = if(list.size > count){
            list.shuffled().subList(0, count)
        }else{
            list
        }

        retrieved.postValue(toReturn)
        inter.recieveWords(toReturn)
    }


    suspend fun retrieveWords(category: String, level: String, language: String, source: Int, countWords: Int, collection: String, inter: IRepository) {
        return withContext(Dispatchers.IO) {
            var listRetrieved = mutableListOf<Password>()

            var localWords =
                if(source == Options.SOURCE_BOTH || source == Options.SOURCE_CUSTOM)
                    retrieveLocalWords(category, level, language)
                else
                    emptyList()

            if(source == Options.SOURCE_CUSTOM){
                sendResults(countWords, localWords, inter)
                return@withContext Unit
            }else {
                val ref = mFirestore.collection(collection)
                Log.i(TAG, "cat: $category, lev: $level, lan: $language, source: $source collection: $collection")


                var query: Query = ref.whereEqualTo(FirestoreConfig.FIELD_LANGUAGE, language)

                if (!Levels.isDefault(level)) {
                    query = query.whereEqualTo(FirestoreConfig.FIELD_LEVEL, level)
                }
                if (!Categories.isDefault(category)) {
                    query = query.whereEqualTo(FirestoreConfig.FIELD_CATEGORY, category)
                }



                query
                    //.whereGreaterThan(FirestoreConfig.FIELD_RANDOM, randomIndex)
                    //.limit(countWords.toLong())
                    //.orderBy(FirestoreConfig.FIELD_RANDOM)
                    .get()
                    .addOnSuccessListener {documents ->
                        try {
                            if (documents != null) {
                                for (document in documents) {
                                    var obj = document.toObject(Password::class.java)
                                    listRetrieved.add(obj)
                                }

                                if(source == Options.SOURCE_BOTH)
                                    listRetrieved.addAll(localWords)

                                sendResults(countWords, listRetrieved, inter)

                                Log.d(TAG, "DocumentSnapshot read successfully: ${listRetrieved.size}!")

                            } else {
                                Log.d(TAG, "No such document!")
                            }

                        } catch (e: Exception) {
                            Log.i(TAG, "Exception trying to retrieve words: " + e.message)
                        }

                    }

            }
            Unit
        }
    }

    

    companion object {
        const val TAG = "PasswordRepository"

        const val MAX_WORDS_RETRIEVED = 20L
    }

}