package workwork.test.andropediagits.presenter.lesson.victorine

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback


import com.google.android.material.snackbar.Snackbar
import com.my.target.ads.Reward
import com.my.target.common.models.IAdLoadingError
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.ErrorStateView
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.core.utils.Constatns.DEFAULT_HEART_COUNT
import workwork.test.andropediagits.core.utils.CustomTimerUtil
import workwork.test.andropediagits.core.utils.GoogleAdManager
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.databinding.FragmentVictorineBinding
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.StrikeModeState
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.lesson.victorine.customview.AnimatedCircleView

import workwork.test.andropediagits.presenter.lesson.victorine.viewmodel.VictorineViewModel
import java.util.concurrent.atomic.AtomicBoolean

@AndroidEntryPoint
class VictorineFragment : Fragment() {
    private var adTarget: com.my.target.ads.RewardedAd? = null
    private var isLoading = false
    private var startTimerViewAds = false
    private var heartAndropointMinus = false
    private var heartCount = DEFAULT_HEART_COUNT
private var backPressedOnce = false
    private var IsFeedback = false
    private var googleMobileAdsConsentManager: GoogleAdManager? = null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var timerObser: Observer<Long>? = null
    private var binding: FragmentVictorineBinding? = null
    private var clickCount = 0
    private var pref: SharedPreferences? = null
    private val args: VictorineFragmentArgs by navArgs()
    private val viewModel: VictorineViewModel by viewModels()
    private var isFirstItemSelected = false
    private var isSecondItemSelected = false
    private var isThirdItemSelected = false
    private var isForthItemSelected = false
    private var isFifthItemSelected = false
    private var animatedCircleViewFirst: AnimatedCircleView? = null
    private var animatedCircleViewSecond: AnimatedCircleView? = null
    private var animatedCircleViewThird: AnimatedCircleView? = null
    private var animatedCircleViewFourth: AnimatedCircleView? = null
    private var animatedCircleViewFifth: AnimatedCircleView? = null
    private var currentIndex = 0
    private var progress = 0
    private var victorinesQuestionsCount:Int = 0
    private var victorinesQuestions: List<VictorineEntity>? = null
    private var victorineAnswerVariants: List<VictorineAnswerVariantEntity>? = null
    private var correctAnswers = 0
    private var isStrikeModeAndropointFlag = false
    private var testCheckResultFlag = false
    private var isThemePassed = false
    private var misstakeAnswers = 0
    private var victorineEnded = false
    private var victorineExit = false
    private var victorineSec: Long = 0
    private var isInfinityHearts = false
    private var isUseNextTest = false

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        val moreMenu = menu.findItem(R.id.action_more)
        moreMenu.isVisible = googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentVictorineBinding.inflate(inflater, container, false)
//        viewModel.getHeartsUser({
//            heartCount = heartCount.plus(it)
//        })
        googleMobileAdsConsentManager = GoogleAdManager(requireActivity())
        adTarget = com.my.target.ads.RewardedAd(1455175, requireContext())
        setUpHearts()
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    private fun setUpHearts(){
        binding?.dimViewVictorine?.visibility = View.VISIBLE
        ShowDialogHelper.loadDialog(requireContext(),{
            binding?.dimViewVictorine?.visibility = View.GONE
        })
        var heartCountUseCaesvar = 0
        viewModel.getHeartsUser({ heartCountUseCaes->
            heartCountUseCaesvar = heartCountUseCaes
        },{isInfinity->
            isInfinityHearts = isInfinity
        },{state->
            when(state){
                ErrorEnum.SUCCESS->{

                    heartCount = heartCount?.plus(heartCountUseCaesvar) ?: 2
                    startTimerFun()
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        if(isInfinityHearts){
                            animateNumber(
                                binding?.tvCountHeart,
                                requireContext(),
                                "∞"
                            )
                        }else {
                            animateNumber(
                                binding?.tvCountHeart,
                                requireContext(),
                                "x ${heartCount}"
                            )
                        }
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            setUpHearts()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            setUpHearts()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            setUpHearts()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            setUpHearts()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            setUpHearts()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.OFFLINEMODE->{
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY->{
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                }
            }

        })
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }}

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        initCircleView(binding!!)
        val nightDrawle = ContextCompat.getDrawable(requireContext(), R.drawable.progress_bar_custom_night)
        val lightDrawle = ContextCompat.getDrawable(requireContext(), R.drawable.progress_bar_custom)
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            binding?.progressBar?.progressDrawable = nightDrawle
        }else{
            binding?.progressBar?.progressDrawable = lightDrawle
        }

//        val color = binding?.cardVictTimer?.background as ColorDrawable
//        val colorInt =
        val color = binding?.cardVictTimer?.cardBackgroundColor?.defaultColor

