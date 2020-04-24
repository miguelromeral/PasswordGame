package es.miguelromeral.password.ui.listeners

class RemoveCustomHintListener(val clickListener: (item: String) -> Unit){
    fun onClick(item: String) = clickListener(item)
}