package es.miguelromeral.password.ui.dashboard

import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
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

class DashboardFragment : Fragment(), SearchView.OnQueryTextListener {

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

        /*
        viewModel.passwords.observe(viewLifecycleOwner, Observer {

            binding.partialEmptyCPLayout.visibility = if(it.isNotEmpty()) View.GONE else View.VISIBLE

            adapter.submitList(viewModel.passwords.value?.sortedBy { it.word })
        })
*/

        viewModel.dataChanged.observe(viewLifecycleOwner, Observer { changed ->
            if(changed == true){
                viewModel.passwords.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        viewModel.filteredList.postValue(it)
                    }
                })
                viewModel.finalDataChanged()
            }
        })

        viewModel.filteredList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if(it.isEmpty()){
                binding.partialEmptyCPLayout.visibility = View.VISIBLE
            }else{
                binding.partialEmptyCPLayout.visibility = View.GONE
            }
        })

        setHasOptionsMenu(true)

        return binding.root
    }


    private lateinit var searchView: SearchView

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.custom_password_options, menu)
        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return filter(p0)
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        return filter(p0)
    }

    private fun filter(p0: String?): Boolean{
        return viewModel.filterPasswords(p0)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_custom_password -> {
                navigateToCustomPassword()
                true
            }
            R.id.action_clear_custom_passwords ->{

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Clear All Custom Passwords?")
                builder.setMessage("Are you sure you really want to clear all the passwords you have in this device? This action can not be changed.")
                builder.setNegativeButton("BACK", null)
                builder.setPositiveButton("DELETE") { dialogInterface: DialogInterface, i: Int ->
                    viewModel.clearAllPasswords()
                }

                val dialog = builder.create()
                dialog.show()

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