package es.miguelromeral.password.ui.finishedgame

import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
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

        viewModel.score.observe(viewLifecycleOwner, Observer {
            binding.tvScore.text = resources.getString(R.string.fg_score, it.toString())
        })
        viewModel.hits.observe(viewLifecycleOwner, Observer {
            binding.tvSuccess.text = resources.getString(R.string.fg_success, it.toString())
        })
        viewModel.mistakes.observe(viewLifecycleOwner, Observer {
            binding.tvFails.text = resources.getString(R.string.fg_fails, it.toString())
        })


        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_finished_game, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_share_results -> {
                viewModel.shareScore(requireContext())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    companion object {
        const val TAG = "FinishedGameFragment"
    }
}
