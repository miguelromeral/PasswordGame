package es.miguelromeral.password.ui.custompassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import es.miguelromeral.password.R

class CustomPasswordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_password)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, CustomPasswordFragment())
                .commitNow()
        }
    }

}
