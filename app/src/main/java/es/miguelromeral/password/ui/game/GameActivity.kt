package es.miguelromeral.password.ui.game

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import es.miguelromeral.password.R
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
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
