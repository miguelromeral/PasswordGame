package es.miguelromeral.password.ui.game

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PermissionGroupInfo
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import es.miguelromeral.password.classes.options.Options
import kotlinx.android.synthetic.main.activity_game.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.*
import android.widget.Toast
import es.miguelromeral.password.R
import es.miguelromeral.password.ui.requestPermission


class GameActivity : AppCompatActivity() {

    private lateinit var viewModel: LoadingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(es.miguelromeral.password.R.layout.activity_game)
        setSupportActionBar(toolbar)

/*
        var navHostFragment = supportFragmentManager.findFragmentById(R.id.myNavHostFragment);
        val t= navHostFragment.getChildFragmentManager().getFragments().get(0)

        gameFragment = FragmentManager.findFragment<GameActivityFragment>(findViewById(R.id.myNavHostFragment))
*/
        viewModel = LoadingViewModel()

        val mic = PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean(
                baseContext.getString(
                        es.miguelromeral.password.R.string.pref_microphone_key
                ), Options.DEFAULT_MICROPHONE_VALUE)

        if(mic){
            checkVoiceCommandPermission()
            /*if(!checkVoiceCommandPermission())
                finish()
            else
                return*/
        }else {
            initGame()
        }
    }

    fun initGame(disableMic: Boolean = false){
        val dir = LoadingFragmentDirections.actionLoadingFragmentToGameActivityFragment()
        dir.disableMic = disableMic
        findNavController(es.miguelromeral.password.R.id.myNavHostFragment).navigate(dir)
    }

    fun showWarning(){

    }

    private fun checkVoiceCommandPermission() {
        // Here, thisActivity is the current activity
        val permission = Manifest.permission.RECORD_AUDIO

        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                var activity = this
                /*
                runOnUiThread {

                }

                var activity = this
                var mHandler = object : Handler(Looper.getMainLooper()) {
                    override fun handleMessage(message: Message) {*/
                        val builder = AlertDialog.Builder(activity)
                        builder.setTitle(es.miguelromeral.password.R.string.gaf_alert_microphone_title)
                        builder.setMessage(es.miguelromeral.password.R.string.gaf_alert_microphone_body)

                        builder.setNeutralButton(es.miguelromeral.password.R.string.gaf_alert_microphone_cancel){ dialog, which ->
                            activity.finish()
                        }

                        builder.setPositiveButton(es.miguelromeral.password.R.string.gaf_alert_microphone_ok){ dialogInterface, i ->
                            //val newIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                            //activity.startActivity(newIntent)

                            requestPermission(activity, permission, MY_PERMISSIONS_REQUEST_READ_CONTACTS)
                        }

                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                  //  }
                //}

                //viewModel.showExplanationMicro(mHandler)
            } else {
                // No explanation needed, we can request the permission.
                requestPermission(this, permission, MY_PERMISSIONS_REQUEST_READ_CONTACTS)
            }
        } else {
            // Permission has already been granted
            initGame()
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_READ_CONTACTS -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initGame()
                } else {
                    Toast.makeText(this, R.string.gaf_alert_microphone_start_without, Toast.LENGTH_LONG).show()
                    initGame(disableMic = true)

                    //finish()

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return
            }

            // Add other 'when' lines to check for other
            // permissions this app might request.
            else -> {
                // Ignore all other requests.
            }
        }
    }

    companion object {
        const val TAG = "GameActivity"
        const val MY_PERMISSIONS_REQUEST_READ_CONTACTS = 10

        fun newInstance(context : Context, category: String?, level: String?, language: String?){
            val extras = Bundle()
            extras.putString(GameActivityFragment.ARG_CATEGORY, category)
            extras.putString(GameActivityFragment.ARG_LEVEL, level)
            extras.putString(GameActivityFragment.ARG_LANGUAGE, language)
            val intent = Intent(context, GameActivity::class.java)
            intent.putExtras(extras)
            context.startActivity(intent)
        }
    }
}
