package workwork.test.andropediagits.presenter.themes

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.app.Activity

import android.widget.Button


import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizationResult
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizeIntent
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.GooglePayParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.TestParameters

import java.util.Locale


import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import com.google.android.play.core.review.ReviewManagerFactory
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.my.target.ads.Reward
import com.my.target.common.models.IAdLoadingError

import dagger.hilt.android.AndroidEntryPoint
import ru.tinkoff.decoro.BuildConfig


import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns.AD_UNIT_ID
import workwork.test.andropediagits.core.utils.Constatns.REQUEST_CODE_TOKENIZE
import workwork.test.andropediagits.core.utils.GoogleAdManager
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.BillingProviderEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.databinding.FragmentThemesBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.domain.useCases.userLogic.state.AddAndropoints
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.LanguagesEnum
import workwork.test.andropediagits.presenter.bottomSheet.BottomSheet
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.setting.SettingActivity
import workwork.test.andropediagits.presenter.themes.viewModel.ThemeViewModel
import java.math.BigDecimal
import java.util.Currency
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class ThemesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var adTarget: com.my.target.ads.RewardedAd? = null
    private var billingManager: BillingManager? = null
    private var currentUser: FirebaseUser?=null
    private var binding: FragmentThemesBinding? = null
    private var googleMobileAdsConsentManager: GoogleAdManager? = null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val args: ThemesFragmentArgs by navArgs()
    private var adapter: ThemesAdapter? = null
    private val viewModel: ThemeViewModel by viewModels()
    private var btnPremiumDrawer: LinearLayout? = null
    private var isLoading = false
    private var tvGradient:TextView?=null
    private var userNameHeader: TextView? = null
    private var countAndropoints: TextView? = null
    private var textAddAndropoint: TextView? = null
    private var parentLang: LinearLayout? = null
    private var andropointIcon:ImageView?=null
    private var listLang: LinearLayout? = null
    private var russianTextLang: TextView? = null
    private var englishTextLang: TextView? = null
    private var auth: FirebaseAuth?=null
    private var imageArrowLangDown: ImageView? = null
    private var imageArrowLangUp: ImageView? = null
    private var timer: CountDownTimer? = null
    private var startTimerViewAds = false
    private var isThemesFavorite = false
    private val russianText = "🇷🇺    Рус"
    private val englishText = "🇺🇸    Eng"
    private var premiumVisible = false


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        val moreMenu = menu.findItem(R.id.action_more)
        moreMenu.isVisible = googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("thisScreen", "ThemesFrag")
        binding = FragmentThemesBinding.inflate(inflater, container, false)
        googleMobileAdsConsentManager = GoogleAdManager(requireActivity())
        init()
        auth = FirebaseAuth.getInstance()
        adTarget = com.my.target.ads.RewardedAd(1455175, requireContext())
        currentUser = auth?.currentUser
        billingManager = BillingManager(requireActivity() as AppCompatActivity)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val action = ThemesFragmentDirections.actionThemesFragmentToCoursesFragment(false)
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
            }
        }

        if(args.feedbackVisible){
            showFeedbackDialog()
        }
        if (args.premiumVisible) {
            openBS()
        }

        viewModel.getCurrentLang { currentLang ->
            russianTextLang?.text = if (currentLang == "rus") russianText else englishText
            englishTextLang?.text = if (currentLang == "rus") englishText else russianText
        }
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val backgroundColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.light_black else R.color.white
        val textColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.white else R.color.black
        parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColorRes))
        russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))

        parentLang?.setOnClickListener { chooseLangParent(currentTheme) }


        listLang?.setOnClickListener { chooseLangParentList() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)


        if (savedInstanceState != null) {
            viewModel.currentState = savedInstanceState.getString("state_key_theme", "")
        }

        googleMobileAdsConsentManager?.gatherConsent { error ->
            if (error != null) {
                Log.d("TAG33333", "${error.errorCode}: ${error.message}")
            }

            if (googleMobileAdsConsentManager?.canRequestAds == true) {
                initializeMobileAdsSdk()
            }

            if (googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true) {
                requireActivity().invalidateOptionsMenu()
            }
        }

        if (googleMobileAdsConsentManager?.canRequestAds == true) {
            initializeMobileAdsSdk()
        }

        billingManager?.addAndropointsCount = {
            when (it) {
                1 -> {
                    val currentAndropointCount = countAndropoints?.text?.toString()?.toInt()
                    val plusAddAndropoint = currentAndropointCount?.plus(1)
                    requireActivity().runOnUiThread {
                        countAndropoints?.text = plusAddAndropoint?.toString()
                    }
                        buyAndropointsTreatmentResult(AddAndropoints.BUYONEANDROPOINT)
                }
                10 -> {
                    val currentAndropointCount = countAndropoints?.text?.toString()?.toInt()
                    val plusAddAndropoint = currentAndropointCount?.plus(10)
                    requireActivity().runOnUiThread {
                        countAndropoints?.text = plusAddAndropoint?.toString()
                    }
                    buyAndropointsTreatmentResult(AddAndropoints.BUYTENANDROPOINT)
                }

                100 ->{
                    val currentAndropointCount = countAndropoints?.text?.toString()?.toInt()
                    val plusAddAndropoint = currentAndropointCount?.plus(100)
                    requireActivity().runOnUiThread {
                        countAndropoints?.text = plusAddAndropoint?.toString()
                    }
                    buyAndropointsTreatmentResult(AddAndropoints.BUYHUNDREDANDROPOINT)
                }
            }
        }

        countAndropoints?.setOnClickListener {
            ShowDialogHelper.showDialogBuyAndropointsImplementation(requireContext(),billingManager)
            { checkLimitActualTreatmentResult(true) }
        }

        billingManager?.infinityAndropoints = {
            requireActivity().runOnUiThread {
                countAndropoints?.text = "∞"
            }
            buyAndropointsTreatmentResult(AddAndropoints.INFINITYANDROPOINTS)
        }

        billingManager?.theneBuyWithUniqueId = { uniqueIdThemeBuy ->
            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
        }


        adapter = ThemesAdapter(args,requireContext())
        adapter?.buyThemeUniqueId = { buyUniqueId ->
            adapter?.buyThemePrice = { buuyThemePrice ->
                adapter?.buyThemePossible = { possibleTheme ->
                    adapter?.buyThemeAndropointPrice = { andropointPrice ->
                        var isOpenDialogBuy = false
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogClose(requireContext(), {
                            isOpenDialogBuy = true
                            ShowDialogHelper.showDialogBuy(requireContext(), buuyThemePrice, andropointPrice, {
                                    when (buuyThemePrice) {
                                        50 -> billingManager?.billingSetup(PayState.THEMEBUYCALCUL, uniqueThemeID = buyUniqueId)
                                        120 -> billingManager?.billingSetup(PayState.THEMEBUYNOTES, uniqueThemeID = buyUniqueId)
                                        200 -> billingManager?.billingSetup(PayState.THEMEBUYNEWSLIST, uniqueThemeID = buyUniqueId)
                                    }
                                },
                                {

                                },{
                                    binding?.dimViewTheme?.visibility = View.GONE
                                })////покупка андропоинтами})
                        }, {
                            if(!isOpenDialogBuy) {
                                binding?.dimViewTheme?.visibility = View.GONE
                            }
                        },true)
                    }
                }

            }
        }

        adapter?.themeCloseUniqueThemeId = { uniqueThemeID ->
            adapter?.themeClosePossible = { possibleTheme ->
                binding?.dimViewTheme?.visibility = View.VISIBLE
                ShowDialogHelper.showDialogClose(requireContext(), dialogDissMiss = {
                      binding?.dimViewTheme?.visibility = View.GONE
                }, themeClose = true)
            }
        }

        andropointIcon?.setOnClickListener {
            ShowDialogHelper.showDialogBuyAndropointsImplementation(requireContext(),billingManager)
            { checkLimitActualTreatmentResult(true) }
        }

        adapter?.checkTermThemeUniqueThemeId = { uniqueThemeID ->
            checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeID)
        }

        adapter?.removeFavorite = { uniqueThemeId ->
            if (isThemesFavorite) {
                viewModel.putCourseNumberLocal(args.courseNumber) {
                    adapter?.diffList?.submitList(it)
                }
                isThemesFavorite = false
            }
            viewModel.removeFavorite(uniqueThemeId)
        }

        adapter?.addFavorite = { uniqueThemeId ->
            viewModel.addFavorite(uniqueThemeId)
        }

        adapter?.checkThisThemeTerm = { uniqueId -> adapter?.currentThemeName = { themeName -> adapter?.currentThemePassed = { themePassed -> checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId,themeName,themePassed) } } }

        binding?.included?.apply {
            tvNameCourse.text = args.courseName
            initRcView(rcViewTheme) }

        textAddAndropoint?.setOnClickListener {
            ShowDialogHelper.showDialogBuyAndropointsImplementation(requireContext(),billingManager)
            { checkLimitActualTreatmentResult(true) } }

        btnPremiumDrawer?.setOnClickListener {
            if(!premiumVisible){
                premiumVisible = true
                startTimerViewPremFun()
                openBS()
            }
         }

        viewModel.getDataUser { userInfo -> getUserInfo(userInfo) }
    }

    private fun getUserInfo(userInfo: UserInfoEntity) {
        if(userInfo.isInfinity == true){
            countAndropoints?.text = "∞"
            userNameHeader?.text = userInfo.name ?: "defaultName"
            return
        }
        val andropointCount = userInfo.andropointCount.toString()
        countAndropoints?.text = andropointCount
        userNameHeader?.text = userInfo.name ?: "defaultName"
    }

    private fun openBS() {
        val bs = BottomSheet()
        val data = Bundle()
        bs.arguments = data
        bs.show(requireActivity().supportFragmentManager, "Tag")
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
    }

    private fun initRcView(rcViewTheme: RecyclerView) {
        rcViewTheme.layoutManager = LinearLayoutManager(requireContext())
        rcViewTheme.adapter = adapter
        viewModel.putCourseNumberLocal(args.courseNumber) {
            val countThemePassed = it.filter { it.isThemePassed }
            val progress = (countThemePassed.size.toFloat() / it.size.toFloat() * 100).toInt()
            binding?.included?.tvNumberOfCompletedLevels?.text = "${countThemePassed.size}/${it.size}"
            binding?.included?.pbTheme?.progress = progress
            adapter?.diffList?.submitList(it)
        }
    }

    private fun chooseLangParentList() {
        if (russianTextLang?.text == russianText) {
            ShowDialogHelper.showDialogLoadData(requireContext())
            langChooseTreatmentResult(LanguagesEnum.ENGLISH)
        } else if (russianTextLang?.text == englishText) {
            ShowDialogHelper.showDialogLoadData(requireContext())
            langChooseTreatmentResult(LanguagesEnum.RUSSIAN)
        }
    }


    private fun showFeedbackDialog() {
        val reviewManager = ReviewManagerFactory.create(requireActivity().applicationContext)
        reviewManager.requestReviewFlow().addOnCompleteListener {
            if (it.isSuccessful) {
                reviewManager.launchReviewFlow(requireActivity(), it.result)
            }
        }
    }

    private fun checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId: Int, themeName: String, themePassed: Boolean) {
        var isThemeOpen = false
        viewModel.checkTermTheme(uniqueId, { errorState ->
            Log.d("nffnrfnrnfrnfnr3434434nfrnf", errorState.toString())
            when (errorState) {
                ErrorEnum.SUCCESS -> {
                    if (isThemeOpen) {
                        requireActivity().runOnUiThread {
                            val action = ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(uniqueId, themeName, themePassed, args.courseName)
                            binding?.root.let { if (it != null) { Navigation.findNavController(it).navigate(action) } }
                        }
                    } else { requireActivity().runOnUiThread { Toast.makeText(requireActivity(), getString(R.string.This_theme_is_closed_for_now), Toast.LENGTH_SHORT).show() } }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {  binding?.dimViewTheme?.visibility = View.GONE

                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {  binding?.dimViewTheme?.visibility = View.GONE

                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {  binding?.dimViewTheme?.visibility = View.GONE

                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed)
                        }) {  binding?.dimViewTheme?.visibility = View.GONE

                        }
                    }
                }
            }
        }, { isThemeOpenState ->
            isThemeOpen = isThemeOpenState
        }, {})
    }

    private fun chooseLangParent(currentTheme: Int) {
        if (listLang?.visibility == View.VISIBLE) {
            imageArrowLangDown?.visibility = View.VISIBLE
            imageArrowLangUp?.visibility = View.GONE
            listLang?.visibility = View.GONE
            val backgroundColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.light_black else R.color.white
            val textColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.white else R.color.black
            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColorRes))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
        } else {
            val backgroundColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.light_black else R.color.white
            val textColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.white else R.color.black

            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColorRes))
            listLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), backgroundColorRes))
            englishTextLang?.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(), textColorRes))
            listLang?.visibility = View.VISIBLE
            imageArrowLangDown?.visibility = View.GONE
            imageArrowLangUp?.visibility = View.VISIBLE
        }
    }

    private fun buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy: Int) {
        viewModel.buyThemeForMoney(uniqueIdThemeBuy) { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),getString(R.string.theme_was_successfully_purchased), Toast.LENGTH_SHORT).show()
                    }

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }) {

                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }) {

                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
            }
        }
    }

    private fun buyAndropointsTreatmentResult(quantityAndropoint: AddAndropoints) {
        viewModel.buyAndropoints(quantityAndropoint){
            when(it){
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),getString(R.string.andropoints_were_successfully_purchased), Toast.LENGTH_SHORT).show()
                    }

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
            }
        }
    }


    private fun langChooseTreatmentResult(language: LanguagesEnum) {
        viewModel.langChoose(language) { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {
                    downloadUpdateLangTreatmentResult()

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            langChooseTreatmentResult(language)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            langChooseTreatmentResult(language)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            langChooseTreatmentResult(language)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            langChooseTreatmentResult(language)
                        }) {

                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            langChooseTreatmentResult(language)
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
            }
        }
    }
    private fun downloadUpdateLangTreatmentResult() {
        viewModel.downloadUpdateLang { downLoadState ->
            Log.d("pgrlepgkperkgperkgperkgpkewpg",downLoadState.toString())
            when (downLoadState) {

                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        val action = ThemesFragmentDirections.actionThemesFragmentToCoursesFragment(false)
                        binding?.root?.let {
                            Navigation.findNavController(it).navigate(action)
                        }
                    }

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            downloadUpdateLangTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadUpdateLangTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadUpdateLangTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            downloadUpdateLangTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadUpdateLangTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.OFFLINEMODE -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
            }
        }
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
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
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
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    checkLimitActualTreatmentResult(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
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


    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state_key_theme", viewModel.currentState)
    }

    private fun init() {
        binding?.apply {
            val toggle = ActionBarDrawerToggle(
                requireActivity(),
                drawerLayout,
                included.toolbar,
                R.string.welcome,
                R.string.reminder_schedule
            )
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            tvGradient = navView.getHeaderView(0).findViewById(R.id.tvGradientnavu)
            andropointIcon = navView.getHeaderView(0).findViewById(R.id.imageViewAndropointNAv)
            navView.setNavigationItemSelectedListener(this@ThemesFragment)
            textAddAndropoint = navView.getHeaderView(0).findViewById(R.id.textViewAddAndropoints2)
            btnPremiumDrawer = navView.getHeaderView(0).findViewById(R.id.btnPremiumDrawer)
            countAndropoints = navView.getHeaderView(0).findViewById(R.id.tvCountAndropoints)
            userNameHeader = navView.getHeaderView(0).findViewById(R.id.userNameHeader)
            parentLang = navView.getHeaderView(0).findViewById(R.id.parentLayoutTheme)
            listLang = navView.getHeaderView(0).findViewById(R.id.listLayoutTheme)
            imageArrowLangDown = navView.getHeaderView(0).findViewById(R.id.arrowImageViewDownTheme)
            imageArrowLangUp = navView.getHeaderView(0).findViewById(R.id.arrowImageViewThemeUp)
            russianTextLang = navView.getHeaderView(0).findViewById(R.id.russianTextViewTheme)
            englishTextLang = navView.getHeaderView(0).findViewById(R.id.englishTextViewTheme)
        }
    }

    private fun startTimerViewPremFun(){
        timer = object : CountDownTimer(2 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) { millisUntilFinished / 1000        }
            override fun onFinish() {
//                binding?.btnInVictorine?.isClickable = true
                premiumVisible = false
            }
        }
        timer?.start()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_favourite -> {
                if (isThemesFavorite) {
                    viewModel.putCourseNumberLocal(args.courseNumber) { adapter?.diffList?.submitList(it) }
                    isThemesFavorite = false
                } else {
                    viewModel.getFavoritesThemes {
                        if (it.isEmpty()) {
                            Toast.makeText(requireContext(), getString(R.string.you_not_have_favorite_theme), Toast.LENGTH_SHORT).show()
                        } else {
                            adapter?.diffList?.submitList(it)
                            isThemesFavorite = true
                        }
                    }
                }
            }

            R.id.id_delete_my_account->{
                 binding?.dimViewTheme?.visibility = View.VISIBLE
                ShowDialogHelper.showDialogDeleteDataAcc(requireContext(),{
                    ShowDialogHelper.showDialogLoadData(requireContext())
                    if(currentUser!=null){
                        Firebase.auth.signOut()
                    }

                    viewModel.deleteMyAccount({
                        when(it){
                            ErrorEnum.NOTNETWORK -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.ERROR -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.SUCCESS -> {
                                ShowDialogHelper.closeDialogLoadData()
                                val action = ThemesFragmentDirections.actionThemesFragmentToSignInFragment()
                                binding?.root?.let {
                                    Navigation.findNavController(it).navigate(action)
                                }
                            }
                            ErrorEnum.UNKNOWNERROR -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.TIMEOUTERROR -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.NULLPOINTERROR -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.OFFLINEMODE -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                            ErrorEnum.OFFLINETHEMEBUY -> {
                                ShowDialogHelper.closeDialogLoadData()
                            }
                        }
                    })
                },{
                    binding?.dimViewTheme?.visibility = View.GONE
                })

            }

            R.id.id_reminder_schedule -> {
                Toast.makeText(requireContext(), getString(R.string.in_developing), Toast.LENGTH_SHORT).show()
            }

            R.id.id_settings -> {
                startActivity(Intent(requireContext(), SettingActivity::class.java))
            }

            R.id.id_about_Us -> {
                binding?.dimViewTheme?.visibility = View.VISIBLE
                ShowDialogHelper.supportDialog(requireContext(), clickTelegram = {
                    openUrl("https://t.me/andropedia_official")
                }, clickTikTok = {
                    openUrl("https://www.tiktok.com/@andropedia.app?_t=8hP78QdSnpO&_r=1")
                }, clickYoutube = {
                    openUrl("https://www.youtube.com/channel/UCpMJV4D4qO4CN6udvYO9Bvw")
                }, dialogClose = {
                    binding?.dimViewTheme?.visibility = View.GONE
                })
            }
            R.id.id_exit_account ->{

                ShowDialogHelper.showDialogLoadData(requireContext())
                if(currentUser!=null){
                    Firebase.auth.signOut()
                }
                viewModel.exitCurrentAccount({ state->
                    Log.d("fuhruigvhewiurhb444viuewrh",state.toString())
                    when(state){
                        ErrorEnum.NOTNETWORK -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.ERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.SUCCESS -> {
                            ShowDialogHelper.closeDialogLoadData()
                            val action = ThemesFragmentDirections.actionThemesFragmentToSignInFragment()
                            binding?.root?.let {
                                Navigation.findNavController(it).navigate(action)
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.OFFLINEMODE -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                        ErrorEnum.OFFLINETHEMEBUY -> {
                            ShowDialogHelper.closeDialogLoadData()
                        }
                    }
                })
            }

            R.id.id_way_ad->{
                viewModel.getAdProvider({ currentProviderAds->
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogChooseWay(requireContext(),{
                        binding?.dimViewTheme?.visibility = View.GONE
                        val adsProviderEntity = AdsProviderEntity(
                            id = currentProviderAds.id,
                            selectedGoogle = true,
                            selectedLMyTarger = false
                        )
                        viewModel.selectAdsProvider(adsProviderEntity)
                    },{
                        binding?.dimViewTheme?.visibility = View.GONE
                        val adsProviderEntity = AdsProviderEntity(
                            id = currentProviderAds.id,
                            selectedGoogle = false,
                            selectedLMyTarger = true
                        )
                        viewModel.selectAdsProvider(adsProviderEntity)
                    }, choiceAd = true, currentGoogleSelect = currentProviderAds.selectedGoogle)
                })
            }

            R.id.id_heart_buy ->{
                viewModel.getMyHearts{heartsCounts->
                    ShowDialogHelper.showDialogBuyAndropoints(
                        requireContext(),{
                            viewModel.getMyHearts{
                                if(99>it){
                                    checkLimitActualTreatmentResult(false)
                                } else{
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(requireContext(),R.string.heart_limit,Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                        },{

                        },{

                        },{
                             if(it=="1"){
                                 var buyForAndropointStates:BuyForAndropointStates?=null
                                 var heartBuyVar = false
                                 viewModel.minusAndropoint({state->
                                     when(state){
                                         ErrorEnum.NOTNETWORK -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(
                                                     requireContext(),
                                                     R.string.not_connected_internet,
                                                     Toast.LENGTH_SHORT
                                                 ).show()
                                             }
                                         }
                                         ErrorEnum.ERROR -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                         ErrorEnum.SUCCESS -> {
                                             when(buyForAndropointStates){
                                                 BuyForAndropointStates.YESMONEY -> {
                                                     countAndropoints?.text = countAndropoints?.text?.toString()?.toInt()?.minus(3)?.toString()
                                                     viewModel.buyHeart(1,{stateHEart->
                                                       when(stateHEart){
                                                           ErrorEnum.NOTNETWORK -> {
                                                             requireActivity().runOnUiThread {
                                                                 Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                             }
                                                           }
                                                           ErrorEnum.ERROR -> {
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                           ErrorEnum.SUCCESS -> {
                                                            if(!heartBuyVar){
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.heart_limit,Toast.LENGTH_SHORT).show()
                                                               }
                                                            }
                                                           }
                                                           ErrorEnum.UNKNOWNERROR -> {
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                           ErrorEnum.TIMEOUTERROR ->{
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                           ErrorEnum.NULLPOINTERROR -> {
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                           ErrorEnum.OFFLINEMODE -> {
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                           ErrorEnum.OFFLINETHEMEBUY -> {
                                                               requireActivity().runOnUiThread {
                                                                   Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                               }
                                                           }
                                                       }
                                                     },{heartBuy->
                                                         heartBuyVar = heartBuy
                                                     })
                                                 }
                                                 BuyForAndropointStates.NOMONEY -> {
                                                     requireActivity().runOnUiThread {
                                                         Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                     }
                                                 }
                                                 null ->{
                                                     requireActivity().runOnUiThread {
                                                         Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                     }
                                                 }
                                             }
                                         }
                                         ErrorEnum.UNKNOWNERROR -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                         ErrorEnum.TIMEOUTERROR ->{
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                         ErrorEnum.NULLPOINTERROR -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                         ErrorEnum.OFFLINEMODE -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                         ErrorEnum.OFFLINETHEMEBUY -> {
                                             requireActivity().runOnUiThread {
                                                 Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                             }
                                         }
                                     }
                                 },{
                                     buyForAndropointStates = it
                                 },3)
                             }
                            if(it=="10"){
                                var buyForAndropointStates:BuyForAndropointStates?=null
                                var heartBuyVar = false
                                viewModel.minusAndropoint({state->
                                    when(state){
                                        ErrorEnum.NOTNETWORK -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(
                                                    requireContext(),
                                                    R.string.not_connected_internet,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        ErrorEnum.ERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.SUCCESS -> {
                                            when(buyForAndropointStates){
                                                BuyForAndropointStates.YESMONEY -> {
                                                    countAndropoints?.text = countAndropoints?.text?.toString()?.toInt()?.minus(25)?.toString()
                                                    viewModel.buyHeart(10,{stateHEart->
                                                        when(stateHEart){
                                                            ErrorEnum.NOTNETWORK -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.ERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.SUCCESS -> {
                                                                if(!heartBuyVar){
                                                                    requireActivity().runOnUiThread {
                                                                        Toast.makeText(requireContext(),R.string.heart_limit,Toast.LENGTH_SHORT).show()
                                                                    }
                                                                }
                                                            }
                                                            ErrorEnum.UNKNOWNERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.TIMEOUTERROR ->{
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.NULLPOINTERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.OFFLINEMODE -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.OFFLINETHEMEBUY -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        }
                                                    },{heartBuy->
                                                        heartBuyVar = heartBuy
                                                    })
                                                }
                                                BuyForAndropointStates.NOMONEY -> {
                                                    requireActivity().runOnUiThread {
                                                        Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                null ->{
                                                    requireActivity().runOnUiThread {
                                                        Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                        ErrorEnum.UNKNOWNERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.TIMEOUTERROR ->{
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.NULLPOINTERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.OFFLINEMODE -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.OFFLINETHEMEBUY -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                },{
                                    buyForAndropointStates = it
                                },25)
                            }
                            if(it=="50"){
                                var buyForAndropointStates:BuyForAndropointStates?=null
                                var heartBuyVar = false
                                viewModel.minusAndropoint({state->
                                    when(state){
                                        ErrorEnum.NOTNETWORK -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(
                                                    requireContext(),
                                                    R.string.not_connected_internet,
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        }
                                        ErrorEnum.ERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.SUCCESS -> {
                                            when(buyForAndropointStates){
                                                BuyForAndropointStates.YESMONEY -> {
                                                    countAndropoints?.text = countAndropoints?.text?.toString()?.toInt()?.minus(120)?.toString()
                                                    viewModel.buyHeart(50,{stateHEart->
                                                        when(stateHEart){
                                                            ErrorEnum.NOTNETWORK -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.ERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.SUCCESS -> {
                                                                if(!heartBuyVar){
                                                                    requireActivity().runOnUiThread {
                                                                        Toast.makeText(requireContext(),R.string.heart_limit,Toast.LENGTH_SHORT).show()
                                                                    }
                                                                }
                                                            }
                                                            ErrorEnum.UNKNOWNERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.TIMEOUTERROR ->{
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.NULLPOINTERROR -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.OFFLINEMODE -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                            ErrorEnum.OFFLINETHEMEBUY -> {
                                                                requireActivity().runOnUiThread {
                                                                    Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                        }
                                                    },{heartBuy->
                                                        heartBuyVar = heartBuy
                                                    })
                                                }
                                                BuyForAndropointStates.NOMONEY -> {
                                                    requireActivity().runOnUiThread {
                                                        Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                                null ->{
                                                    requireActivity().runOnUiThread {
                                                        Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                    }
                                                }
                                            }
                                        }
                                        ErrorEnum.UNKNOWNERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.TIMEOUTERROR ->{
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.NULLPOINTERROR -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.unknown_error,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.OFFLINEMODE -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                        ErrorEnum.OFFLINETHEMEBUY -> {
                                            requireActivity().runOnUiThread {
                                                Toast.makeText(requireContext(),R.string.not_connected_internet,Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                },{
                                    buyForAndropointStates = it
                                },120)
                            }
                        },
                        isAndropointsBuy = false,
                        heartCount = heartsCounts
                    )
                }

            }
            R.id.id_way_pay->{
                binding?.dimViewTheme?.visibility = View.VISIBLE
                viewModel.getBillingProvider {
                    if(it.selectedGoogleBilling){
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogChooseWay(requireContext(),{},{},{
                                val billingProviderEntity = BillingProviderEntity(
                                    id = it.id,
                                    selectedRuService = false,
                                    selectedGoogleBilling = true
                                )
                                viewModel.selectBillingProvider(billingProviderEntity)
                                binding?.dimViewTheme?.visibility = View.GONE
                            },{
                                val billingProviderEntity = BillingProviderEntity(
                                    id = it.id,
                                    selectedRuService = true,
                                    selectedGoogleBilling = false
                                )
                                viewModel.selectBillingProvider(billingProviderEntity)
                                binding?.dimViewTheme?.visibility = View.GONE
                            }, choiceAd = false,currentGoogleSelect = true)
                        }
                    }else{
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.showDialogChooseWay(requireContext(),{},{},{
                                val billingProviderEntity = BillingProviderEntity(
                                    id = it.id,
                                    selectedRuService = false,
                                    selectedGoogleBilling = true
                                )
                                viewModel.selectBillingProvider(billingProviderEntity)
                                binding?.dimViewTheme?.visibility = View.GONE
                            },{
                                val billingProviderEntity = BillingProviderEntity(
                                    id = it.id,
                                    selectedRuService = true,
                                    selectedGoogleBilling = false
                                )
                                viewModel.selectBillingProvider(billingProviderEntity)
                                binding?.dimViewTheme?.visibility = View.GONE
                            }, choiceAd = false)
                        }
                    }
                }

            }
        }
        return true
    }



    private fun openUrl(link: String) {
        val uri = Uri.parse(link)
        val intent = Intent(Intent.ACTION_VIEW,uri)
        requireActivity().startActivity(intent)
    }


    private fun statrTokenize(amount:Double,productName:String,productSubtitle:String,phoneNumber:String){
        val paymentMethodTypes = setOf(
            PaymentMethodType.BANK_CARD,
            PaymentMethodType.SBERBANK,
            PaymentMethodType.YOO_MONEY,
            PaymentMethodType.SBP,
        )
        val paymentParameters = PaymentParameters(
            amount = Amount(BigDecimal.valueOf(amount), Currency.getInstance("RUB")),
            title = productName,
            subtitle = productSubtitle,
            clientApplicationKey = "live_MjY5MTkyOoec5X4t8WgTcO3B673-WBny5wuo_DfZR0k",
            shopId ="269192",
            savePaymentMethod = SavePaymentMethod.OFF,
            paymentMethodTypes = paymentMethodTypes,
            userPhoneNumber = phoneNumber,
            authCenterClientId = "143526464560"
        )

        val intent = createTokenizeIntent(requireContext(), paymentParameters, TestParameters(showLogs = true))
        startActivityForResult(intent, REQUEST_CODE_TOKENIZE)
    }

    private fun onBuyStart(payState: PayState,phoneNumber: String) {
        when(payState){
            PayState.COURSEBUYADVANCED -> statrTokenize(180.0,getString(R.string.course_nuy_advanced),getString(R.string.course_nuy_advanced_subtitle),phoneNumber)
            PayState.COURSEBUYINDEPTH -> statrTokenize(540.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)

            PayState.THEMEBUYCALCUL -> TODO()
            PayState.THEMEBUYNEWSLIST -> TODO()
            PayState.THEMEBUYNOTES -> TODO()
            PayState.ONEANDROPOINTBUY -> statrTokenize(10.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.TENANDROPOINTBUY ->  statrTokenize(80.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.FIFTYANDROPOINTBUY -> statrTokenize(100.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.HUNDREDANDROPOINTBUY -> statrTokenize(810.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.FIVEHUNDREDANDROPOINTBUY -> statrTokenize(810.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.THOUSANDANDROPOINTBUY -> statrTokenize(5000.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.PREMIUMONEMOUNTHBUY ->statrTokenize(180.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.PREMIUMSIXMOUNTHBUY -> statrTokenize(990.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
            PayState.PREMIUMYEARBUY -> statrTokenize(1712.0,getString(R.string.course_nuy_indepth),getString(R.string.course_nuy_indepth_subtitle),phoneNumber)
        }

    }

    private fun showError() {
        Toast.makeText(requireContext(), R.string.tokenization_canceled, Toast.LENGTH_SHORT).show()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_TOKENIZE) {
            when (resultCode) {
                Activity.RESULT_OK -> showToken(data)
                Activity.RESULT_CANCELED -> showError()
            }
        }
    }


    private fun showToken(data: Intent?) {
        if (data != null) {
            val token = createTokenizationResult(data).paymentToken
//            Toast.makeText(
//                this,
//                String.format(Locale.getDefault(), getString(R.string.tokenization_success), token),
//                Toast.LENGTH_LONG
//            ).show()
        } else {
            showError()
        }
    }

    private fun checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId: Int, ) {
        var resultCheckTermTheme: ErrorEnum? = null
        var isTerm = false
        viewModel.checkTermTheme(uniqueThemeId, { resultCheckTermTheme = it }, { isTerm = it })
        when (resultCheckTermTheme) {
            ErrorEnum.SUCCESS -> {
                requireActivity().runOnUiThread {
                    val action = ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(uniqueThemeId, args.courseName, isTerm, args.courseName)
                    binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                }

            }

            ErrorEnum.NOTNETWORK -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }

            ErrorEnum.ERROR -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }

            ErrorEnum.NULLPOINTERROR -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }

            ErrorEnum.TIMEOUTERROR -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }

            ErrorEnum.UNKNOWNERROR -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }

            else -> {
                requireActivity().runOnUiThread {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                        checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                    }) {
                        binding?.dimViewTheme?.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun addHeartAds(){

        viewModel.buyHeart(1,{
            when(it){
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            addHeartAds()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.SUCCESS -> {
                    startTimerViewAdsFun()
                    startTimerViewAds = true
                   Toast.makeText(requireContext(),R.string.success_award_credited,Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
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

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) { return }
        MobileAds.initialize(requireContext()) {

        }
        loadRewardedAd()
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
                    addAndropoints()
                }else{
                    addHeartAds()
                }
                Log.d("TAG", "User earned the reward.")
            }
            adsViewTreatmentResult()
        }
    }

    private fun adsViewTreatmentResult() {
        viewModel.adsView { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {}
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    binding?.dimViewTheme?.visibility = View.VISIBLE
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            adsViewTreatmentResult()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        })
                    }
                }
            }
        }
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

    private fun addAndropoints() {
        viewModel.addAndropointAds { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {
                    val currentAndropointCount = countAndropoints?.text?.toString()?.toInt()
                    val plusAddAndropoint = currentAndropointCount?.plus(1)

                    startTimerViewAdsFun()
                    startTimerViewAds = true
                    requireActivity().runOnUiThread {
                        countAndropoints?.text = plusAddAndropoint?.toString()
                        Toast.makeText(requireContext(), getString(R.string.success_award_credited), Toast.LENGTH_SHORT).show()
                    }

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            addAndropoints()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            addAndropoints()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            addAndropoints()
                        }) {      binding?.dimViewTheme?.visibility = View.GONE }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            addAndropoints()
                        }) {
                            binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            addAndropoints()
                        }) { binding?.dimViewTheme?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        }) }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewTheme?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewTheme?.visibility = View.GONE
                        }) }
                }
            }
        }
    }

    private fun initAd(andropointAds: Boolean) {

        // Устанавливаем слушатель событий
        adTarget?.setListener(object : com.my.target.ads.RewardedAd.RewardedAdListener {

            override fun onLoad(p0: com.my.target.ads.RewardedAd) {
                adTarget?.show()
                viewModel.adsView { state->
                    when(state){
                        ErrorEnum.NOTNETWORK -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.ERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.SUCCESS -> Log.d("forkfoktogktg","SUCCESMAMBET")
                        ErrorEnum.UNKNOWNERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.TIMEOUTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.NULLPOINTERROR -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                    initAd(andropointAds)
                                }) {
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }
                            }
                        }
                        ErrorEnum.OFFLINEMODE -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogOffline(requireContext(),{

                                },{
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }) }
                        }
                        ErrorEnum.OFFLINETHEMEBUY -> {
                            requireActivity().runOnUiThread {
                                binding?.dimViewTheme?.visibility = View.VISIBLE
                                ShowDialogHelper.showDialogOffline(requireContext(),{

                                },{
                                    binding?.dimViewTheme?.visibility = View.GONE
                                }) }
                        }
                    }
                }
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
                if(andropointAds) {
                    addAndropoints()
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

    private fun loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true
            val adRequest = AdRequest.Builder().build()
            RewardedAd.load(requireContext(), AD_UNIT_ID, adRequest, object : RewardedAdLoadCallback() {
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

}