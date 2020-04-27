package es.miguelromeral.password.ui.custompassword

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.password.databinding.ItemCustomHintBinding
import es.miguelromeral.password.ui.listeners.RemoveCustomHintListener


class CustomHintAdapter(
    val removeCustomHintListener: RemoveCustomHintListener
) : ListAdapter<String, CustomHintAdapter.ViewHolder>(HintDiffCallback()){

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val item = getItem(position)
        holder.bind(item, removeCustomHintListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor (val binding: ItemCustomHintBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: String, removeCustomHintListener: RemoveCustomHintListener) {
            binding.hint = item
            binding.removeListener = removeCustomHintListener
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCustomHintBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
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

