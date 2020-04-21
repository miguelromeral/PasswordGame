package es.miguelromeral.password.ui.finishedgame

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import es.miguelromeral.password.R

class FinishedGameFragment : Fragment() {

    companion object {
        fun newInstance() = FinishedGameFragment()
    }

    private lateinit var viewModel: FinishedGameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game_finished, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FinishedGameViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
