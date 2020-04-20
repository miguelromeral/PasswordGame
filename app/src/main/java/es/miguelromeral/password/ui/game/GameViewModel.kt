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

    private val _listOfWords = MutableLiveData<List<String>>()
    val listOfWords = _listOfWords

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

        mFirestore
            .collection(COLL_PASSWORD)
            //.whereEqualTo("level","medium")
            .limit(2)
            .get()
            .addOnSuccessListener {documents ->
                try {
                    if (documents != null) {
                        var res = ""
                        for (document in documents) {
                            var obj = document.toObject(Password::class.java)
                            Log.i(TAG, "Hints: "+obj.hintsSplit.toString())
                            res += "${document.id} => ${document.data}\n"
                        }
                        Log.d(TAG, res)
                        _text.postValue(res)
                        Log.d(TAG, "DocumentSnapshot read successfully!")
                    } else {
                        Log.d(TAG, "No such document!")
                    }
                }catch (ex: Exception){
                    Log.e(TAG, ex.message)
                }
            }

        Log.i(TAG, "Finish!")
    }

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    companion object {
        val TAG = "GameViewModel"

        val COLL_PASSWORD = "passwords"
    }
}