package es.miguelromeral.password.ui.settings

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import es.miguelromeral.password.R
import androidx.preference.*


class SettingsFragment : PreferenceFragmentCompat(),  SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        findPreference(getString(R.string.pref_github_key)).setOnPreferenceClickListener {
            context?.let{
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(resources.getString(R.string.pref_github_web))
                }
                it.startActivity(i)
                return@setOnPreferenceClickListener true
            }
            false
        }
        findPreference(getString(R.string.pref_tip_key)).setOnPreferenceClickListener {
            context?.let{
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(paypal_tip_web)
                }
                it.startActivity(i)
                return@setOnPreferenceClickListener true
            }
            false
        }
        findPreference(getString(R.string.pref_policy_key)).setOnPreferenceClickListener {
            context?.let{
                val i = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse(policy_private_web)
                }
                it.startActivity(i)
                return@setOnPreferenceClickListener true
            }
            false
        }

        setTintColor(context)
    }


    private fun setTintColor(context: Context?){
        context?.let {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val night = isNightThemeEnabled(context, sharedPreferences)
            val preferences = listOf(
                    getString(R.string.pref_microphone_key),
                    getString(R.string.pref_language_key),
                    getString(R.string.pref_tip_key),
                    getString(R.string.pref_policy_key),
                    getString(R.string.pref_words_source_key),
                    getString(R.string.pref_theme_key),
                    getString(R.string.pref_github_key),
                    getString(R.string.pref_timer_key),
                    getString(R.string.pref_hints_key),
                    getString(R.string.pref_count_key)

            )

            for (spr in preferences) {
                val pref = findPreference(spr)
                pref.icon.setTint(
                    if (night) context.getColor(R.color.colorAccent) else context.getColor(R.color.black)
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

        fun isNightThemeEnabled(context: Context): Boolean =
                when(context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK){
                    Configuration.UI_MODE_NIGHT_YES -> true
                    Configuration.UI_MODE_NIGHT_NO -> false
                    else -> {
                        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                                context.resources.getString(R.string.pref_theme_key), false)
                    }
                }


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

        const val paypal_tip_web = "https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=M4CR7FHADMVXN&source=url"
        const val policy_private_web = "https://github.com/miguelromeral/PasswordGame/blob/master/PRIVACY-POLICY.md"
    }

}