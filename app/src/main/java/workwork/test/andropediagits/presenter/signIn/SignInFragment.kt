package workwork.test.andropediagits.presenter.signIn

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.EmailErrorEnum
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.databinding.FragmentSignInBinding
import workwork.test.andropediagits.presenter.lesson.utils.ErrorHelper
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.signIn.viewModel.SignInViewModel

@AndroidEntryPoint
class SignInFragment : Fragment() {
    private var backPressedOnce = false
    private var nextCourse = false
    private var googleSignIn = false
    private var binding: FragmentSignInBinding? = null
    private var auth:FirebaseAuth?=null
    private var googleSignInClient:GoogleSignInClient?=null
    private var gso:GoogleSignInOptions?=null
    // private var selectedImage: Uri? = null

    private var selectedImageBitmap: Bitmap? = null
    private val viewModel: SignInViewModel by viewModels()
    private var chooseLang:String = "rus"
    private val russianText = "🇷🇺    Рус"
    private val englishText = "🇺🇸    Eng"

//    var lunchActGoogle =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            var result: ErrorEnum?=null
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    val user = UserSignInModel(
//                        "${account.displayName}",
//
//                       token= account.idToken.toString()
//                    )
//                    viewModel.getCurrentTime {currentTime->
//                        val userLocal = UserInfoEntity(name="${account.displayName}", lastOnlineDate = currentTime, token = account.idToken.toString(), userLanguage = chooseLang)
//                        saveUserInfoTreatmentResult(userLocal)
//                    }
//                    ShowDialogHelper.showDialogLoadData(requireContext())
//
//                }
//            } catch (e: ApiException) {
//                ShowDialogHelper.showDialogUnknownError(requireContext(),{
//
//                }) {
//                    binding?.dimViewSignIng?.visibility = View.GONE
//                    //////////////////////////////////////////////////////////
//                }
//                Log.d("googleJackanfojrofj32",e.toString())
//            }
//
//            }

    private fun saveUserInfoTreatmentResult(userLocal: UserInfoEntity) {
        viewModel.saveUserInfo(userLocal,
            {
                when(it){
                    ErrorEnum.SUCCESS -> {
                        if(!nextCourse) {
                            nextCourse = true
                            downloadlCourses(chooseLang,userLocal.token)
                        }
                    }
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
    ,chooseLang)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = FirebaseAuth.getInstance()
      gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.defaul_web_client_id))
           .requestEmail()
           .build()
        gso?.let {
            googleSignInClient = GoogleSignIn.getClient(requireContext(),it)
        }

        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("ResourceType", "SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    requireActivity().finish()
                } else {
                    backPressedOnce = true
                    Toast.makeText(requireContext(), R.string.back_press_again_exit, Toast.LENGTH_SHORT).show()
                    Handler(Looper.getMainLooper()).postDelayed({ backPressedOnce = false }, 2000)

                }

            }}
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
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
            btnSignGoogle?.setOnClickListener {
                signIngGoogle()
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

   private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){

//       val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
//            var result: ErrorEnum?=null
//            try {
//                val account = task.getResult(ApiException::class.java)
//                if (account != null) {
//                    viewModel.getCurrentTime {currentTime->
//                        val userLocal = UserInfoEntity(name="${account.displayName}", lastOnlineDate = currentTime, token = account.idToken.toString(), userLanguage = chooseLang)
//                        saveUserInfoTreatmentResult(userLocal)
//                    }
//                    ShowDialogHelper.showDialogLoadData(requireContext())
//
//                }
//            } catch (e: ApiException) {
//                ShowDialogHelper.showDialogUnknownError(requireContext(),{
//
//                }) {
//                    binding?.dimViewSignIng?.visibility = View.GONE
//                    //////////////////////////////////////////////////////////
//                }
//                Log.d("googleJackanfojrofj32",e.toString())
//            }
//
        if(it.resultCode==Activity.RESULT_OK){
            val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            handRes(task)
        }
    }

    private fun signIngGoogle(){
        val signInIntent = googleSignInClient?.signInIntent
       launcher.launch(signInIntent)
    }

    private fun handRes(task: Task<GoogleSignInAccount>) {
       if(task.isSuccessful){
           val accout:GoogleSignInAccount? = task.result
           if(accout!=null){
               viewModel.getCurrentTime {currentTime->

                   val userLocal = UserInfoEntity(name="${accout.displayName}", lastOnlineDate = currentTime, token =  accout.id.toString(), userLanguage = chooseLang)
                   saveUserInfoTreatmentResult(userLocal)
               }
               googleSignIn = true
//               if(!nextCourse){
//                   nextCourse=true
//                   downloadlCourses(chooseLang,accout.idToken.toString())
//               }


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
        var token = ""
        var isRegisters = false
        binding?.apply {

            viewModel.signInEmail(edEmail.text.toString(), edPassword.text.toString().trimEnd(),lang = chooseLang,{ isRegister->
                isRegisters = isRegister
            },{

                    emailResult->
                when (emailResult) {
                    EmailErrorEnum.SUCCESS -> {
                        if(!nextCourse) {
                            nextCourse  =true
                            downloadlCourses(chooseLang,token)
                        }
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
            },{
                token = it
            })
        }
    }

    private fun downloadlCourses(lang:String?=null,token:String?=null){

        ShowDialogHelper.showDialogLoadData(requireContext())
        viewModel.loadData({loadState->
            Log.d("latest30Nov",loadState.toString())
            when(loadState) {
                ErrorEnum.SUCCESS -> {
                    viewModel.checkSubscribesAndBuyCourse {checkState->
                        Log.d("latest30Nov",checkState.toString())
                        when(checkState){
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
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
                                    ShowDialogHelper.closeDialogLoadData()
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

                                    if(googleSignIn){
                                        ShowDialogHelper.closeDialogLoadData()

                                            val action = SignInFragmentDirections.actionSignInFragmentToCoursesFragment(true)
                                            binding?.root.let { it1 ->
                                                if (it1 != null) {
                                                    Navigation.findNavController(it1).navigate(action)
                                                }
                                            }
                                    }else{
                                        ShowDialogHelper.closeDialogLoadData()


                                            val action =
                                                SignInFragmentDirections.actionSignInFragmentToPasswordRecoveryMethodFragment()
                                            binding?.root.let { it1 ->
                                                if (it1 != null) {
                                                    Navigation.findNavController(it1)
                                                        .navigate(action)
                                                }
                                            }

                                    }

                                }
                            }
                            ErrorEnum.UNKNOWNERROR -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
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
                                    ShowDialogHelper.closeDialogLoadData()
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
                                    ShowDialogHelper.closeDialogLoadData()
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
                                    ShowDialogHelper.closeDialogLoadData()
                                    ShowDialogHelper.showDialogOffline(requireContext())
                                }
                            }
                            ErrorEnum.OFFLINETHEMEBUY -> {
                                requireActivity().runOnUiThread {
                                    ShowDialogHelper.closeDialogLoadData()
                                    ShowDialogHelper.showDialogOffline(requireContext())
                                }
                            }
                        }
                    }
                    Log.d("LoadFragmState", "SUCCESS")

                }

                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
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
                        ShowDialogHelper.closeDialogLoadData()
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
                        ShowDialogHelper.closeDialogLoadData()
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
                        ShowDialogHelper.closeDialogLoadData()
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
                        ShowDialogHelper.closeDialogLoadData()
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
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }

                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.closeDialogLoadData()
                        ShowDialogHelper.showDialogOffline(requireContext())
                    }
                }
            }
        },lang,token)
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