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
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.util.Log
import android.view.View


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

        private lateinit var item: Password
        private lateinit var editListener: CustomPasswordListener
        private lateinit var deleteListener: CustomPasswordListener


        fun bind(item: Password, listener: CustomPasswordListener, removeListener: CustomPasswordListener) {
            val res = itemView.context.resources
            this.item = item
            binding.password = item
            editListener = listener
            deleteListener = removeListener
            binding.tvLanguage.text = Options.getLanguageTextFromValue(res, item.language ?: "-")
            binding.tvWord.text = item.word?.capitalize()
            binding.tvHints.text = item.hints
            binding.tvLevel.text = Options.getStringFromLevelValue(res, item.level ?: "-")
            binding.tvCategory.text = Options.getStringFromCategoryValue(res, item.category ?: "-")

            binding.cardViewAnswer.setOnLongClickListener{ view ->
                showMenu(view)
                true
            }

            binding.tvThreeDots.setOnClickListener { view ->
                showMenu(view)
            }
        }

        private fun showMenu(view: View){
            val popupMenu = PopupMenu(view.context, view)
            popupMenu.inflate(es.miguelromeral.password.R.menu.option_custom_password)
            popupMenu.setOnMenuItemClickListener {
                when(it.itemId){
                    es.miguelromeral.password.R.id.option_edit_password -> {
                        editListener.onClick(item)
                        true
                    }
                    es.miguelromeral.password.R.id.option_delete_password -> {
                        deleteListener.onClick(item)
                        true
                    }
                    else -> false
                }
            }
            popupMenu.show()
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

