package es.miguelromeral.password.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.databinding.FragmentHomeBinding
import es.miguelromeral.password.ui.game.GameActivity
import es.miguelromeral.password.ui.game.GameActivityFragment

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

        binding.bStartGame.setOnClickListener { b ->
            b.isEnabled = false
            Toast.makeText(context, "Preparing the game!", Toast.LENGTH_LONG).show()

            val category =
                if(homeViewModel.filterCategory.value!!)
                    Options.getCategoryValue(binding.partialSpinnerCategory.spCategory.selectedItemPosition)
                else
                    Options.getCategoryValue(-1)


            val level =
                if(homeViewModel.filterLevel.value!!)
                    Options.getLevelValue(binding.partialSpinnerLevel.spLevel.selectedItemPosition)
                else
                    Options.getLevelValue(-1)

            GameActivity.newInstance(requireContext(),
                category,
                level,
                "language")
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