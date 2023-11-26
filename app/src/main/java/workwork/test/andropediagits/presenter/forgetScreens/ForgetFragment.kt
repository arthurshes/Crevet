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
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.databinding.FragmentForgetBinding
import workwork.test.andropediagits.presenter.forgetScreens.viewmodel.ForgetViewModel
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
        binding?.apply {
            btnReady.setOnClickListener {
                email = edEmailForget.text.toString()
                getMethodUser(edEmailForget.text.toString(), binding)
            }
            btnReadyNewPassword.setOnClickListener {
                if (edEnterNewPassword.text.toString() == edEnterNewPasswordAgain.text.toString()) {
                    newPasswordTreatmentResult(email, edEnterNewPassword.text.toString())
                } else {
                    binding?.tvErrorNewPasswordForget?.visibility = View.VISIBLE
                    binding?.tvErrorNewPasswordForget?.postDelayed({
                        binding?.tvErrorNewPasswordForget?.visibility = View.GONE
                    }, 3000)
                }
            }
        }
    }

    private fun checkResetTextTreatmentResult(
        keyword: String,
        isCorrect: ((Boolean) -> Unit)? = null
    ) {
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
                ErrorEnum.SUCCESS ->     requireActivity().runOnUiThread {ShowDialogHelper.closeDialogLoadData()}

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkResetTextTreatmentResult(keyword)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetTextTreatmentResult(keyword)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetTextTreatmentResult(keyword)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkResetTextTreatmentResult(keyword)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetTextTreatmentResult(keyword)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
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
        })
    }

    private fun checkResetDateTreatmentResult(
        date: String,
        isCorrect: ((Boolean) -> Unit)? = null
    ) {
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
            Log.d("4440404liltecca", it.toString())
            when (it) {

                ErrorEnum.SUCCESS -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                    }

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            checkResetDateTreatmentResult(date, isCorrect)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetDateTreatmentResult(date, isCorrect)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetDateTreatmentResult(date, isCorrect)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            checkResetDateTreatmentResult(date, isCorrect)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            checkResetDateTreatmentResult(date, isCorrect)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
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
        })
    }
    private fun newPasswordTreatmentResult(email: String, newPassword: String) {
        ShowDialogHelper.showDialogLoadData(requireContext())
        viewModel.sendNewPassword(email, newPassword.trimEnd()) {
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
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
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
        }
    }

    private fun getMethodUser(email: String, binding: FragmentForgetBinding?) {
        var response: ResetMethodGetModel? = null
        ShowDialogHelper.showDialogLoadData(requireContext())

        viewModel.getUserMethods(email, { methods ->
            response = methods
        }) { state ->
            when (state) {
                ErrorEnum.SUCCESS -> {
                    if (response?.isResetDate == true) {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.enterEmailCard?.visibility = View.GONE
                            treatmentResultDate(response)
                        }

                    }

                    if (response?.isResetText == true) {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.enterEmailCard?.visibility = View.GONE
                            treatmentResultKeyword(response)
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
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {
                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
                        }
                    }

                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        binding?.dimForget?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            getMethodUser(email, binding)
                        }) {

                            binding?.dimForget?.visibility = View.GONE
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
        }
    }

    private fun treatmentResultDate(response: ResetMethodGetModel?) {
        ShowDialogHelper.showDialogSelectDateForget(
            requireContext(),
            requireActivity(),
            viewLifecycleOwner,
            response?.queryText.toString()
        )
        { date ->
            checkResetDateTreatmentResult(date) {
                if (it) {
                    requireActivity().runOnUiThread {
                        binding?.newPasswordCard?.visibility = View.VISIBLE
                    }
                } else {
                    requireActivity().runOnUiThread {
                        treatmentResultDate(response)
                        Toast.makeText(requireContext(), R.string.invalid_date, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }

    private fun treatmentResultKeyword(response: ResetMethodGetModel?) {
        ShowDialogHelper.showDialogSelectKeywordForget(
            requireContext(),
            response?.queryText.toString()
        )
        { keyword ->
            checkResetTextTreatmentResult(keyword) {
                if (it) {
                    requireActivity().runOnUiThread {
                        binding?.newPasswordCard?.visibility = View.VISIBLE
                    }
                } else {
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
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}