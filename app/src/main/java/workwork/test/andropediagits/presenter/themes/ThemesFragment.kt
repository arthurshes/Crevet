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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager



import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.navigation.NavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns.AD_UNIT_ID
import workwork.test.andropediagits.core.utils.GoogleAdManager
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
import java.util.concurrent.atomic.AtomicBoolean


@AndroidEntryPoint
class ThemesFragment : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    private var billingManager: BillingManager?=null
    private var binding: FragmentThemesBinding? = null
    private var googleMobileAdsConsentManager: GoogleAdManager?=null
    private var rewardedAd: RewardedAd? = null
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)
    private val args: ThemesFragmentArgs by navArgs()
    private var adapter:ThemesAdapter?=null
    private val viewModel: ThemeViewModel by viewModels()
    private  var btnPremiumDrawer: LinearLayout?=null
    private var isLoading = false
    private  var userNameHeader:TextView?=null
    private  var countAndropoints: TextView?=null
    private  var textAddAndropoint:TextView?=null
    private var parentLang:LinearLayout?=null
    private var listLang:LinearLayout?=null
    private var russianTextLang:TextView?=null
    private var englishTextLang:TextView?=null
    private var imageArrowLangDown:ImageView?=null
    private var imageArrowLangUp:ImageView?=null
    private var isNeedTryAgain = false
    private var timer: CountDownTimer?=null
    private var startTimerViewAds = false
    private var isThemesFavorite = false
    private var chooseLang:String?=null



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_menu, menu)
        val moreMenu = menu.findItem(R.id.action_more)
        moreMenu.isVisible = googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true
        super.onCreateOptionsMenu(menu, inflater)
    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        requireActivity().menuInflater.inflate(R.menu.action_menu, menu)
