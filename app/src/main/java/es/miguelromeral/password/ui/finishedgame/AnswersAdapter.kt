package es.miguelromeral.password.ui.finishedgame

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Color.MAGENTA
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.databinding.ItemAnswerBinding
import es.miguelromeral.password.ui.setTimeFormatted
import es.miguelromeral.password.ui.settings.SettingsFragment


class AnswersAdapter : ListAdapter<Password, AnswersAdapter.ViewHolder>(
    HintDiffCallback()
){

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor (val binding: ItemAnswerBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Password) {
            val res = binding.cardViewAnswer.resources
            binding.password = item
            binding.tvState.text = item.score.toString()
            binding.tvWord.text = item.word?.capitalize()
/*
            val nightModeFlags = res.configuration.uiMode

            when(nightModeFlags){
                Configuration.UI_MODE_NIGHT_YES -> Log.i("TEST", "Night: Yes")
                    Configuration.UI_MODE_NIGHT_NO -> Log.i("TEST", "Night: No")
                Configuration.UI_MODE_NIGHT_UNDEFINED -> Log.i("TEST", "Night: Undefined")
            }*/

            val nightMode = SettingsFragment.isNightThemeEnabled(binding.cardViewAnswer.context)

            binding.cardViewAnswer.setBackgroundColor(res.getColor(
                    if(item.solved)
                        if(nightMode) R.color.darkGreen else R.color.green
                    else if(item.failed)
                        if(nightMode) R.color.darkRed else R.color.lightRed
                    else
                        R.color.colorPrimaryDark
            ))
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemAnswerBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    class HintDiffCallback : DiffUtil.ItemCallback<Password>(){
        override fun areItemsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem.random == newItem.random
        }

        override fun areContentsTheSame(oldItem: Password, newItem: Password): Boolean {
            return oldItem == newItem
        }
    }
}

