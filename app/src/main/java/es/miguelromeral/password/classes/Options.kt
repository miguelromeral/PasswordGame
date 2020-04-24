package es.miguelromeral.password.classes

import android.content.res.Resources
import es.miguelromeral.password.R

class Options {
    companion object {
        const val DEFAULT_CATEGORY = "mixed"
        const val DEFAULT_LEVEL = "mixed"

        const val DEFAULT_LANGUAGE = "Mixed"


        const val LEVEL_EASY = "easy"
        const val LEVEL_MEDIUM = "medium"
        const val LEVEL_HARD = "hard"

        const val CATEGORY_ANIMALS = "animals"
        const val CATEGORY_TECHNOLOGY = "technology"


        fun isDefaultLevel(level: String) = level.equals(DEFAULT_LEVEL, true)

        fun getLevelValue(index: Int): String =
            when(index){
                0 -> LEVEL_EASY
                1 -> LEVEL_MEDIUM
                2 -> LEVEL_HARD
                else -> DEFAULT_LEVEL
            }


        fun getLevelValueIndex(value: String): Int =
            when(value){
                LEVEL_EASY -> 0
                LEVEL_MEDIUM -> 1
                LEVEL_HARD -> 2
                else -> 0
            }

        fun getStringFromLevelValue(resources: Resources, value: String): String =
             when(value){
                 LEVEL_EASY -> resources.getString(R.string.dpm_level_easy)
                 LEVEL_MEDIUM -> resources.getString(R.string.dpm_level_medium)
                 LEVEL_HARD -> resources.getString(R.string.dpm_level_hard)
                 else -> resources.getString(R.string.dpm_level_mixed)
             }




        fun isDefaultCategory(category: String) = category.equals(DEFAULT_CATEGORY, true)

        fun getCategoryValue(index: Int): String =
            when(index){
                0 -> CATEGORY_ANIMALS
                1 -> CATEGORY_TECHNOLOGY
                else -> DEFAULT_CATEGORY
            }

        fun getCategoryValueIndex(value: String): Int =
            when(value){
                CATEGORY_ANIMALS -> 0
                CATEGORY_TECHNOLOGY -> 1
                else -> 0
            }

        fun getStringFromCategoryValue(resources: Resources, value: String): String =
            when(value){
                CATEGORY_ANIMALS -> resources.getString(R.string.dpm_category_animals)
                CATEGORY_TECHNOLOGY -> resources.getString(R.string.dpm_category_technology)
                else -> resources.getString(R.string.dpm_category_mixed)
            }




        const val DEFAULT_MICROPHONE_VALUE = true
        const val DEFAULT_MIX_PASSWORDS_VALUE = true
    }
}