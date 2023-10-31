package workwork.test.andropediagits.presenter.bottomSheet

import android.app.Activity
import android.content.Intent
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.core.utils.GoogleAdManager
import workwork.test.andropediagits.databinding.FragmentVictorineBottomSheetBinding
import workwork.test.andropediagits.presenter.bottomSheet.viewModels.VictorineBottomSheetViewModel
import workwork.test.andropediagits.presenter.lesson.ListLessonsFragmentDirections
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import java.util.concurrent.atomic.AtomicBoolean

// TODO: Rename parameter arguments, choose names that match

@AndroidEntryPoint
class VictorineBottomSheetFragment : BottomSheetDialogFragment() {

    private var googleMobileAdsConsentManager: GoogleAdManager?=null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var isLoading = false
    private var timer: CountDownTimer?=null
    private var terString = ""
    private var isTermVar = false
    private var startTimerViewAds = false
    protected val navController: NavController by lazy {
        Navigation. findNavController(
           requireActivity(),
        R.id. fragmentContainerView)
    }
    private val viewModel: VictorineBottomSheetViewModel by viewModels()
    private var binding: FragmentVictorineBottomSheetBinding? = null
    private var isTermVictorine = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVictorineBottomSheetBinding.inflate(inflater,container,false)
        googleMobileAdsConsentManager = GoogleAdManager(requireActivity())
        isTermVar = arguments?.getBoolean("isTerm",false) == true
        terString = arguments?.getString("termDate","Invalid date").toString()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colors = intArrayOf(    ContextCompat.getColor(requireContext(),R.color.color1), // Замените R.color.color1 на цвет
            ContextCompat.getColor(requireContext(),R.color.color2), ContextCompat.getColor(requireContext(),R.color.color3)
        )
        val shader = binding?.tvGradient?.textSize?.let {
            LinearGradient(0f, 0f, 0f,
                it, colors, null, Shader.TileMode.CLAMP)
        }
        binding?.tvGradient?.paint?.shader = shader



//        val textView = binding?.textBuyPremium
//
//        val fullText = "Купить PREMIUM сегодня"
//
//        // Создаем SpannableString
//        val spannableString = SpannableString(fullText)
//
//        // Создаем градиент с тремя цветами
//        val gradientColors = intArrayOf(0xFF1290D9.toInt(), 0xFFCD5DA5.toInt(), 0xFFF6870C.toInt()) // Три цвета для градиента
//        val positions = floatArrayOf(0.0f, 0.5f, 1.0f) // Положения цветов (0.0 - начало, 0.5 - середина, 1.0 - конец)
//        val shader = LinearGradient(0f, 0f, 0f, textView?.textSize ?: 1.0f, gradientColors, positions, Shader.TileMode.CLAMP)
//        val gradientSpan = ShaderSpan(shader)
//
//        // Находим индекс начала и конца слова "PREMIUM" в тексте
//        val startIndex = fullText.indexOf("PREMIUM")
//        val endIndex = startIndex + "PREMIUM".length
//
//        // Применяем градиент к слову "PREMIUM"
//        spannableString.setSpan(gradientSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//
//        // Устанавливаем SpannableString в TextView
//        textView?.text = spannableString


        googleMobileAdsConsentManager?.gatherConsent { error ->
            if (error != null) {
                // Consent not obtained in current session.
                Log.d("TAG33333", "${error.errorCode}: ${error.message}")
            }

            if (googleMobileAdsConsentManager?.canRequestAds == true) {
                initializeMobileAdsSdk()
            }

            if (googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true) {
                // Regenerate the options menu to include a privacy setting.
                requireActivity().invalidateOptionsMenu()
            }
        }

        if (googleMobileAdsConsentManager?.canRequestAds == true) {
            initializeMobileAdsSdk()
        }


