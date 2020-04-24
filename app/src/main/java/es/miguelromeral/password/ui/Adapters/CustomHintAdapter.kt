package es.miguelromeral.password.ui.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.password.databinding.ItemCustomHintBinding


class CustomHintAdapter : ListAdapter<String, CustomHintAdapter.ViewHolder>(HintDiffCallback()){

    override fun onBindViewHolder(holder:ViewHolder, position:Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor (val binding: ItemCustomHintBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String) {
            binding.hint = item
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCustomHintBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    class HintDiffCallback : DiffUtil.ItemCallback<String>(){
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem.equals(newItem)
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
}

