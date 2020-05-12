package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class FirestoreConfig {
    companion object {



        fun getCollectionByLocale(res: Resources, value: String?): String =
            when(value){
                res.getString(R.string.pref_language_value_english) -> COLL_PASSWORD_EN
                res.getString(R.string.pref_language_value_spanish) -> COLL_PASSWORD_ES
                else -> COLL_PASSWORD_EN
            }

        const val COLL_PASSWORD = "passwords"
        const val COLL_PASSWORD_EN = "passwords_en"
        const val COLL_PASSWORD_ES = "passwords_es"
        const val COLL_PASSWORD_DEV = "passwords_dev"

        const val FIELD_CATEGORY = "category"
        const val FIELD_LEVEL = "level"
        const val FIELD_LANGUAGE = "language"
        const val FIELD_RANDOM = "random"
    }
}