        val uniqueCurrentThemeId = arguments?.getInt("uniqueThemeID",2)
        val courseNumber = arguments?.getInt("courseNumber",1)
        val courseName = arguments?.getString("courseName","")
         val isThemePassed = arguments?.getBoolean("isThemePassed") ?: false
        val courseNameReal = arguments?.getString("courseNameReal","")
//        viewModel.checkCurrentThemeTerm(uniqueCurrentThemeId ?: 2,{state->
//            when(state){
//                ErrorEnum.NOTNETWORK -> {
//                    TODO()
//                }
//                ErrorEnum.ERROR -> {
//                    TODO()
//                }
//                ErrorEnum.SUCCESS -> {
//                    Log.d("isTernVarStateVictorine",isTermVar.toString())
        if(isTermVar){
            binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
            isTermVictorine = false
        }else{
            if(terString == ""){
                binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
                isTermVictorine = false

            } else if(terString == "Invalid date"){
                binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
                isTermVictorine = false
            }else{

                if(!isThemePassed){
                    isTermVictorine = true
                    binding?.linearTermVictorineSheet?.visibility = View.VISIBLE

                    binding?.termTextViewVictorineBottom?.text = terString
                }

            }
        }
//                        viewModel.howManyTerm({stateHow->
//                            when(stateHow){
//                                ErrorEnum.NOTNETWORK -> TODO()
//                                ErrorEnum.ERROR -> TODO()
//                                ErrorEnum.SUCCESS -> {
//                                    Log.d("termVictoBottom",terString.toString())
//                                    if(terString == "Invalid date"){
//                                        binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
//                                        isTermVictorine = false
//                                    }else{
//                                        isTermVictorine = true
//                                        binding?.linearTermVictorineSheet?.visibility = View.VISIBLE
//
//                                        binding?.termTextViewVictorineBottom?.text = terString
//                                    }
//
//
//                                }
//                                ErrorEnum.UNKNOWNERROR -> TODO()
//                                ErrorEnum.TIMEOUTERROR -> TODO()
//                                ErrorEnum.NULLPOINTERROR -> TODO()
//                                ErrorEnum.OFFLINEMODE -> TODO()
//                                ErrorEnum.OFFLINETHEMEBUY -> TODO()
//                            }
//                        },{termText->
//                            terString = termText
//                        })
//                    }
//                }
//                ErrorEnum.UNKNOWNERROR -> {
//                    TODO()
//                }
//                ErrorEnum.TIMEOUTERROR -> {
//                    TODO()
//                }
//                ErrorEnum.NULLPOINTERROR -> {
//                    TODO()
//                }
//                ErrorEnum.OFFLINEMODE -> {
//                    TODO()
//                }
//                ErrorEnum.OFFLINETHEMEBUY -> {
//                    TODO()
//                }
//            }
//        }, { isTerm ->
//            isTermVar = isTerm
//        })




