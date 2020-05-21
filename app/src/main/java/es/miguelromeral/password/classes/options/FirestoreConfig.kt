package es.miguelromeral.password.classes.options

import android.content.res.Resources
import es.miguelromeral.password.R

class FirestoreConfig {
    companion object {



        const val COLL_PASSWORD = "passwords"
        const val COLL_PASSWORD_DEV = "passwords_dev"

        const val CONFIG_CACHE = "collectionCache"
        const val CONFIG_CACHE_DEFAULT_VALUE = "0"

        const val FIELD_CATEGORY = "category"
        const val FIELD_LEVEL = "level"
        const val FIELD_LANGUAGE = "language"
        const val FIELD_RANDOM = "random"
    }
}