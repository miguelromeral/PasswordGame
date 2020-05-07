package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R
import java.util.*

class Languages {
    companion object {

        fun getLanguageLocale(res: Resources, value: String?): Locale =
                when(value){
                    res.getString(R.string.pref_language_value_english) -> Locale.ENGLISH
                    res.getString(R.string.pref_language_value_spanish) -> Locale("es", "ES")
                    else -> Locale.getDefault()
                }

        fun getLanguageValueIndex(res: Resources, value: String?): Int =
                when(value){
                    res.getString(R.string.pref_language_value_english) -> 0
                    res.getString(R.string.pref_language_value_spanish) -> 1
                    else -> 0
                }

        fun getLanguageTextFromValue(resources: Resources, value: String?): String =
                when(value){
                    resources.getString(R.string.pref_language_value_spanish) ->
                        resources.getString(R.string.pref_language_entry_spanish)
                    else ->
                        resources.getString(R.string.pref_language_entry_english)
                }

        fun getLanguageValueFromEntry(resources: Resources, entry: String): String =
                when(entry){
                    resources.getString(R.string.pref_language_entry_spanish) ->
                        resources.getString(R.string.pref_language_value_spanish)
                    else ->
                        resources.getString(R.string.pref_language_value_english)
                }


    }
}