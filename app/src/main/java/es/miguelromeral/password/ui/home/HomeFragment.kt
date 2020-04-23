package es.miguelromeral.password.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import es.miguelromeral.password.R
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

        binding.textHome.text = "eyeyeyeyey"

        binding.bStartGame.setOnClickListener { b ->
            GameActivity.newInstance(requireContext(),
                binding.spCategory.selectedItem.toString(),
                binding.partialSpinnerLevel.spLevel.selectedItem.toString(),
                "language")
        }

        return binding.root
    }
}