//        val moreMenu = menu?.findItem(R.id.action_more)
//        moreMenu?.isVisible = googleMobileAdsConsentManager?.isPrivacyOptionsRequired == true
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("thisScreen","ThemesFrag")
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
        var russianText = "🇷🇺    Рус"
        var englishText = "🇺🇸    Eng"

        if(args.premiumVisible){
            val bs = BottomSheet()
            val data = Bundle()
            bs.arguments = data
            bs.show(requireActivity().supportFragmentManager, "Tag")
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }

        viewModel.getCurrentLang{currentLang->
            if(currentLang=="rus"){
                russianTextLang?.text = russianText
                englishTextLang?.text = englishText
            } else if(currentLang == "eng"){
                russianTextLang?.text = englishText
                englishTextLang?.text = russianText
            }
        }
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_black))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
        }else{
            parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
            russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        }

        parentLang?.setOnClickListener {
            if (listLang?.visibility == View.VISIBLE) {
                imageArrowLangDown?.visibility = View.VISIBLE
                imageArrowLangUp?.visibility = View.GONE
                listLang?.visibility = View.GONE
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_black))
                    russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }else{
                    parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                    russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                }
            } else {
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_black))
                    listLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.light_black))
                    englishTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                }else{
                    parentLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                    listLang?.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.white))
                    englishTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                    russianTextLang?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
                }
                listLang?.visibility = View.VISIBLE
                imageArrowLangDown?.visibility = View.GONE
                imageArrowLangUp?.visibility = View.VISIBLE
            }
        }


        listLang?.setOnClickListener {
            if(russianTextLang?.text==russianText){
//                chooseLang = "eng"
                ShowDialogHelper.showDialogLoadData(requireContext())
                viewModel.langChoose(LanguagesEnum.ENGLISH,{ state->
                    when(state){
                        ErrorEnum.NOTNETWORK -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.ERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.SUCCESS -> {
                            viewModel.downloadUpdateLang {downLoadState->
                                when(downLoadState){
                                    ErrorEnum.NOTNETWORK -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.ERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.SUCCESS -> {
                                        ShowDialogHelper.closeDialogLoadData()
                                        val action = ThemesFragmentDirections.actionThemesFragmentToCoursesFragment(false)
                                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                    }
                                    ErrorEnum.UNKNOWNERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.TIMEOUTERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.NULLPOINTERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.OFFLINEMODE -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.OFFLINETHEMEBUY -> Log.d("555teccareandirfirfb","error")
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.TIMEOUTERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.NULLPOINTERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.OFFLINEMODE -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.OFFLINETHEMEBUY -> Log.d("555teccareandirfirfb","error")
                    }
                })
//                russianTextLang?.text = englishText
//                englishTextLang?.text = russianText
            } else if(russianTextLang?.text==englishText){
//                chooseLang = "rus"
                ShowDialogHelper.showDialogLoadData(requireContext())
                viewModel.langChoose(LanguagesEnum.RUSSIAN,{ state->
                    when(state){
                        ErrorEnum.NOTNETWORK -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.ERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.SUCCESS -> {
                            viewModel.downloadUpdateLang {downLoadState->
                                when(downLoadState){
                                    ErrorEnum.NOTNETWORK -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.ERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.SUCCESS -> {

                                        requireActivity().runOnUiThread {
                                            ShowDialogHelper.closeDialogLoadData()
                                            val action = ThemesFragmentDirections.actionThemesFragmentToCoursesFragment(false)
                                            binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                                        }

                                    }
                                    ErrorEnum.UNKNOWNERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.TIMEOUTERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.NULLPOINTERROR -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.OFFLINEMODE -> Log.d("555teccareandirfirfb","error")
                                    ErrorEnum.OFFLINETHEMEBUY -> Log.d("555teccareandirfirfb","error")
                                }
                            }
                        }
                        ErrorEnum.UNKNOWNERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.TIMEOUTERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.NULLPOINTERROR -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.OFFLINEMODE -> Log.d("555teccareandirfirfb","error")
                        ErrorEnum.OFFLINETHEMEBUY -> Log.d("555teccareandirfirfb","error")
                    }
                })
//                russianTextLang?.text = russianText
//                englishTextLang?.text = englishText
            }
        }




        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)



        if (savedInstanceState != null) {
            viewModel.currentState = savedInstanceState.getString("state_key_theme", "")
        }

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

        billingManager?.addAndropointsCount = {

             if(it==1){
                 viewModel.buyAndropoints({

                 }, AddAndropoints.BUYONEANDROPOINT)
             }
            if(it==10){
                viewModel.buyAndropoints({

                }, AddAndropoints.BUYTENANDROPOINT)
            }
            if(it==100){
                viewModel.buyAndropoints({

                }, AddAndropoints.BUYHUNDREDANDROPOINT)
            }
        }

        billingManager?.infinityAndropoints = {
            viewModel.buyAndropoints({

            }, AddAndropoints.INFINITYANDROPOINTS)
        }

        billingManager?.theneBuyWithUniqueId = {uniqueIdThemeBuy->
            viewModel.buyThemeForMoney(uniqueIdThemeBuy,{state->
                when(state){
                    ErrorEnum.NOTNETWORK -> TODO()
                    ErrorEnum.ERROR -> TODO()
                    ErrorEnum.SUCCESS -> TODO()
                    ErrorEnum.UNKNOWNERROR -> TODO()
                    ErrorEnum.TIMEOUTERROR -> TODO()
                    ErrorEnum.NULLPOINTERROR -> TODO()
                    ErrorEnum.OFFLINEMODE -> TODO()
                    ErrorEnum.OFFLINETHEMEBUY -> TODO()
                }
            })
        }


        adapter= ThemesAdapter(args)

//        adapter?.checkClickThemeUniqueId = {uniqueThemeID->
//            adapter?.checkClickThemePossible = {possibleTheme->
//                val action = ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(uniqueThemeID, args.courseName)
//                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
//            }
//        }

        adapter?.buyThemeUniqueId = {buyUniqueId->
            adapter?.buyThemePrice = {buuyThemePrice->
                adapter?.buyThemePossible = {possibleTheme->
                    adapter?.buyThemeAndropointPrice = { andropointPrice->
                        ShowDialogHelper.showDialogClose(requireContext(),{
                            ShowDialogHelper.showDialogBuy(requireContext(),buuyThemePrice,andropointPrice,{
                                if(buuyThemePrice==50){
                                    billingManager?.billingSetup(PayState.THEMEBUYCALCUL, uniqueThemeID = buyUniqueId)
                                }
                                if(buuyThemePrice==120){
                                    billingManager?.billingSetup(PayState.THEMEBUYNOTES, uniqueThemeID = buyUniqueId)
                                }
                                if(buuyThemePrice==200){
                                    billingManager?.billingSetup(PayState.THEMEBUYNEWSLIST, uniqueThemeID = buyUniqueId)
                                }
                            },{
                                ///fjigjitg
//                               viewModel.buyAndropoints()
                            })
                        },true)
                    }
                }

            }
        }

        adapter?.themeCloseUniqueThemeId = {uniqueThemeID->
            adapter?.themeClosePossible = {possibleTheme->
                themeClose(uniqueThemeID,possibleTheme)
            }
        }

        adapter?.checkTermThemeUniqueThemeId = { uniqueThemeID->
            checkTermTheme(uniqueThemeID)
        }

        adapter?.removeFavorite = { uniqueThemeId->
            if(isThemesFavorite){
                    viewModel.putCourseNumberLocal(args.courseNumber){
                        adapter?.diffList?.submitList(it)
                    }
                    isThemesFavorite = false
            }
            Log.d("themeTffrfrgt","removeId:${uniqueThemeId}")
            viewModel.removeFavorite(uniqueThemeId)
        }

        adapter?.addFavorite = { uniqueThemeId->
            Log.d("themeTffrfrgt","addId:${uniqueThemeId}")
            viewModel.addFavorite(uniqueThemeId)
        }

        adapter?.checkThisThemeTerm ={ uniqueId->
            adapter?.currentThemeName = {themeName->
                adapter?.currentThemePassed = {themePassed->
                    var isThemeOpen = false
                    viewModel.checkTermTheme(uniqueId,{errorState->
                        Log.d("nffnrfnrnfrnfnr3434434nfrnf",errorState.toString())
                        when (errorState) {
                            ErrorEnum.SUCCESS -> {
                                if(isThemeOpen){
                                    requireActivity().runOnUiThread {
                                        val action =
                                            ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(
                                                uniqueId,
                                                themeName,
                                                themePassed,
                                                args.courseName
                                            )
                                        binding?.root.let {
                                            if (it != null) {
                                                Navigation.findNavController(it).navigate(action)
                                            }
                                        }
                                    }
                                }else{
                                    requireActivity().runOnUiThread {
                                        Toast.makeText(
                                            requireActivity(),
                                            "Данная тема пока закрыта",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogNotNetworkError(requireContext()){
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }

                            }

                            ErrorEnum.ERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }
                            }

                            ErrorEnum.NULLPOINTERROR -> {

                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }
                            }

                            ErrorEnum.TIMEOUTERROR -> {
                                requireActivity().runOnUiThread {

                                    ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }
                            }

                            ErrorEnum.UNKNOWNERROR -> {

                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }
                            }

                            else -> {

                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                        checkTermThemeTreatmentResult(uniqueId)
                                    }
                                }
                            }
                        }

                    },{ isThemeOpenState->
                        isThemeOpen = isThemeOpenState

                    },{

                    })
                }
            }
        }

        binding?.included?.apply {
            tvNameCourse.text =args.courseName


            rcViewTheme.layoutManager = LinearLayoutManager(requireContext())
            rcViewTheme.adapter = adapter
            viewModel.putCourseNumberLocal(args.courseNumber,{
                val countThemePassed=it.filter { it.isThemePassed }
                val progress = (countThemePassed.size.toFloat() /it.size.toFloat()* 100).toInt()
                binding?.included?.tvNumberOfCompletedLevels?.text = "${countThemePassed.size}/${it.size}"
                binding?.included?.pbTheme?.progress = progress
                adapter?.diffList?.submitList(it)
            })

//            observer = Observer {

//            }
        }

        textAddAndropoint?.setOnClickListener {
            var isActualNotTerm = false
                ShowDialogHelper.showDialogBuyAndropoints(requireContext(),{
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

                },{

                },{
                    Log.d("andropointsIeefffgbbCout","moneyRub:${it}")
                },{
                    Log.d("andropointsIeefffgbbCout","andropointsCount:${it}")
                    if(it==1){
                        billingManager?.billingSetup(PayState.ONEANDROPOINTBUY)
                    }
                    if(it==10){
                        billingManager?.billingSetup(PayState.TENANDROPOINTBUY)
                    }
                    if(it==100){
                        billingManager?.billingSetup(PayState.HUNDREDANDROPOINTBUY)
                    }
                })

        }

        btnPremiumDrawer?.setOnClickListener {
            val bs = BottomSheet()
            val data = Bundle()
            bs.arguments = data
            bs.show(requireActivity().supportFragmentManager, "Tag")
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        }


