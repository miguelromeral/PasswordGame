package es.miguelromeral.password.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import es.miguelromeral.password.R
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat(),  SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        /*val p = findPreference(getString(R.string.preference_help_id))
        p.setOnPreferenceClickListener {
            context?.let{
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("https://github.com/miguelromeral/SecretManager/blob/master/HOW-TO.md")
                }
                it.startActivity(i)
                return@setOnPreferenceClickListener true
            }
            false
        }
*/
        setTintColor(context)
    }


    private fun setTintColor(context: Context?){
        context?.let {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val night = isNightThemeEnabled(context, sharedPreferences)
            val preferences = listOf(
                getString(R.string.pref_microphone_key)
            )

            for (spr in preferences) {
                val pref = findPreference(spr)
                pref.icon.setTint(
                    if (night) context.getColor(R.color.purple) else context.getColor(R.color.black)
                )
            }
        }
    }


    override fun onSharedPreferenceChanged(preferences: SharedPreferences?, key: String?) {
        when(key){
            resources.getString(R.string.pref_theme_key) -> {
                setStyleTheme(requireContext(), preferences)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    companion object {

        fun isNightThemeEnabled(context: Context, sharedPreferences: SharedPreferences) =
            sharedPreferences!!.getBoolean(context.getString(R.string.pref_theme_key), false)


        private fun setStyleTheme(context: Context, sharedPreferences: SharedPreferences? = null){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val resources = context.resources

            val style = preferences!!.getBoolean(resources.getString(R.string.pref_theme_key), false)
            if(style){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

    }

}