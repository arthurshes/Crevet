package workwork.test.andropediagits.presenter.lesson.victorine

import android.animation.ObjectAnimator
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager


import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.review.ReviewManagerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.ErrorStateView
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.core.utils.CustomTimerUtil
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.databinding.FragmentVictorineBinding
import workwork.test.andropediagits.domain.useCases.userLogic.state.StrikeModeState
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.lesson.victorine.customview.AnimatedCircleView

import workwork.test.andropediagits.presenter.lesson.victorine.viewmodel.VictorineViewModel
import javax.inject.Inject
import kotlin.time.Duration.Companion.nanoseconds

@AndroidEntryPoint
class VictorineFragment : Fragment() {
    private var timerObser:Observer<Long>?=null
    private var binding: FragmentVictorineBinding? = null
    private var clickCount = 0
    private var pref: SharedPreferences?=null
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
    private var victorinesQuestions:List<VictorineEntity>?=null
    private var victorineAnswerVariants:List<VictorineAnswerVariantEntity>?=null
    private var correctAnswers = 0
    private var isStrikeModeAndropointFlag = false
    private var testCheckResultFlag = false
    private var isThemePassed = false
    private var misstakeAnswers = 0
    private var victorineEnded = false
    private var victorineExit = false
    private  var victorineSec:Long= 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVictorineBinding.inflate(inflater, container, false)
        return binding?.root

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCircleView(binding!!)

        pref = PreferenceManager.getDefaultSharedPreferences(requireContext())
//        startTimerTreatmentResult(args.uniqueThemeId)
        timerObser = Observer {

            if(10>=it){
                binding?.cardVictTimer?.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.error))


            } else{

            }
            binding?.tvTimer?.text = it.toString()
            Log.d("fkprkfprpfkrpkfprkfprkf",it.toString())
        }

        startTimerFun()

        viewModel.thisThemeIsPassed(args.uniqueThemeId,{
            isThemePassed = it
        })



        viewModel.resetOlddata(uniqueThemeId = args.uniqueThemeId)

