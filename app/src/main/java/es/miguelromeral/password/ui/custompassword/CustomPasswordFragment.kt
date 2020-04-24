package es.miguelromeral.password.ui.custompassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Password
import es.miguelromeral.password.classes.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentCustomPasswordBinding
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.ui.Adapters.CustomHintAdapter
import es.miguelromeral.password.ui.listeners.RemoveCustomHintListener
import android.widget.LinearLayout
import es.miguelromeral.password.MainActivity
import android.widget.EditText
import android.content.DialogInterface
import es.miguelromeral.password.ui.finishedgame.FinishedGameFragmentArgs


class CustomPasswordFragment : Fragment() {

    private lateinit var binding: FragmentCustomPasswordBinding
    private lateinit var viewModel: CustomPasswordViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val args = CustomPasswordFragmentArgs.fromBundle(requireArguments())
        val pwdLoaded = args.password

        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao
        val vmf = CustomPasswordFactory(dataSource, application)

        viewModel = ViewModelProviders.of(this, vmf).get(CustomPasswordViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, es.miguelromeral.password.R.layout.fragment_custom_password, container, false)


        binding.password = if(pwdLoaded != null){
                viewModel.setPassword(pwdLoaded)
                binding.bInsert.text = "UPDATE"
                binding.partialSpinnerLevel.spLevel.setSelection(Options.getLevelValueIndex(pwdLoaded.level ?: Options.DEFAULT_LEVEL))
                binding.partialSpinnerCategory.spCategory.setSelection(Options.getCategoryValueIndex(pwdLoaded.category ?: Options.DEFAULT_CATEGORY))
                pwdLoaded
            }else{
                Password(word = "a", hints = "b,c,d")
            }



        val adapter = CustomHintAdapter(RemoveCustomHintListener { item ->
            viewModel.removeHint(item)
        })

        binding.customHintList.adapter = adapter


        binding.bAddHint.setOnClickListener { b ->

            val builder = AlertDialog.Builder(requireContext())

            val input = EditText(context)
            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            input.layoutParams = lp

            builder.setView(input)

            builder.setTitle("New Hint")
            builder.setMessage("Write the new hint.")


                .setCancelable(false)
                .setPositiveButton("OK"){ dialogInterface, i ->
                    viewModel.addHint(input.text.toString())
                }
                .setNegativeButton("Cancel"){ dialogInterface, i ->
                    dialogInterface.cancel()
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
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
