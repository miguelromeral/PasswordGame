package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class Languages {
    companion object {

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