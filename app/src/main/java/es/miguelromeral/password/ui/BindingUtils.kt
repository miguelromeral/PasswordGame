package es.miguelromeral.password.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("timeFormatted")
fun TextView.setTimeFormatted(item: Password) {
    item?.let {
        text = formatTime(item.time ?: 0)
    }
}

fun formatTime(time: Long): String{
    val dou = time.toDouble() / 1000
    return dou.format(1)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)





@BindingAdapter("customPasswordText")
fun TextView.setCustomPasswordText(custom: Boolean){
    text = resources.getString(if(custom) R.string.fg_word_custom else R.string.fg_word_default)
}

@BindingAdapter("solvedPasswordText")
fun TextView.setSolvedPasswordText(pwd: Password){
    text = resources.getString(if(pwd.solved ?: false) R.string.fg_answer_solved else R.string.fg_answer_failed)
}


@BindingAdapter("hintsFormatted")
fun TextView.setCustomPasswordText(pwd: Password){
    var formatted = ""
    pwd.hintsSplit()?.let { list ->
        for (hint in list) {
            formatted += "${hint}${Password.SEPARATOR} "
        }
        if(list.isNotEmpty())
            formatted = formatted.substring(0, formatted.length - 2)
    }

    text = if (formatted.isNullOrBlank()) resources.getString(R.string.cp_no_hints) else formatted
}