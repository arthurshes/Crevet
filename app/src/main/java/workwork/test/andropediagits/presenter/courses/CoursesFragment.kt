package workwork.test.andropediagits.presenter.courses

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.navArgs
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
    private var appUpdateManager: AppUpdateManager?=null
    private val updateType = AppUpdateType.IMMEDIATE
    private var binding: FragmentCoursesBinding? = null
    private val args: workwork.test.andropediagits.presenter.courses.CoursesFragmentArgs by navArgs()
    private var adapter : CourseAdapter?=null
    private val viewModel: CoursesViewModel by viewModels()
    private var billingManager: BillingManager?=null


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
            Toast.makeText(requireContext(),"Загрузка завершена, приложение будет перезагружено через 5 секунд",Toast.LENGTH_LONG).show()
            lifecycleScope.launch {
                delay(5.seconds)
                appUpdateManager?.completeUpdate()
            }
//            appUpdateManager.completeUpdate()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val sharedPreferences = requireContext().getSharedPreferences("PromoCodeState", Context.MODE_PRIVATE)
//        val editor = sharedPreferences.edit()
        adapter = CourseAdapter(requireContext())
        if (savedInstanceState != null) {
            viewModel.currentState = savedInstanceState.getString("state_key_course", "")
        }

//        isPromoShow = args.isShowPromoCode
//        editor.putBoolean("flag_key", isFlag)
        if(args.isShowPromoCode){
            val dialog = Dialog(requireContext())
            dialog.setContentView(R.layout.promo_code_dialog)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            dialog.setCancelable(false)
            val dialogReady = dialog.findViewById<CardView>(R.id.btnReadyPromo)
            val dialogPromoCode = dialog.findViewById<EditText>(R.id.edEnterPromoCode)
            val dialogError = dialog.findViewById<TextView>(R.id.tvPromoCodeError)
            val btnClose = dialog.findViewById<LinearLayoutCompat>(R.id.btnClose)
            dialogReady.setOnClickListener {
                promoCodeTreatmentResult(dialogPromoCode,dialogError,dialog,{

                })

            }
            btnClose.setOnClickListener {
                dialog.dismiss()

            }

            dialog.show()
        }

        adapter?.andropointPrice = {androPrice->
            adapter?.rubPrice = {rubPrice->
                adapter?.buyCourseNumber = {buyCourseNumber->
                    ShowDialogHelper.showDialogClose(requireContext(),{
                        ShowDialogHelper.showDialogBuy(requireContext(),rubPrice,androPrice,{
                            if(rubPrice==150){
                              billingManager?.billingSetup(PayState.COURSEBUYADVANCED,buyCourseNumber)
                            }
                            if(rubPrice==500){
                                billingManager?.billingSetup(PayState.COURSEBUYINDEPTH,buyCourseNumber)
                            }
                        },{
                            if(rubPrice==150){
                                var isBuy:BuyForAndropointStates?=null
                                viewModel.buyCourseAndropoint({state->
                                    when(state){
                                        ErrorEnum.NOTNETWORK -> {
                                            TODO()
                                        }
                                        ErrorEnum.ERROR -> {
                                            TODO()
                                        }
                                        ErrorEnum.SUCCESS -> {
                                            when(isBuy){
                                                BuyForAndropointStates.YESMONEY -> {
                                                    viewModel.buyCourseAndropointOpen({
                                                        when(it){
                                                            ErrorEnum.NOTNETWORK -> {
                                                                TODO()
                                                            }
                                                            ErrorEnum.ERROR -> {
                                                                TODO()
                                                            }
                                                            ErrorEnum.SUCCESS -> {
                                                                TODO()
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
                                                                TODO()
                                                            }
                                                            ErrorEnum.OFFLINETHEMEBUY -> {
                                                                TODO()
                                                            }
                                                        }
                                                    },buyCourseNumber)
                                                }
                                                BuyForAndropointStates.NOMONEY -> {
                                                   Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
                                                null -> {
                                                    Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
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
                                        ErrorEnum.OFFLINEMODE -> TODO()
                                        ErrorEnum.OFFLINETHEMEBUY -> TODO()
                                    }
                                },600,{
                                     isBuy = it
                                })

                            }
                            if(rubPrice==500){
                                var isBuy:BuyForAndropointStates?=null
                                viewModel.buyCourseAndropoint({state->
                                    when(state){
                                        ErrorEnum.NOTNETWORK -> TODO()
                                        ErrorEnum.ERROR -> TODO()
                                        ErrorEnum.SUCCESS -> {
                                            when(isBuy){
                                                BuyForAndropointStates.YESMONEY -> {
                                                    viewModel.buyCourseAndropointOpen({
                                                        when(it){
                                                            ErrorEnum.NOTNETWORK -> {
                                                                TODO()
                                                            }
                                                            ErrorEnum.ERROR -> {
                                                                TODO()
                                                            }
                                                            ErrorEnum.SUCCESS -> {
                                                                TODO()
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
                                                                TODO()
                                                            }
                                                            ErrorEnum.OFFLINETHEMEBUY -> {
                                                                TODO()
                                                            }
                                                        }
                                                    },buyCourseNumber)
                                                }
                                                BuyForAndropointStates.NOMONEY -> {
                                                    Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
                                                null -> {
                                                    Toast.makeText(requireContext(),R.string.node_money_andropoint,Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                        }
                                        ErrorEnum.UNKNOWNERROR -> TODO()
                                        ErrorEnum.TIMEOUTERROR -> TODO()
                                        ErrorEnum.NULLPOINTERROR -> TODO()
                                        ErrorEnum.OFFLINEMODE -> TODO()
                                        ErrorEnum.OFFLINETHEMEBUY -> TODO()
                                    }
                                },4500,{
                                    isBuy = it
                                })

                            }

                        })
                    })
                }
            }
        }

        adapter?.onClickCourse = {courseNum->
            adapter?.onCourseName = { courseName->
                adapter?.onPossibleBuy = { courBuy->
                    adapter?.isCourseOpen = { courseOpem->
                        Log.d("clickThemeTest", "courseNumber:${courseNum},courseName:${courseName},courseBuy:${courBuy}")
//                        checkCourseBuyTreatmentResult(courseNum,courseName,courBuy,courseOpem)
                        checkCoursesThemes(courseNum,courseName)
                    }

                }
            }
        }

        billingManager?.courseBuyWithNumber = {courseNumber->
            viewModel.buyCourseForMoney({state->
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
            },courseNumber)
        }

        binding?.rcViewCourses?.layoutManager = LinearLayoutManager(requireContext())
        binding?.rcViewCourses?.adapter = adapter
        viewModel.allCourses?.observe(viewLifecycleOwner) {
            adapter?.diffList?.submitList(it)
        }
    }

    private fun promoCodeTreatmentResult(
        dialogPromoCode: EditText,
        dialogError: TextView,
        dialog: Dialog,
        isPromoShow:((Boolean)->Unit)
    ) {
//        var resultPromoCode:ErrorEnum?=null




        var promoCodeState: PromoCodeState? = null
        lifecycleScope.launch {
        viewModel.checkPromoCode(dialogPromoCode.text.toString(), { resultPromoCode ->
            when (resultPromoCode) {
                ErrorEnum.SUCCESS -> {
                    when (promoCodeState) {
                        PromoCodeState.PROMOEXISTSUCCESS -> {
                            isPromoShow.invoke(false)
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
                            isPromoShow.invoke(true)
                            requireActivity().runOnUiThread {
                                errorPromoCode(
                                    dialogPromoCode,
                                    dialogError,
                                    getString(R.string.promo_code_already)
                                )
                            }
                        }


                        PromoCodeState.PROMONOTEXIST -> {
                            isPromoShow.invoke(true)
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
                                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                    promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{

                                    })
                                }
                            }
                        }
                    }
                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
                        dialog.dismiss()
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
                        dialog.dismiss()
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
                        dialog.dismiss()
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            promoCodeTreatmentResult(dialogPromoCode, dialogError, dialog,{})
                        }
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

//    override fun checkCourseBuy(
//        courseNumber: Int,
//        courseName: String,
//        possibleToOpenCourseFree: Boolean
//    ) {
//        checkCourseBuyTreatmentResult(courseNumber,courseName,possibleToOpenCourseFree)
//
//    }

    private fun checkCoursesThemes(courseNumber: Int,       courseName: String){
        viewModel.checkAllCourseThemesTerm(courseNumber,{ errorState->
            when(errorState){
                ErrorEnum.NOTNETWORK -> {
                    TODO()
                }
                ErrorEnum.ERROR -> {
                    TODO()
                }
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        val action = CoursesFragmentDirections.actionCoursesFragmentToThemesFragment(
                            courseNumber,
                            courseName
                        )
                        binding?.root?.let { Navigation.findNavController(it).navigate(action) }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    TODO()
                }
                ErrorEnum.TIMEOUTERROR -> {

                }
                ErrorEnum.NULLPOINTERROR -> {
                    TODO()
                }
                ErrorEnum.OFFLINEMODE -> {
                    TODO()
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    TODO()
                }
            }
        },{

        })
    }

//    private fun checkCourseBuyTreatmentResult(
//        courseNumber: Int,
//        courseName: String,
//        possibleToOpenCourseFree: Boolean,
//        isCourseOpen:Boolean,
//        andropointPrice:Int,
//        rubPrice:Int
//    ) {
////        var resultCheckCourseBuy:ErrorEnum?=null
//
//        var isBuyCourse=false
//        var userBuyCourse=false
//        var userLookCourse=false
//        var pressGoogle=false
//        var pressTinkoff=false
//        var pressAndroBuy=false
//        lifecycleScope.launch {
//            viewModel.checkCourseBuy({ resultCheckCourseBuy->
//                when(resultCheckCourseBuy){
//                    ErrorEnum.SUCCESS -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(),"SUCCESS",Toast.LENGTH_LONG).show()
//                        }
//                        Log.d("cccccoo","buy buy var: "+isBuyCourse)
//                        if (isBuyCourse) {
//
//                         checkCoursesThemes(courseNumber,courseName,isBuyCourse)
//
//                        } else {
//                            if(isCourseOpen){
//                                checkCoursesThemes(courseNumber,courseName,isBuyCourse)
//                            }else{
//                                requireActivity().runOnUiThread {
//                                    ShowDialogHelper.showDialogClose(requireContext(),
//                                        {
//                                            ShowDialogHelper.showDialogBuy(
//                                                requireContext(),
//                                                {},
//                                                {},
//                                                { buyCourseTreatmentResult() }
//                                            )
//                                        }
//                                    )
//                                }
//                            }
//                        }
//                    }
//
//                    ErrorEnum.NOTNETWORK -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "NOTNETWORK", Toast.LENGTH_LONG).show()
//                            ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//
//                    ErrorEnum.TIMEOUTERROR -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "TIMEOUTERROR", Toast.LENGTH_LONG)
//                                .show()
//                            ShowDialogHelper.showDialogTimeOutError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//
//                    ErrorEnum.ERROR -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_LONG).show()
//                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//
//                    ErrorEnum.NULLPOINTERROR -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "NULLPOINTERROR", Toast.LENGTH_LONG)
//                                .show()
//                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//
//                    ErrorEnum.UNKNOWNERROR -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "UNKNOWNERROR", Toast.LENGTH_LONG)
//                                .show()
//                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//
//                    else -> {
//                        requireActivity().runOnUiThread {
//                            Toast.makeText(requireContext(), "else", Toast.LENGTH_LONG).show()
//                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                                checkCourseBuyTreatmentResult(
//                                    courseNumber,
//                                    courseName,
//                                    possibleToOpenCourseFree,
//                                    isCourseOpen
//                                )
//                            }
//                        }
//                    }
//                }
//            },{
//                Log.d("cccccoo","buy buy : "+it.toString())
//                isBuyCourse = it
//              },courseNumber)
//        }
//
//
//
//    }

    private fun buyCourseTreatmentResult() {
        var resultCourseBuy:ErrorEnum?=null
        var pressWatchAd=false
        var pressPay=false
        var andropoints=0
        var money=0
        var isHaveMoneyResult: BuyForAndropointStates?=null
//        viewModel.buyCourse({ resultCourseBuy=it },{isHaveMoneyResult=it})
        when(resultCourseBuy){
            ErrorEnum.SUCCESS -> {
                if (isHaveMoneyResult == BuyForAndropointStates.YESMONEY) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.course_buy_success),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    ShowDialogHelper.showDialogBuyAndropoints(requireContext(),
                        {},
                        {},
                        { andropoints = it },
                        { money = it })

                }
            }
            ErrorEnum.NOTNETWORK -> {
                ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }

            ErrorEnum.TIMEOUTERROR -> {
                ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }

            ErrorEnum.ERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }

            ErrorEnum.NULLPOINTERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }

            ErrorEnum.UNKNOWNERROR -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }

            else -> {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    buyCourseTreatmentResult()
                }
            }
        }
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