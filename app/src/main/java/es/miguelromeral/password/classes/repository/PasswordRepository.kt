package es.miguelromeral.password.classes.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import es.miguelromeral.password.classes.*
import es.miguelromeral.password.ui.game.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository(private val database: PasswordDatabaseDao){


    private var mFirestore: FirebaseFirestore

    val retrieved = MutableLiveData(listOf<Password>())

    val localWords = database.getAllPasswords()

    init {
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }



    suspend fun retrieveWords(category: String, level: String, language: String, useLocalDB: Boolean, inter: IRepository) {
        return withContext(Dispatchers.IO) {
            var listRetrieved = mutableListOf<Password>()

            var myWords = listOf<Password>()

            if(useLocalDB){
                val tmp = database.getAllPasswordsSync()

                val tmp3 = localWords.value
                tmp?.let{ list ->
                    myWords = list
                    if(!Options.isDefaultLevel(level))
                        myWords = myWords.filter { it.level.equals(level) }

                    if(!Options.isDefaultCategory(category))
                        myWords = myWords.filter { it.category.equals(category) }

                    myWords.forEach { it.custom = true }
                }
            }

            val ref = mFirestore.collection(GameViewModel.COLL_PASSWORD)
            Log.i(TAG, "cat: $category, lev: $level, lan: $language, useLocalDB: $useLocalDB")


            var query: Query? = null

            if (!Options.isDefaultLevel(level)) {
                query = (query ?: ref).whereEqualTo(GameViewModel.FIELD_LEVEL, level)
            }
            if (!Options.isDefaultCategory(category)) {
                query = (query ?: ref).whereEqualTo(GameViewModel.FIELD_CATEGORY,category)
            }
            if (query == null)
                query = ref

/*
                query.limit(GameViewModel.DEFAULT_MAX_WORDS)
                    .get()
                    .addOn
  */

            query
                .limit(GameViewModel.DEFAULT_MAX_WORDS)
                .get()
                .addOnSuccessListener { documents ->
                    try{
                        if (documents != null) {
                            for (document in documents) {
                                var obj = document.toObject(Password::class.java)
                                listRetrieved.add(obj)
                            }

                            listRetrieved.addAll(myWords)

                            inter.retrievedWords(listRetrieved.toList())

                            retrieved.postValue(listRetrieved.toList())

                            Log.d(TAG, "DocumentSnapshot read successfully!")

                            Log.i("TEST", "Retrieved words: ${listRetrieved.size}")


                        } else {
                            Log.d(TAG, "No such document!")
                        }

                } catch (e: Exception) {
                        Log.i(TAG, "Exception trying to retrieve words: " + e.message)
                    }

                }

            /*Log.i("TEST", "End of retrieving")*/
            Unit
        }
    }

    

    companion object {
        const val TAG = "PasswordRepository"
    }

}