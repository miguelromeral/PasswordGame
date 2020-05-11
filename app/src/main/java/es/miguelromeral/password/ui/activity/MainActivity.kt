package es.miguelromeral.password.ui.activity

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.database.PasswordDatabase
import es.miguelromeral.password.ui.utils.actionViewFile
import es.miguelromeral.password.ui.utils.executeExportSecrets
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.action_settings
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        NavigationUI.setupActionBarWithNavController(this,navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.nav_host_fragment)
        return navController.navigateUp()
    }


    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    exportSecrets(baseContext)
                } else {
                    Toast.makeText(this, R.string.df_warning_export_toast, Toast.LENGTH_LONG).show()
                }
                return
            }


            else -> {
                // Ignore all other requests.
            }
        }
    }


    fun exportSecrets(context: Context){
        executeExportSecrets(context, PasswordDatabase.getInstance(context))?.let{ uri ->

            val view: View = findViewById(R.id.nav_host_fragment)

            val snackbar = Snackbar.make(view, R.string.df_export_complete, Snackbar.LENGTH_LONG)
            snackbar.setAction(R.string.df_export_complete_open){

                try {
                    startActivity(actionViewFile(context, uri))
                }catch (e: Exception){
                    e.printStackTrace()
                }

            }
            snackbar.show()
        }

    }

    companion object {
        const val REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 20
    }

}
