package workwork.test.andropediagits.presenter.courses

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.ktx.isFlexibleUpdateAllowed
import com.google.android.play.core.ktx.isImmediateUpdateAllowed

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.databinding.FragmentCoursesBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.PromoCodeState
import workwork.test.andropediagits.presenter.courses.viewModel.CoursesViewModel
import workwork.test.andropediagits.presenter.lesson.utils.ErrorHelper
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import kotlin.time.Duration.Companion.seconds


@AndroidEntryPoint
class CoursesFragment (): Fragment(){
    private var  backPressedOnce = false
    private var appUpdateManager: AppUpdateManager?=null
    private val updateType = AppUpdateType.IMMEDIATE
    private var binding: FragmentCoursesBinding? = null
    private val args:CoursesFragmentArgs by navArgs()
    private var adapter : CourseAdapter?=null
    private val viewModel: CoursesViewModel by viewModels()
    private var billingManager: BillingManager?=null
    private var obServer:Observer<List<CourseEntity>>?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        billingManager = BillingManager(requireActivity() as AppCompatActivity)
        appUpdateManager = AppUpdateManagerFactory.create(requireActivity().applicationContext)
        checkForAppUpdates()
        return binding?.root
    }

    private val installStateUpdateListner = InstallStateUpdatedListener {state->
        if(state.installStatus() == InstallStatus.DOWNLOADED){
            Toast.makeText(requireContext(),getString(R.string.loading_is_complete),Toast.LENGTH_LONG).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager?.completeUpdate()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

            }}
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        adapter = CourseAdapter(requireContext())
        if (savedInstanceState != null) {
            viewModel.currentState = savedInstanceState.getString("state_key_course", "")
        }
        if(args.isShowPromoCode) {
            binding?.dimViewCourse?.visibility = View.GONE
            ShowDialogHelper.showDialogChooseWay(requireContext(), {
                val adsProviderEntity = AdsProviderEntity(
                    selectedLMyTarger = false,
                    selectedGoogle = true
                )
                viewModel.selectAdsProvider(adsProviderEntity)
                binding?.dimViewCourse?.visibility = View.GONE
                val dialog = Dialog(requireContext())
                dialog.setContentView(R.layout.promo_code_dialog)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                dialog.setCancelable(false)
                val dialogReady = dialog.findViewById<CardView>(R.id.btnReadyPromo)
                val dialogPromoCode = dialog.findViewById<EditText>(R.id.edEnterPromoCode)
                val dialogError = dialog.findViewById<TextView>(R.id.tvPromoCodeError)
                val btnClose = dialog.findViewById<LinearLayoutCompat>(R.id.btnClose)
                dialogReady.setOnClickListener {
                    promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                }
                btnClose.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()

            }, {
                val adsProviderEntity = AdsProviderEntity(
                    selectedLMyTarger = true,
                    selectedGoogle = false
                )
                viewModel.selectAdsProvider(adsProviderEntity)
                binding?.dimViewCourse?.visibility = View.GONE
                    val dialog = Dialog(requireContext())
                    dialog.setContentView(R.layout.promo_code_dialog)
                    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                    dialog.setCancelable(false)
                    val dialogReady = dialog.findViewById<CardView>(R.id.btnReadyPromo)
                    val dialogPromoCode = dialog.findViewById<EditText>(R.id.edEnterPromoCode)
                    val dialogError = dialog.findViewById<TextView>(R.id.tvPromoCodeError)
                    val btnClose = dialog.findViewById<LinearLayoutCompat>(R.id.btnClose)
                    dialogReady.setOnClickListener {
                        promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                    }
                    btnClose.setOnClickListener {
                        dialog.dismiss()
                    }
                    dialog.show()

            }, choiceAd = true)
        }

        adapter?.andropointPrice = {androPrice->
            adapter?.rubPrice = {rubPrice->
                adapter?.buyCourseNumber = {buyCourseNumber->
                    var isOpenDialogBuy = false
                    binding?.dimViewCourse?.visibility = View.VISIBLE
                    ShowDialogHelper.showDialogClose(requireContext(),{
                        isOpenDialogBuy = true
                        if(rubPrice==600){
                            ShowDialogHelper.showDialogBuy(requireContext(),6,androPrice,{
                                if(rubPrice==150){
                                    billingManager?.billingSetup(PayState.COURSEBUYADVANCED,buyCourseNumber)
                                }
                                if(rubPrice==600){
                                    billingManager?.billingSetup(PayState.COURSEBUYINDEPTH,buyCourseNumber)
                                }
                            },{
                                if(rubPrice==150){
                                    buyCourseAndropointTreatmentResult(buyCourseNumber,300)
                                }
                                if(rubPrice==600){
                                    buyCourseAndropointTreatmentResult(buyCourseNumber,600)
                                }
                            },{
                                binding?.dimViewCourse?.visibility = View.GONE
                            })
                        }
                     if(rubPrice==150){
                         ShowDialogHelper.showDialogBuy(requireContext(),2,androPrice,{
                             if(rubPrice==150){
                                 billingManager?.billingSetup(PayState.COURSEBUYADVANCED,buyCourseNumber)
                             }
                             if(rubPrice==600){
                                 billingManager?.billingSetup(PayState.COURSEBUYINDEPTH,buyCourseNumber)
                             }
                         },{
                             if(rubPrice==150){
                                 buyCourseAndropointTreatmentResult(buyCourseNumber,300)
                             }
                             if(rubPrice==600){
                                 buyCourseAndropointTreatmentResult(buyCourseNumber,600)
                             }
                         },{
                             binding?.dimViewCourse?.visibility = View.GONE
                         })
                     }
                    },{
                        if(!isOpenDialogBuy){
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    })
                }
            }
        }

        adapter?.onClickCourse = {courseNum->
            adapter?.onCourseName = { courseName->
                adapter?.onPossibleBuy = { courBuy->
                    adapter?.isCourseOpen = { courseOpem->
                        Log.d("clickThemeTest", "courseNumber:${courseNum},courseName:${courseName},courseBuy:${courBuy}")
                        checkCoursesThemes(courseNum,courseName)
                    }

                }
            }
        }

        billingManager?.courseBuyWithNumber = {courseNumber->
            buyCourseForMoneyTreatmentResult(courseNumber)

        }

        binding?.rcViewCourses?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rcViewCourses?.adapter = adapter
//        viewModel.allCourses.observe(viewLifecycleOwner) {
//
//            333rtgvfbvgf
//        }
        obServer = Observer {
            adapter?.diffList?.submitList(it)
        }
    }

    private fun buyCourseForMoneyTreatmentResult(courseNumber: Int) {
        viewModel.buyCourseForMoney({state->
            when(state){
                ErrorEnum.SUCCESS -> {
//                    requireActivity().runOnUiThread {
////                        Toast.makeText(requireContext(),getString(R.string.course_was_successfully_purchased), Toast.LENGTH_SHORT).show()
//                    }

                    viewModel.initialCourse()

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            buyCourseForMoneyTreatmentResult(courseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseForMoneyTreatmentResult(courseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseForMoneyTreatmentResult(courseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            buyCourseForMoneyTreatmentResult(courseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseForMoneyTreatmentResult(courseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
            }
        },courseNumber)
    }

    private fun buyCourseAndropointTreatmentResult(buyCourseNumber: Int, price: Int) {
        var isBuy:BuyForAndropointStates?=null
        viewModel.buyCourseAndropoint({state->

            when(state){
                ErrorEnum.SUCCESS -> {
                    when(isBuy){
                        BuyForAndropointStates.YESMONEY -> {
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
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
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            buyCourseAndropointTreatmentResult(buyCourseNumber,price)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointTreatmentResult(buyCourseNumber,price)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointTreatmentResult(buyCourseNumber,price)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            buyCourseAndropointTreatmentResult(buyCourseNumber,price)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointTreatmentResult(buyCourseNumber,price)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })

                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
            }
        },price,{
            isBuy = it
        })

    }

    private fun buyCourseAndropointOpenTreatmentResult(buyCourseNumber: Int) {
        viewModel.buyCourseAndropointOpen({
            when(it){
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        Toast.makeText(requireContext(),getString(R.string.course_was_successfully_purchased), Toast.LENGTH_SHORT).show()
                    }
                    viewModel.initialCourse()
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }

                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }

                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
                        }) {

                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            buyCourseAndropointOpenTreatmentResult(buyCourseNumber)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }

                    }
                }
                ErrorEnum.OFFLINEMODE -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
            }
        },buyCourseNumber)
    }

    private fun promoCodeTreatmentResult(
        dialogPromoCode: EditText,
        dialogError: TextView,
        dialog: Dialog
    ) {
        var promoCodeState: PromoCodeState? = null
        lifecycleScope.launch {
            viewModel.checkPromoCode(dialogPromoCode.text.toString(), { resultPromoCode ->
                Log.d("promoCodeTestTest",resultPromoCode.toString())
                when (resultPromoCode) {
                    ErrorEnum.SUCCESS -> {
                        Log.d("promoCodeTestTest",promoCodeState.toString()+" PromoExist")
                        when (promoCodeState) {

                            PromoCodeState.PROMOEXISTSUCCESS -> {
                                requireActivity().runOnUiThread {
                                    Toast.makeText(
                                        context,
                                        getString(R.string.promo_code_was_success_used),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    dialog.dismiss()
                                }

                            }

                            PromoCodeState.PROMOUSEREXIST ->{
                                requireActivity().runOnUiThread {
                                    errorPromoCode(
                                        dialogPromoCode,
                                        dialogError,
                                        getString(R.string.promo_code_already)
                                    )
                                }
                            }


                            PromoCodeState.PROMONOTEXIST -> {
                                requireActivity().runOnUiThread {
                                    errorPromoCode(
                                        dialogPromoCode,
                                        dialogError,
                                        getString(R.string.promo_code_not_exist)
                                    )
                                }
                            }


                            else -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewCourse?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                                    }) {
                                        binding?.dimViewCourse?.visibility = View.GONE
                                    }
                                    dialog.dismiss()
                                }
                            }
                        }
                    }

                    ErrorEnum.NOTNETWORK -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog?.dismiss()
                        }
                    }

                    ErrorEnum.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog?.dismiss()
                        }
                    }

                    ErrorEnum.ERROR -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog.dismiss()
                        }
                    }

                    ErrorEnum.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog.dismiss()
                        }
                    }

                    ErrorEnum.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog.dismiss()
                        }
                    }

                    else -> {
                        requireActivity().runOnUiThread {
                            binding?.dimViewCourse?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog)
                            }) {
                                binding?.dimViewCourse?.visibility = View.GONE
                            }
                            dialog.dismiss()
                        }
                    }
                }
                Log.d("promoDialdodod", resultPromoCode.toString())
            }, { promoCodeState = it })
        }

    }

    private fun errorPromoCode(dialogPromoCode: EditText, dialogError: TextView,textError:String) {
        dialogError.visibility=View.VISIBLE
        dialogError.postDelayed({
            dialogError.visibility = View.GONE
        }, 3000)
        dialogError.text=textError
        ErrorHelper.showEmailErrorFeedback(requireContext(),dialogPromoCode, isNeedDrawable = false)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state_key_course", viewModel.currentState)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    private fun checkCoursesThemes(courseNumber: Int,courseName: String){
        viewModel.checkAllCourseThemesTerm(courseNumber,{ errorState->
            when(errorState){
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        val action = CoursesFragmentDirections.actionCoursesFragmentToThemesFragment(
                            courseNumber,
                            courseName
                        )
                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkCoursesThemes(courseNumber,courseName)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkCoursesThemes(courseNumber,courseName)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkCoursesThemes(courseNumber,courseName)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkCoursesThemes(courseNumber,courseName)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkCoursesThemes(courseNumber,courseName)
                        }) {
                            binding?.dimViewCourse?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {

                    requireActivity().runOnUiThread {
                        binding?.dimViewCourse?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext(),{

                        },{
                            binding?.dimViewCourse?.visibility = View.GONE
                        })
                    }
                }
            }
        },{

        })
    }

    override fun onStart() {
        super.onStart()
        obServer?.let { viewModel.allCourses.observe(this, it) }
    }

    override fun onStop() {
        super.onStop()
        obServer?.let { viewModel.allCourses.removeObserver(it
        ) }
    }



    private fun checkForAppUpdates(){
        appUpdateManager?.appUpdateInfo?.addOnSuccessListener { info->
            val isUpdateAviable = info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
            val isUpdateSupported = when(updateType){
                AppUpdateType.FLEXIBLE-> info.isFlexibleUpdateAllowed
                AppUpdateType.IMMEDIATE-> info.isImmediateUpdateAllowed
                else->false
            }
            if (isUpdateAviable&&isUpdateSupported){
                appUpdateManager?.startUpdateFlowForResult(
                    info,
                    updateType,
                    requireActivity(),
                    123
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(updateType == AppUpdateType.IMMEDIATE) {
            appUpdateManager?.appUpdateInfo?.addOnSuccessListener { info ->
                if (info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager?.startUpdateFlowForResult(
                        info,
                        updateType,
                        requireActivity(),
                        123
                    )
                }
            }
        }
    }





}