//        timerObser = Observer { timerValue ->
//            Log.d("timerVict",timerValue.toString())
//            binding?.tvTimer?.text = timerValue.toString()
//        }

        viewModel.getAllVictorineTheme(args.uniqueThemeId){ victorineEntities ->
            Log.d("vicotinresViewModel4",
                victorineEntities.toString()
            )
            victorinesQuestions = victorineEntities
//            victorinesQuestions?.shuffled()
            binding?.tvQuestion?.text = victorinesQuestions?.get(currentIndex)?.questionText ?: ""
            viewModel.getAllQuestionAnswerVariants(victorineEntities[0].questionId){ victorineAnswerVariantEntities ->
                showFirstElement(victorineAnswerVariantEntities, binding)
                victorineAnswerVariants = victorineAnswerVariantEntities
                Log.d("vicotinresViewModel4",
                    victorineAnswerVariantEntities.toString()
                )
            }
        }

        //  viewModel.allVictorineAnswerVariantByTheme?.observe(viewLifecycleOwner) { victorineAnswerVariant ->
        Log.d("vicotinresViewModel",
            victorinesQuestions.toString()
        )
        binding?.apply {

//                tvQuestion.text = victorinesQuestions?.get(currentIndex)?.questionText ?: ""
//                victorineUseCase.getVictoineAnswerVariantsWithQuestionId(victorines[0].questionId)

            /////commit 15sep
//                        viewModel.putUniqueThemeIdForGetVictorine(args.uniqueThemeId)
//                         victorineAnswerVariants =  viewModel.getVictoineAnswerVariantsWithQuestionId(victorines[currentIndex].questionId)
//                victorinesQuestions?.get(currentIndex)?.let {
//
//                }

//                victorineAnswerVariants?.shuffled()
//            showFirstElement(victorineAnswerVariants ?: emptyList(), binding)
            btnNext.setOnClickListener {
                Log.d("victorineGrhtut",currentIndex.toString())
//                if((victorinesQuestions?.size ?: 0) > currentIndex){
                    Log.d("victorineGrhtut",progress.toString())

                    checkAnswer(victorineAnswerVariants)
                    tvQuestion.text = victorinesQuestions?.get(currentIndex)?.questionText ?: ""
                    if (progress < (victorinesQuestions?.size ?: 0)) {
                        updateProgressBarWithAnimation(victorinesQuestions ?: emptyList())
                    }
//                }

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

//    private fun startTimerTreatmentResult(uniqueThemeId: Int) {
////        var resultTimer: ErrorEnum? = null
//        var isSubscribe=false
//        var isEnding=false
//
//        viewModel.startTimer({ resultTimer ->
//            when (resultTimer) {
//                ErrorEnum.SUCCESS -> {
//                    requireActivity().runOnUiThread {
//                        if(isEnding){
//                            /*  val action =
//                                  VictorineFragmentDirections.actionVictorineFragmentToListLessonsFragment(args.uniqueThemeId, args.courseName)
//                              binding?.root?.let { Navigation.findNavController(it).navigate(action) }
//                              mistakeTest?.let {
//                                  ShowDialogHelper.showDialogFailTest(requireContext(), it, victorines.size,dateUnlockTheme)
//                              }*/
//                            Toast.makeText(requireContext(),"end",Toast.LENGTH_LONG).show()
//                        } }
//
//                }
//
//                ErrorEnum.OFFLINEMODE -> {
//                    requireActivity().runOnUiThread{
//                        if(isEnding){
//
//                            Toast.makeText(requireContext(),"endOFFLINEMODE",Toast.LENGTH_LONG).show()
//                        }}
//
//                }
//                ErrorEnum.OFFLINETHEMEBUY -> {
//                    requireActivity().runOnUiThread{
//                        if(isEnding){
//
//                            Toast.makeText(requireContext(),"endOFFLINETHEMEBUY",Toast.LENGTH_LONG).show()
//                        }}
//
//                }
//                ErrorEnum.NOTNETWORK -> {
//                    requireActivity().runOnUiThread{                    ShowDialogHelper.showDialogNotNetworkError(requireContext()){
//                        startTimerTreatmentResult(uniqueThemeId)
//                    }
//                        Toast.makeText(requireContext(),"NOTNETWORK",Toast.LENGTH_LONG).show()}
//
//                }
//
//                ErrorEnum.ERROR -> {
//                    requireActivity().runOnUiThread{ ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                        startTimerTreatmentResult(uniqueThemeId)
//                    }
//                        Toast.makeText(requireContext(),"ERROR",Toast.LENGTH_LONG).show()}
//
//                }
//
//                ErrorEnum.UNKNOWNERROR -> {
//                    requireActivity().runOnUiThread{                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                        startTimerTreatmentResult(uniqueThemeId)
//                    }
//                        Toast.makeText(requireContext(),"UNKNOWNERROR",Toast.LENGTH_LONG).show()}
//
//                }
//
//                ErrorEnum.TIMEOUTERROR -> {
//                    requireActivity().runOnUiThread{   ShowDialogHelper.showDialogTimeOutError(requireContext()) {
//                        startTimerTreatmentResult(uniqueThemeId)
//                    }}
//
//                }
//
//                ErrorEnum.NULLPOINTERROR -> {
//                    requireActivity().runOnUiThread{                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                        startTimerTreatmentResult(uniqueThemeId)
//                    }
//                        Toast.makeText(requireContext(),"NULLPOINTERROR",Toast.LENGTH_LONG).show()}
//
//                }
//                else -> {
//                    requireActivity().runOnUiThread{                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                        startTimerTreatmentResult(uniqueThemeId) }
//                        Toast.makeText(requireContext(),"else",Toast.LENGTH_LONG).show()}
//
//                }
//            }
//        },uniqueThemeId, { isSubscribe = it },{isEnding=it})
//
//
//    }

    private fun showFirstElement(
        item: List<VictorineAnswerVariantEntity>,
        binding: FragmentVictorineBinding?
    ) {
        Log.d("vicotinresViewModel4","showFirst:${item}")
        binding?.apply {
            if(item.size>=1) {
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
            if(item.size>=2) {
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
            if(item.size>=3) {
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
            if(item.size>=4) {
                optionFourth.cardOption.visibility = View.VISIBLE
                optionFourth.textView.visibility = View.VISIBLE
                optionFourth.animatedCircleView.visibility = View.VISIBLE
                optionFifth.cardOption.visibility = View.GONE
                optionFifth.textView.visibility = View.GONE
                optionFifth.animatedCircleView.visibility = View.GONE
                optionFourth.textView.text = item.get(3).text
            }
            if(item.size>=5) {
                optionFifth.cardOption.visibility = View.VISIBLE
                optionFifth.textView.visibility = View.VISIBLE
                optionFifth.animatedCircleView.visibility = View.VISIBLE
                optionFifth.textView.text = item.get(4).text
            }
        }

    }

    private fun showNextElement() {
        if (victorinesQuestions?.size?.minus(1)!=currentIndex){
            currentIndex++
        }
        Log.d("victorineGrhtut",victorinesQuestions.toString())
        Log.d("victorineGrhtut","currentIndex:${currentIndex}")
//        viewModel.getAllVictorineTheme(args.uniqueThemeId){ victorineEntities ->
//            victorinesQuestions = victorineEntities
            binding?.tvQuestion?.text = victorinesQuestions?.get(currentIndex)?.questionText ?: ""
        victorinesQuestions?.get(currentIndex)?.let {
            viewModel.getAllQuestionAnswerVariants(it.questionId){ victorineAnswerVariantEntities ->
                showFirstElement(victorineAnswerVariantEntities, binding)
                victorineAnswerVariants = victorineAnswerVariantEntities
                Log.d("vicotinresViewModel4",
                    victorineAnswerVariantEntities.toString()
                )
            }
        }
//        }
    }

    private fun checkAnswer(item: List<VictorineAnswerVariantEntity>?) {
        if (isFirstItemSelected) {
//            if((victorinesQuestions?.size ?: 0 ) > currentIndex) {
                checkResultAnswer(0, item)
//            }
        }
        if (isSecondItemSelected) {
//            if((victorinesQuestions?.size ?: 0 ) > currentIndex) {
                checkResultAnswer(1, item)
//            }
        }
        if (isThirdItemSelected) {
//            if((victorinesQuestions?.size ?: 0 ) > currentIndex) {
                checkResultAnswer(2, item)
//            }
        }
        if (isForthItemSelected) {
//            if((victorinesQuestions?.size ?: 0 )> currentIndex) {
                checkResultAnswer(3, item)
//            }
        }
        if (isFifthItemSelected) {
//            if((victorinesQuestions?.size ?: 0 ) > currentIndex) {
                checkResultAnswer(4, item)
//            }
        }
    }

    private fun checkResultAnswer(
        numberItem: Int,
        item: List<VictorineAnswerVariantEntity>?
    ) {
//        var resultAnswer: ErrorEnum? = null
        val currentAnswer = item?.get(numberItem)
        if(currentAnswer?.isCorrectAnswer == true){
           correctAnswers++
        } else{
            misstakeAnswers++
        }
        viewModel.checkAnswer(item!![numberItem], { resultAnswer ->
            Log.d("checkAnswerState",resultAnswer.toString())
            when (resultAnswer) {
                ErrorEnum.SUCCESS -> {
                    Log.d("succsNextVictQuestCo","victorines:${victorinesQuestions}")
                    Log.d("succsNextVictQuestCo","currentIndex:${currentIndex}")
                    Log.d("succsNextVictQuestCo", (victorinesQuestions?.size ?: 0).toString())
//                    if((victorinesQuestions?.size ?: 0 ) > currentIndex){
                    if (victorinesQuestions?.size!=currentIndex) {
                        showNextElement()
                    }
//                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    ShowDialogHelper.showDialogNotNetworkError(requireContext()){
                        checkResultAnswer(numberItem, item)
                    }
                }

                ErrorEnum.ERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                        checkResultAnswer(numberItem, item)
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                        checkResultAnswer(numberItem, item) }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    ShowDialogHelper.showDialogTimeOutError(requireContext()){
                        checkResultAnswer(numberItem, item) }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                        checkResultAnswer(numberItem, item) }
                }

                else -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkResultAnswer(numberItem, item) }
                }
            }
        }, { isClue ->
            Log.d("victorineClueadfrofkrof",isClue.toString())
            if(pref?.getString(Constatns.CLUE_KEY,"")=="true") {
                if (isClue != null) {
Toast.makeText(requireActivity(),isClue,Toast.LENGTH_LONG).show()
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
            CustomTimerUtil.stopTimer()
                checkTestTreatmentResult(victorines,false)
        }
    }

    private fun showFeedbackDialog(){
        val reviewManager = ReviewManagerFactory.create(requireActivity().applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if(it.isSuccessful){
                reviewManager.launchReviewFlow(requireActivity(),it.result)
            }
        }
    }

    private fun checkTestTreatmentResult(victorines: List<VictorineEntity>,isTimerOut:Boolean) {

//        var resultTest: ErrorStateView? = null
        victorineEnded = true
        var mistakeTest: String? = null
        var dateUnlockTheme:((String)->Unit)?=null
        viewModel.checkTestResult(args.uniqueThemeId, { resultTest->
         Log.d("victorineTestResultState",resultTest.toString())
            when (resultTest) {
                ErrorStateView.SUCCESS -> {}

                ErrorStateView.NEXTTHEME -> {
                 if(!testCheckResultFlag){

                         testCheckResultFlag = true
                         if(isThemePassed){
                             strikeModeTreatmentResult()
                         } else{
                             requireActivity().runOnUiThread {
                                 ShowDialogHelper.showDialogSuccessTest(requireContext()) {
                                     strikeModeTreatmentResult()
                                 }
                             }
                             showFeedbackDialog()
                             addAndropointsTreatmentResult()
                         }

                 }

                }

                ErrorStateView.OFFLINE -> {
                    requireActivity().runOnUiThread {
                        val action = VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseName)
                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                        ShowDialogHelper.showDialogSuccessTest(requireContext()) {
                            strikeModeTreatmentResult()
                        }
                    }
                }

                ErrorStateView.TERM -> {
                    dateUnlockTheme = {dateUnlockTheme->
                        Log.d("victorineTestResultState","term tw")
                        requireActivity().runOnUiThread {

//                            mistakeTest?.let {
                                ShowDialogHelper.showDialogFailTest(requireContext(),correctAnswers ,misstakeAnswers, victorines.size,dateUnlockTheme,{
                                    strikeModeTreatmentResult()
                                },isTimerOut)
//                            }
                        }
                    }
                }

                ErrorStateView.OFFLINEBUYTHEME -> {}
                ErrorStateView.TRYAGAIN -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkTryAgainResult()
                            checkTestTreatmentResult(victorines,isTimerOut)
                        }
                    }
                }

                ErrorStateView.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext()
                        ) {

                            checkTryAgainResult()
                            checkTestTreatmentResult(victorines,isTimerOut)
                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {    ShowDialogHelper.showDialogUnknownError(
                        requireContext()
                    ) {
                        checkTryAgainResult()
                        checkTestTreatmentResult(victorines,isTimerOut)
                    } }

                }
            }
        },{ dateUnlockTheme?.invoke(it)
            Log.d("misstakeDialog","dateTest:${it}")},correctAnswers,misstakeAnswers,isTimerOut)

    }

    private fun addAndropointsTreatmentResult() {
        viewModel.addAndropoints { resultAddAndropoints->
            Log.d("victorineTestResultStateAddAndropoint",resultAddAndropoints.toString())
            when (resultAddAndropoints) {

                ErrorEnum.SUCCESS -> {}

                ErrorEnum.OFFLINEMODE -> {
                    /* ShowDialogHelper.showDialogAttentionStrikeMode(requireContext()) { isNeedTryAgain = it }
                     if (isNeedTryAgain) {
                         strikeModeTreatmentResult()
                     }*/
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    /* ShowDialogHelper.showDialogAttentionStrikeMode(requireContext()) { isNeedTryAgain = it }
                     if (isNeedTryAgain) {
                         strikeModeTreatmentResult()
                     }*/
                }
                ErrorEnum.NOTNETWORK -> {
                    ShowDialogHelper.showDialogNotNetworkError(
                        requireContext()
                    ) {
                        addAndropointsTreatmentResult()
                    }
                }

                ErrorEnum.ERROR -> {
                    ShowDialogHelper.showDialogUnknownError(
                        requireContext()
                    ) {
                        addAndropointsTreatmentResult()
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    ShowDialogHelper.showDialogUnknownError(
                        requireContext()
                    ) {
                        addAndropointsTreatmentResult()
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    ShowDialogHelper.showDialogTimeOutError(
                        requireContext()
                    ) {
                        addAndropointsTreatmentResult()
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    ShowDialogHelper.showDialogUnknownError(
                        requireContext()
                    ) {
                        addAndropointsTreatmentResult()
                    }
                }
                else -> {  ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    addAndropointsTreatmentResult() } }
            }
        }

    }

    private fun strikeModeTreatmentResult() {
        var resultStrikeModeDay = 0
        var isNextFlag = false
        viewModel.strikeModeProgress({ resultStrikeMode->
            Log.d("victorineTestResultStateSimpleTreamtResult",resultStrikeModeDay.toString())
            if(resultStrikeModeDay==0&&!isNextFlag){
                isNextFlag = true
                requireActivity().runOnUiThread {
                    val action =
                        VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseName)
                    binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                }
            } else{
                Log.d("victorineTestResultStateSimpleTreamtResult",resultStrikeMode.toString())
                when (resultStrikeMode) {
                    ErrorEnum.SUCCESS -> {
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                    }

                    ErrorEnum.OFFLINEMODE -> {
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
//                    ShowDialogHelper.showDialogAttentionStrikeMode(requireContext()) {
//                        strikeModeTreatmentResult()
//                    }
                    }
                    ErrorEnum.OFFLINETHEMEBUY -> {
                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
//                    ShowDialogHelper.showDialogAttentionStrikeMode(requireContext()) {
//                        strikeModeTreatmentResult()
//                    }
                    }
                    ErrorEnum.NOTNETWORK -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogNotNetworkError(
                                requireContext()
                            ) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }

                    ErrorEnum.ERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext()
                            ) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }

                    ErrorEnum.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext()
                            ) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }

                    ErrorEnum.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogTimeOutError(
                                requireContext()
                            ) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }

                    ErrorEnum.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogUnknownError(
                                requireContext()
                            ) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }
                    else -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                strikeModeTreatmentResult()
                            }
                        }
                    }
            }
            }
        }, { resultStrikeModeDay = it
        })

    }

    private fun strikeModeAndropointTreatmentResult(resultStrikeModeDay: Int) {
        strikeModeAndropointChooseDay(resultStrikeModeDay) { resultStrikeModeAndropoints->
            Log.d("victorineTestResultStateStrikeResultTreaAndropoint",resultStrikeModeAndropoints.toString())
            var subscribeActual = false
            when (resultStrikeModeAndropoints) {
                ErrorEnum.SUCCESS -> {

                    viewModel.checSubscribe({
                         subscribeActual = it
                    },{checkkState->
                        when(checkkState){
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }
                                }
                            }
                            ErrorEnum.ERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }
                                }
                            }
                            ErrorEnum.SUCCESS -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogStrikeMode(
                                        requireContext(),
                                        resultStrikeModeDay,
                                        layoutInflater,
                                        subscribeActual,
                                        {
                                            val action =
                                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
                                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                        },
                                        {
                                            val action =
                                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal,premiumVisible = true)
                                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                        }
                                        )
                                }
                            }
                            ErrorEnum.UNKNOWNERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }
                                }
                            }
                            ErrorEnum.TIMEOUTERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogTimeOutError(
                                        requireContext()
                                    ) {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }
                                }
                            }
                            ErrorEnum.NULLPOINTERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(
                                        requireContext()
                                    ) {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }
                                }
                            }
                            ErrorEnum.OFFLINEMODE -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                                        strikeModeTreatmentResult()
                                    }) {
                                        val action =
                                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
                                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                    }
                                }
                            }
                            ErrorEnum.OFFLINETHEMEBUY -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                                        strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                                    }) {
                                        val action =
                                            VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
                                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                    }
                                }
                            }
                        }
                    })
