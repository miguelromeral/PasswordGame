package es.miguelromeral.password.ui.listeners

import es.miguelromeral.password.classes.Password

class CustomPasswordListener(val clickListener: (item: Password) -> Unit){
    fun onClick(item: Password) = clickListener(item)
}