package workwork.test.andropediagits.presenter.forgetScreens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation

import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.email.RecoverPassState
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.databinding.FragmentForgetBinding
import workwork.test.andropediagits.presenter.forgetScreens.viewmodel.ForgetViewModel
import workwork.test.andropediagits.presenter.lesson.LessonFragmentDirections
import workwork.test.andropediagits.presenter.lesson.utils.ErrorHelper
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper

@AndroidEntryPoint
class ForgetFragment : Fragment() {
    private var binding: FragmentForgetBinding? = null
    private val viewModel: ForgetViewModel by viewModels()
    private var email = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentForgetBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var selectDate="02/02/2020"
        binding?.apply {
            btnReady.setOnClickListener {
                email = edEmailForget.text.toString()
                getMethodUser(edEmailForget.text.toString(), binding)
                // recoverPasswordTreatmentResult(email, binding)
            }
//            btnSelectForgetKeyword.setOnClickListener {
//                val resetTextCheckSendModel = ResetTextCheckSendModel(
//                    text = edKeywordForgetDialog.text.toString(),
//                    email = email
//                )
//                checkResetTextTreatmentResult(resetTextCheckSendModel)
//
//            }
//            btnSelectForgetKeyword.setOnClickListener {
//                val resetDateCheckSendModel = ResetDateCheckSendModel(
//                    date = selectDate,
//                    email = email
//                )
//                checkResetDateTreatmentResult(resetDateCheckSendModel)
//            }

//            btnSelectDateForgetDialog.setOnClickListener {
//                ShowDialogHelper.datePicker(requireActivity(), viewLifecycleOwner,tvSelectedDateForgetDialog){
//                    selectDate=it
//                }
//
//
//            }
//
//            btnReadyForgetDialog.setOnClickListener {
//            if(selectDate=="02/02/2020")   {
//                Toast.makeText(requireContext(),"Выберите другую дату",Toast.LENGTH_SHORT).show()
//            }else{
//                val resetDateCheckSendModel = ResetDateCheckSendModel(
//                    date = selectDate,
//                    email = email
//                )
//                checkResetDateTreatmentResult(resetDateCheckSendModel)
//            }
//
//            }
//
            btnReadyNewPassword.setOnClickListener {
                if (edEnterNewPassword.text.toString() == edEnterNewPasswordAgain.text.toString()) {
                    newPasswordTreatmentResult(email,edEnterNewPassword.text.toString())
                }else{
                    binding?.tvErrorNewPasswordForget?.visibility = View.VISIBLE
                    binding?.tvErrorNewPasswordForget?.postDelayed({binding?.tvErrorNewPasswordForget?.visibility = View.GONE }, 3000)
                }
            }
        }

    }

    private fun checkResetTextTreatmentResult(keyword:String, isCorrect:((Boolean)->Unit)?=null) {
        ShowDialogHelper.showDialogLoadData(requireContext())
        val resetTextCheckSendModel = ResetTextCheckSendModel(
            text = keyword,
            email = email
        )
        viewModel.checkResetText(resetTextCheckSendModel, {
            if (it) {
                isCorrect?.invoke(true)
            } else {
                isCorrect?.invoke(false)
                /* context?.let { it1 ->
                     binding?.edKeywordForgetDialog?.let { it2 ->
                         ErrorHelper.showEmailErrorFeedback(
                             it1,/////////не удалять !!!!!!!!!!
                             it2,
                             errorDrawable =R.drawable.ic_clue_error,
                             oldDrawable=R.drawable.ic_clue_gray
                         )
                     }
                 }*/
            }
        }, {
            when (it) {
                ErrorEnum.SUCCESS -> ShowDialogHelper.closeDialogLoadData()

                ErrorEnum.NOTNETWORK -> {
                    ShowDialogHelper.closeDialogLoadData()
                    ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                        checkResetTextTreatmentResult(keyword)
                    }
                }

                ErrorEnum.ERROR -> {
                    ShowDialogHelper.closeDialogLoadData()
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkResetTextTreatmentResult(keyword)
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    ShowDialogHelper.closeDialogLoadData()
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkResetTextTreatmentResult(keyword)
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    ShowDialogHelper.closeDialogLoadData()
                    ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                        checkResetTextTreatmentResult(keyword)
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    ShowDialogHelper.closeDialogLoadData()
                    ShowDialogHelper.showDialogUnknownError(requireContext()) {
                        checkResetTextTreatmentResult(keyword)
                    }
                }

                ErrorEnum.OFFLINEMODE -> {}
                ErrorEnum.OFFLINETHEMEBUY -> {}
            }
        })
    }

    private fun checkResetDateTreatmentResult(date: String,  isCorrect:((Boolean)->Unit)?=null) {
        ShowDialogHelper.showDialogLoadData(requireContext())
        val resetDateCheckSendModel = ResetDateCheckSendModel(
            date = date,
            email = email
        )
        viewModel.checkResetDate(resetDateCheckSendModel, {
            if (it) {

                    isCorrect?.invoke(true)


            } else {

                    isCorrect?.invoke(false)

            }
        }, {
            Log.d("4440404liltecca",it.toString())
            when (it) {

                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }

                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            checkResetDateTreatmentResult(date,isCorrect)
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkResetDateTreatmentResult(date,isCorrect)
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkResetDateTreatmentResult(date,isCorrect)
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            checkResetDateTreatmentResult(date,isCorrect)
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            checkResetDateTreatmentResult(date,isCorrect)
                        }
                    }

                }

                ErrorEnum.OFFLINEMODE -> {}
                ErrorEnum.OFFLINETHEMEBUY -> {}
            }
        })

    }

