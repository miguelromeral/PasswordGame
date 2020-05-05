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
                    res.getString(R.string.value_category_cooking)-> 1
                    res.getString(R.string.value_category_furniture)-> 2
                    res.getString(R.string.value_category_movies)-> 3
                    res.getString(R.string.value_category_custom)-> 4
                    else -> 0
                }

        fun getCategoryTextFromValue(resources: Resources, value: String?): String =
                when(value){
                    resources.getString(R.string.value_category_animals) ->
                        resources.getString(R.string.entry_category_animals)
                    resources.getString(R.string.value_category_cooking) ->
                        resources.getString(R.string.entry_category_cooking)
                    resources.getString(R.string.value_category_furniture) ->
                        resources.getString(R.string.entry_category_furniture)
                    resources.getString(R.string.value_category_movies) ->
                        resources.getString(R.string.entry_category_movies)
                    resources.getString(R.string.value_category_custom) ->
                        resources.getString(R.string.entry_category_custom)
                    else ->
                        resources.getString(R.string.entry_category_unknown)
                }

        fun getCategoryValueFromEntry(resources: Resources, entry: String): String =
                when(entry){
                    resources.getString(R.string.entry_category_cooking) ->
                        resources.getString(R.string.value_category_cooking)
                    resources.getString(R.string.entry_category_furniture) ->
                        resources.getString(R.string.value_category_furniture)
                    resources.getString(R.string.entry_category_movies) ->
                        resources.getString(R.string.value_category_movies)
                    resources.getString(R.string.entry_category_custom) ->
                        resources.getString(R.string.value_category_custom)
                    else ->
                        resources.getString(R.string.value_category_animals)
                }
    }
}