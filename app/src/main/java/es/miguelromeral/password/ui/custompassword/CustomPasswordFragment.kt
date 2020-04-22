package es.miguelromeral.password.ui.custompassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentCustomPasswordBinding
import es.miguelromeral.password.ui.Adapters.AnswersAdapter
import es.miguelromeral.password.ui.Adapters.CustomPasswordAdapter
import es.miguelromeral.password.ui.dashboard.DashboardFragmentDirections
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
        binding.password = Password(word = "a", hints = "b,c,d")

        viewModel.warning.observe(viewLifecycleOwner, Observer {
            it?.let{
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                findNavController().navigate(CustomPasswordFragmentDirections.actionCustomPasswordFragmentToNavigationDashboard())
            }
        })

        binding.bInsert.setOnClickListener { view ->
            viewModel.addPassword(binding.password)
        }

        return binding.root
    }

}