        binding?.btnWatchAdBS?.setOnClickListener {
            if(!isTermVictorine){
             Toast.makeText(requireContext(),"Реклама доступна только при задержке",Toast.LENGTH_SHORT).show()
            }else{
                var isActualNotTerm = false
                viewModel.checkLimitActual({state->
                    when(state){
                        ErrorEnum.NOTNETWORK -> {
                            TODO()
                        }
                        ErrorEnum.ERROR -> {
                            TODO()
                        }
                        ErrorEnum.SUCCESS -> {
                            Log.d("adsViewCount","isActualTerm:${isActualNotTerm}")
                            Log.d("adsViewCount","isStartTimer:${startTimerViewAds}")

                            if(isActualNotTerm){
                                if(startTimerViewAds){
                                    Toast.makeText(requireContext(),"Реклама будет доступна через 11секунд",Toast.LENGTH_SHORT).show()
                                }else{
                                    showRewardedVideo()
                                }
                            }else{
                                Toast.makeText(requireContext(),"У вас исчерпан лимит рекламы на 2часа",Toast.LENGTH_SHORT).show()
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            TODO()
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            TODO()
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            TODO()
                        }
                        ErrorEnum.OFFLINEMODE -> {

                        }
                        ErrorEnum.OFFLINETHEMEBUY -> {
                            TODO()
                        }
                    }
                },{
                    isActualNotTerm = it
                })
            }
        }

        binding?.btnVictorineBS?.setOnClickListener {
            if(isTermVictorine){
                if(!isThemePassed){
                    Toast.makeText(requireContext(),"Викторина закрыта на задержку до:${terString}",Toast.LENGTH_LONG).show()
                }else{
                    ShowDialogHelper.showDialogAttention(requireContext()) {
                        dismiss()
                        val action =
                            ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                                uniqueCurrentThemeId ?: 2,
                                courseName ?: "defaultTest",
                                courseNumber ?: 1,
                                courseNameReal ?: ""

                            )
                        navController.navigate(action)
                    }
                }

            }else{
//                val parentFragment = targetFragment as ListLessonsFragment
//                val argument = Bundle()
//                argument.putString("HowNavNext","victorine")
//                parentFragment.onActivityResult(22234,Activity.RESULT_OK, Intent().putExtras(argument))
//                dismiss()
                ShowDialogHelper.showDialogAttention(requireContext()) {
                    dismiss()
                    val action =
                        ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                            uniqueCurrentThemeId ?: 2,
                            courseName ?: "defaultTest",
                            courseNumber ?: 1,
                            courseNameReal ?: ""
                        )
                    navController.navigate(action)
                    }
                }

//                binding?.root?.let {
//                    Navigation.findNavController(it).navigate(action)
//                }
            }

        binding?.btnBuyPremiumBS?.setOnClickListener {
            dismiss()
            val action =
                ListLessonsFragmentDirections.actionListLessonsFragmentToThemesFragment(
                    courseNumber ?: 1,
                    courseNameReal ?: "lvptgtv",
                    true
                )
            navController.navigate(action)
        }


        }

    private fun loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true
            var adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                requireContext(),
                Constatns.AD_UNIT_ID,
                adRequest,
                object : RewardedAdLoadCallback() {
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

    private fun showRewardedVideo() {

        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Log.d("TAG", "Ad was dismissed.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                        if (googleMobileAdsConsentManager?.canRequestAds == true) {
                            loadRewardedAd()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d("TAG", "Ad failed to show.")
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("TAG", "Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }

            rewardedAd?.show(
                requireActivity(),
                OnUserEarnedRewardListener { rewardItem ->
                    // Handle the reward.
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    minusTwoHoursTerm()
                    Log.d("TAG", "User earned the reward.")
                }
            )
            viewModel.adsView({ state->
                when(state){
                    ErrorEnum.NOTNETWORK -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.ERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.SUCCESS -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.UNKNOWNERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.TIMEOUTERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.NULLPOINTERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.OFFLINEMODE -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                    ErrorEnum.OFFLINETHEMEBUY -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                }
            },true)
        }

//        binding.showVideoButton.visibility = View.INVISIBLE

    }

    private fun minusTwoHoursTerm(){
        viewModel.minus2HoursTermAds({state->
            when(state){
                ErrorEnum.NOTNETWORK -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.ERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.SUCCESS -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.UNKNOWNERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.TIMEOUTERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.NULLPOINTERROR -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.OFFLINEMODE -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
                ErrorEnum.OFFLINETHEMEBUY -> Log.d("fprlfrpfl404r004t5t5","NOTNETWORK")
            }
        },arguments?.getInt("uniqueThemeID",2) ?: 0,{ remainingHours->
           if(remainingHours=="0null"){

           }
        })
    }

    private fun startTimerViewAdsFun(){
        timer = object : CountDownTimer(11 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) { millisUntilFinished / 1000        }
            override fun onFinish() {
                startTimerViewAds = false
            }
        }
        timer?.start()
    }


    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) {
            return
        }

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(requireContext()) {}
        // Load an ad.
        loadRewardedAd()
    }





    }

