package workwork.test.andropediagits.presenter.reset

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View


import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextSendModel
import workwork.test.andropediagits.databinding.FragmentPasswordRecoveryMethodBinding
import workwork.test.andropediagits.presenter.lesson.LessonFragmentDirections
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.reset.viewModel.PasswordRecoveryMethodViewModel

@AndroidEntryPoint
class PasswordRecoveryMethodFragment : Fragment() {
    private var binding: FragmentPasswordRecoveryMethodBinding? = null
    private val viewModel: PasswordRecoveryMethodViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPasswordRecoveryMethodBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Ваш код для обработки нажатия на кнопку "назад"        // Например, закрытие фрагмента или выполнение определенного действия
            }}
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        binding?.apply {
            btnSelectDate.setOnClickListener {
                dimView.visibility = View.VISIBLE
                ShowDialogHelper.showDialogSelectDate(requireContext(),requireActivity(),viewLifecycleOwner,{text->
                    val (textClue, textDate) = removeSpecialWord(text)
                    viewModel.getEmail { email ->
                        val resetDateSendModel = ResetDateSendModel(email = email, questionAnswerReset = textClue, correctDate = textDate)
                        sendDateTreatmentResult(resetDateSendModel)
                    }
                },{
                    dimView.visibility = View.GONE
                })
            }
            btnSelectKeyword.setOnClickListener{
                dimView.visibility = View.VISIBLE
                ShowDialogHelper.showDialogSelectKeyword(requireContext(),{text->
                    val (textClue, textKeyWord) = removeSpecialWord(text)
                    viewModel.getEmail { email->
                        val resetTextSendModel= ResetTextSendModel(email = email, questionAnswerReset = textClue, correctText = textKeyWord)
                        sendKeywordTreatmentResult(resetTextSendModel)
                    }
                },{
                    dimView.visibility = View.GONE
                })
            }
        }
    }

    private fun sendDateTreatmentResult(resetDateSendModel: ResetDateSendModel) {
        viewModel.sendDate(resetDateSendModel) {
            when(it){
                ErrorEnum.SUCCESS -> {
                    viewModel.deleteResetNext()
                    requireActivity().runOnUiThread {
                        val action = PasswordRecoveryMethodFragmentDirections.actionPasswordRecoveryMethodFragmentToCoursesFragment(true)
                        binding?.root?.let { it1 ->
                            Navigation.findNavController(it1).navigate(action)
                        }
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendDateTreatmentResult(resetDateSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendDateTreatmentResult(resetDateSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            sendDateTreatmentResult(resetDateSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendDateTreatmentResult(resetDateSendModel)
                        }) {
                            binding?.dimView?.visibility = View.GONE

                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            sendDateTreatmentResult(resetDateSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
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

    private fun sendKeywordTreatmentResult(resetTextSendModel: ResetTextSendModel) {
        viewModel.sendKeyword(resetTextSendModel) {
            when(it){
                ErrorEnum.SUCCESS -> {
                    viewModel.deleteResetNext()
                    requireActivity().runOnUiThread {
                        val action = PasswordRecoveryMethodFragmentDirections.actionPasswordRecoveryMethodFragmentToCoursesFragment(true)
                        binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendKeywordTreatmentResult(resetTextSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendKeywordTreatmentResult(resetTextSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            sendKeywordTreatmentResult(resetTextSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            sendKeywordTreatmentResult(resetTextSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimView?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            sendKeywordTreatmentResult(resetTextSendModel)
                        }) {

                            binding?.dimView?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.OFFLINEMODE ->{
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

    private fun removeSpecialWord(text: String): Pair<String, String> {
        val separator = "!!!TEMPWORD!!!"
        val separatorIndex = text.indexOf(separator)
        return if (separatorIndex != -1) {
            val firstPart = text.substring(0, separatorIndex)
            val secondPart = text.substring(separatorIndex + separator.length)

            Pair(firstPart, secondPart)
        } else {
            Pair(text, "")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}