//                    val action = VictorineFragmentDirections.actionVictorineFragmentToListLessonsFragment(args.uniqueThemeId, args.courseName)
//                    binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                }
                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                            strikeModeTreatmentResult()
                        }) {
                            val action =
                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                        }
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogAttentionStrikeMode(requireContext(), {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }) {
                            val action =
                                VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                        }
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(
                            requireContext()
                        ) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(
                            requireContext()
                        ) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }
                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            strikeModeAndropointTreatmentResult(resultStrikeModeDay)
                        }
                    }
                }
            }
        }

    }

    private fun strikeModeAndropointChooseDay(resultStrikeModeDay: Int, resultStrikeModeAndropoints: (ErrorEnum) -> Unit) {
        Log.d("tttttt22tt",resultStrikeModeDay.toString())
        if(!isStrikeModeAndropointFlag){
            isStrikeModeAndropointFlag = true
            when (resultStrikeModeDay) {
                1 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it) }, StrikeModeState.ONE) }
                2 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it)}, StrikeModeState.TWO)}
                3 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it) }, StrikeModeState.THREE) }
                4 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it) }, StrikeModeState.FOUR) }
                5 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it)}, StrikeModeState.FIVE) }
                6 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it)}, StrikeModeState.SIX) }
                7 -> { viewModel.strikeModeAndropointProgress({ resultStrikeModeAndropoints(it)}, StrikeModeState.SEVEN) }
            }
        }
    }


    private fun checkTryAgainResult() {
        viewModel.tryAgainSendProgress({ resultTryAgain->
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
                    ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                        checkTryAgainResult()
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkTryAgainResult()
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                        checkTryAgainResult()
                    }
                }
                ErrorEnum.ERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkTryAgainResult()
                    }
                }
                ErrorEnum.UNKNOWNERROR -> { ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    checkTryAgainResult()
                }
                }
                else -> {ShowDialogHelper.showDialogUnknownError(requireContext()) { checkTryAgainResult( ) }}
            }
        })
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onStop() {
        super.onStop()
        CustomTimerUtil.stopTimer()
//        timerObser?.let { viewModel.timerValue?.removeObserver(it) }
        timerObser?.let { CustomTimerUtil.timerValue.removeObserver(it) }
        if(!victorineEnded){

            viewModel.victorineExit(args.uniqueThemeId,{
               if(it){
                   victorineExit = true
               }
            },{

            })
        }
    }

    private fun startTimerFun(){
       var timerStart = false


        viewModel.getTimeVictorine({sec->
            Log.d("kfkrofkorkfo5t59t9564g54",sec.toString())
            victorineSec = sec
        },args.uniqueThemeId,{
            when(it){
                ErrorEnum.SUCCESS -> {
                    Log.d("kfkrofkorkfo5t59t9564g5444",timerStart.toString())
                    Log.d("kfkrofkorkfo5t59t9564g5444",victorineSec.toString())
                   if(!timerStart){
                       if(victorineSec?.equals(0) == false){
                           CustomTimerUtil.startTimer(victorineSec,{
                               checkTestTreatmentResult(victorinesQuestions ?: emptyList(),true)
                           })
                       }
                   }else{
                       binding?.tvTimer?.text =  "∞"
                   }
                }

                ErrorEnum.NOTNETWORK -> {
                    ShowDialogHelper.showDialogNotNetworkError(requireContext()){
                        startTimerFun()
                    }
                }

                ErrorEnum.ERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                        startTimerFun()
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                         startTimerFun() }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    ShowDialogHelper.showDialogTimeOutError(requireContext()){
                        startTimerFun() }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()){
                        startTimerFun() }
                }

                else -> {
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        startTimerFun() }
                }
            }
        },{
            timerStart = it
        })
    }

    override fun onStart() {
        super.onStart()
//        timerObser?.let { viewModel.timerValue?.observe(this, it) }
        timerObser?.let { CustomTimerUtil.timerValue.observe(this, it) }
       if(victorineExit){
           Toast.makeText(requireContext(),R.string.term_exit_victorine,Toast.LENGTH_SHORT).show()
           val action =
               VictorineFragmentDirections.actionVictorineFragmentToThemesFragment(args.courseNumber,args.courseNameReal)
           binding?.root?.let { Navigation.findNavController(it).navigate(action) }
       }

    }


}