package es.miguelromeral.password.classes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import es.miguelromeral.password.ui.game.GameViewModel

class GameRecognitionService(
    val context: Context,
    val viewModel: GameViewModel) : RecognitionListener {

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.i(TAG,"Called: onReadyForSpeech")
    }

    override fun onRmsChanged(p0: Float) {
        Log.i(TAG,"Called: onRmsChanged")
    }

    override fun onBufferReceived(p0: ByteArray?) {
        Log.i(TAG,"Called: onBufferReceived")
    }

    override fun onPartialResults(p0: Bundle?) {
        Log.i(TAG,"Called: onPartialResults")
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        Log.i(TAG,"Called: onEvent: $p0")
    }

    override fun onBeginningOfSpeech() {
        Log.i(TAG,"Called: onBegginningOfSpeech")
    }

    override fun onEndOfSpeech() {
        viewModel.startEnableMic()
        Log.i(TAG,"Called: onEndOfSpeech")
    }

    override fun onError(p0: Int) {
        viewModel.startEnableMic()
        Log.i(TAG,"Called: onError: $p0")
    }

    override fun onResults(bundle: Bundle?) {
        Log.i(TAG,"onResults called")

        bundle?.let {
            val matches = it.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            Log.i(TAG,"matches: ${matches?.size}")

            matches?.let{
                for(str in it){
                    Log.i(GameViewModel.TAG, "Sentence: $str")
                    viewModel.checkHintsFromMicrophone(str)
                }
            }
        }

        viewModel.startEnableMic()
    }


    companion object{
        const val TAG = "GameRecognitionService"
    }

}