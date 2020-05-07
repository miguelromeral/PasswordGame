package es.miguelromeral.password.ui.utils

import android.app.Activity
import androidx.core.app.ActivityCompat

/*
fun createSpinnerAdapter_Level(context: Context): ArrayAdapter<String> {

    val resources = context.resources
    val items = arrayOf(
        resources.getString(es.miguelromeral.password.R.string.dpm_level_mixed),
        resources.getString(es.miguelromeral.password.R.string.dpm_level_easy),
        resources.getString(es.miguelromeral.password.R.string.dpm_level_medium),
        resources.getString(es.miguelromeral.password.R.string.dpm_level_hard)
    )
    val items_value = arrayOf<String>(Options.DEFAULT_LEVEL, "easy", "medium", "hard")

    return ArrayAdapter<String>(context,
        R.layout.simple_spinner_dropdown_item, items)
}
*/


fun requestPermission(activity: Activity, permission: String, code: Int){
    ActivityCompat.requestPermissions(activity, arrayOf(permission), code)
}