//////Andropoint changes
        viewModel.getDataUser { userInfo->
            val andropointCount = userInfo.andropointCount.toString()

            countAndropoints?.text= andropointCount ?: "0"
            userNameHeader?.text = userInfo.name ?: "defaultName"
        }





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
              if(isThemesFavorite){
                  viewModel.putCourseNumberLocal(args.courseNumber){
                      adapter?.diffList?.submitList(it)
                  }
                  isThemesFavorite = false
              }else{
                   viewModel.getFavoritesThemes {
                    if (it.isNullOrEmpty()) {

                      Toast.makeText(
                          requireContext(),
                          "У вас нет избранных тем",
                          Toast.LENGTH_SHORT
                      ).show()
                   } else {
                      adapter?.diffList?.submitList(it)
                      isThemesFavorite = true
                   }
                }

              }


//                    if (it.isNullOrEmpty()){
//                        if(isThemesFavorite){
//
//                        }
//                        Toast.makeText(requireContext(),"У вас нет избранных тем",Toast.LENGTH_SHORT).show()
//                    }else{
//                        isThemesFavorite = true
//                        adapter?.diffList?.submitList(it)
//                    }
//
//                }
            }
            R.id.id_reminder_schedule -> {
                Toast.makeText(requireContext(),"В разработке",Toast.LENGTH_SHORT).show()
            }
            R.id.id_settings -> {
//                val action = ThemesFragmentDirections.actionThemesFragmentToSettingActivity()
//                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                startActivity(Intent(requireContext(), SettingActivity::class.java))
            }
            R.id.id_about_Us -> {}
        }
        return true
    }

    ///this
    private fun checkTermTheme(uniqueThemeId: Int) {
        checkTermThemeTreatmentResult(uniqueThemeId)

    }

    private fun checkThemeBuy(uniqueThemeId: Int, possibleToOpenThemeFree: Boolean) {

    }

    private fun themeClose(uniqueThemeId: Int, possibleToOpenThemeFree: Boolean) {

        ShowDialogHelper.showDialogClose(requireContext(),
             themeClose = true)

    }



    private fun buyThemeTreatmentResult() {
        var resultCourseBuy:ErrorEnum?=null
        var isHaveMoneyResult: BuyForAndropointStates?=null
        viewModel.buyTheme({ resultCourseBuy=it },{isHaveMoneyResult=it},900)
        when(resultCourseBuy){
            ErrorEnum.SUCCESS -> {
                if(isHaveMoneyResult== BuyForAndropointStates.YESMONEY){
                    Toast.makeText(requireContext(),getString(R.string.theme_buy_success), Toast.LENGTH_SHORT).show()
                }else{
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
    }

    private fun checkTermThemeTreatmentResult(uniqueThemeId: Int) {
        var resultCheckTermTheme:ErrorEnum?=null
        var isTerm=false
        viewModel.checkTermTheme(uniqueThemeId, { resultCheckTermTheme = it },{isTerm=it})
        when (resultCheckTermTheme) {
            ErrorEnum.SUCCESS -> {
                val action= ThemesFragmentDirections.actionThemesFragmentToListLessonsFragment(uniqueThemeId, args.courseName,isTerm,args.courseName)
                binding?.root?.let { Navigation.findNavController(it).navigate(action) }
            }
            ErrorEnum.NOTNETWORK -> {
                ShowDialogHelper.showDialogNotNetworkError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.ERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.NULLPOINTERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.TIMEOUTERROR -> {
                ShowDialogHelper.showDialogTimeOutError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }

            ErrorEnum.UNKNOWNERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }

            else -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()){
                    checkTermThemeTreatmentResult(uniqueThemeId)
                }
            }
        }
    }

//    override fun onStart() {
//        super.onStart()
//        observer?.let { viewModel.allThemes.observe(this, it) }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        observer?.let { viewModel.allThemes.removeObserver(it) }
//    }
private fun initializeMobileAdsSdk() {
    if (isMobileAdsInitializeCalled.getAndSet(true)) {
        return
    }

    // Initialize the Mobile Ads SDK.
    MobileAds.initialize(requireContext()) {}
    // Load an ad.
    loadRewardedAd()
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
                addAndropoints()
                Log.d("TAG", "User earned the reward.")
            }
        )
           viewModel.adsView { state->
               when(state){
                   ErrorEnum.NOTNETWORK -> Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.ERROR -> Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.SUCCESS -> Log.d("forkfoktogktg","SUCCESMAMBET")
                   ErrorEnum.UNKNOWNERROR -> Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.TIMEOUTERROR -> Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.NULLPOINTERROR ->Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.OFFLINEMODE -> Log.d("forkfoktogktg","ogktogktokgot")
                   ErrorEnum.OFFLINETHEMEBUY -> Log.d("forkfoktogktg","ogktogktokgot")
               }
           }
    }

