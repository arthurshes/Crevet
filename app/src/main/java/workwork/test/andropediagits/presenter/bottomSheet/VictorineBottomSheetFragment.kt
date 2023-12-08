package workwork.test.andropediagits.presenter.bottomSheet

import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation

import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.my.target.ads.Reward
import com.my.target.common.models.IAdLoadingError

import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.core.utils.GoogleAdManager
import workwork.test.andropediagits.databinding.FragmentVictorineBottomSheetBinding
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.presenter.bottomSheet.viewModels.VictorineBottomSheetViewModel
import workwork.test.andropediagits.presenter.lesson.ListLessonsFragmentDirections
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import java.util.concurrent.atomic.AtomicBoolean

// TODO: Rename parameter arguments, choose names that match

@AndroidEntryPoint
class VictorineBottomSheetFragment : BottomSheetDialogFragment() {
    private var isDialogOpen = false
    private var adTarget: com.my.target.ads.RewardedAd? = null
    private var googleMobileAdsConsentManager: GoogleAdManager? = null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private var isLoading = false
    private var timer: CountDownTimer? = null
    private var terString = ""
    private var isTermVar = false
    private var startTimerViewAds = false
    protected val navController: NavController by lazy {
        Navigation.findNavController(
            requireActivity(),
            R.id.fragmentContainerView
        )
    }
    private val viewModel: VictorineBottomSheetViewModel by viewModels()
    private var binding: FragmentVictorineBottomSheetBinding? = null
    private var isTermVictorine = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adTarget = com.my.target.ads.RewardedAd(1455175, requireContext())
        binding = FragmentVictorineBottomSheetBinding.inflate(inflater, container, false)
        googleMobileAdsConsentManager = GoogleAdManager(requireActivity())
        isTermVar = arguments?.getBoolean("isTerm", false) == true
        terString = arguments?.getString("termDate", "Invalid date").toString()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val colors = intArrayOf(
            ContextCompat.getColor(requireContext(), R.color.color1),
            ContextCompat.getColor(requireContext(), R.color.color2),
            ContextCompat.getColor(requireContext(), R.color.color3)
        )
        val shader = binding?.tvGradient?.textSize?.let { LinearGradient(0f, 0f, 0f, it, colors, null, Shader.TileMode.CLAMP) }
        binding?.tvGradient?.paint?.shader = shader
        googleMobileAdsConsentManager()

        if (googleMobileAdsConsentManager?.canRequestAds == true) {
            initializeMobileAdsSdk()
        }


        val uniqueCurrentThemeId = arguments?.getInt("uniqueThemeID", 2)
        val courseNumber = arguments?.getInt("courseNumber", 1)
        val courseName = arguments?.getString("courseName", "")
        val isThemePassed = arguments?.getBoolean("isThemePassed") ?: false
        val courseNameReal = arguments?.getString("courseNameReal", "")

        if (isTermVar) {
            binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
            isTermVictorine = false
        } else {
            if (terString == "") {
                binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
                isTermVictorine = false

            } else if (terString == "Invalid date") {
                binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
                isTermVictorine = false
            }

            else {

                if (!isThemePassed) {
                    viewModel.termExistCheckLocal(uniqueCurrentThemeId ?: 2,{
                        if(it){
                            binding?.linearTermVictorineSheet?.visibility = View.INVISIBLE
                            isTermVictorine = false
                        }else{
                            isTermVictorine = true
                            binding?.linearTermVictorineSheet?.visibility = View.VISIBLE

                            binding?.termTextViewVictorineBottom?.text = terString
                        }
                    })

                }

            }
        }

        binding?.btnPassDelayForAndropointBS?.setOnClickListener {
            if (!isTermVictorine) {
                Toast.makeText(requireContext(), getString(R.string.delay_andropoint_victorine_aviable), Toast.LENGTH_SHORT).show()
            } else {
                delayAndropoint()
            }
        }

        binding?.btnWatchAdBS?.setOnClickListener {
            if (!isTermVictorine) {
                Toast.makeText(requireContext(), getString(R.string.advertising_is_only_available_when_there_delay), Toast.LENGTH_SHORT).show()
            } else {
                checkLimitActualTreatmentResult()

            }
        }

        binding?.btnVictorineBS?.setOnClickListener {
            inVictorine(uniqueCurrentThemeId,isThemePassed,courseName,courseNameReal,courseNumber)
        }

