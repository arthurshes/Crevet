package workwork.test.andropediagits.presenter.bottomSheet

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import workwork.test.andropediagits.presenter.lesson.utils.ShowDialogHelper


class BottomSheet : BottomSheetDialogFragment() {
    private val viewModel: PremiumBottomSheetViewModel by viewModels()
    private var binding: FragmentBottomSheetBinding?=null
    private var billingManager: BillingManager?=null
    private var selectedTermPremium = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBottomSheetBinding.inflate(layoutInflater,container,false)
        billingManager = BillingManager(requireActivity() as AppCompatActivity)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       billingManager?.premiumBuyWithTerm = {subscribeTerm->
           buySubscribeTreatmentResult(subscribeTerm)
       }
        val currentTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        binding?.oneMonthSubscription?.let { setSubscriptionOnClickListeners(it, 1,currentTheme) }
        binding?.halfYearSubscription?.let { setSubscriptionOnClickListeners(it, 6,currentTheme) }
        binding?.oneYearSubscription?.let { setSubscriptionOnClickListeners(it, 12,currentTheme) }

        binding?.btnPayGoogle?.setOnClickListener {
            when (selectedTermPremium) {
                1 -> billingManager?.billingSetup(PayState.PREMIUMONEMOUNTHBUY)
                6 -> billingManager?.billingSetup(PayState.PREMIUMSIXMOUNTHBUY)
                12 -> billingManager?.billingSetup(PayState.PREMIUMYEARBUY)
            }
        }
    }

    private fun buySubscribeTreatmentResult(subscribeTerm: Int) {
        viewModel.buySubscribe({state->
            when(state){
                ErrorEnum.SUCCESS -> {
                    Toast.makeText(requireContext(),getString(R.string.subscription_was_successfully_purchased), Toast.LENGTH_SHORT).show()
                }
                ErrorEnum.NOTNETWORK -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogNotNetworkError(requireContext()) {
                            buySubscribeTreatmentResult(subscribeTerm)
                        }
                    }
                }
                ErrorEnum.ERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buySubscribeTreatmentResult(subscribeTerm)
                        }
                    }
                }
                ErrorEnum.UNKNOWNERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buySubscribeTreatmentResult(subscribeTerm)
                        }
                    }
                }
                ErrorEnum.TIMEOUTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogTimeOutError(requireContext()) {
                            buySubscribeTreatmentResult(subscribeTerm)
                        }
                    }
                }
                ErrorEnum.NULLPOINTERROR -> {
                    requireActivity().runOnUiThread {
                        ShowDialogHelper.showDialogUnknownError(requireContext()) {
                            buySubscribeTreatmentResult(subscribeTerm)
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
        },subscribeTerm)
    }
    fun setSubscriptionOnClickListeners(selectedView: View, termPremium: Int, currentTheme: Int) {
        selectedView.setOnClickListener {
            selectedView.background = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_subscription_selection)

            val otherViews = arrayOf(binding?.oneMonthSubscription, binding?.halfYearSubscription, binding?.oneYearSubscription)
            val themeColorRes = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) R.color.subscriptionNight else R.color.subscriptionLight

            otherViews.forEach { view ->
                if (view != selectedView) {
                    view?.setBackgroundColor(ContextCompat.getColor(requireContext(), themeColorRes))
                }
            }

            selectedTermPremium = termPremium
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}