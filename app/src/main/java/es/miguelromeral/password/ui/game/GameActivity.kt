package es.miguelromeral.password.ui.game

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.preference.PreferenceManager
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Options
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)

        if(PreferenceManager.getDefaultSharedPreferences(baseContext).getBoolean(
                baseContext.getString(
                        R.string.pref_microphone_key
                ), Options.DEFAULT_MICROPHONE_VALUE)){

            checkVoiceCommandPermission()
        }
    }


    private fun checkVoiceCommandPermission(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(!(ContextCompat.checkSelfPermission(baseContext, android.Manifest.permission.RECORD_AUDIO)
                        == PackageManager.PERMISSION_GRANTED)){



                val builder = AlertDialog.Builder(this)
                builder.setTitle(R.string.gaf_alert_microphone_title)
                builder.setMessage(R.string.gaf_alert_microphone_body)

                builder.setNeutralButton(R.string.gaf_alert_microphone_cancel){dialog , which ->
                    finish()
                }

                builder.setPositiveButton(R.string.gaf_alert_microphone_ok){ dialogInterface, i ->
                    val newIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
                    startActivity(newIntent)
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()
            }
        }
    }

    companion object {
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
