package workwork.test.andropediagits.presenter.signIn

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var binding: FragmentSignInBinding? = null
    // private var selectedImage: Uri? = null
    private val accountGoogleHelper = AccountGoogleHelper(this)
    private var selectedImageBitmap: Bitmap? = null
    private val viewModel: SignInViewModel by viewModels()
    private var chooseLang:String = "rus"

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
                        val userLocal = UserInfoEntity(
                            name="${account.displayName}",
                            lastOnlineDate = currentTime,
                            token = account.idToken.toString(),
                            userLanguage = chooseLang
                        )
                        viewModel.saveUserInfo(userLocal) {itResult-> result = itResult }
                    }



//                    val wrokRequest = PeriodicWorkRequestBuilder<CheckCacheWorker>(
//                          repeatInterval = 7,
//                        repeatIntervalTimeUnit = TimeUnit.DAYS
//                    ).setInitialDelay(duration = 7,
//                    TimeUnit.DAYS)
//                        .build()
//                    WorkManager.getInstance(requireContext()).enqueueUniquePeriodicWork(
//                        "checkCacheWorkerManager",
//                        ExistingPeriodicWorkPolicy.KEEP,
//                        wrokRequest
//                    )
                    ShowDialogHelper.showDialogLoadData(requireContext())
                    downloadlCourses(chooseLang)
//                    Navigation.findNavController(binding?.root!!).navigate(R.id.action_signInFragment_to_coursesFragment)

                }
            } catch (e: ApiException) {
                ShowDialogHelper.showDialogUnknownError(requireContext()) {
                    //////////////////////////////////////////////////////////
                }
                Log.d("googleJackanfojrofj32",e.toString())
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
        var russianText = "🇷🇺    Рус"
        var englishText = "🇺🇸    Eng"

            binding?.apply {
                parentLayout?.setOnClickListener {
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
                listLayout?.setOnClickListener {
                    if(englishTextView.text==russianText){
                        chooseLang = "rus"
                        russianTextView.text = russianText
                        englishTextView.text = englishText
                    } else if(englishTextView.text==englishText){
                        chooseLang = "eng"
                        russianTextView.text = englishText
                        englishTextView.text = russianText
                    }
                Log.d("chhofbtjbrjbijbt4t",chooseLang)
                }
//            parentLayout?.setOnClickListener {
//                if (listLayout?.visibility == View.VISIBLE) {
//                    val imageResourceDarkTheme = R.drawable.ic_arrow_down_light
//                    if(arrowImageView?.drawable == ContextCompat.getDrawable(requireContext(), imageResourceDarkTheme)){
//                        arrowImageView?.setImageResource(R.drawable.ic_arrow_up_light)
//                    }else{
//                        arrowImageView?.setImageResource(R.drawable.baseline_keyboard_arrow_up_24)
//                    }
//                    listLayout.visibility = View.GONE
//                } else {
//                    val imageResourceDarkTheme = R.drawable.ic_arrow_up_light
//                    listLayout?.visibility = View.VISIBLE
//                    if(arrowImageView?.drawable == ContextCompat.getDrawable(requireContext(), imageResourceDarkTheme)){
//                        arrowImageView?.setImageResource(R.drawable.ic_arrow_down_light)
//                    }else{
//                        arrowImageView?.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
//                    }
//
//                }
//            }
//            listLayout?.setOnClickListener {
//                if(russianTextView.text==russianText){
//                    chooseLang = "rus"
//                    russianTextView.text = englishText
//                    englishTextView.text = russianText
//                } else if(russianTextView.text==englishText){
//                    chooseLang = "eng"
//                    russianTextView.text = russianText
//                    englishTextView.text = englishText
//                }
//
//            }
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
                                ShowDialogHelper.closeDialogLoadData()
                                ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                                    checkResultSignInUseCase()
                                }
                        }

                        EmailErrorEnum.TIMEOUTERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogTimeOutError(requireContext()){
                                checkResultSignInUseCase()
                            }
                        }

                        EmailErrorEnum.ERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()){
                                checkResultSignInUseCase()
                            }
                        }

                        EmailErrorEnum.WrongAddressEmail -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edEmail,
                                R.drawable.ic_email_error,
                                R.drawable.ic_email,
                                getString(R.string.email_wrong)
                            )
                        }

                        EmailErrorEnum.TooLongPassword -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_too_long)
                            )
                        }

                        EmailErrorEnum.IncorrectPassword -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_incorrect)
                            )
                        }

                        EmailErrorEnum.NULLPOINTERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()){
                                checkResultSignInUseCase()
                            }
                        }

                        EmailErrorEnum.PasswordIsEmpty -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edPassword,
                                R.drawable.ic_lock_error,
                                R.drawable.ic_lock,
                                getString(R.string.password_empty)
                            )
                        }

                        EmailErrorEnum.EmailIsEmpty -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ErrorHelper.showEmailErrorFeedback(
                                requireContext(),
                                edEmail,
                                R.drawable.ic_email_error,
                                R.drawable.ic_email,
                                getString(R.string.email_empty)
                            )

                        }

                        EmailErrorEnum.UNKNOWNERROR -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()){
                                checkResultSignInUseCase()
                            }
                        }

                        else -> {
                            ShowDialogHelper.closeDialogLoadData()
                            ShowDialogHelper.showDialogUnknownError(requireContext()){
                                checkResultSignInUseCase()
                            }
                        }
                    }
                }
        }
    }

    private fun downloadlCourses(lang:String?=null){
            viewModel.loadData({loadState->
                when(loadState){
                    ErrorEnum.NOTNETWORK -> {
                        Log.d("LoadFragmState","NOTNETWORK")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.ERROR -> {
                        Log.d("LoadFragmState","ERROR")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.SUCCESS ->         {
                        Log.d("LoadFragmState","SUCCESS")
                        ShowDialogHelper.closeDialogLoadData()
                        val action =
                            SignInFragmentDirections.actionSignInFragmentToPasswordRecoveryMethodFragment()
                        binding?.root.let { it1 ->
                            if (it1 != null) {
                                Navigation.findNavController(it1).navigate(action)
                            }
                        }
                    }
                    ErrorEnum.UNKNOWNERROR ->          {
                        Log.d("LoadFragmState","UNKNOWNERROR")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.TIMEOUTERROR ->         {
                        Log.d("LoadFragmState","TIMEOUTERROR")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.NULLPOINTERROR ->          {
                        Log.d("LoadFragmState","NULLPOINTERROR")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.OFFLINEMODE ->          {
                        Log.d("LoadFragmState","OFFLINEMODE")
                        ShowDialogHelper.closeDialogLoadData()
                    }
                    ErrorEnum.OFFLINETHEMEBUY ->         {
                        Log.d("LoadFragmState","OFFLINETHEMEBUY")
                        ShowDialogHelper.closeDialogLoadData()
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