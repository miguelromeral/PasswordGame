package es.miguelromeral.password.classes

import android.content.res.Resources
import es.miguelromeral.password.R

class Options {
    companion object {
        const val DEFAULT_CATEGORY = "Mixed"
        const val DEFAULT_LEVEL = "Mixed"
        const val DEFAULT_LANGUAGE = "Mixed"


        const val LEVEL_EASY = "easy"
        const val LEVEL_MEDIUM = "medium"
        const val LEVEL_HARD = "hard"


        fun getLevelValue(index: Int): String =
            when(index){
                1 -> LEVEL_EASY
                2 -> LEVEL_MEDIUM
                3 -> LEVEL_HARD
                else -> DEFAULT_LEVEL
            }

        fun getStringFromLevelValue(resources: Resources, value: String): String =
             when(value){
                 LEVEL_EASY -> resources.getString(R.string.dpm_level_easy)
                 LEVEL_MEDIUM -> resources.getString(R.string.dpm_level_medium)
                 LEVEL_HARD -> resources.getString(R.string.dpm_level_hard)
                 else -> resources.getString(R.string.dpm_level_mixed)
             }

        const val DEFAULT_MICROPHONE_VALUE = true
        const val DEFAULT_MIX_PASSWORDS_VALUE = true
    }
}