package es.miguelromeral.password.ui.custompassword

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import es.miguelromeral.password.databinding.FragmentCustomPasswordBinding
import es.miguelromeral.password.ui.listeners.RemoveCustomHintListener
import android.widget.LinearLayout
import android.widget.EditText
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.*
import es.miguelromeral.password.classes.options.Categories
import es.miguelromeral.password.classes.options.Languages
import es.miguelromeral.password.classes.options.Levels
import es.miguelromeral.password.classes.database.PasswordDatabase


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
                binding.bInsert.text = requireContext().getString(R.string.cpf_button_update_password)

                binding.partialSpinnerLevel.spLevel.setSelection(Levels.getLevelValueIndex(resources, pwdLoaded.level))
                binding.partialSpinnerLanguage.spLanguage.setSelection(Languages.getLanguageValueIndex(resources, pwdLoaded.language))
                binding.partialSpinnerCategory.spCategory.setSelection(Categories.getCategoryValueIndex(resources, pwdLoaded.category))
                pwdLoaded
            }else{
                Password()
            }



        val adapter =
            CustomHintAdapter(RemoveCustomHintListener { item ->
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

            builder.setTitle(R.string.cpf_alert_new_hint_title)
            builder.setMessage(R.string.cpf_alert_new_hint_body)

                .setCancelable(false)
                .setPositiveButton(R.string.cpf_alert_new_hint_ok){ dialogInterface, i ->
                    viewModel.addHint(input.text.toString())
                }
                .setNegativeButton(R.string.cpf_alert_new_hint_cancel){ dialogInterface, i ->
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
                pwd.level = Levels.getLevelValueFromEntry(resources, binding.partialSpinnerLevel.spLevel.selectedItem.toString())
                pwd.category = Categories.getCategoryValueFromEntry(resources, binding.partialSpinnerCategory.spCategory.selectedItem.toString())
                pwd.hints = viewModel.getHintsFormatted()

                pwd.language = Languages.getLanguageValueFromEntry(resources, binding.partialSpinnerLanguage.spLanguage.selectedItem.toString())

                if(pwd.word.isNotEmpty())
                    viewModel.addPassword(resources, pwd)
            }
        }



        return binding.root
    }

}
