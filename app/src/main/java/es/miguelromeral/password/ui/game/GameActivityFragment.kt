package es.miguelromeral.password.ui.game

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import es.miguelromeral.password.R
import es.miguelromeral.password.databinding.FragmentGameBinding
import es.miguelromeral.password.databinding.FragmentHomeBinding
import es.miguelromeral.password.ui.Adapters.HintAdapter
import es.miguelromeral.password.ui.home.HomeViewModel

/**
 * A placeholder fragment containing a simple view.
 */
class GameActivityFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(GameViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        binding.viewModel = viewModel

        val adapter = HintAdapter()
        binding.rvHints.adapter = adapter

        binding.bFail.setOnClickListener { viewModel.nextWord() }
        binding.bSuccess.setOnClickListener { viewModel.nextWord() }

        viewModel.text.observe(viewLifecycleOwner, Observer {
            binding.tvPassword.text = it
        })

        viewModel.currentIndex.observe(viewLifecycleOwner, Observer { index ->
            if(index != GameViewModel.DEFAULT_INDEX){
                binding.tvIndex.text = index.toString()

                val list = viewModel.listOfWords.value
                Log.i(TAG, "List of passwords: $list")
                list?.let{ list ->
                    if(index < list.size) {
                        val pwd = list[index]
                        binding.tvPassword.text = pwd.word
                        adapter.submitList(pwd.hintsSplit)
                    }
                }
            }
        })



        return binding.root
    }


    companion object {
        const val TAG = "GameActivityFragment"
    }
}
