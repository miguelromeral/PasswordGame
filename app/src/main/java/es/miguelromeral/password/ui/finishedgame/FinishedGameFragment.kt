package es.miguelromeral.password.ui.finishedgame

import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds

import es.miguelromeral.password.R
import es.miguelromeral.password.classes.database.PasswordDatabase
import es.miguelromeral.password.classes.options.Options
import es.miguelromeral.password.databinding.FragmentGameFinishedBinding
import es.miguelromeral.password.ui.dashboard.CustomPasswordAdapter
import es.miguelromeral.password.ui.listeners.CustomPasswordListener

class FinishedGameFragment : Fragment() {

    private lateinit var viewModel: FinishedGameViewModel
    private lateinit var binding: FragmentGameFinishedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        MobileAds.initialize(requireContext(), getString(R.string.admob_app_id))
        val mInterstitialAd = InterstitialAd(requireContext())
        mInterstitialAd.adUnitId = getString(R.string.admob_interstitial_finishedgame)
        mInterstitialAd.loadAd(AdRequest.Builder().build())

        mInterstitialAd.adListener = object : AdListener(){
            override fun onAdLoaded() {
                mInterstitialAd.show()
                super.onAdLoaded()
            }
        }

        val args = FinishedGameFragmentArgs.fromBundle(requireArguments())

        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao

        //Log.i(TAG, "Success: ${args.success}, fails: ${args.fails}, passwords: ${args.passwords}")

        val vmf = FinishedGameFactory(dataSource, application, args.success, args.fails, args.passwords)

        viewModel = ViewModelProviders.of(this, vmf).get(FinishedGameViewModel::class.java)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_game_finished, container, false)
        binding.viewModel = viewModel


        val hintsEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                resources.getString(R.string.pref_hints_key), Options.DEFAULT_HINTS_VALUE)


        val badgesShown = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            resources.getString(R.string.pref_badges_key), Options.DEFAULT_BADGES_VALUE)

        val adapter =
                AnswersAdapter(CustomPasswordListener { pwd -> viewModel.saveWord(pwd) }, hintsEnabled, badgesShown)
        binding.answersList.adapter = adapter

        viewModel.listOfWords.observe(viewLifecycleOwner, Observer {
            it?.let{
                adapter.submitList(it)
            }
        })

        viewModel.added.observe(viewLifecycleOwner, Observer {
            if(!it.isEmpty()){
                Toast.makeText(context, getString(R.string.fg_action_save_success, it), Toast.LENGTH_LONG).show()
                viewModel.endAdd()
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

        viewModel.fastest.observe(viewLifecycleOwner, Observer {
            binding.tvFastest.text = if(it == null){
                resources.getString(R.string.fg_fastest_empty)
            }else{
                if(it.isEmpty()){
                    resources.getString(R.string.fg_fastest_empty)
                }else{
                    resources.getString(R.string.fg_fastest, it)
                }
            }
        })

        viewModel.morePoints.observe(viewLifecycleOwner, Observer {
            binding.tvMoreScore.text = if(it == null){
                resources.getString(R.string.fg_morePoints_empty)
            }else{
                if(it.isEmpty()){
                    resources.getString(R.string.fg_morePoints_empty)
                }else{
                    resources.getString(R.string.fg_morePoints, it)
                }
            }
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