        val colorInt = R.color.error

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())

        timerObser = Observer {

            if (10 >= it) {
                binding?.cardVictTimer?.setCardBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.error
                    )
                )
            }
          if(it>10&&color==colorInt){
              binding?.cardVictTimer?.setCardBackgroundColor(
                  ContextCompat.getColor(
                      requireContext(),
                      R.color.timer_deafult
                  )
              )
          }

            binding?.tvTimer?.text = it.toString()
            Log.d("fkprkfprpfkrpkfprkfprkf", it.toString())
        }



        viewModel.thisThemeIsPassed(args.uniqueThemeId) {
            isThemePassed = it
        }



        viewModel.resetOlddata(uniqueThemeId = args.uniqueThemeId)

        viewModel.getAllVictorineTheme(args.uniqueThemeId) { victorineEntities ->
            Log.d(
                "vicotinresViewModel4",
                victorineEntities.toString()
            )
            victorinesQuestionsCount = victorineEntities.size
            victorinesQuestions = victorineEntities
            victorinesQuestions?.shuffled()

            binding?.tvQuestion?.text = victorinesQuestions?.get(currentIndex)?.questionText?.trimEnd()
            viewModel.getAllQuestionAnswerVariants(victorineEntities[0].questionId,victorineEntities[0].vicotineTestId) { victorineAnswerVariantEntities ->
                Log.d("showShowShow200",victorineAnswerVariantEntities.toString())
                if(!victorineAnswerVariantEntities.isNullOrEmpty()){
                    showFirstElement(victorineAnswerVariantEntities, binding)
                    victorineAnswerVariants = victorineAnswerVariantEntities
                    Log.d(
                        "vicotinresViewModel4",
                        victorineAnswerVariantEntities.toString()
                    )
                }else{
                    victorinesQuestionsCount--
                    showNextElement()
                }

            }
        }

        Log.d("vicotinresViewModel", victorinesQuestions.toString())
        binding?.apply {
            btnNext.setOnClickListener {
                if(!isFirstItemSelected&&!isSecondItemSelected&&!isThirdItemSelected&&!isForthItemSelected&&!isFifthItemSelected){

                }else{
                         btnNext.isEnabled = false
                    checkAnswer(victorineAnswerVariants)
                    tvQuestion.text = victorinesQuestions?.get(currentIndex)?.questionText?.trimEnd()
                    if (progress < (victorinesQuestions?.size ?: 0)) {
                        updateProgressBarWithAnimation(victorinesQuestions ?: emptyList())
                    }
                }

            }

            optionFirst.cardOption.setOnClickListener {
                if (!isSecondItemSelected && !isThirdItemSelected && !isForthItemSelected && !isFifthItemSelected) {
                    isFirstItemSelected = if (animatedCircleViewFirst!!.isAnimating) {
                        animatedCircleViewFirst!!.startAnimation()
                        true
                    } else {
                        animatedCircleViewFirst!!.startReverseAnimation()
                        false
                    }
                } else if (isSecondItemSelected) {
                    isFirstItemSelected = true
                    isSecondItemSelected = false
                    animatedCircleViewFirst!!.startAnimation()
                    animatedCircleViewSecond!!.startReverseAnimation()
                } else if (isThirdItemSelected) {
                    isFirstItemSelected = true
                    isThirdItemSelected = false
                    animatedCircleViewFirst!!.startAnimation()
                    animatedCircleViewThird!!.startReverseAnimation()
                } else if (isForthItemSelected) {
                    isFirstItemSelected = true
                    isForthItemSelected = false
                    animatedCircleViewFirst!!.startAnimation()
                    animatedCircleViewFourth!!.startReverseAnimation()
                } else if (isFifthItemSelected) {
                    isFirstItemSelected = true
                    isFifthItemSelected = false
                    animatedCircleViewFirst!!.startAnimation()
                    animatedCircleViewFifth!!.startReverseAnimation()
                }
            }
            optionSecond.cardOption.setOnClickListener {
                if (!isFirstItemSelected && !isThirdItemSelected && !isForthItemSelected && !isFifthItemSelected) {
                    isSecondItemSelected = if (animatedCircleViewSecond!!.isAnimating) {
                        animatedCircleViewSecond!!.startAnimation()
                        true
                    } else {
                        animatedCircleViewSecond!!.startReverseAnimation()
                        false
                    }
                } else if (isFirstItemSelected) {
                    isSecondItemSelected = true
                    isFirstItemSelected = false
                    animatedCircleViewFirst!!.startReverseAnimation()
                    animatedCircleViewSecond!!.startAnimation()
                } else if (isThirdItemSelected) {
                    isSecondItemSelected = true
                    isThirdItemSelected = false
                    animatedCircleViewSecond!!.startAnimation()
                    animatedCircleViewThird!!.startReverseAnimation()
                } else if (isForthItemSelected) {
                    isSecondItemSelected = true
                    isForthItemSelected = false
                    animatedCircleViewSecond!!.startAnimation()
                    animatedCircleViewFourth!!.startReverseAnimation()
                } else if (isFifthItemSelected) {
                    isSecondItemSelected = true
                    isFifthItemSelected = false
                    animatedCircleViewSecond!!.startAnimation()
                    animatedCircleViewFifth!!.startReverseAnimation()
                }
            }
            optionThird.cardOption.setOnClickListener {
                if (!isSecondItemSelected && !isFirstItemSelected && !isForthItemSelected && !isFifthItemSelected) {
                    isThirdItemSelected = if (animatedCircleViewThird!!.isAnimating) {
                        animatedCircleViewThird!!.startAnimation()
                        true
                    } else {
                        animatedCircleViewThird!!.startReverseAnimation()
                        false
                    }
                } else if (isSecondItemSelected) {
                    isThirdItemSelected = true
                    isSecondItemSelected = false
                    animatedCircleViewThird!!.startAnimation()
                    animatedCircleViewSecond!!.startReverseAnimation()
                } else if (isFirstItemSelected) {
                    isThirdItemSelected = true
                    isFirstItemSelected = false
                    animatedCircleViewFirst!!.startReverseAnimation()
                    animatedCircleViewThird!!.startAnimation()
                } else if (isForthItemSelected) {
                    isThirdItemSelected = true
                    isForthItemSelected = false
                    animatedCircleViewThird!!.startAnimation()
                    animatedCircleViewFourth!!.startReverseAnimation()
                } else if (isFifthItemSelected) {
                    isThirdItemSelected = true
                    isFifthItemSelected = false
                    animatedCircleViewThird!!.startAnimation()
                    animatedCircleViewFifth!!.startReverseAnimation()
                }
            }
            optionFourth.cardOption.setOnClickListener {
                if (!isSecondItemSelected && !isThirdItemSelected && !isFirstItemSelected && !isFifthItemSelected) {
                    isForthItemSelected = if (animatedCircleViewFourth!!.isAnimating) {
                        animatedCircleViewFourth!!.startAnimation()
                        true
                    } else {
                        animatedCircleViewFourth!!.startReverseAnimation()
                        false
                    }
                } else if (isSecondItemSelected) {
                    isForthItemSelected = true
                    isSecondItemSelected = false
                    animatedCircleViewFourth!!.startAnimation()
                    animatedCircleViewSecond!!.startReverseAnimation()
                } else if (isThirdItemSelected) {
                    isForthItemSelected = true
                    isThirdItemSelected = false
                    animatedCircleViewFourth!!.startAnimation()
                    animatedCircleViewThird!!.startReverseAnimation()
                } else if (isFirstItemSelected) {
                    isForthItemSelected = true
                    isFirstItemSelected = false
                    animatedCircleViewFirst!!.startReverseAnimation()
                    animatedCircleViewFourth!!.startAnimation()
                } else if (isFifthItemSelected) {
                    isForthItemSelected = true
                    isFifthItemSelected = false
                    animatedCircleViewFourth!!.startAnimation()
                    animatedCircleViewFifth!!.startReverseAnimation()
                }
            }
            optionFifth.cardOption.setOnClickListener {
                if (!isSecondItemSelected && !isThirdItemSelected && !isForthItemSelected && !isFirstItemSelected) {
                    isFifthItemSelected = if (animatedCircleViewFifth!!.isAnimating) {
                        animatedCircleViewFifth!!.startAnimation()
                        true
                    } else {
                        animatedCircleViewFifth!!.startReverseAnimation()
                        false
                    }
                } else if (isSecondItemSelected) {
                    isFifthItemSelected = true
                    isSecondItemSelected = false
                    animatedCircleViewFifth!!.startAnimation()
                    animatedCircleViewSecond!!.startReverseAnimation()
                } else if (isThirdItemSelected) {
                    isFifthItemSelected = true
                    isThirdItemSelected = false
                    animatedCircleViewFifth!!.startAnimation()
                    animatedCircleViewThird!!.startReverseAnimation()
                } else if (isForthItemSelected) {
                    isFifthItemSelected = true
                    isForthItemSelected = false
                    animatedCircleViewFifth!!.startAnimation()
                    animatedCircleViewFourth!!.startReverseAnimation()
                } else if (isFirstItemSelected) {
                    isFifthItemSelected = true
                    isFirstItemSelected = false
                    animatedCircleViewFirst!!.startReverseAnimation()
                    animatedCircleViewFifth!!.startAnimation()
                }
            }

        }
    }

    private fun showFirstElement(
        item: List<VictorineAnswerVariantEntity>,
        binding: FragmentVictorineBinding?
    ) {
        Log.d("vicotinresViewModel4", "showFirst:${item}")
        binding?.apply {
            if (item.size >= 1) {
                optionFirst.textView.text = item.get(0).text
                optionSecond.cardOption.visibility = View.GONE
                optionSecond.animatedCircleView.visibility = View.GONE
                optionSecond.textView.visibility = View.GONE
                optionThird.cardOption.visibility = View.GONE
                optionThird.textView.visibility = View.GONE
                optionThird.animatedCircleView.visibility = View.GONE
                optionFourth.cardOption.visibility = View.GONE
                optionFourth.textView.visibility = View.GONE
                optionFourth.animatedCircleView.visibility = View.GONE
                optionFifth.cardOption.visibility = View.GONE
                optionFifth.textView.visibility = View.GONE
                optionFifth.animatedCircleView.visibility = View.GONE
            }
            if (item.size >= 2) {
                optionSecond.cardOption.visibility = View.VISIBLE
                optionSecond.animatedCircleView.visibility = View.VISIBLE
                optionSecond.textView.visibility = View.VISIBLE
                optionThird.cardOption.visibility = View.GONE
                optionThird.textView.visibility = View.GONE
                optionThird.animatedCircleView.visibility = View.GONE
                optionFourth.cardOption.visibility = View.GONE
                optionFourth.textView.visibility = View.GONE
                optionFourth.animatedCircleView.visibility = View.GONE
                optionFifth.cardOption.visibility = View.GONE
                optionFifth.textView.visibility = View.GONE
                optionFifth.animatedCircleView.visibility = View.GONE
                optionSecond.textView.text = item.get(1).text
            }
            if (item.size >= 3) {
                optionThird.cardOption.visibility = View.VISIBLE
                optionThird.textView.visibility = View.VISIBLE
                optionThird.animatedCircleView.visibility = View.VISIBLE
                optionFourth.cardOption.visibility = View.GONE
                optionFourth.textView.visibility = View.GONE
                optionFourth.animatedCircleView.visibility = View.GONE
                optionFifth.cardOption.visibility = View.GONE
                optionFifth.textView.visibility = View.GONE
                optionFifth.animatedCircleView.visibility = View.GONE
                optionThird.textView.text = item.get(2).text
            }
            if (item.size >= 4) {
                optionFourth.cardOption.visibility = View.VISIBLE
                optionFourth.textView.visibility = View.VISIBLE
                optionFourth.animatedCircleView.visibility = View.VISIBLE
                optionFifth.cardOption.visibility = View.GONE
                optionFifth.textView.visibility = View.GONE
                optionFifth.animatedCircleView.visibility = View.GONE
                optionFourth.textView.text = item.get(3).text
            }
            if (item.size >= 5) {
                optionFifth.cardOption.visibility = View.VISIBLE
                optionFifth.textView.visibility = View.VISIBLE
                optionFifth.animatedCircleView.visibility = View.VISIBLE
                optionFifth.textView.text = item.get(4).text
            }
        }

    }

    private fun showNextElement() {
        if (victorinesQuestions?.size?.minus(1) != currentIndex) {
            currentIndex++
        }
        binding?.btnNext?.isEnabled = true
        Log.d("victorineGrhtut", victorinesQuestions.toString())
        Log.d("victorineGrhtut", "currentIndex:${currentIndex}")
        binding?.tvQuestion?.text = victorinesQuestions?.get(currentIndex)?.questionText ?: ""
        victorinesQuestions?.get(currentIndex)?.let {
            viewModel.getAllQuestionAnswerVariants(it.questionId,victorinesQuestions?.get(0)?.vicotineTestId ?: 2006) { victorineAnswerVariantEntities ->
                if(!victorineAnswerVariantEntities.isNullOrEmpty()){
                    showFirstElement(victorineAnswerVariantEntities, binding)
                    victorineAnswerVariants = victorineAnswerVariantEntities
                    Log.d("vicotinresViewModel4", victorineAnswerVariantEntities.toString())
                }else{
                    victorinesQuestionsCount--
                    showNextElement()
                }

            }
        }
    }

    private fun checkAnswer(item: List<VictorineAnswerVariantEntity>?) {
        if (isFirstItemSelected) {
            checkResultAnswer(0, item)
        }
        if (isSecondItemSelected) {
            checkResultAnswer(1, item)
        }
        if (isThirdItemSelected) {
            checkResultAnswer(2, item)
        }
        if (isForthItemSelected) {
            checkResultAnswer(3, item)
        }
        if (isFifthItemSelected) {
            checkResultAnswer(4, item)
        }
    }

    private fun minusHeart(){
        var isEnd = false
        viewModel.minusHeart(1,{state->
            when(state){
                ErrorEnum.SUCCESS -> {
                      if(isEnd&&heartCount<1&&!isUseNextTest){
                          /////выводим повотрить попытку
                          CustomTimerUtil.stopTimer()
                          var buyHeartCounts = 0
                          var buyAndroStates:BuyForAndropointStates?=null
                          viewModel.getAndropoint { userAndropointCount ->
                              requireActivity().runOnUiThread {
                                  ShowDialogHelper.showDialogApptempHeartVictorine(
                                      requireContext(),
                                      resources,
                                      { buyHeartCount ->
                                          buyHeartCounts = buyHeartCount
                                      },
                                      { andropointCount ->

                                          viewModel.buyAndropointHeart({ state ->
                                              when (state) {
                                                  ErrorEnum.NOTNETWORK -> {
                                                      requireActivity().runOnUiThread {
                                                          binding?.dimViewVictorine?.visibility =
                                                              View.VISIBLE
                                                          ShowDialogHelper.showDialogNotNetworkError(
                                                              requireContext(),
                                                              {
                                                                  minusHeart()
                                                              }) {
                                                              binding?.dimViewVictorine?.visibility =
                                                                  View.GONE
                                                          }
                                                      }

                                                  }

                                                  ErrorEnum.ERROR -> {
                                                      requireActivity().runOnUiThread {
                                                          binding?.dimViewVictorine?.visibility =
                                                              View.VISIBLE
                                                          ShowDialogHelper.showDialogUnknownError(
                                                              requireContext(),
                                                              {
                                                                  minusHeart()
                                                              }) {

                                                              binding?.dimViewVictorine?.visibility =
                                                                  View.GONE
                                                          }
                                                      }

                                                  }

                                                  ErrorEnum.SUCCESS -> {
                                                      when (buyAndroStates) {
                                                          BuyForAndropointStates.YESMONEY -> {
                                                              isUseNextTest = true
                                                              viewModel.buyHeart({ heartState ->
                                                                  when (heartState) {
                                                                      ErrorEnum.NOTNETWORK -> {
                                                                          requireActivity().runOnUiThread {
                                                                              binding?.dimViewVictorine?.visibility =
                                                                                  View.VISIBLE
                                                                              ShowDialogHelper.showDialogNotNetworkError(
                                                                                  requireContext(),
                                                                                  {
                                                                                      minusHeart()
                                                                                  }) {
                                                                                  binding?.dimViewVictorine?.visibility =
                                                                                      View.GONE
                                                                              }
                                                                          }

                                                                      }

                                                                      ErrorEnum.ERROR -> {
                                                                          requireActivity().runOnUiThread {
                                                                              binding?.dimViewVictorine?.visibility =
                                                                                  View.VISIBLE
                                                                              ShowDialogHelper.showDialogUnknownError(
                                                                                  requireContext(),
                                                                                  {
                                                                                      minusHeart()
                                                                                  }) {

                                                                                  binding?.dimViewVictorine?.visibility =
                                                                                      View.GONE
                                                                              }
                                                                          }

                                                                      }

                                                                      ErrorEnum.SUCCESS -> {
                                                                          heartCount =
                                                                              buyHeartCounts
                                                                          requireActivity().runOnUiThread {
                                                                              animateNumber(
                                                                                  binding?.tvCountHeart,
                                                                                  requireContext(),
                                                                                  "x ${heartCount}"
                                                                              )
                                                                          }

                                                                          CustomTimerUtil.startTimer(
                                                                              victorineSec
                                                                          ) {
                                                                              Log.d(
                                                                                  "checkTestTreatmentResultUse",
                                                                                  "MinusHEartshowApttempTimer"
                                                                              )
                                                                              checkTestTreatmentResult(
                                                                                  victorinesQuestions
                                                                                      ?: emptyList(),
                                                                                  true
                                                                              )
                                                                          }
                                                                      }

                                                                      ErrorEnum.UNKNOWNERROR -> {
                                                                          requireActivity().runOnUiThread {
                                                                              binding?.dimViewVictorine?.visibility =
                                                                                  View.VISIBLE
                                                                              ShowDialogHelper.showDialogUnknownError(
                                                                                  requireContext(),
                                                                                  {
                                                                                      minusHeart()
                                                                                  }) {

                                                                                  binding?.dimViewVictorine?.visibility =
                                                                                      View.GONE
                                                                              }
                                                                          }

                                                                      }

                                                                      ErrorEnum.TIMEOUTERROR -> {
                                                                          requireActivity().runOnUiThread {
                                                                              binding?.dimViewVictorine?.visibility =
                                                                                  View.VISIBLE
                                                                              ShowDialogHelper.showDialogTimeOutError(
                                                                                  requireContext(),
                                                                                  {
                                                                                      minusHeart()
                                                                                  }) {

                                                                                  binding?.dimViewVictorine?.visibility =
                                                                                      View.GONE
                                                                              }
                                                                          }

                                                                      }

                                                                      ErrorEnum.NULLPOINTERROR -> {
                                                                          requireActivity().runOnUiThread {
                                                                              binding?.dimViewVictorine?.visibility =
                                                                                  View.VISIBLE
                                                                              ShowDialogHelper.showDialogUnknownError(
                                                                                  requireContext(),
                                                                                  {
                                                                                      minusHeart()
                                                                                  }) {

                                                                                  binding?.dimViewVictorine?.visibility =
                                                                                      View.GONE
                                                                              }
                                                                          }

                                                                      }

                                                                      ErrorEnum.OFFLINEMODE -> {

                                                                      }

                                                                      ErrorEnum.OFFLINETHEMEBUY -> {

                                                                      }
                                                                  }
                                                              }, buyHeartCounts)
                                                          }

                                                          BuyForAndropointStates.NOMONEY -> {
                                                              requireActivity().runOnUiThread {
                                                                  Toast.makeText(
                                                                      requireContext(),
                                                                      R.string.node_money_andropoint,
                                                                      Toast.LENGTH_SHORT
                                                                  ).show()
                                                              }
                                                          }

                                                          null -> {
                                                              requireActivity().runOnUiThread {
                                                                  Toast.makeText(
                                                                      requireContext(),
                                                                      R.string.node_money_andropoint,
                                                                      Toast.LENGTH_SHORT
                                                                  ).show()
                                                              }
                                                          }
                                                      }
                                                  }

                                                  ErrorEnum.UNKNOWNERROR -> {
                                                      requireActivity().runOnUiThread {
                                                          binding?.dimViewVictorine?.visibility =
                                                              View.VISIBLE
                                                          ShowDialogHelper.showDialogUnknownError(
                                                              requireContext(),
                                                              {
                                                                  minusHeart()
                                                              }) {

                                                              binding?.dimViewVictorine?.visibility =
                                                                  View.GONE
                                                          }
                                                      }

                                                  }

                                                  ErrorEnum.TIMEOUTERROR -> {
                                                      requireActivity().runOnUiThread {
                                                          binding?.dimViewVictorine?.visibility =
                                                              View.VISIBLE
                                                          ShowDialogHelper.showDialogTimeOutError(
                                                              requireContext(),
                                                              {
                                                                  minusHeart()
                                                              }) {

                                                              binding?.dimViewVictorine?.visibility =
                                                                  View.GONE
                                                          }
                                                      }

                                                  }

                                                  ErrorEnum.NULLPOINTERROR -> {
                                                      requireActivity().runOnUiThread {
                                                          binding?.dimViewVictorine?.visibility =
                                                              View.VISIBLE
                                                          ShowDialogHelper.showDialogUnknownError(
                                                              requireContext(),
                                                              {
                                                                  minusHeart()
                                                              }) {

                                                              binding?.dimViewVictorine?.visibility =
                                                                  View.GONE
                                                          }
                                                      }

                                                  }

                                                  ErrorEnum.OFFLINEMODE -> {

                                                  }

                                                  ErrorEnum.OFFLINETHEMEBUY -> {

                                                  }
                                              }
                                          }, { buyState ->
                                              buyAndroStates = buyState
                                          }, andropointCount)


                                      },
                                      {
                                          isUseNextTest = true
                                          CustomTimerUtil.startTimer(victorineSec) {
                                              Log.d(
                                                  "checkTestTreatmentResultUse",
                                                  "MinusHEartshowApttempTimer"
                                              )
                                              checkTestTreatmentResult(
                                                  victorinesQuestions ?: emptyList(), true
                                              )
                                          }
                                      },
                                      {
                                          Log.d(
                                              "checkTestTreatmentResultUse",
                                              "MinusHEartshowApttempClose"
                                          )
                                          checkTestTreatmentResult(
                                              victorinesQuestions ?: emptyList(), false
                                          )
                                      },
                                      andropointUser = userAndropointCount
                                  )
                              }
                          }


                      }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            minusHeart()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusHeart()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusHeart()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            minusHeart()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusHeart()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.OFFLINEMODE->{

                }
                ErrorEnum.OFFLINETHEMEBUY->{

                }
            }
        },{
            isEnd = it
        })
    }

    private fun animateNumber(textView: TextView?, context: Context, newValue:String) {
        // Анимация: уменьшаем alpha при нажатии на кнопку
        val fadeIn: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
        fadeIn.duration = 200
        val fadeOut: Animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
        fadeOut.duration = 200
        fadeOut.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                textView?.text = newValue          //  textView?.setTextColor(ContextCompat.getColor(context,R.color.white))
                textView?.startAnimation(fadeIn)        }
            override fun onAnimationRepeat(animation: Animation) {}
        })
        textView?.startAnimation(fadeOut)
    }


    private fun decrementAndAnimate() {
//        val currentValue = binding?.tvCountHeart?.?.replace("x ","").toString().toInt()
//        val newValue = currentValue - 1    // Анимация: новое число появляется чуть выше, а старая цифра уходит чуть ниже
        val moveUp = ObjectAnimator.ofFloat(binding?.tvCountHeart, "translationY", -25f)
        moveUp.duration = 200
        val moveDown = ObjectAnimator.ofFloat(binding?.tvCountHeart, "translationY", 0f)
        moveDown.duration = 200
        moveUp.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                binding?.tvCountHeart?.setTextColor(ContextCompat.getColor(requireContext(),R.color.red))        }
            override fun onAnimationEnd(animation: Animator) {
                binding?.tvCountHeart?.text = "x "+heartCount.toString()
                binding?.tvCountHeart?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                moveDown.start()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })
        moveUp.start()
    }

    private fun checkResultAnswer(
        numberItem: Int,
        item: List<VictorineAnswerVariantEntity>?
    ) {
        if(isFirstItemSelected){
            isFirstItemSelected = false
            animatedCircleViewFirst!!.startReverseAnimation()
        }
        if(isSecondItemSelected){
            isSecondItemSelected = false
            animatedCircleViewSecond!!.startReverseAnimation()
        }
        if(isThirdItemSelected){
            isThirdItemSelected = false
            animatedCircleViewThird!!.startReverseAnimation()
        }
        if(isFifthItemSelected){
            isFifthItemSelected = false
            animatedCircleViewFifth!!.startReverseAnimation()
        }
        if(isForthItemSelected){
            isForthItemSelected = false
            animatedCircleViewFourth!!.startReverseAnimation()
        }
//        var isClueExist = false
        val currentAnswer = item?.get(numberItem)
        if (currentAnswer?.isCorrectAnswer == true) {
            correctAnswers++
        } else {
            if(!isInfinityHearts&&heartCount>0) {
                if(!isInfinityHearts) {
                    heartCount--
                    minusHeart()
                    decrementAndAnimate()
                }
            }
       Log.d("ATG@)(*!)@#","heartCount:${heartCount}")
            Log.d("ATG@)(*!)@#","isUseNextTest:${isUseNextTest}")
            if(heartCount==0&&isUseNextTest){
                Log.d("checkTestTreatmentResultUse","if(heartCount==0&&isUseNextTest)")
                checkTestTreatmentResult(victorinesQuestions ?: emptyList(),false)
            }else if(heartCount==0&&!isUseNextTest){
                ///Предложтьб
                CustomTimerUtil.stopTimer()
                var buyHeartCounts = 0
                var buyAndroStates:BuyForAndropointStates?=null
                viewModel.getAndropoint { userAndropointCount->
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogApptempHeartVictorine(requireContext(),resources,{buyHeartCount->
                            buyHeartCounts = buyHeartCount

                        },{andropointCount->

                            viewModel.buyAndropointHeart({state->
                                when(state){
                                    ErrorEnum.NOTNETWORK -> {
                                        requireActivity().runOnUiThread {
                                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                                            ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                                minusHeart()
                                            }) {
                                                binding?.dimViewVictorine?.visibility = View.GONE
                                            }
                                        }

                                    }

                                    ErrorEnum.ERROR -> {
                                        requireActivity().runOnUiThread {
                                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                minusHeart()
                                            }) {

                                                binding?.dimViewVictorine?.visibility = View.GONE
                                            }
                                        }

                                    }
                                    ErrorEnum.SUCCESS -> {
                                        when(buyAndroStates){
                                            BuyForAndropointStates.YESMONEY -> {
                                                isUseNextTest = true
                                                viewModel.buyHeart({ heartState->
                                                    when(heartState){
                                                        ErrorEnum.NOTNETWORK -> {
                                                            requireActivity().runOnUiThread {
                                                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                                                    minusHeart()
                                                                }) {
                                                                    binding?.dimViewVictorine?.visibility = View.GONE
                                                                }
                                                            }

                                                        }

                                                        ErrorEnum.ERROR -> {
                                                            requireActivity().runOnUiThread {
                                                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                                    minusHeart()
                                                                }) {

                                                                    binding?.dimViewVictorine?.visibility = View.GONE
                                                                }
                                                            }

                                                        }
                                                        ErrorEnum.SUCCESS -> {
                                                            heartCount = buyHeartCounts
                                                            requireActivity().runOnUiThread {
                                                                animateNumber(binding?.tvCountHeart,requireContext(),"x ${heartCount}")
                                                            }

                                                            CustomTimerUtil.startTimer(victorineSec){
                                                                Log.d(
                                                                    "checkTestTreatmentResultUse",
                                                                    "checkAnswerTimer"
                                                                )
                                                                checkTestTreatmentResult(victorinesQuestions ?: emptyList(), true)
                                                            }
                                                        }
                                                        ErrorEnum.UNKNOWNERROR -> {
                                                            requireActivity().runOnUiThread {
                                                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                                    minusHeart()
                                                                }) {

                                                                    binding?.dimViewVictorine?.visibility = View.GONE
                                                                }
                                                            }

                                                        }
                                                        ErrorEnum.TIMEOUTERROR -> {
                                                            requireActivity().runOnUiThread {
                                                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                                                    minusHeart()
                                                                }) {

                                                                    binding?.dimViewVictorine?.visibility = View.GONE
                                                                }
                                                            }

                                                        }
                                                        ErrorEnum.NULLPOINTERROR -> {
                                                            requireActivity().runOnUiThread {
                                                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                                    minusHeart()
                                                                }) {

                                                                    binding?.dimViewVictorine?.visibility = View.GONE
                                                                }
                                                            }

                                                        }
                                                        ErrorEnum.OFFLINEMODE->{

                                                        }
                                                        ErrorEnum.OFFLINETHEMEBUY->{

                                                        }
                                                    }
                                                },buyHeartCounts)
                                            }
                                            BuyForAndropointStates.NOMONEY -> {
                                                requireActivity().runOnUiThread {
                                                    Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                            null -> {
                                                requireActivity().runOnUiThread {
                                                    Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                    ErrorEnum.UNKNOWNERROR -> {
                                        requireActivity().runOnUiThread {
                                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                minusHeart()
                                            }) {

                                                binding?.dimViewVictorine?.visibility = View.GONE
                                            }
                                        }

                                    }
                                    ErrorEnum.TIMEOUTERROR -> {
                                        requireActivity().runOnUiThread {
                                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                                            ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                                minusHeart()
                                            }) {

                                                binding?.dimViewVictorine?.visibility = View.GONE
                                            }
                                        }

                                    }
                                    ErrorEnum.NULLPOINTERROR -> {
                                        requireActivity().runOnUiThread {
                                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                minusHeart()
                                            }) {

                                                binding?.dimViewVictorine?.visibility = View.GONE
                                            }
                                        }

                                    }
                                    ErrorEnum.OFFLINEMODE->{

                                    }
                                    ErrorEnum.OFFLINETHEMEBUY->{

                                    }
                                }
                            },{buyState->
                                buyAndroStates = buyState
                            },andropointCount)


                        },{

                            isUseNextTest = true
                            CustomTimerUtil.startTimer(victorineSec) {
                                checkTestTreatmentResult(victorinesQuestions ?: emptyList(), true)
                            }
                        },{
                            Log.d(
                                "checkTestTreatmentResultUse",
                                "closeCheckAnswer"
                            )
                            checkTestTreatmentResult(victorinesQuestions ?: emptyList(),false)
                        },userAndropointCount)
                    }
                }

            }
            misstakeAnswers++
        }
        viewModel.checkAnswer(item!![numberItem], { resultAnswer ->
            Log.d("checkAnswerState", resultAnswer.toString())
            when (resultAnswer) {
                ErrorEnum.SUCCESS -> {

                    Log.d("succsNextVictQuestCo", "victorines:${victorinesQuestions}")
                    Log.d("succsNextVictQuestCo", "currentIndex:${currentIndex}")
                    Log.d("succsNextVictQuestCo", (victorinesQuestions?.size ?: 0).toString())
                    if (victorinesQuestions?.size != currentIndex) {
                        showNextElement()
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.OFFLINEMODE->{
                    if (victorinesQuestions?.size != currentIndex) {
                        showNextElement()
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResultAnswer(numberItem, item)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
            }
        }, { isClue ->
            Log.d("victorineClueadfrofkrof", isClue.toString())
            if (pref?.getString(Constatns.CLUE_KEY, "true") == "true") {
                if (isClue != null) {
//                    isClueExist = true
//                    Toast.makeText(requireActivity(), isClue, Toast.LENGTH_LONG).show()
//                    val rootView: View = requireView()
//                    val snackbar = Snackbar.make(rootView, "Snackbar с кнопкой", Snackbar.LENGTH_LONG)
//                    snackbar.setAction("Нажми меня") {
//                       showDialogClue(requireContext(), isClue!!)
//                    }/
//                    snackbar.show()
                    if (victorinesQuestions?.size != currentIndex) {
                        requireActivity().runOnUiThread {
                            val snackbar = view?.let {
                                Snackbar.make(
                                    it,
                                    R.string.clue_snackbar,
                                    Snackbar.LENGTH_LONG
                                )
                            }
                            snackbar?.setAction(R.string.clue_snackbar_view) {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogClue(requireContext(), isClue,{
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                })
                            }
                            snackbar?.show()
                        }

                    }
//                    val rootView = requireView()
//                    val snackbar = Snackbar.make(rootView, "Snackbar с кнопкой", Snackbar.LENGTH_LONG)
//                    snackbar.setAction("Нажми меня") {
//                        ShowDialogHelper.showDialogClue(requireContext(), isClue!!)
//                    }
//                    snackbar.show()
                }
            }
        })


    }

    private fun checkLimitActualTreatmentResult(andropointAds:Boolean) {
        viewModel.getMyAdsProvider { adsProviderEntity ->
            if(adsProviderEntity.selectedGoogle){
                var isActualNotTerm = false
                viewModel.checkLimitActual({ state ->
                    when (state) {
                        ErrorEnum.SUCCESS -> {
                            Log.d("adsViewCount", "isActualTerm:${isActualNotTerm}")
                            Log.d("adsViewCount", "isStartTimer:${startTimerViewAds}")
                            if (isActualNotTerm) {
                                if (startTimerViewAds) {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(requireContext(), getString(R.string.advertising_will_be_available_through), Toast.LENGTH_SHORT).show()
                                    }

                                } else {
                                    showRewardedVideo(andropointAds)
                                }
                            } else {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(requireContext(), getString(R.string.advertising_limit_has_been_reached), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        else -> {}
                    }
                }, {
                    isActualNotTerm = it
                })
            }
            if(adsProviderEntity.selectedLMyTarger){
                var isActualNotTerm = false
                viewModel.checkLimitActual({ state ->
                    when (state) {
                        ErrorEnum.SUCCESS -> {
                            Log.d("adsViewCount", "isActualTerm:${isActualNotTerm}")
                            Log.d("adsViewCount", "isStartTimer:${startTimerViewAds}")
                            if (isActualNotTerm) {
                                if (startTimerViewAds) {
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(requireContext(), getString(R.string.advertising_will_be_available_through), Toast.LENGTH_SHORT).show()
                                    }

                                } else {

                                    initAd(andropointAds)


                                }
                            } else {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(requireContext(), getString(R.string.advertising_limit_has_been_reached), Toast.LENGTH_SHORT).show()
                                }

                            }
                        }

                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        else -> {}
                    }
                }, {
                    isActualNotTerm = it
                })

            }

        }

    }

    private fun showRewardedVideo(andropointAds: Boolean) {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("TAG", "Ad was dismissed.")
                        rewardedAd = null
                        if (googleMobileAdsConsentManager?.canRequestAds == true) { loadRewardedAd() }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d("TAG", "Ad failed to show.")
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("TAG", "Ad showed fullscreen content.")
                    }
                }

            rewardedAd?.show(requireActivity()) {
                if(andropointAds) {
                    addHeartAds()
                }else{
                    addHeartAds()
                }
                Log.d("TAG", "User earned the reward.")
            }
            adsViewTreatmentResult()
        }
    }

    private fun loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(requireContext(),
                Constatns.AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Log.d("TAG", adError.message)
                    isLoading = false
                    rewardedAd = null
                }

                override fun onAdLoaded(ad: RewardedAd) {
                    Log.d("TAG", "Ad was loaded.")
                    rewardedAd = ad
                    isLoading = false
                }
            }
            )
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) { return }
        MobileAds.initialize(requireContext()) {}
        loadRewardedAd()
    }

    private fun initCircleView(binding: FragmentVictorineBinding) {
        animatedCircleViewFirst = binding.optionFirst.animatedCircleView
        animatedCircleViewSecond = binding.optionSecond.animatedCircleView
        animatedCircleViewThird = binding.optionThird.animatedCircleView
        animatedCircleViewFourth = binding.optionFourth.animatedCircleView
        animatedCircleViewFifth = binding.optionFifth.animatedCircleView
    }

    private fun updateProgressBarWithAnimation(victorines: List<VictorineEntity>) {
        clickCount++
        val progress = (clickCount * 100) / (victorines.size)


        val anim = ObjectAnimator.ofInt(binding?.progressBar, "progress", progress)
        anim.duration = 500
        anim.start()
        if (clickCount == victorines.size) {
            binding?.btnNext?.isEnabled = false
            CustomTimerUtil.stopTimer()
            Log.d(
                "checkTestTreatmentResultUse",
                "updateProgressBarWithAnimation"
            )
            checkTestTreatmentResult(victorines, false)
        }
    }

