package es.miguelromeral.password.ui.game

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.provider.Settings
import android.speech.RecognitionListener
import android.speech.RecognitionService
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.GameRecognitionService
import es.miguelromeral.password.classes.Options
import es.miguelromeral.password.classes.PasswordDatabase
import es.miguelromeral.password.databinding.FragmentGameBinding
import es.miguelromeral.password.databinding.FragmentHomeBinding
import es.miguelromeral.password.databinding.PartialGameBinding
import es.miguelromeral.password.ui.Adapters.HintAdapter
import es.miguelromeral.password.ui.finishedgame.FinishedGameFragment
import es.miguelromeral.password.ui.home.HomeViewModel
import kotlinx.android.synthetic.main.partial_game.view.*
import java.util.*
import java.util.jar.Manifest

/**
 * A placeholder fragment containing a simple view.
 */
class GameActivityFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var category: String? = Options.DEFAULT_CATEGORY
        var level: String? = Options.DEFAULT_LEVEL
        var language: String? = Options.DEFAULT_LANGUAGE

        activity?.intent?.extras?.let{
            category = it.getString(ARG_CATEGORY)
            level = it.getString(ARG_LEVEL)
            language = it.getString(ARG_LANGUAGE)
        }

        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao

        val source = Options.getSourceByString(
            PreferenceManager.getDefaultSharedPreferences(context).getString(
                requireContext().getString(
                    R.string.pref_words_source_key),
                    "")!!,
            resources
        )

        val vmf = GameFactory(dataSource, application, category!!, level!!, language!!, source)

        viewModel = ViewModelProviders.of(this, vmf).get(GameViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        binding.viewModel = viewModel
        val lg = binding.layoutGame

        val microphoneEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            requireContext().getString(
                R.string.pref_microphone_key
            ), Options.DEFAULT_MICROPHONE_VALUE)

        if(microphoneEnabled){

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            speechRecognizer.setRecognitionListener(GameRecognitionService(requireContext(), viewModel))

            binding.fabAudio.setOnTouchListener(View.OnTouchListener { view, motionEvent ->

                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN ->
                        speechRecognizer.startListening(speechRecognizerIntent)
                    MotionEvent.ACTION_UP ->
                        speechRecognizer.stopListening()
                }

                return@OnTouchListener false
            })

        }else{
            binding.clSpeech.visibility = View.GONE
        }

        val manager = GridLayoutManager(activity, 2)
        lg.rvHints.layoutManager = manager

        val adapter = HintAdapter()
        lg.rvHints.adapter = adapter

        lg.bFail.setOnClickListener {
            viewModel.nextWord(false)
        }
        lg.bSuccess.setOnClickListener {
            viewModel.nextWord(true)
        }

        viewModel.countdown.observe(viewLifecycleOwner, Observer {
            lg.tvTimer.text = it.toString()
        })

        viewModel.nFails.observe(viewLifecycleOwner, Observer {
            lg.tvFails.text = it.toString()
        })
        viewModel.nSuccess.observe(viewLifecycleOwner, Observer {
            lg.tvSuccess.text = it.toString()
        })

        viewModel.text.observe(viewLifecycleOwner, Observer {
            lg.tvPassword.text = it.toUpperCase()
        })

        viewModel.currentIndex.observe(viewLifecycleOwner, Observer { index ->
            if(index != GameViewModel.VALUE_NOT_STARTED){
                if(index == GameViewModel.VALUE_NO_WORDS){
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("No words found")
                    builder.setMessage("We haven't been able to find words according to that criteria.\n" +
                            "Please, try filtering other values or try with these again later.")


                    builder.setPositiveButton("Go Home"){ dialogInterface, i ->
                        activity?.finish()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }else if(index == GameViewModel.VALUE_PREPARE_TIMER) {
                    viewModel.initTimer()
                } else {
                    lg.bFail.isEnabled = true
                    lg.bSuccess.isEnabled = true
                    lg.tvIndex.text = index.toString()

                    viewModel.listened.observe(viewLifecycleOwner, Observer {
                        binding.etSpeech.setText(it)
                        binding.etSpeech.setSelection(it.length)
                    })

                    val list = viewModel.listOfWords.value
                    Log.i(TAG, "List of passwords: $list")
                    list?.let { list ->
                        if (index < list.size) {
                            val pwd = list[index]
                            lg.tvPassword.text = pwd.word
                            adapter.submitList(pwd.hintsSplit())
                        }
                    }

                    binding.layoutLoading.visibility = View.GONE

                    lg.clGame.visibility = View.VISIBLE
                }
            }
        })

        viewModel.gameFinished.observe(viewLifecycleOwner, Observer {
            if(it == true) {

                val dir = GameActivityFragmentDirections
                    .actionGameActivityFragmentToFinishedGameFragment(viewModel.listOfWords.value!!.toTypedArray())

                dir.success = viewModel.nSuccess.value ?: 0
                dir.fails = viewModel.nFails.value ?: 0

                findNavController().navigate(dir)

                viewModel.finishGameOK()
            }
        })

        return binding.root
    }


    /*
        override fun onResume() {
            super.onResume()
            viewModel.initSettings()
        }
    */

    companion object {
        const val TAG = "GameActivityFragment"

        const val ARG_CATEGORY = "category"
        const val ARG_LEVEL = "level"
        const val ARG_LANGUAGE = "language"
    }
}
