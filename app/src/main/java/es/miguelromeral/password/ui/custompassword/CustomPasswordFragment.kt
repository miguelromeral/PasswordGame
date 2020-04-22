package es.miguelromeral.password.ui.custompassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentCustomPasswordBinding
import es.miguelromeral.password.ui.game.GameActivity
import es.miguelromeral.password.ui.home.HomeViewModel

class CustomPasswordFragment : Fragment() {

    private lateinit var binding: FragmentCustomPasswordBinding
    private lateinit var viewModel: CustomPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao
        val vmf = CustomPasswordFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this, vmf).get(CustomPasswordViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_custom_password, container, false)

        return binding.root
    }

}
