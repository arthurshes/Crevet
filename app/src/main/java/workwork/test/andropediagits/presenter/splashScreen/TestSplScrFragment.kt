package workwork.test.andropediagits.presenter.splashScreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation



import dagger.hilt.android.AndroidEntryPoint

import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.SplashActionEnum
import workwork.test.andropediagits.databinding.FragmentTestSplScrBinding
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper
import workwork.test.andropediagits.presenter.signIn.SignInFragmentDirections
import workwork.test.andropediagits.presenter.splashScreen.viewmodel.SplashScreenViewModel

@AndroidEntryPoint
class TestSplScrFragment : Fragment() {

    private val viewModel: SplashScreenViewModel by viewModels()
    private var binding: FragmentTestSplScrBinding?=null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentTestSplScrBinding.inflate(inflater, container, false)
        splashScreenTreatmentResult()
        return binding?.root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun splashScreenTreatmentResult() {

         viewModel.checkCacheActual { checkResult->
             Log.d("sppspsp",checkResult.toString())
             when(checkResult){
                 ErrorEnum.NOTNETWORK -> {
                     ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                         splashScreenTreatmentResult()
                     }
                 }
                 ErrorEnum.ERROR -> {
                     requireActivity().runOnUiThread {
                         ShowDialogHelper.showDialogUnknownError(requireContext()) {
                             splashScreenTreatmentResult()
                         }

                     }
                 }
                 ErrorEnum.SUCCESS -> {
                     viewModel.checkSplashScreen { resultSplashScreen->

                         when (resultSplashScreen) {
                             SplashActionEnum.SIGNINSCREEN -> {
                                 requireActivity().runOnUiThread {
                                     val action = TestSplScrFragmentDirections.actionTestSplScrFragmentToSignInFragment()
                                     binding?.root?.let { it1 ->
                                         Navigation.findNavController(it1).navigate(action)
                                     }

                                 }

                             }

                             SplashActionEnum.RESETDATASCREEN ->{
                                 val action =
                                     TestSplScrFragmentDirections.actionTestSplScrFragmentToPasswordRecoveryMethodFragment()
                                 binding?.root.let { it1 ->
                                     if (it1 != null) {
                                         Navigation.findNavController(it1).navigate(action)
                                     }
                                 }
//                                 val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
//                                 binding?.root?.let { it1 ->
//                                     Navigation.findNavController(it1).navigate(action)
//                                 }
//                                 Toast.makeText(requireContext(), "ре2222г", Toast.LENGTH_LONG).show()
                             }

                             SplashActionEnum.HOMESCREEN -> {
                                 requireActivity().runOnUiThread {
                                     val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                                     binding?.root?.let { it1 ->
                                         Navigation.findNavController(it1).navigate(action)
                                     }

//                            val fragment = CoursesFragment()
//                            supportFragmentManager.beginTransaction()
//                                .replace(R.id.fragmentCourses, fragment).commit()

                                 }


                             }

                             SplashActionEnum.OFFLINEMODE -> {
                                 requireActivity().runOnUiThread {
                                     Toast.makeText(requireContext(), "offline", Toast.LENGTH_LONG).show()
                                 }



                             }

                             SplashActionEnum.ERRORSCREEN -> {
                                 requireActivity().runOnUiThread {
                                     ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                         splashScreenTreatmentResult()
                                     }

                                 }



                             }

                             SplashActionEnum.LONGWAITSERVER -> {
                                 requireActivity().runOnUiThread {
                                     ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                                         splashScreenTreatmentResult()
                                     }

                                 }

                             }

                             SplashActionEnum.TRYAGAINSNACk -> {
                                 requireActivity().runOnUiThread {
                                     ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                         splashScreenTreatmentResult()
                                     }

                                 }


                             }

                             else -> {
                                 requireActivity().runOnUiThread {
                                     ShowDialogHelper.showDialogUnknownError(requireContext()) {
                                         splashScreenTreatmentResult()
                                     }


                                 }


                             }
                         }
                     }
                 }
                 ErrorEnum.UNKNOWNERROR -> {
                     ShowDialogHelper.showDialogUnknownError(requireContext()) {
                         splashScreenTreatmentResult()
                     }
                 }
                 ErrorEnum.TIMEOUTERROR -> {
                     ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                         splashScreenTreatmentResult()
                     }
                 }
                 ErrorEnum.NULLPOINTERROR -> {
                     ShowDialogHelper.showDialogUnknownError(requireContext()) {
                         splashScreenTreatmentResult()
                     }
                 }
                 ErrorEnum.OFFLINEMODE -> {
                     Toast.makeText(requireContext(), "offline", Toast.LENGTH_LONG).show()
                 }
                 ErrorEnum.OFFLINETHEMEBUY -> {
                     Toast.makeText(requireContext(), "offlinetheme", Toast.LENGTH_LONG).show()
                 }
             }
         }

    }



}