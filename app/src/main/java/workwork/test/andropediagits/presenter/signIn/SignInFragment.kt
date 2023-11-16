package workwork.test.andropediagits.presenter.signIn

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.EmailErrorEnum
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.AccountGoogleHelper
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.databinding.FragmentSignInBinding
import workwork.test.andropediagits.presenter.lesson.utils.ErrorHelper
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.signIn.viewModel.SignInViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null
    // private var selectedImage: Uri? = null
    private val accountGoogleHelper = AccountGoogleHelper(this)
    private var selectedImageBitmap: Bitmap? = null
    private val viewModel: SignInViewModel by viewModels()
    private var chooseLang:String = "rus"
    private val russianText = "🇷🇺    Рус"
    private val englishText = "🇺🇸    Eng"

    var lunchActGoogle =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            var result: ErrorEnum?=null
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val user = UserSignInModel(
                        "${account.displayName}",

                       token= account.idToken.toString()
                    )
                    viewModel.getCurrentTime {currentTime->
                        val userLocal = UserInfoEntity(name="${account.displayName}", lastOnlineDate = currentTime, token = account.idToken.toString(), userLanguage = chooseLang)
                        saveUserInfoTreatmentResult(userLocal)
                    }
                    ShowDialogHelper.showDialogLoadData(requireContext())
                    downloadlCourses(chooseLang)
                }
            } catch (e: ApiException) {
                ShowDialogHelper.showDialogUnknownError(requireContext(),{

                }) {
                    binding?.dimViewSignIng?.visibility = View.GONE
                    //////////////////////////////////////////////////////////
                }
                Log.d("googleJackanfojrofj32",e.toString())
            }

            }

    private fun saveUserInfoTreatmentResult(userLocal: UserInfoEntity) {
        viewModel.saveUserInfo(userLocal) {
            when(it){
                ErrorEnum.SUCCESS -> {}
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            saveUserInfoTreatmentResult(userLocal)
                        }) {           binding?.dimViewSignIng?.visibility = View.GONE
                        } }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            saveUserInfoTreatmentResult(userLocal)
                        }) {  binding?.dimViewSignIng?.visibility = View.GONE} }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            saveUserInfoTreatmentResult(userLocal)
                        }) { binding?.dimViewSignIng?.visibility = View.GONE } }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            saveUserInfoTreatmentResult(userLocal)
                        }) { binding?.dimViewSignIng?.visibility = View.GONE} }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            saveUserInfoTreatmentResult(userLocal)
                        }) { binding?.dimViewSignIng?.visibility = View.GONE } }
                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext()) }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogOffline(requireContext()) }
                }
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState != null) {
            viewModel.currentState = savedInstanceState.getString("state_key_signIn", "")
        }

            binding?.apply {
                parentLayout.setOnClickListener {
                    chooseLangParent(binding)
                }
                listLayout.setOnClickListener {
                    chooseLangList(binding)
                    Log.d("chhofbtjbrjbijbt4t", chooseLang)
                }
            btnSignGoogle.setOnClickListener {
                accountGoogleHelper.signIn()
            }
            btnSignInUp.setOnClickListener {

                checkResultSignInUseCase()
            }
            tvForgotPassword.setOnClickListener {
               val action = SignInFragmentDirections.actionSignInFragmentToForgetFragment()
                binding?.root?.let { it1 ->
                    Navigation.findNavController(it1).navigate(action)
                }
            }
        }
    }

    private fun chooseLangList(binding: FragmentSignInBinding?) {
        binding?.apply {
            if (englishTextView.text == russianText) {
                chooseLang = "rus"
                russianTextView.text = russianText
                englishTextView.text = englishText
            } else if (englishTextView.text == englishText) {
                chooseLang = "eng"
                russianTextView.text = englishText
                englishTextView.text = russianText
            }
        }
    }

    private fun chooseLangParent(binding: FragmentSignInBinding?) {
        binding?.apply {
            if (listLayout?.visibility == View.VISIBLE) {
                arrowImageViewDown?.visibility = View.VISIBLE
                arrowImageViewUp?.visibility = View.GONE
                listLayout.visibility = View.GONE
            } else {
                listLayout?.visibility = View.VISIBLE
                arrowImageViewDown?.visibility = View.GONE
                arrowImageViewUp?.visibility = View.VISIBLE
            }
        }
    }

    private fun checkResultSignInUseCase() {
        ShowDialogHelper.showDialogLoadData(requireContext())
        var isRegisters = false
        binding?.apply {

            viewModel.signInEmail(edEmail.text.toString(), edPassword.text.toString(),lang = chooseLang,{ isRegister->
                isRegisters = isRegister
            }) {

                    emailResult->
                when (emailResult) {
                    EmailErrorEnum.SUCCESS -> {
                        downloadlCourses(chooseLang)
                    }

                    EmailErrorEnum.NOTNETWORK -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.dimViewSignIng?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                checkResultSignInUseCase()
                            }) {

                                binding?.dimViewSignIng?.visibility = View.GONE
                            }
                        }

                    }

                    EmailErrorEnum.TIMEOUTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.dimViewSignIng?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                checkResultSignInUseCase()
                            }){

                                binding?.dimViewSignIng?.visibility = View.GONE
                            }
                        }

                    }

                    EmailErrorEnum.ERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.dimViewSignIng?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                checkResultSignInUseCase()
                            }){

                                binding?.dimViewSignIng?.visibility = View.GONE
                            }
                        }

                    }

                    EmailErrorEnum.WrongAddressEmail -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edEmail,
                                R.drawable.ic_email_error,
                                R.drawable.ic_email,
                                getString(R.string.email_wrong)
                            )
                        }

                    }

                    EmailErrorEnum.TooLongPassword -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_too_long)
                            )
                        }

                    }

                    EmailErrorEnum.IncorrectPassword -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_incorrect)
                            )
                        }

                    }

                    EmailErrorEnum.NULLPOINTERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.dimViewSignIng?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                checkResultSignInUseCase()
                            }){

                                binding?.dimViewSignIng?.visibility = View.GONE
                            }
                        }

                    }

                    EmailErrorEnum.PasswordIsEmpty -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_empty)
                            )
                        }

                    }

                    EmailErrorEnum.EmailIsEmpty -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edEmail,
                                R.drawable.ic_email_error,
                                R.drawable.ic_email,
                                getString(R.string.email_empty)
                            )
                        }


                    }

                    EmailErrorEnum.UNKNOWNERROR -> {
                        requireActivity().runOnUiThread {
                            ShowDialogHelper.closeDialogLoadData()
                            binding?.dimViewSignIng?.visibility = View.VISIBLE
                            ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                checkResultSignInUseCase()
                            }) {

                                binding?.dimViewSignIng?.visibility = View.GONE
                            }
                        }

                    }
                }
            }
        }
    }

    private fun downloadlCourses(lang:String?=null){
        viewModel.loadData({loadState->
            when(loadState) {
                ErrorEnum.SUCCESS -> {
                    viewModel.checkSubscribesAndBuyCourse {checkState->
                        when(checkState){
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSignIng?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                        downloadlCourses(lang)
                                    }) {
                                        binding?.dimViewSignIng?.visibility = View.GONE
                                    }
                                }
                            }
                            ErrorEnum.ERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSignIng?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        downloadlCourses(lang)
                                    }) {
                                        binding?.dimViewSignIng?.visibility = View.GONE
                                    }
                                }
                            }
                            ErrorEnum.SUCCESS -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                    val action =
                                        SignInFragmentDirections.actionSignInFragmentToPasswordRecoveryMethodFragment()
                                    binding?.root.let { it1 ->
                                        if (it1 != null) {
                                            Navigation.findNavController(it1).navigate(action)
                                        }
                                    }
                                }
                            }
                            ErrorEnum.UNKNOWNERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSignIng?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        downloadlCourses(lang)
                                    }) {
                                        binding?.dimViewSignIng?.visibility = View.GONE
                                    }
                                }
                            }
                            ErrorEnum.TIMEOUTERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSignIng?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                        downloadlCourses(lang)
                                    }) {
                                        binding?.dimViewSignIng?.visibility = View.GONE
                                    }
                                }
                            }
                            ErrorEnum.NULLPOINTERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSignIng?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        downloadlCourses(lang)
                                    }) {
                                        binding?.dimViewSignIng?.visibility = View.GONE
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
                    Log.d("LoadFragmState", "SUCCESS")

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            downloadlCourses(lang)
                        }) {
                            binding?.dimViewSignIng?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadlCourses(lang)
                        }) {
                            binding?.dimViewSignIng?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadlCourses(lang)
                        }) {
                            binding?.dimViewSignIng?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            downloadlCourses(lang)
                        }) {
                            binding?.dimViewSignIng?.visibility = View.GONE
                        }
                    }
                }

                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSignIng?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            downloadlCourses(lang)
                        }) {
                            binding?.dimViewSignIng?.visibility = View.GONE
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
        },lang)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("state_key_signIn", viewModel.currentState)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}