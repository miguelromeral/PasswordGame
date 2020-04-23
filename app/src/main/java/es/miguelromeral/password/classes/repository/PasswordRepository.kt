package es.miguelromeral.password.classes.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import es.miguelromeral.password.classes.*
import es.miguelromeral.password.ui.game.GameViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PasswordRepository(private val database: PasswordDatabaseDao){


    private var mFirestore: FirebaseFirestore

    val retrieved = MutableLiveData(listOf<Password>())

    init {
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()
    }


    suspend fun retrieveWords(category: String, level: String, language: String, useLocalDB: Boolean, inter: IRepository) : Task<QuerySnapshot> {
        return withContext(Dispatchers.IO) {

/*            retrieved.postValue(listOf(
                Password(
                    category = category,
                    language = language,
                    level = level,
                    word = "Test",
                    hints = "test,test,test")

            ))*/

            var listRetrieved = mutableListOf<Password>()

                val ref = mFirestore.collection(GameViewModel.COLL_PASSWORD)
                Log.i(TAG, "cat: $category, lev: $level, lan: $language, useLocalDB: $useLocalDB")


                var query: Query? = null

                if (!level.equals(Options.DEFAULT_LEVEL, true)) {
                    query =
                        (query ?: ref).whereEqualTo(GameViewModel.FIELD_LEVEL, level.toLowerCase())
                }
                if (!category.equals(Options.DEFAULT_CATEGORY, true)) {
                    query = (query ?: ref).whereEqualTo(
                        GameViewModel.FIELD_CATEGORY,
                        category.toLowerCase()
                    )
                }
                if (query == null)
                    query = ref

/*
                query.limit(GameViewModel.DEFAULT_MAX_WORDS)
                    .get()
                    .addOn
  */

                return@withContext query
                    .limit(GameViewModel.DEFAULT_MAX_WORDS)
                    .get()
                    .addOnSuccessListener { documents ->
                        try{
                        if (documents != null) {
                            for (document in documents) {
                                var obj = document.toObject(Password::class.java)
                                listRetrieved.add(obj)
                            }


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

            /*Log.i("TEST", "End of retrieving")
            Unit*/
        }
    }

    

    companion object {
        const val TAG = "PasswordRepository"
    }

}