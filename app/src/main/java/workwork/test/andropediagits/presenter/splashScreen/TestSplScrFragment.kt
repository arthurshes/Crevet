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
        binding = FragmentTestSplScrBinding.inflate(inflater, container, false)
        splashScreenTreatmentResult()
        return binding?.root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun splashScreenTreatmentResult() {

        viewModel.checkCacheActual { checkResult->
            Log.d("sppspsp",checkResult.toString())
            when(checkResult){
                ErrorEnum.SUCCESS -> {
                    viewModel.checkSubscribeActual{state->
                        when(state){
                            ErrorEnum.NOTNETWORK -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSplash?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                                        splashScreenTreatmentResult()
                                    }) {

                                        binding?.dimViewSplash?.visibility = View.GONE
                                    }
                                }

                            }
                            ErrorEnum.ERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSplash?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        splashScreenTreatmentResult()
                                    }) {
                                        binding?.dimViewSplash?.visibility = View.GONE
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
                                            requireActivity().runOnUiThread {
                                                val action = TestSplScrFragmentDirections.actionTestSplScrFragmentToPasswordRecoveryMethodFragment()
                                                binding?.root.let { it1 ->
                                                    if (it1 != null) {
                                                        Navigation.findNavController(it1).navigate(action)
                                                    }
                                                }
                                            }

                                        }
                                        SplashActionEnum.HOMESCREEN -> {
                                            requireActivity().runOnUiThread {
                                                val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                                                binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                                            }
                                        }
                                        SplashActionEnum.OFFLINEMODE -> {
                                            requireActivity().runOnUiThread {
                                                val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                                                binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                                            }
                                        }
                                        SplashActionEnum.ERRORSCREEN -> {
                                            requireActivity().runOnUiThread {
                                                binding?.dimViewSplash?.visibility = View.VISIBLE
                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                    splashScreenTreatmentResult()
                                                }) {

                                                    binding?.dimViewSplash?.visibility = View.GONE
                                                }

                                            }
                                        }
                                        SplashActionEnum.LONGWAITSERVER -> {
                                            requireActivity().runOnUiThread {
                                                binding?.dimViewSplash?.visibility = View.VISIBLE
                                                ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                                    splashScreenTreatmentResult()
                                                }) {

                                                    binding?.dimViewSplash?.visibility = View.GONE
                                                }
                                            }
                                        }

                                        SplashActionEnum.TRYAGAINSNACk -> {
                                            requireActivity().runOnUiThread {
                                                binding?.dimViewSplash?.visibility = View.VISIBLE
                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                    splashScreenTreatmentResult()
                                                }) {

                                                    binding?.dimViewSplash?.visibility = View.GONE
                                                }

                                            }
                                        }
                                        ///
                                        else -> {
                                            requireActivity().runOnUiThread {
                                                ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                                    splashScreenTreatmentResult()
                                                }) {
                                                    binding?.dimViewSplash?.visibility = View.GONE
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            ErrorEnum.UNKNOWNERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSplash?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        splashScreenTreatmentResult()
                                    }) {

                                        binding?.dimViewSplash?.visibility = View.GONE
                                    }
                                }

                            }
                            ErrorEnum.TIMEOUTERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSplash?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                                        splashScreenTreatmentResult()
                                    }) {

                                        binding?.dimViewSplash?.visibility = View.GONE
                                    }
                                }

                            }
                            ErrorEnum.NULLPOINTERROR -> {
                                requireActivity().runOnUiThread {
                                    binding?.dimViewSplash?.visibility = View.VISIBLE
                                    ShowDialogHelper.showDialogUnknownError(requireContext(),{
                                        splashScreenTreatmentResult()
                                    }) {

                                        binding?.dimViewSplash?.visibility = View.GONE
                                    }
                                }

                            }
                            ErrorEnum.OFFLINEMODE -> {
                                requireActivity().runOnUiThread {
                                    val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                                    binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                                }
                            }
                            ErrorEnum.OFFLINETHEMEBUY -> {
                                requireActivity().runOnUiThread {
                                    val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                                    binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                                }
                            }
                        }
                    }
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSplash?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogNotNetworkError(requireContext(),{
                            splashScreenTreatmentResult()
                        }) {

                            binding?.dimViewSplash?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSplash?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            splashScreenTreatmentResult()
                        }) {

                            binding?.dimViewSplash?.visibility = View.GONE
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSplash?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            splashScreenTreatmentResult()
                        }) {

                            binding?.dimViewSplash?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSplash?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogTimeOutError(requireContext(),{
                            splashScreenTreatmentResult()
                        }) {

                            binding?.dimViewSplash?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        binding?.dimViewSplash?.visibility = View.VISIBLE
                        ShowDialogHelper.showDialogUnknownError(requireContext(),{
                            splashScreenTreatmentResult()
                        }) {

                            binding?.dimViewSplash?.visibility = View.GONE
                        }
                    }

                }
                ErrorEnum.OFFLINEMODE ->{
                    requireActivity().runOnUiThread {
                        val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                        binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                    }
                }
                ErrorEnum.OFFLINETHEMEBUY -> {
                    requireActivity().runOnUiThread {
                        val action =TestSplScrFragmentDirections.actionTestSplScrFragmentToCoursesFragment(false)
                        binding?.root?.let { it1 -> Navigation.findNavController(it1).navigate(action) }
                    }
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}