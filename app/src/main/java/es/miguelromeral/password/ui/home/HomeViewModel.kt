package es.miguelromeral.password.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {

    private val _filterLevel = MutableLiveData<Boolean>(false)
    val filterLevel = _filterLevel

    private val _filterCategory = MutableLiveData<Boolean>(false)
    val filterCategory  = _filterCategory

}