package es.miguelromeral.password.ui.dashboard

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentDashboardBinding
import es.miguelromeral.password.ui.listeners.CustomPasswordListener

class DashboardFragment : Fragment() {

    private lateinit var viewModel: DashboardViewModel
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao
        val vmf = DashboardFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this, vmf).get(DashboardViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)

        val adapter =
            CustomPasswordAdapter(CustomPasswordListener { pwd ->
                navigateToCustomPassword(pwd)
            },
            CustomPasswordListener { item ->
                viewModel.deletePassword(item)
            })

        binding.customPasswordsList.adapter = adapter

        viewModel.passwords.observe(viewLifecycleOwner, Observer {
            adapter.submitList(viewModel.passwords.value?.sortedBy { it.word })
        })

        setHasOptionsMenu(true)

        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.custom_password_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_custom_password -> {
                navigateToCustomPassword()
                true
            }
            R.id.action_clear_custom_passwords ->{
                viewModel.clearAllPasswords()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToCustomPassword(pwd: Password? = null){
        var dir = DashboardFragmentDirections.actionNavigationDashboardToCustomPasswordFragment()
        dir.password = pwd
        findNavController().navigate(dir)
    }
}