package es.miguelromeral.password.ui.Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.password.classes.CustomPassword
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.databinding.ItemAnswerBinding
import es.miguelromeral.password.databinding.ItemCustomPasswordsBinding
import es.miguelromeral.password.databinding.ItemHintBinding
import es.miguelromeral.password.ui.setTimeFormatted


class CustomPasswordAdapter : ListAdapter<CustomPassword, CustomPasswordAdapter.ViewHolder>(HintDiffCallback()){

    override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (val binding: ItemCustomPasswordsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CustomPassword) {
            //val res = itemView.context.resources
            binding.password = item
            binding.tvWord.text = item.word?.capitalize()
            binding.tvHints.text = item.hints
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCustomPasswordsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


    class HintDiffCallback : DiffUtil.ItemCallback<CustomPassword>(){
        override fun areItemsTheSame(oldItem: CustomPassword, newItem: CustomPassword): Boolean {
            return oldItem.word == newItem.word
        }

        override fun areContentsTheSame(oldItem: CustomPassword, newItem: CustomPassword): Boolean {
            return oldItem == newItem
        }
    }
}

