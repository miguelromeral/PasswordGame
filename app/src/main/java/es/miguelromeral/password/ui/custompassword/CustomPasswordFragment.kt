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
import android.widget.ArrayAdapter
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.ui.Adapters.CustomHintAdapter


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



        val adapter = CustomHintAdapter()
        binding.customHintList.adapter = adapter


        binding.bAddHint.setOnClickListener {b ->
            //adapter.submitList(listOf("test","dos"))

            viewModel.addHint()
        }

        viewModel.hints.observe(viewLifecycleOwner, Observer {
            it?.let { list ->
                adapter.submitList(list.sorted())
            }
        })


        viewModel.warning.observe(viewLifecycleOwner, Observer {
            it?.let{
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                findNavController().navigate(CustomPasswordFragmentDirections.actionCustomPasswordFragmentToNavigationDashboard())
            }
        })

        binding.bInsert.setOnClickListener { view ->
            binding.password?.let{ pwd ->
                pwd.level = Options.getLevelValue(binding.partialSpinnerLevel.spLevel.selectedItemPosition)
                pwd.category = Options.getCategoryValue(binding.partialSpinnerCategory.spCategory.selectedItemPosition)
                pwd.hints = viewModel.getHintsFormatted()

                if(pwd.word.isNotEmpty())
                    viewModel.addPassword(pwd)
            }
        }



        return binding.root
    }

}
