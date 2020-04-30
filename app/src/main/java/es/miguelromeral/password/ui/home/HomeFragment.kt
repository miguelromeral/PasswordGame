package es.miguelromeral.password.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.preference.PreferenceManager
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.options.Categories
import es.miguelromeral.password.classes.options.Levels
import es.miguelromeral.password.databinding.FragmentHomeBinding
import es.miguelromeral.password.ui.game.GameActivity
import kotlinx.android.synthetic.main.partial_welcome.view.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.viewModel = homeViewModel

        binding.layoutWelcome.bRules.setOnClickListener {
            val i = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://github.com/miguelromeral/PasswordGame/blob/master/USAGE.md#game-rules")
            }
            startActivity(i)
        }

        binding.bStartGame.setOnClickListener { b ->
            b.isEnabled = false
            Toast.makeText(context, R.string.fh_toast_preparing, Toast.LENGTH_LONG).show()

            val category =
                if(homeViewModel.filterCategory.value!!)
                    Levels.getLevelValueFromEntry(resources, binding.partialSpinnerCategory.spCategory.selectedItem.toString())
                else
                    Categories.DEFAULT_CATEGORY


            val level =
                if(homeViewModel.filterLevel.value!!)
                    Levels.getLevelValueFromEntry(resources, binding.partialSpinnerLevel.spLevel.selectedItem.toString())
                else
                    Levels.DEFAULT_LEVEL

            val language = PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(
                    resources.getString(R.string.pref_language_key), null) ?:
                    resources.getString(R.string.pref_language_value_english)

            GameActivity.newInstance(requireContext(),
                category,
                level,
                language)
        }

        homeViewModel.filterCategory.observe(viewLifecycleOwner, Observer {
            binding.partialSpinnerCategory.categoryLayout.visibility =
                if(it){
                    View.VISIBLE
                }else{
                    View.GONE
                }
        })

        homeViewModel.filterLevel.observe(viewLifecycleOwner, Observer {
            binding.partialSpinnerLevel.levelLayout.visibility =
                if(it){
                    View.VISIBLE
                }else{
                    View.GONE
                }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.bStartGame.isEnabled = true
    }
}