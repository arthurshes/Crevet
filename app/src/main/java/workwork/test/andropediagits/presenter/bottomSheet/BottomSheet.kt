package workwork.test.andropediagits.presenter.bottomSheet

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.databinding.FragmentBottomSheetBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.presenter.bottomSheet.viewModels.PremiumBottomSheetViewModel


class BottomSheet : BottomSheetDialogFragment() {

    private val viewModel: PremiumBottomSheetViewModel by viewModels()
    private var binding: FragmentBottomSheetBinding?=null
    private var billingManager: BillingManager?=null
    private var selectedTermPremium = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBottomSheetBinding.inflate(layoutInflater,container,false)
        billingManager = BillingManager(requireActivity() as AppCompatActivity)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       billingManager?.premiumBuyWithTerm = {subscribeTerm->
         viewModel.buySubscribe({state->
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
         },subscribeTerm)
       }
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding?.oneMonthSubscription?.setOnClickListener {

            binding?.oneMonthSubscription?.background = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_subscription_selection)
//            binding?.tvOneMonthPeriod
//            binding?.tvOneMonthMonth
//            binding?.tvOnePrice
//            binding?.tvOneMonthSkid
            if(selectedTermPremium==12){
//                binding?.tvYearPeriod?
//                binding?.tvYear
//                binding?.tvYearPrice
//                binding?.tvYearSkid
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    binding?.oneYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                }else{
                    binding?.oneYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                }

            }
            if(selectedTermPremium==6){
//                binding?.tvHalfPeriod
//                binding?.tvHalfMonth
//                binding?.tvHalfPrice
//                binding?.tvHalfYearSkid
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    binding?.halfYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                }else{
                    binding?.halfYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                }

            }
            selectedTermPremium = 1
        }

         binding?.halfYearSubscription?.setOnClickListener {

             binding?.halfYearSubscription?.background = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_subscription_selection)
             if(selectedTermPremium==1){
                 if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                     binding?.oneMonthSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                 }else{
                     binding?.oneMonthSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                 }

             }
             if(selectedTermPremium==12){
                 if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                     binding?.oneYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                 }else{
                     binding?.oneYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                 }

             }
             selectedTermPremium = 6
         }

        binding?.oneYearSubscription?.setOnClickListener {
            binding?.oneYearSubscription?.background = ContextCompat.getDrawable(requireContext(),R.drawable.gradient_subscription_selection)
            if(selectedTermPremium==6){
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    binding?.halfYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                }else{
                    binding?.halfYearSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                }

            }
            if(selectedTermPremium==1){
                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                    binding?.oneMonthSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionNight))
                }else{
                    binding?.oneMonthSubscription?.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.subscriptionLight))
                }

            }
            selectedTermPremium = 12
        }

        binding?.btnPayGoogle?.setOnClickListener {
           if(selectedTermPremium==1){
               billingManager?.billingSetup(PayState.PREMIUMONEMOUNTHBUY)
           }
            if(selectedTermPremium==6){
                billingManager?.billingSetup(PayState.PREMIUMSIXMOUNTHBUY)
            }
            if(selectedTermPremium==12){
                billingManager?.billingSetup(PayState.PREMIUMYEARBUY)
            }
        }


    }

}