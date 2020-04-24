package es.miguelromeral.password.ui.dashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.databinding.ItemCustomPasswordsBinding
import es.miguelromeral.password.ui.listeners.CustomPasswordListener


class CustomPasswordAdapter(
    val listener: CustomPasswordListener,
    val removeListener: CustomPasswordListener
) : ListAdapter<Password, CustomPasswordAdapter.ViewHolder>(
    HintDiffCallback()
){

    override fun onBindViewHolder(holder: ViewHolder, position:Int) {
        val item = getItem(position)
        holder.bind(item, listener, removeListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    class ViewHolder private constructor (val binding: ItemCustomPasswordsBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Password, listener: CustomPasswordListener, removeListener: CustomPasswordListener) {
            val res = itemView.context.resources
            binding.password = item
            binding.listener = listener
            binding.tvWord.text = item.word?.capitalize()
            binding.tvHints.text = item.hints
            binding.tvLevel.text = Options.getStringFromLevelValue(res, item.level ?: "-")
            binding.tvCategory.text = Options.getStringFromCategoryValue(res, item.category ?: "-")



            binding.tvThreeDots.setOnClickListener { view ->
                val popupMenu = PopupMenu(view.context, view)
                popupMenu.inflate(R.menu.option_custom_password)
                popupMenu.setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.option_edit_password -> {
                            listener.onClick(item)
                            true
                        }
                        R.id.option_delete_password -> {
                            removeListener.onClick(item)
                            true
                        }
                        else -> false
                    }
                }
                popupMenu.show()
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemCustomPasswordsBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
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