        binding?.btnBuyPremiumBS?.setOnClickListener {
            dismiss()
            val action =
                ListLessonsFragmentDirections.actionListLessonsFragmentToThemesFragment(courseNumber ?: 1, courseNameReal ?: "lvptgtv", true)
            navController.navigate(action)
        }


    }

    private fun delayAndropoint(){
        var buyForAndropointStates:BuyForAndropointStates?=null
        viewModel.spendAndropoints(4,{
            when(it){
                ErrorEnum.SUCCESS -> {
                     when(buyForAndropointStates){
                         BuyForAndropointStates.YESMONEY -> {
                           minusTwoHoursTerm()
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
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            delayAndropoint()
                        }) {

                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            delayAndropoint()
                        }) {

                        }
                    }
                }


                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            delayAndropoint()
                        }) {

                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            delayAndropoint()
                        }) {

                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            delayAndropoint()
                        }) {

                        }
                    }
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
            }
        },{
           buyForAndropointStates = it
        })
    }

    private fun inVictorine(uniqueCurrentThemeId: Int?, isThemePassed: Boolean, courseName: String?, courseNameReal: String?, courseNumber: Int?) {
        if (isTermVictorine) {
            if (!isThemePassed) {
                Toast.makeText(requireContext(), "${getString(R.string.victorine_is_closed_for_delay_until)}${terString} GMT+3", Toast.LENGTH_LONG).show()
            } else {
                if(!isDialogOpen) {
                    ShowDialogHelper.showDialogAttention(requireContext(), {
                        dismiss()
                        val action =
                            ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                                uniqueCurrentThemeId ?: 2,
                                courseName ?: "defaultTest",
                                courseNumber ?: 1,
                                courseNameReal ?: ""
                            )
                        navController.navigate(action)

                    }) {
                        isDialogOpen = false
                    }
                }
                isDialogOpen = true
            }

        } else {
            if(!isDialogOpen) {
                ShowDialogHelper.showDialogAttention(requireContext(), {

                    dismiss()
                    val action =
                        ListLessonsFragmentDirections.actionListLessonsFragmentToVictorineFragment(
                            uniqueCurrentThemeId ?: 2,
                            courseName ?: "defaultTest",
                            courseNumber ?: 1,
                            courseNameReal ?: ""
                        )
                    navController.navigate(action)

                }) {
                    isDialogOpen = false
                }
            }
            isDialogOpen = true
        }
    }

    private fun googleMobileAdsConsentManager() {
        googleMobileAdsConsentManager?.gatherConsent { error ->
            if (error != null) {
                Log.d("TAG33333", "${error.errorCode}: ${error.message}")
            }

            if (googleMobileAdsConsentManager?.canRequestAds == true) {
                initializeMobileAdsSdk()
            }

//            if (googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true) {
//                requireActivity().invalidateOptionsMenu()
//            }
        }
    }
    private fun checkLimitActualTreatmentResult() {
        viewModel.getMyAdsProvider {adsProviderEntity ->
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
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.advertising_will_be_available_through),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    showRewardedVideo()
                                }
                            } else {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.advertising_limit_has_been_reached),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }


                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.OFFLINEMODE -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogOffline(requireContext())
                            }
                        }

                        ErrorEnum.OFFLINETHEMEBUY -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogOffline(requireContext())
                            }
                        }
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
                                        Toast.makeText(
                                            requireContext(),
                                            getString(R.string.advertising_will_be_available_through),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                } else {
                                    initAd()
                                }
                            } else {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        requireContext(),
                                        getString(R.string.advertising_limit_has_been_reached),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }

                            }
                        }
                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }


                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult()
                                }) {

                                }
                            }
                        }

                        ErrorEnum.OFFLINEMODE -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogOffline(requireContext())
                            }
                        }

                        ErrorEnum.OFFLINETHEMEBUY -> {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.showDialogOffline(requireContext())
                            }
                        }
                    }
                }, {
                    isActualNotTerm = it
                })
            }
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
                        rewardedAd = null
                        if (googleMobileAdsConsentManager?.canRequestAds == true) {
                            loadRewardedAd()
                        }
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Log.d("TAG", "Ad failed to show.")

                        rewardedAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Log.d("TAG", "Ad showed fullscreen content.")
                    }
                }

            rewardedAd?.show(
                requireActivity(),
                OnUserEarnedRewardListener { rewardItem ->
                    val rewardAmount = rewardItem.amount
                    val rewardType = rewardItem.type
                    adsViewTreatmentResult()
                    Log.d("TAG", "User earned the reward.")
                }
            )

        }
    }

    private fun adsViewTreatmentResult() {
        viewModel.adsView({ state ->
            Log.d("victorineAdsRuTestState",state.toString())
            when (state) {
                ErrorEnum.SUCCESS -> {
                    minusTwoHoursTerm()
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {

                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {

                        }
                    }
                }


                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {

                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {

                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {

                        }
                    }
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
            }
        }, true)
    }

    private fun minusTwoHoursTerm() {
        viewModel.minus2HoursTermAds({ state ->
            Log.d("victorineAdsRuTestState","minusTwoHourseState: "+state.toString())
            when (state) {
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),getString(R.string.delay_was_successfully_reset), Toast.LENGTH_SHORT).show()
                    }

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            minusTwoHoursTerm()
                        }) {

                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusTwoHoursTerm()
                        }) {

                        }
                    }
                }


                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusTwoHoursTerm()
                        }) {

                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            minusTwoHoursTerm()
                        }) {

                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            minusTwoHoursTerm()
                        }) {

                        }
                    }
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
            }
        }, arguments?.getInt("uniqueThemeID", 2) ?: 0, { remainingHours ->
            if (remainingHours == "0null") {
                requireActivity().runOnUiThread {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.delay_has_been_successfully_reset),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            requireActivity().runOnUiThread {
                dismiss()
            }
        })
    }


    private fun startTimerViewAdsFun() {
        timer = object : CountDownTimer(11 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                millisUntilFinished / 1000
            }

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
        MobileAds.initialize(requireContext()) {}
        loadRewardedAd()
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun initAd() {

        // Устанавливаем слушатель событий
        adTarget?.setListener(object : com.my.target.ads.RewardedAd.RewardedAdListener {

            override fun onLoad(p0: com.my.target.ads.RewardedAd) {
                adTarget?.show()
            }

            override fun onNoAd(p0: IAdLoadingError, p1: com.my.target.ads.RewardedAd) {
                Toast.makeText(requireContext(),R.string.no_ads_view,Toast.LENGTH_SHORT).show()
            }

            override fun onClick(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onClick")
            }

            override fun onDismiss(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onDismiss")
            }

            override fun onReward(p0: Reward, p1: com.my.target.ads.RewardedAd) {
                adsViewTreatmentResult()
            }

            override fun onDisplay(p0: com.my.target.ads.RewardedAd) {
                Log.d("adsTargetTest","onDisplay")
            }
        })

        // Запускаем загрузку данных
        adTarget?.load()
    }

}