//        binding.showVideoButton.visibility = View.INVISIBLE

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

    private fun addAndropoints(){

        viewModel.addAndropointAds {state->
            when(state){
                ErrorEnum.NOTNETWORK -> {
                    Toast.makeText(requireContext(),"ошибка нет интернета",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.ERROR -> {
                    Toast.makeText(requireContext(),"Неизвестная ошибка",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.SUCCESS -> {

                    var currentAndropointCount = countAndropoints?.text?.toString()?.toInt()
                    var plusAddAndropoint = currentAndropointCount?.plus(1)
                    countAndropoints?.text = plusAddAndropoint?.toString()
                    startTimerViewAdsFun()
                    startTimerViewAds = true
                    Toast.makeText(requireContext(),"Награда успеша зачислено",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.UNKNOWNERROR -> {
                    Toast.makeText(requireContext(),"Неизвестная ошибка",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.TIMEOUTERROR -> {
                    Toast.makeText(requireContext(),"Таймаут",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.NULLPOINTERROR -> {
                    Toast.makeText(requireContext(),"мепепепепепепепепепеп",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.OFFLINEMODE -> {
                    Toast.makeText(requireContext(),"Оффлайн мод",Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    Toast.makeText(requireContext(),"Награда успеша зачислено",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun loadRewardedAd() {
        if (rewardedAd == null) {
            isLoading = true
            var adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                requireContext(),
                AD_UNIT_ID,
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


}