//    private fun showFeedbackDialog() {
//        val reviewManager = ReviewManagerFactory.create(requireActivity().applicationContext)
//        reviewManager.requestReviewFlow().addOnCompleteListener {
//            if (it.isSuccessful) {
//                reviewManager.launchReviewFlow(requireActivity(), it.result)
//            }
//        }
//    }

    private fun initAd(andropointAds: Boolean) {

        // Устанавливаем слушатель событий
        adTarget?.setListener(object : com.my.target.ads.RewardedAd.RewardedAdListener {

            override fun onLoad(p0: com.my.target.ads.RewardedAd) {
                adTarget?.show()
                viewModel.adsView { state->
                    when(state){
                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.SUCCESS -> Log.d("forkfoktogktg","SUCCESMAMBET")
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.OFFLINEMODE -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogOffline(requireContext(),{

                                },{
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }) }
                        }
                        ErrorEnum.OFFLINETHEMEBUY -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewVictorine?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogOffline(requireContext(),{

                                },{
                                    binding?.dimViewVictorine?.visibility = View.GONE
                                }) }
                        }
                    }
                }
            }

            override fun onNoAd(p0: IAdLoadingError, p1: com.my.target.ads.RewardedAd) {
                Toast.makeText(requireContext(),R.string.no_ads_view,Toast.LENGTH_SHORT).show()
                checkTestTreatmentResult(victorinesQuestions ?: emptyList(),false)
            }

            override fun onClick(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onClick")
            }

            override fun onDismiss(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onDismiss")
            }

            override fun onReward(p0: Reward, p1: com.my.target.ads.RewardedAd) {
                if(andropointAds) {
                    addHeartAds()
                }else{
                    addHeartAds()
                }
            }

            override fun onDisplay(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onDisplay")
            }
        })

        // Запускаем загрузку данных
        adTarget?.load()
    }

    private fun adsViewTreatmentResult() {
        viewModel.adsView { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {}
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewVictorine?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewVictorine?.visibility = View.GONE
                        })
                    }
                }
            }
        }
    }

    private fun addHeartAds(){

        viewModel.buyHeart2(1,{
            when(it){
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            addHeartAds()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.SUCCESS -> {
                    Toast.makeText(requireContext(),R.string.success_award_credited,Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE -> {

                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                }
            }
        },{

        })
    }


    private fun checkTestTreatmentResult(victorines: List<VictorineEntity>, isTimerOut: Boolean) {
        victorineEnded = true
        var mistakeTest: String? = null
        var dateUnlockTheme: ((String) -> Unit)? = null
        viewModel.checkTestResult(args.uniqueThemeId, { resultTest ->
            Log.d("victorineTestResultState", resultTest.toString())
            when (resultTest) {
                ErrorStateView.SUCCESS -> {}

                ErrorStateView.NEXTTHEME -> {
                    if (!testCheckResultFlag) {
                        testCheckResultFlag = true
                        if (isThemePassed) {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogReplayTest(requireContext(), {
                                    binding?.dimViewVictorine?.visibility =View.VISIBLE
                                    ShowDialogHelper.loadDialog(requireContext(),{
                                        binding?.dimViewVictorine?.visibility =View.GONE
                                    })
                                    strikeModeTreatmentResult()
                                }, correctAnswers, victorinesQuestionsCount)
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogSuccessTest(requireContext(), {
                                    binding?.dimViewVictorine?.visibility =View.VISIBLE
                                    ShowDialogHelper.loadDialog(requireContext(),{
                                        binding?.dimViewVictorine?.visibility =View.GONE
                                    })
                                    strikeModeTreatmentResult()
                                },correctAnswers,victorinesQuestionsCount)
                            }
//                            showFeedbackDialog()
                            IsFeedback = true
                            addAndropointsTreatmentResult()
                        }
                    }
                }

                ErrorStateView.OFFLINE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogSuccessTest(requireContext(),{

                            val action =
                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                    args.courseNumber,
                                    args.courseName
                                )
                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }

                        },correctAnswers,victorinesQuestionsCount)
                    }
                }

                ErrorStateView.TERM -> {
                    dateUnlockTheme = { dateUnlockTheme ->
                        IsFeedback = true
                        Log.d("victorineTestResultState", "term tw")
                        requireActivity().runOnUiThread {


                            ShowDialogHelper.showDialogFailTest(
                                requireContext(),
                                correctAnswers,
                                misstakeAnswers,
                                victorinesQuestionsCount,
                                dateUnlockTheme,
                                {
                                    binding?.dimViewVictorine?.visibility =View.VISIBLE
                                    ShowDialogHelper.loadDialog(requireContext(),{
                                        binding?.dimViewVictorine?.visibility =View.GONE
                                    })
                                    strikeModeTreatmentResult()
                                },
                                isTimerOut
                            )
                        }
                    }
                }

                ErrorStateView.OFFLINEBUYTHEME -> {}
                ErrorStateView.TRYAGAIN -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTryAgainResult()
                            checkTestTreatmentResult(victorines, isTimerOut)
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }

                ErrorStateView.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext(),{
                                checkTryAgainResult()
                                checkTestTreatmentResult(victorines, isTimerOut)
                            }
                        ) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
            }
        }, {
            dateUnlockTheme?.invoke(it)
            Log.d("misstakeDialog", "dateTest:${it}")
        }, correctAnswers, misstakeAnswers, isTimerOut,heartCount)

    }

    private fun addAndropointsTreatmentResult() {
        viewModel.addAndropoints { resultAddAndropoints ->
            Log.d("victorineTestResultStateAddAndropoint", resultAddAndropoints.toString())
            when (resultAddAndropoints) {

                ErrorEnum.SUCCESS -> {}

                ErrorEnum.OFFLINEMODE ->{

                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewVictorine?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewVictorine?.visibility = View.GONE
                        })
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(
                            requireContext(),{
                                strikeModeTreatmentResult()
                            }
                        ) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext()
                            ,{
                                strikeModeTreatmentResult()
                            }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext(),{
                                strikeModeTreatmentResult()
                            }
                        ) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(
                            requireContext(),{
                                strikeModeTreatmentResult()
                            }
                        ) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext(),{
                                strikeModeTreatmentResult()
                            }
                        ) { binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }

                }
            }
        }
    }

    private fun strikeModeTreatmentResult() {
        var resultStrikeModeDay = 0
        var isNextFlag = false
        viewModel.strikeModeProgress({ resultStrikeMode ->
            Log.d("victorineTestResultStateSimpleTreamtResult", resultStrikeModeDay.toString())
            if (resultStrikeModeDay == 0 && !isNextFlag) {
                requireActivity().runOnUiThread {
                    ShowDialogHelper.closeDialogLoadData()
                }

                isNextFlag = true
                if(IsFeedback){
                    requireActivity().runOnUiThread {
                        val action =
                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                args.courseNumber,
                                args.courseNameReal,
                                feedbackVisible = true
                            )
                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                    }
                }else{
                    requireActivity().runOnUiThread {
                        val action =
                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                args.courseNumber,
                                args.courseNameReal
                            )
                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                    }
                }

            } else {
                Log.d("victorineTestResultStateSimpleTreamtResult", resultStrikeMode.toString())
                when (resultStrikeMode) {
                    ErrorEnum.SUCCESS -> {
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                    }

                    ErrorEnum.OFFLINEMODE -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                    }

                    ErrorEnum.OFFLINETHEMEBUY -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                    }

                    ErrorEnum.NOTNETWORK -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        requireActivity().runOnUiThread {

                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogNotNetworkError(
                                requireContext(),{
                                    strikeModeTreatmentResult()
                                }
                            ) {

                                binding?.dimViewVictorine?.visibility = View.GONE
                            }
                        }
                    }

                    ErrorEnum.ERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        requireActivity().runOnUiThread {

                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext(),{
                                    strikeModeTreatmentResult()
                                }
                            ) {

                                binding?.dimViewVictorine?.visibility = View.GONE
                            }
                        }
                    }

                    ErrorEnum.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        requireActivity().runOnUiThread {
                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext(),{
                                    strikeModeTreatmentResult()
                                }
                            ) {

                                binding?.dimViewVictorine?.visibility = View.GONE
                            }
                        }
                    }

                    ErrorEnum.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        requireActivity().runOnUiThread {
                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogTimeOutError(
                                requireContext(),{
                                    strikeModeTreatmentResult()
                                }
                            ) {

                                binding?.dimViewVictorine?.visibility = View.GONE
                            }
                        }
                    }

                    ErrorEnum.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        requireActivity().runOnUiThread {
                            binding?.dimViewVictorine?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext(),{
                                    strikeModeTreatmentResult()
                                }
                            ) {

                                binding?.dimViewVictorine?.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }, {
            resultStrikeModeDay = it
        })

    }

    private fun strikeModeAndropointTreatmentResult(resultStrikeModeDay: Int) {
        strikeModeAndropointChooseDay(resultStrikeModeDay) { resultStrikeModeAndropoints ->
            Log.d(
                "victorineTestResultStateStrikeResultTreaAndropoint",
                resultStrikeModeAndropoints.toString()
            )
            Log.d(
                "victorineTestResultStateStrikeResultTreaAndropoint",
                resultStrikeModeDay.toString()
            )
            var nextThemeFrag = false
            var subscribeActual = false
            var isPromoActual = false
            when (resultStrikeModeAndropoints) {
                ErrorEnum.SUCCESS -> {
                    viewModel.checSubscribe({
                        subscribeActual = it
                    }, { checkkState ->
                        Log.d(
                            "victorineTestResultStateStrikeResultTreaAndropointcheckkState",
                            checkkState.toString()
                        )
                        when (checkkState) {
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }) {
                                        binding?.dimViewVictorine?.visibility = View.GONE

                                    }
                                }
                            }

                            ErrorEnum.ERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }) {
                                        binding?.dimViewVictorine?.visibility = View.GONE

                                    }
                                }
                            }

                            ErrorEnum.SUCCESS -> {
                                Log.d(
                                    "victorineTestResultStateStrikeResultTreaAndropointsubscribeActual",
                                    subscribeActual.toString()
                                )
                                Log.d(
                                    "victorineTestResultStateStrikeResultTreaAndropointisPromoActual",
                                    isPromoActual.toString()
                                )
                                requireActivity().runOnUiThread {
                                    requireActivity().runOnUiThread {
                                        ShowDialogHelper.closeDialogLoadData()
                                    }
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    if(subscribeActual){
                                        ShowDialogHelper.showDialogStrikeMode(
                                            requireContext(),
                                            resultStrikeModeDay,
                                            layoutInflater,
                                            subscribeActual,
                                            {
                                                binding?.dimViewVictorine?.visibility = View.GONE
                                                if(!nextThemeFrag){
                                                    val action =
                                                        VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                            args.courseNumber,
                                                            args.courseNameReal,
                                                            feedbackVisible = true
                                                        )
                                                    binding?.root?.let {
                                                        Navigation.findNavController(it).navigate(action)
                                                    }
                                                    nextThemeFrag = true
                                                }

                                            },
                                            {
                                                binding?.dimViewVictorine?.visibility = View.GONE
                                                if(!nextThemeFrag) {
                                                    val action =
                                                        VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                            args.courseNumber,
                                                            args.courseNameReal,
                                                            premiumVisible = true
                                                        )
                                                    binding?.root?.let {
                                                        Navigation.findNavController(it)
                                                            .navigate(action)
                                                    }
                                                    nextThemeFrag = true
                                                }
                                            }
                                        )
                                    }else{
                                        viewModel.checkPromoCode({promoState->
                                            when(promoState){
                                                ErrorEnum.NOTNETWORK -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                        }) {
                                                            binding?.dimViewVictorine?.visibility = View.GONE

                                                        }
                                                    }
                                                }

                                                ErrorEnum.ERROR -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                        }) {
                                                            binding?.dimViewVictorine?.visibility = View.GONE

                                                        }
                                                    }
                                                }
                                                ErrorEnum.SUCCESS -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility =
                                                            View.VISIBLE
                                                        ShowDialogHelper.showDialogStrikeMode(
                                                            requireContext(),
                                                            resultStrikeModeDay,
                                                            layoutInflater,
                                                            isPromoActual,
                                                            {
                                                                binding?.dimViewVictorine?.visibility =
                                                                    View.GONE
                                                                val action =
                                                                    VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                                        args.courseNumber,
                                                                        args.courseNameReal,
                                                                        feedbackVisible = true
                                                                    )
                                                                binding?.root?.let {
                                                                    Navigation.findNavController(it)
                                                                        .navigate(action)
                                                                }
                                                            },
                                                            {
                                                                binding?.dimViewVictorine?.visibility =
                                                                    View.GONE
                                                                val action =
                                                                    VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                                        args.courseNumber,
                                                                        args.courseNameReal,
                                                                        premiumVisible = true
                                                                    )
                                                                binding?.root?.let {
                                                                    Navigation.findNavController(it)
                                                                        .navigate(action)
                                                                }
                                                            }
                                                        )
                                                    }
                                                }
                                                ErrorEnum.UNKNOWNERROR -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                        }) {
                                                            binding?.dimViewVictorine?.visibility = View.GONE

                                                        }
                                                    }
                                                }

                                                ErrorEnum.TIMEOUTERROR -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                        ShowDialogHelper.showDialogTimeOutError(
                                                            requireContext()
                                                            ,{
                                                                strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                            }) {
                                                            binding?.dimViewVictorine?.visibility = View.GONE

                                                        }
                                                    }
                                                }

                                                ErrorEnum.NULLPOINTERROR -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                                                        ShowDialogHelper.showDialogUnknownError(
                                                            requireContext(),{
                                                                strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                            }
                                                        ) {
                                                            binding?.dimViewVictorine?.visibility = View.GONE

                                                        }
                                                    }
                                                }

                                                ErrorEnum.OFFLINEMODE -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {

                                                        ShowDialogHelper.showDialogAttentionStrikeMode(
                                                            requireContext(),
                                                            {
                                                                strikeModeTreatmentResult()
                                                            }) {
                                                            val action =
                                                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                                    args.courseNumber,
                                                                    args.courseNameReal
                                                                )
                                                            binding?.root?.let {
                                                                Navigation.findNavController(it).navigate(action)
                                                            }
                                                        }
                                                    }
                                                }

                                                ErrorEnum.OFFLINETHEMEBUY -> {
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.closeDialogLoadData()
                                                    }
                                                    requireActivity().runOnUiThread {
                                                        ShowDialogHelper.showDialogAttentionStrikeMode(
                                                            requireContext(),
                                                            {
                                                                strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                                            }) {
                                                            val action =
                                                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                                    args.courseNumber,
                                                                    args.courseNameReal
                                                                )
                                                            binding?.root?.let {
                                                                Navigation.findNavController(it).navigate(action)
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        },{
                                            isPromoActual = it
                                        })
                                    }

                                }
                            }

                            ErrorEnum.UNKNOWNERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }) {
                                        binding?.dimViewVictorine?.visibility = View.GONE

                                    }
                                }
                            }

                            ErrorEnum.TIMEOUTERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogTimeOutError(
                                        requireContext()
                                        ,{
                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                        }) {
                                        binding?.dimViewVictorine?.visibility = View.GONE

                                    }
                                }
                            }

                            ErrorEnum.NULLPOINTERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    binding?.dimViewVictorine?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(
                                        requireContext(),{
                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                        }
                                    ) {
                                        binding?.dimViewVictorine?.visibility = View.GONE

                                    }
                                }
                            }

                            ErrorEnum.OFFLINEMODE -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {

                                    ShowDialogHelper.showDialogAttentionStrikeMode(
                                        requireContext(),
                                        {
                                            strikeModeTreatmentResult()
                                        }) {
                                        val action =
                                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                args.courseNumber,
                                                args.courseNameReal
                                            )
                                        binding?.root?.let {
                                            Navigation.findNavController(it).navigate(action)
                                        }
                                    }
                                }
                            }

                            ErrorEnum.OFFLINETHEMEBUY -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                }
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogAttentionStrikeMode(
                                        requireContext(),
                                        {
                                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                        }) {
                                        val action =
                                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                                args.courseNumber,
                                                args.courseNameReal
                                            )
                                        binding?.root?.let {
                                            Navigation.findNavController(it).navigate(action)
                                        }
                                    }
                                }
                            }
                        }
                    })
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                            strikeModeTreatmentResult()
                        }) {
                            val action =
                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                    args.courseNumber,
                                    args.courseNameReal
                                )
                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                        }
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }) {
                            val action =
                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                                    args.courseNumber,
                                    args.courseNameReal
                                )
                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                        }
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(
                            requireContext(),{
                                strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                            }
                        ) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext(),{
                                strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                            }
                        ) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun strikeModeAndropointChooseDay(
        resultStrikeModeDay: Int,
        resultStrikeModeAndropoints: (ErrorEnum) -> Unit
    ) {
        Log.d("tttttt22tt", resultStrikeModeDay.toString())
        if (!isStrikeModeAndropointFlag) {
            when (resultStrikeModeDay) {
                1 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.ONE
                    )
                    isStrikeModeAndropointFlag = true
                }

                2 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.TWO
                    )
                    isStrikeModeAndropointFlag = true
                }

                3 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.THREE
                    )
                    isStrikeModeAndropointFlag = true
                }

                4 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.FOUR
                    )
                    isStrikeModeAndropointFlag = true
                }

                5 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.FIVE
                    )
                    isStrikeModeAndropointFlag = true
                }

                6 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.SIX
                    )
                    isStrikeModeAndropointFlag = true
                }

                7 -> {
                    viewModel.strikeModeAndropointProgress(
                        { resultStrikeModeAndropoints.invoke(it) },
                        StrikeModeState.SEVEN
                    )
                    isStrikeModeAndropointFlag = true
                }
            }
        }
    }


    private fun checkTryAgainResult() {
        viewModel.tryAgainSendProgress({ resultTryAgain ->
            Log.d("startTimerVic", resultTryAgain.toString()+"tryAgain")
            when (resultTryAgain) {

                ErrorEnum.SUCCESS -> {
                    strikeModeTreatmentResult()
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    strikeModeTreatmentResult()
                }

                ErrorEnum.OFFLINEMODE -> {
                    strikeModeTreatmentResult()
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkTryAgainResult()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTryAgainResult()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkTryAgainResult()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTryAgainResult()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTryAgainResult()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onStop() {
        super.onStop()
        testCheckResultFlag = false
        CustomTimerUtil.stopTimer()
        timerObser?.let { CustomTimerUtil.timerValue.removeObserver(it) }
        if (!victorineEnded) {

            viewModel.victorineExit(args.uniqueThemeId, {
                if (it) {
                    victorineExit = true
                }
            }, {

            },{

            })
        }
    }





    private fun startTimerFun() {
        var timerStart = false

        var timerCours:((Boolean)->Unit)?=null

        viewModel.getTimeVictorine({ sec ->
            Log.d("kfkrofkorkfo5t59t9564g54", sec.toString())
            victorineSec = sec
        }, args.uniqueThemeId, {
            Log.d("startTimerVic", it.toString())
            when (it) {
                ErrorEnum.SUCCESS -> {
                    Log.d("kfkrofkorkfo5t59t9564g5444", timerStart.toString())
                    Log.d("kfkrofkorkfo5t59t9564g5444", victorineSec.toString())
                    if (!timerStart) {
                        checkBuyCourse()
                    } else {
                        requireActivity().runOnUiThread {
                            binding?.tvTimer?.text = "∞"
                        }

                    }
                }

                ErrorEnum.OFFLINEMODE->{
//                    Log.d("kfkrofkorkfo5t59t9564g5444", timerStart.toString())
//                    Log.d("kfkrofkorkfo5t59t9564g5444", victorineSec.toString())
//
                    requireActivity().runOnUiThread {    binding?.tvTimer?.text = "∞"
                    }


                }

                ErrorEnum.NOTNETWORK -> {
                    checkBuyCourse()
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {
                            binding?.dimViewVictorine?.visibility = View.GONE

                        }
                    }
                }
            }
        }, {
            Log.d("kfkrofkorkfo5t59t9564g5444",it.toString())
            timerStart = it
        })
    }

    private fun checkBuyCourse(){
        var timerCours = false
        viewModel.checkCourseBuy({
            Log.d("startTimerVic2", it.toString())
            when(it){
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.SUCCESS -> {


                    Log.d("startTimerVic","testBool:${it.toString()}")
                    if(!timerCours){
                        if (victorineSec?.equals(0) == false) {
                            CustomTimerUtil.startTimer(victorineSec) {
                                checkTestTreatmentResult(victorinesQuestions ?: emptyList(), true)
                            }
                        }
                    }else{
                        requireActivity().runOnUiThread {
                            binding?.tvTimer?.text = "∞"
                        }

                    }

                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewVictorine?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            startTimerFun()
                        }) {

                            binding?.dimViewVictorine?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.OFFLINEMODE -> {

                    binding?.tvTimer?.text = "∞"



                }
                ErrorEnum.OFFLINETHEMEBUY -> TODO()
            }
        },{
            Log.d("startTimerVic2", "${it} it.toString()")
            timerCours = it
        })
    }


    override fun onStart() {
        super.onStart()
        timerObser?.let { CustomTimerUtil.timerValue.observe(this, it) }
        if (victorineExit) {
            Toast.makeText(requireContext(), R.string.term_exit_victorine, Toast.LENGTH_SHORT)
                .show()
            val action =
                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(
                    args.courseNumber,
                    args.courseNameReal
                )
            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
        }
    }
}