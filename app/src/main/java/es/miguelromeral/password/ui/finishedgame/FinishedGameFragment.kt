package es.miguelromeral.password.ui.finishedgame

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import es.miguelromeral.password.R
import es.miguelromeral.password.databinding.FragmentGameFinishedBinding

class FinishedGameFragment : Fragment() {

    private lateinit var viewModel: FinishedGameViewModel
    private lateinit var binding: FragmentGameFinishedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val args = FinishedGameFragmentArgs.fromBundle(requireArguments())

        //Log.i(TAG, "Success: ${args.success}, fails: ${args.fails}, passwords: ${args.passwords}")

        val vmf = FinishedGameFactory(args.success, args.fails, args.passwords)

        viewModel = ViewModelProviders.of(this, vmf).get(FinishedGameViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_finished, container, false)
        binding.viewModel = viewModel

        val adapter = AnswersAdapter()
        binding.answersList.adapter = adapter

        viewModel.listOfWords.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        return binding.root
    }


    companion object {
        const val TAG = "FinishedGameFragment"

        const val ARG_PASSWORDS = "passwords"
    }
}
