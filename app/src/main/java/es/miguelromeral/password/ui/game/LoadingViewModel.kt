package es.miguelromeral.password.ui.game

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import es.miguelromeral.password.R
import kotlinx.coroutines.*
import java.lang.Compiler.command
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Handler


class LoadingViewModel : ViewModel(){


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    fun showExplanationMicro(activity: Activity){
        uiScope.launch {
            showWarning(activity)
        }
    }


    suspend fun showWarning(activity: Activity){
        return withContext(Dispatchers.Default) {

/*
            val message = activity.obtainMessage(command, parameter)
            message.sendToTarget()
            var act: Activity

            act.runO
            activity.runrunOnU*/
        }
    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}