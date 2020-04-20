package es.miguelromeral.password.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import es.miguelromeral.password.classes.Password
import java.util.HashMap

class GameViewModel : ViewModel() {

    private val _text = MutableLiveData<String>("Password!")
    val text: LiveData<String> = _text

    private val _listOfWords = MutableLiveData<List<Password>>()
    val listOfWords = _listOfWords

    private val _currentIndex = MutableLiveData<Int>(DEFAULT_INDEX)
    val currentIndex = _currentIndex

    private var mFirestore: FirebaseFirestore

    init{
        mFirestore = FirebaseFirestore.getInstance()
        mFirestore.firestoreSettings = FirebaseFirestoreSettings.Builder().build()

        /*
        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912
        )

        // Add a new document with a generated ID
        mFirestore.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
*/

        var listRetrieved = mutableListOf<Password>()

        mFirestore
            .collection(COLL_PASSWORD)
            //.whereEqualTo("level","medium")
            .limit(2)
            .get()
            .addOnSuccessListener {documents ->
                try {
                    if (documents != null) {
                        for (document in documents) {
                            var obj = document.toObject(Password::class.java)
                            listRetrieved.add(obj)
                        }
                        Log.d(TAG, "DocumentSnapshot read successfully!")
                    } else {
                        Log.d(TAG, "No such document!")
                    }
                }catch (ex: Exception){
                    Log.e(TAG, ex.message)
                }
            }

        _listOfWords.postValue(listRetrieved)
        _currentIndex.postValue(0)
        Log.i(TAG, "Finish!")
    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun nextWord(){
        try {
            listOfWords?.value?.let { list ->
                var listSize = list.size - 1
                var index = _currentIndex.value!!

                if (index < listSize){
                    _currentIndex.postValue(index + 1)
                }
            }
        }catch (e: Exception){
            Log.e(TAG, e.message)
        }
    }

    companion object {
        val TAG = "GameViewModel"

        val COLL_PASSWORD = "passwords"
        val DEFAULT_INDEX = -1
    }
}