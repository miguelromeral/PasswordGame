package es.miguelromeral.password.ui.game

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.Fade
import com.transitionseverywhere.TransitionManager
import com.transitionseverywhere.TransitionSet
import com.transitionseverywhere.extra.Scale
import es.miguelromeral.password.R
import es.miguelromeral.password.classes.*
import es.miguelromeral.password.classes.options.Categories
import es.miguelromeral.password.classes.options.Levels
import es.miguelromeral.password.classes.options.Options
import es.miguelromeral.password.classes.database.PasswordDatabase
import es.miguelromeral.password.classes.options.Languages
import es.miguelromeral.password.databinding.FragmentGameBinding
import es.miguelromeral.password.ui.finishedgame.FinishedGameFragmentArgs
import java.util.*

/**
 * A placeholder fragment containing a simple view.
 */
class GameActivityFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var binding: FragmentGameBinding

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var speechRecognizerIntent: Intent

    private lateinit var mediaPlayerSuccess: MediaPlayer
    private lateinit var mediaPlayerFail: MediaPlayer

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var category: String? = Categories.DEFAULT_CATEGORY
        var level: String? = Levels.DEFAULT_LEVEL
        var language: String? = resources.getString(R.string.pref_language_value_english)

        activity?.intent?.extras?.let{
            category = it.getString(ARG_CATEGORY)
            level = it.getString(ARG_LEVEL)
            language = it.getString(ARG_LANGUAGE)
        }

        val disableMic = GameActivityFragmentArgs.fromBundle(requireArguments()).disableMic

        val application = requireNotNull(this.activity).application
        val dataSource = PasswordDatabase.getInstance(application).passwordDatabaseDao

        val source = Options.getSourceByString(
            PreferenceManager.getDefaultSharedPreferences(context).getString(
                requireContext().getString(
                    R.string.pref_words_source_key),
                    "")!!,
            resources
        )

        mediaPlayerSuccess = MediaPlayer.create(requireContext(), R.raw.success)
        mediaPlayerFail = MediaPlayer.create(requireContext(), R.raw.fail)


        val gameTime = (PreferenceManager.getDefaultSharedPreferences(context).getString(
                resources.getString(R.string.pref_timer_key), Options.DEFAULT_TIMER_VALUE) ?: Options.DEFAULT_TIMER_VALUE).toLong()

        val countWords = (PreferenceManager.getDefaultSharedPreferences(context).getString(
                resources.getString(R.string.pref_count_key), Options.DEFAULT_COUNT_VALUE) ?: Options.DEFAULT_COUNT_VALUE).toInt()

        val vmf = GameFactory(dataSource, application, category!!, level!!, language!!, source, gameTime, countWords)

        viewModel = ViewModelProviders.of(this, vmf).get(GameViewModel::class.java)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game, container, false)
        binding.viewModel = viewModel
        val lg = binding.layoutGame


        MobileAds.initialize(requireContext(), getString(R.string.admob_app_id))
        val adRequest = AdRequest.Builder().build()

        binding.adIngame.loadAd(adRequest)
        binding.adIngame.visibility = View.GONE
        binding.adIngame.adListener = object : AdListener() {
            override fun onAdLoaded() {
                binding.adIngame.visibility = View.VISIBLE
                super.onAdLoaded()
            }
        }


        val microphoneEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            requireContext().getString(
                R.string.pref_microphone_key
            ), Options.DEFAULT_MICROPHONE_VALUE)

        val hintsEnabled = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                resources.getString(R.string.pref_hints_key), Options.DEFAULT_HINTS_VALUE)

        if(!hintsEnabled){
            lg.layoutHintsTitle.visibility = View.GONE
        }

        if(microphoneEnabled && hintsEnabled && !disableMic){

            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(requireContext())
            speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Languages.getLanguageLocale(resources, language))
            speechRecognizer.setRecognitionListener(GameRecognitionService(requireContext(), viewModel))

            binding.fabAudio.setOnTouchListener(View.OnTouchListener { view, motionEvent ->

                when(motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        binding.fabAudio.visibility = View.INVISIBLE
                        speechRecognizer.startListening(speechRecognizerIntent)
                    }
                    MotionEvent.ACTION_UP -> {
                        speechRecognizer.stopListening()
                    }
                }

                return@OnTouchListener false
            })

            viewModel.enableFABMic.observe(viewLifecycleOwner, Observer {
                if(it){
                    binding.fabAudio.visibility = View.VISIBLE
                    viewModel.endEnableMic()
                }
            })

        }else{
            binding.clSpeech.visibility = View.GONE
        }

        val manager = GridLayoutManager(activity, 2)
        lg.rvHints.layoutManager = manager

        val adapter = HintAdapter()

        if(hintsEnabled) {
            lg.rvHints.adapter = adapter
        }else{
            lg.rvHints.visibility = View.GONE
        }

        lg.bFail.setOnClickListener {
            TransitionManager.beginDelayedTransition(lg.clGame, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
            viewModel.nextWord(false)
        }
        lg.bSuccess.setOnClickListener {
            TransitionManager.beginDelayedTransition(lg.clGame, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
            viewModel.nextWord(true)
        }

        viewModel.countdown.observe(viewLifecycleOwner, Observer {
            lg.tvTimer.text = it.toString()
        })

        viewModel.liveScore.observe(viewLifecycleOwner, Observer {
            lg.tvScore.text = "+$it"
        })

        viewModel.nFails.observe(viewLifecycleOwner, Observer {
            lg.tvFails.text = it.toString()
        })
        viewModel.nSuccess.observe(viewLifecycleOwner, Observer {
            lg.tvSuccess.text = it.toString()
        })

        viewModel.text.observe(viewLifecycleOwner, Observer {
            //TransitionManager.beginDelayedTransition(lg.clGame, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
            lg.tvPassword.text = it.toUpperCase()
        })

        viewModel.currentIndex.observe(viewLifecycleOwner, Observer { index ->
            if(index != GameViewModel.VALUE_NOT_STARTED){
                if(index == GameViewModel.VALUE_NO_WORDS){
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(R.string.gaf_alert_no_words_title)
                    builder.setMessage(R.string.gaf_alert_no_words_body)
                    builder.setPositiveButton(R.string.gaf_alert_no_words_ok){ dialogInterface, i ->
                        activity?.finish()
                    }

                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }else if(index == GameViewModel.VALUE_PREPARE_TIMER) {
                    viewModel.initTimer()
                } else {
                    lg.bFail.isEnabled = true
                    lg.bSuccess.isEnabled = true


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
                            lg.tvCategory.text = Categories.getCategoryTextFromValue(resources, pwd.category)
                            lg.tvLevel.text = Levels.getLevelTextFromValue(resources, pwd.level)
                            if(hintsEnabled)
                                adapter.submitList(pwd.hintsSplit())
                        }
                    }

                    binding.layoutLoading.visibility = View.GONE

                    lg.clGame.visibility = View.VISIBLE
                }
            }
        })

        viewModel.solutionState.observe(viewLifecycleOwner, Observer {
            when(it){
                GameViewModel.STATE_FAIL -> {
                    playFail()
                    viewModel.endState()
                }
                GameViewModel.STATE_SUCCESS -> {
                    playSuccess()
                    viewModel.endState()
                }
            }
        })

        viewModel.gameFinished.observe(viewLifecycleOwner, Observer {
            if(it == true) {
                viewModel.nextWord(false)

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


    private fun playSuccess(){
        play(mediaPlayerSuccess)
    }
    private fun playFail(){
        play(mediaPlayerFail)
    }

    private fun play(mediaPlayer: MediaPlayer){
        mediaPlayer.seekTo(0)
        mediaPlayer.start()
    }



    fun animatePassword(view: View, viewGroup: ViewGroup, visible: Boolean){
        val set = TransitionSet()
                .addTransition(Scale(0.7f))
                .addTransition(Fade())
                .setInterpolator(
                        if(visible)
                            LinearOutSlowInInterpolator()
                        else
                            FastOutLinearInInterpolator())

        TransitionManager.beginDelayedTransition(viewGroup, set)
        view.visibility = if (visible) View.VISIBLE else View.INVISIBLE
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
