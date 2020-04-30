package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class Levels {
    companion object{
        const val DEFAULT_LEVEL = "mixed"

        fun isDefault(level: String) =
                level.equals(DEFAULT_LEVEL, true)

        fun getLevelValueIndex(res: Resources, value: String?): Int =
                when(value){
                    res.getString(R.string.value_level_easy) -> 0
                    res.getString(R.string.value_level_medium)-> 1
                    res.getString(R.string.value_level_hard)-> 2
                    else -> 0
                }

        fun getLevelTextFromValue(resources: Resources, value: String?): String =
                when(value){
                    resources.getString(R.string.value_level_medium) ->
                        resources.getString(R.string.entry_level_medium)
                    resources.getString(R.string.value_level_hard) ->
                        resources.getString(R.string.entry_level_hard)
                    else ->
                        resources.getString(R.string.entry_level_easy)
                }

        fun getLevelValueFromEntry(resources: Resources, entry: String): String =
                when(entry){
                    resources.getString(R.string.entry_level_medium) ->
                        resources.getString(R.string.value_level_medium)
                    resources.getString(R.string.entry_level_hard) ->
                        resources.getString(R.string.value_level_hard)
                    else ->
                        resources.getString(R.string.value_level_easy)
                }

    }
}