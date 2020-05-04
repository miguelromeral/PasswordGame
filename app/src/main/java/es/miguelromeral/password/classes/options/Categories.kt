package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class Categories {
    companion object {

        const val DEFAULT_CATEGORY = "mixed"


        fun isDefault(level: String) =
                level.equals(DEFAULT_CATEGORY, true)

        fun getCategoryValueIndex(res: Resources, value: String?): Int =
                when(value){
                    res.getString(R.string.value_category_animals) -> 0
                    res.getString(R.string.value_category_furniture)-> 1
                    res.getString(R.string.value_category_movies)-> 2
                    else -> -1
                }

        fun getCategoryTextFromValue(resources: Resources, value: String?): String =
                when(value){
                    resources.getString(R.string.value_category_furniture) ->
                        resources.getString(R.string.entry_category_furniture)
                    resources.getString(R.string.value_category_movies) ->
                        resources.getString(R.string.entry_category_movies)
                    else ->
                        resources.getString(R.string.entry_category_animals)
                }

        fun getCategoryValueFromEntry(resources: Resources, entry: String): String =
                when(entry){
                    resources.getString(R.string.entry_category_furniture) ->
                        resources.getString(R.string.value_category_furniture)
                    resources.getString(R.string.entry_category_movies) ->
                        resources.getString(R.string.value_category_movies)
                    else ->
                        resources.getString(R.string.value_category_animals)
                }
    }
}