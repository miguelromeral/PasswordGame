package es.miguelromeral.password.ui.dashboard

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import es.miguelromeral.password.ui.activity.MainActivity
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.database.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentDashboardBinding
import es.miguelromeral.password.ui.listeners.CustomPasswordListener
import es.miguelromeral.password.ui.utils.requestPermission

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


        viewModel.dataChanged.observe(viewLifecycleOwner, Observer { changed ->
            if(changed == true){
                viewModel.passwords.observe(viewLifecycleOwner, Observer {
                    it?.let {
                        viewModel.filteredList.postValue(it.sortedBy { it.word })
                    }
                })
                viewModel.finalDataChanged()
            }
        })

        viewModel.filteredList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
                if (it.isEmpty()) {
                    binding.partialEmptyCPLayout.visibility = View.VISIBLE
                } else {
                    binding.partialEmptyCPLayout.visibility = View.GONE
                }
            }
        })


        binding.fabAdd.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
            try {
                navigateToCustomPassword()
                true
            }catch (e: Exception){
                return@OnTouchListener false
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
            R.id.action_clear_custom_passwords ->{

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.df_alert_clear_db_title)
                builder.setMessage(R.string.df_alert_clear_db_body)
                builder.setNegativeButton(R.string.df_alert_clear_db_cancel, null)
                builder.setPositiveButton(R.string.df_alert_clear_db_ok) { dialogInterface: DialogInterface, i: Int ->
                    viewModel.clearAllPasswords()
                }

                val dialog = builder.create()
                dialog.show()

                true
            }
            R.id.action_import -> {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.df_alert_import_title)
                builder.setMessage(R.string.df_alert_import_body)
                builder.setNegativeButton(R.string.df_alert_import_cancel, null)
                builder.setPositiveButton(R.string.df_alert_import_ok) { dialogInterface: DialogInterface, i: Int ->
                    viewModel.importSecrets(requireActivity())
                }
                val dialog = builder.create()
                dialog.show()

                true
            }
            R.id.action_export -> {

                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.df_alert_export_title)
                builder.setMessage(R.string.df_alert_export_body)
                builder.setNegativeButton(R.string.df_alert_export_cancel, null)
                builder.setPositiveButton(R.string.df_alert_export_ok) { dialogInterface: DialogInterface, i: Int ->
                    exportSecrets(requireContext())
                }
                val dialog = builder.create()
                dialog.show()

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToCustomPassword(pwd: Password? = null){
        var dir =
            DashboardFragmentDirections.actionNavigationDashboardToCustomPasswordFragment()
        dir.password = pwd
        findNavController().navigate(dir)
    }


    private fun exportSecrets(it: Context){
        val permission = Manifest.permission.WRITE_EXTERNAL_STORAGE

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(requireActivity(), permission) != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Should we show an explanation?
            if (!ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), permission)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.


                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(es.miguelromeral.password.R.string.df_warning_export_title)
                builder.setMessage(es.miguelromeral.password.R.string.df_warning_export_body)
                builder.setNeutralButton(es.miguelromeral.password.R.string.df_warning_export_cancel, null)
                builder.setPositiveButton(es.miguelromeral.password.R.string.df_warning_export_ok){ dialogInterface, i ->
                    requestPermission(requireActivity(), permission, MainActivity.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
                }

                val dialog: AlertDialog = builder.create()
                dialog.show()

            }else {

                // No explanation needed, we can request the permission.
                requestPermission(requireActivity(), permission, MainActivity.REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION)
            }
        } else {
            (requireActivity() as MainActivity).exportSecrets(it)
        }
    }
}