package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class Options {
    companion object {

        const val DEFAULT_NIGHT_THEME = false


        const val CATEGORY_ANIMALS = "animals"
        const val CATEGORY_TECHNOLOGY = "technology"



        const val SOURCE_BOTH = 0
        const val SOURCE_DEFAULT = 1
        const val SOURCE_CUSTOM = 2

        fun getSourceByString(value: String, resources: Resources): Int =
            when(value){
                resources.getString(R.string.pref_words_source_value_default) -> SOURCE_DEFAULT
                resources.getString(R.string.pref_words_source_value_custom) -> SOURCE_CUSTOM
                else -> SOURCE_BOTH
            }




        const val DEFAULT_MICROPHONE_VALUE = true
        const val DEFAULT_MIX_PASSWORDS_VALUE = true
    }
}