package workwork.test.andropediagits.presenter.themes

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager


import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns.AD_UNIT_ID
import workwork.test.andropediagits.core.utils.GoogleAdManager
import workwork.test.andropediagits.databinding.FragmentThemesBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.domain.useCases.userLogic.state.AddAndropoints
import workwork.test.andropediagits.domain.useCases.userLogic.state.LanguagesEnum
import workwork.test.andropediagits.presenter.bottomSheet.BottomSheet
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.setting.SettingActivity
import workwork.test.andropediagits.presenter.themes.viewModel.ThemeViewModel
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class ThemesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var billingManager: BillingManager? = null
    private var binding: FragmentThemesBinding? = null
    private var googleMobileAdsConsentManager: GoogleAdManager? = null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val args: ThemesFragmentArgs by navArgs()
    private var adapter: ThemesAdapter? = null
    private val viewModel: ThemeViewModel by viewModels()
    private var btnPremiumDrawer: LinearLayout? = null
    private var isLoading = false
    private var userNameHeader: TextView? = null
    private var countAndropoints: TextView? = null
    private var textAddAndropoint: TextView? = null
    private var parentLang: LinearLayout? = null
    private var listLang: LinearLayout? = null
    private var russianTextLang: TextView? = null
    private var englishTextLang: TextView? = null
    private var imageArrowLangDown: ImageView? = null
    private var imageArrowLangUp: ImageView? = null
    private var timer: CountDownTimer? = null
    private var startTimerViewAds = false
    private var isThemesFavorite = false
    private val russianText = "🇷🇺    Рус"
    private val englishText = "🇺🇸    Eng"



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
        if (args.premiumVisible) {
            val bs = BottomSheet()
            val data = Bundle()
            bs.arguments = data
            bs.show(requireActivity().supportFragmentManager, "Tag")
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }

        viewModel.getCurrentLang { currentLang ->
            if (currentLang == "rus") {
                russianTextLang?.text = russianText
                englishTextLang?.text = englishText
            } else if (currentLang == "eng") {
                russianTextLang?.text = englishText
                englishTextLang?.text = russianText
            }
        }
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.light_black))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
        } else {
            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        }

        parentLang?.setOnClickListener {
            chooseLangParent(currentTheme)
        }


        listLang?.setOnClickListener {
            chooseLangParentList()
        }

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
                1 -> buyAndropointsTreatmentResult(AddAndropoints.BUYONEANDROPOINT)
                10 -> buyAndropointsTreatmentResult(AddAndropoints.BUYTENANDROPOINT)
                100 ->buyAndropointsTreatmentResult(AddAndropoints.BUYHUNDREDANDROPOINT)
            }
        }

        billingManager?.infinityAndropoints = {
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
            Log.d("themeTffrfrgt", "removeId:${uniqueThemeId}")
            viewModel.removeFavorite(uniqueThemeId)
        }

        adapter?.addFavorite = { uniqueThemeId ->
            Log.d("themeTffrfrgt", "addId:${uniqueThemeId}")
            viewModel.addFavorite(uniqueThemeId)
        }

        adapter?.checkThisThemeTerm = { uniqueId ->
            adapter?.currentThemeName = { themeName ->
                adapter?.currentThemePassed = { themePassed ->
                    checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId,themeName,themePassed)
                }
            }
        }

        binding?.included?.apply {
            tvNameCourse.text = args.courseName


            rcViewTheme.layoutManager = LinearLayoutManager(requireContext())
            rcViewTheme.adapter = adapter
            viewModel.putCourseNumberLocal(args.courseNumber) {
                val countThemePassed = it.filter { it.isThemePassed }
                val progress = (countThemePassed.size.toFloat() / it.size.toFloat() * 100).toInt()
                binding?.included?.tvNumberOfCompletedLevels?.text =
                    "${countThemePassed.size}/${it.size}"
                binding?.included?.pbTheme?.progress = progress
                adapter?.diffList?.submitList(it)
            }
        }

        textAddAndropoint?.setOnClickListener {
            ShowDialogHelper.showDialogBuyAndropointsImplementation(requireContext(),billingManager) { checkLimitActualTreatmentResult() }
        }
        btnPremiumDrawer?.setOnClickListener {
            val bs = BottomSheet()
            val data = Bundle()
            bs.arguments = data
            bs.show(requireActivity().supportFragmentManager, "Tag")
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }
        viewModel.getDataUser { userInfo ->
            val andropointCount = userInfo.andropointCount.toString()

            countAndropoints?.text = andropointCount 
            userNameHeader?.text = userInfo.name ?: "defaultName"
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {

                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { checkTermThemeListLessonsFragmentMoreOptionsTreatmentResult(uniqueId, themeName, themePassed) }
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyThemeForMoneyTreatmentResult(uniqueIdThemeBuy)
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            buyAndropointsTreatmentResult(quantityAndropoint)
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buyAndropointsTreatmentResult(quantityAndropoint)
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            langChooseTreatmentResult(language)
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            langChooseTreatmentResult(language)
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            langChooseTreatmentResult(language)
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            langChooseTreatmentResult(language)
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            langChooseTreatmentResult(language)
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            downloadUpdateLangTreatmentResult()
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            downloadUpdateLangTreatmentResult()
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            downloadUpdateLangTreatmentResult()
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            downloadUpdateLangTreatmentResult()
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            downloadUpdateLangTreatmentResult()
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


    private fun checkLimitActualTreatmentResult() {
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
                            showRewardedVideo() }
                    } else {
                        requireActivity().runOnUiThread {
                            Toast.makeText(requireContext(), getString(R.string.advertising_limit_has_been_reached), Toast.LENGTH_SHORT).show()
                        }

                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            checkLimitActualTreatmentResult()
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkLimitActualTreatmentResult()
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkLimitActualTreatmentResult()
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            checkLimitActualTreatmentResult()
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkLimitActualTreatmentResult()
                        }
                    }
                }
                else -> {}
            }
        }, {
            isActualNotTerm = it
        })
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

            R.id.id_reminder_schedule -> {
                Toast.makeText(requireContext(), getString(R.string.in_developing), Toast.LENGTH_SHORT).show()
            }

            R.id.id_settings -> {
                startActivity(Intent(requireContext(), SettingActivity::class.java))
            }

            R.id.id_about_Us -> {}
        }
        return true
    }



    private fun checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId: Int, ) {
        var resultCheckTermTheme: ErrorEnum? = null
        var isTerm = false
        viewModel.checkTermTheme(uniqueThemeId, { resultCheckTermTheme = it }, { isTerm = it })
        when (resultCheckTermTheme) {
            ErrorEnum.SUCCESS -> {
                val action = ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(uniqueThemeId, args.courseName, isTerm, args.courseName)
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
            }

            ErrorEnum.NOTNETWORK -> {
                ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.ERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.NULLPOINTERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.TIMEOUTERROR -> {
                ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.UNKNOWNERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }

            else -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    checkTermThemeListLessonsFragmentTreatmentResult(uniqueThemeId)
                }
            }
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.getAndSet(true)) { return }
        MobileAds.initialize(requireContext()) {}
        loadRewardedAd()
    }

    private fun showRewardedVideo() {
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
                addAndropoints()
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) { adsViewTreatmentResult() }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { adsViewTreatmentResult() }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { adsViewTreatmentResult() }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) { adsViewTreatmentResult() }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { adsViewTreatmentResult() }
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
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) { addAndropoints() }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { addAndropoints() }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { addAndropoints() }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) { addAndropoints() }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) { addAndropoints() }
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
    /* private fun buyThemeTreatmentResult() {
       var resultCourseBuy: ErrorEnum? = null
       var isHaveMoneyResult: BuyForAndropointStates? = null
       viewModel.buyTheme({ resultCourseBuy = it }, { isHaveMoneyResult = it }, 900)
       when (resultCourseBuy) {
           ErrorEnum.SUCCESS -> {
               if (isHaveMoneyResult == BuyForAndropointStates.YESMONEY) {
                   Toast.makeText(
                       requireContext(),
                       getString(R.string.theme_buy_success),
                       Toast.LENGTH_SHORT
                   ).show()
               } else {
                   //показ окна покупки
               }
           }

           ErrorEnum.NOTNETWORK -> {
               ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                   buyThemeTreatmentResult()
               }

           }

           ErrorEnum.TIMEOUTERROR -> {
               ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                   buyThemeTreatmentResult()
               }

           }

           ErrorEnum.ERROR -> {
               ShowDialogHelper.showDialogUnknownError(requireContext()) {
                   buyThemeTreatmentResult()
               }
           }

           ErrorEnum.NULLPOINTERROR -> {
               ShowDialogHelper.showDialogUnknownError(requireContext()) {
                   buyThemeTreatmentResult()
               }
           }

           ErrorEnum.UNKNOWNERROR -> {
               ShowDialogHelper.showDialogUnknownError(requireContext()) {
                   buyThemeTreatmentResult()
               }
           }

           else -> {
               ShowDialogHelper.showDialogUnknownError(requireContext()) {
                   buyThemeTreatmentResult()
               }
           }
       }
   }*/
}