//    private fun checkResetDateTreatmentResult(resetDateCheckSendModel: ResetDateCheckSendModel) {
//        ShowDialogHelper.showDialogLoadData(requireContext())
//        viewModel.checkResetDate(resetDateCheckSendModel, {
//            if (it) {
//                requireActivity().runOnUiThread {
//                    binding?.enterDateWordForgetCard?.visibility = View.GONE
//                    binding?.newPasswordCard?.let { it1 -> animationAppearance(it1) }
//                }
//            } else {
//                requireActivity().runOnUiThread {
//                    binding?.tvErrorDateForgetDialog?.visibility = View.VISIBLE
//                    binding?.tvErrorDateForgetDialog?.postDelayed({
//                        binding?.tvErrorDateForgetDialog?.visibility = View.GONE
//                    }, 3000)
//                }
//            }
//        }, {
//            when (it) {
//                ErrorEnum.SUCCESS ->   {
//                    requireActivity().runOnUiThread {
//                        ShowDialogHelper.closeDialogLoadData()
//                    }
//                }
//                ErrorEnum.NOTNETWORK -> {
//                    requireActivity().runOnUiThread {
//                        ShowDialogHelper.closeDialogLoadData()
//                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
//                            checkResetDateTreatmentResult(resetDateCheckSendModel)
//                        }
//                    }
//
//                }
//
//                ErrorEnum.ERROR -> {
//                    requireActivity().runOnUiThread {
//                        ShowDialogHelper.closeDialogLoadData()
//                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                            checkResetDateTreatmentResult(resetDateCheckSendModel)
//                        }
//                    }
//
//                }
//
//                ErrorEnum.UNKNOWNERROR -> {
//                    requireActivity().runOnUiThread {
//                        ShowDialogHelper.closeDialogLoadData()
//                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                            checkResetDateTreatmentResult(resetDateCheckSendModel)
//                        }
//                    }
//
//                }
//
//                ErrorEnum.TIMEOUTERROR -> {
//                    requireActivity().runOnUiThread {
//
//                    }
//                    ShowDialogHelper.closeDialogLoadData()
//                    ShowDialogHelper.showDialogTimeOutError(requireContext()) {
//                        checkResetDateTreatmentResult(resetDateCheckSendModel)
//                    }
//                }
//
//                ErrorEnum.NULLPOINTERROR -> {
//                    requireActivity().runOnUiThread {
//                        ShowDialogHelper.closeDialogLoadData()
//                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
//                            checkResetDateTreatmentResult(resetDateCheckSendModel)
//                        }
//                    }
//
//                }
//
//                ErrorEnum.OFFLINEMODE -> {}
//                ErrorEnum.OFFLINETHEMEBUY -> {}
//            }
//        })
//    }

    private fun newPasswordTreatmentResult(email: String, newPassword: String) {
        ShowDialogHelper.showDialogLoadData(requireContext())
        viewModel.sendNewPassword(email,   newPassword){
            when (it) {
                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        val action =
                            ForgetFragmentDirections.actionForgetFragmentToSignInFragment()
                        binding?.root?.let { it1 ->
                            Navigation.findNavController(it1).navigate(action)
                        }
                    }

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            getMethodUser(email,binding)
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            getMethodUser(email,binding)
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            getMethodUser(email,binding)
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            getMethodUser(email,binding)
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            getMethodUser(email,binding)
                        }
                    }

                }

                ErrorEnum.OFFLINEMODE -> {}
                ErrorEnum.OFFLINETHEMEBUY -> {}
            }
        }
    }

    private  fun getMethodUser(email: String,binding: FragmentForgetBinding?){
        var response: ResetMethodGetModel?=null
        ShowDialogHelper.showDialogLoadData(requireContext())

            viewModel.getUserMethods(email, { methods ->
                response = methods
            }) { state ->
                when (state) {
                    ErrorEnum.SUCCESS -> {
                        if (response?.isResetDate == true) {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.closeDialogLoadData()
                                binding?.enterEmailCard?.visibility=View.GONE
                                test(response)
//                                binding?.enterEmailCard?.visibility=View.GONE
//                                binding?.enterDateWordForgetCard?.let { animationAppearance(it) }
//                                binding?.tvQuestionDialogDate?.text = response?.queryText
                            }

                        }

                        if (response?.isResetText == true) {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.closeDialogLoadData()
                                binding?.enterEmailCard?.visibility=View.GONE
                                treatmentResultKeyword(response)
//                                binding?.enterKeywordForgetCard?.let { animationAppearance(it) }
//                                binding?.tvQuestionKeywordForgetDialog?.text = response?.queryText
                            }

                        }

                        if (response?.codeAnswer == 2013) {
                            requireActivity().runOnUiThread {
                                ShowDialogHelper.closeDialogLoadData()
                                binding?.edEmailForget?.let {
                                    ErrorHelper.showEmailErrorFeedback(
                                        requireContext(),
                                        it,
                                        text = getString(R.string.email_not_exist),
                                        isNeedDrawable = false
                                    )
                                }
                            }

                        }
                    }

                    ErrorEnum.NOTNETWORK -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                                getMethodUser(email,binding)
                            }
                        }

                    }

                    ErrorEnum.ERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                getMethodUser(email,binding)
                            }
                        }

                    }

                    ErrorEnum.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                getMethodUser(email,binding)
                            }
                        }

                    }

                    ErrorEnum.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                                getMethodUser(email,binding)
                            }
                        }

                    }

                    ErrorEnum.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                getMethodUser(email,binding)
                            }
                        }

                    }

                    ErrorEnum.OFFLINEMODE -> {}
                    ErrorEnum.OFFLINETHEMEBUY -> {}
                }
            }
        }


    /*private fun recoverPasswordNewPasswordTreatmentResult(
        email: String,
        code: String,
        newPassword: String
    ) {
        viewModel.recoverPassword(
            email,
            { forgetResult ->
                when (forgetResult) {
                    RecoverPassState.PASSWORDCHANGE -> {
                        requireActivity().runOnUiThread {    val action =
                            ForgetFragmentDirections.actionForgetFragmentToSignInFragment()
                            binding?.root?.let { it1 ->
                                Navigation.findNavController(it1).navigate(action)
                            }
                            Toast.makeText(
                                requireContext(),
                                getString(R.string.password_change),
                                Toast.LENGTH_SHORT
                            ).show() }

                    }

                    RecoverPassState.NOTNETWORK -> {
                        requireActivity().runOnUiThread {     ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        } }

                    }

                    RecoverPassState.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {    ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        } }

                    }

                    RecoverPassState.ERROR -> {
                        requireActivity().runOnUiThread {          ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        } }

                    }

                    RecoverPassState.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {         ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        }  }

                    }

                    RecoverPassState.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        } }

                    }

                    else -> {
                        requireActivity().runOnUiThread {      ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordNewPasswordTreatmentResult(email, code, newPassword)
                        } }

                    }
                }
            },
            code,
            newPassword
        )
    }*/


    private fun recoverPasswordOtpTreatmentResult(
        email: String,
        binding: FragmentForgetBinding?,
        otp: String?
    ) {

        /*viewModel.recoverPassword(email, { forgetResult->

            when (forgetResult) {
                RecoverPassState.CORRECTCODE -> {
                    requireActivity().runOnUiThread {
                        binding?.otpCode?.let {
                            it.visibility = View.GONE
                        }
                        binding?.otpCard?.let {
                            it.visibility = View.GONE
                        } ?: Log.d("otpotplfofrfjo","null(")

                        Toast.makeText(requireContext(), getString(R.string.code_success), Toast.LENGTH_SHORT).show()
                        binding?.newPasswordCard?.let { animationAppearance(it) }
                    }

                }

                RecoverPassState.CODENOTEXIST -> {}
                RecoverPassState.INCORRECTCODE -> {
                    requireActivity().runOnUiThread {
                        binding?.codeError?.visibility = View.VISIBLE
                        binding?.codeError?.postDelayed({   binding?.codeError?.visibility = View.GONE }, 3000)
                    }

                }

                RecoverPassState.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }

                RecoverPassState.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }

                RecoverPassState.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }

                RecoverPassState.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }

                RecoverPassState.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordOtpTreatmentResult(email, binding, otp)
                        }
                    }
                }
            }

        }, otp)
*/
    }

    /* private fun recoverPasswordTreatmentResult(email: String, binding: FragmentForgetBinding?) {
        *//* viewModel.recoverPassword(email, { forgetResult->
            when (forgetResult) {
                RecoverPassState.CODESENDEMAIL -> {
                    requireActivity().runOnUiThread {
                        binding?.enterEmailCard?.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.send_code_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding?.otpCard?.let { animationAppearance(it) }
                    }
                }

                RecoverPassState.CODEEXIST -> {
                    requireActivity().runOnUiThread {
                        binding?.enterEmailCard?.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.send_code_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        binding?.otpCard?.let { animationAppearance(it) }
                    }
                }

                RecoverPassState.EMAILISEMPTY -> {
                    requireActivity().runOnUiThread {
                        binding?.edEmailForget?.let {
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                it,
                                text = getString(R.string.email_empty),
                                isNeedDrawable = false
                            )
                        }
                    }
                }

                RecoverPassState.EMAILISNOTEXIST -> {
                    requireActivity().runOnUiThread {
                        binding?.edEmailForget?.let {
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                it,
                                text = getString(R.string.email_not_exist),
                                isNeedDrawable = false
                            )
                        }
                    }
                }

                RecoverPassState.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }

                RecoverPassState.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }

                RecoverPassState.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }

                RecoverPassState.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }

                RecoverPassState.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }

                else -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            recoverPasswordTreatmentResult(email, binding)
                        }
                    }
                }
            }
        })
*//*
    }
*/
    private fun test(response: ResetMethodGetModel?) {
        ShowDialogHelper.showDialogSelectDateForget(requireContext(),requireActivity(),viewLifecycleOwner, response?.queryText.toString())
        { date ->
            checkResetDateTreatmentResult(date){
                if(it){
                    requireActivity().runOnUiThread {
//                        binding?.newPasswordCard?.let { it1 -> animationAppearance(it1) }
                        binding?.newPasswordCard?.visibility = View.VISIBLE
                    }

                }else{

                    requireActivity().runOnUiThread {
                        test(response)
                        Toast.makeText(requireContext(),R.string.invalid_date,Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    private fun treatmentResultKeyword(response: ResetMethodGetModel?) {
        ShowDialogHelper.showDialogSelectKeywordForget(requireContext(), response?.queryText.toString())
        { keyword ->
            checkResetTextTreatmentResult(keyword){
                if(it){
                    requireActivity().runOnUiThread {
                        binding?.newPasswordCard?.visibility = View.VISIBLE
                    }
                }else{
                    requireActivity().runOnUiThread {
                        treatmentResultKeyword(response)
                        Toast.makeText(
                            requireContext(),
                            R.string.invalid_keyword,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun animationAppearance(view: View) {
        val animation = AlphaAnimation(0f, 1f)
        animation.duration = 1000
        animation.fillAfter = true
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                view.visibility = View.VISIBLE
            }

            override fun onAnimationEnd(animation: Animation) {
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
        view.startAnimation(animation)
    }
}