package es.miguelromeral.password.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import es.miguelromeral.password.classes.Password
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(item: Password) {
    item?.let {
        text = formatTime(item.time)
    }
}

fun formatTime(time: Long): String{
    val dou = time.toDouble() / 1000
    return dou.format(1)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)