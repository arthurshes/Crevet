package workwork.test.andropediagits.presenter.lesson.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner

import workwork.test.andropediagits.R
import workwork.test.andropediagits.databinding.StrikeModeDialogBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.presenter.reset.DatePickerFragment

object ShowDialogHelper {
   private var dialog: Dialog? = null
   private var isDialogStrikeShow = false
    private var isDialogSuccess = false
    fun showDialogUnknownError(
        context: Context,
        pressButton: (() -> Unit)
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.unknown_error_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        val dialogButton = dialog?.findViewById<CardView>(R.id.btnTryAgainUnknown)
        dialogButton?.setOnClickListener {
            pressButton.invoke()
            dialog?.dismiss()
            dialog=null
        }
        dialog?.show()
    }

    fun showDialogTimeOutError(
        context: Context,
        pressButton: (() -> Unit)
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.time_out_error_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        val dialogButton = dialog?.findViewById<CardView>(R.id.btnTryAgainTimeOut)
        dialogButton?.setOnClickListener {
            pressButton.invoke()
            dialog?.dismiss()
            dialog=null
        }
        dialog?.show()
    }

    fun showDialogNotNetworkError(
        context: Context,
        pressButton: (() -> Unit)
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.not_network_error_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        val dialogButton = dialog?.findViewById<CardView>(R.id.btnTryAgainInternet)
        dialogButton?.setOnClickListener {
            pressButton?.invoke()
            dialog?.dismiss()
            dialog = null
        }
        dialog?.show()
    }  fun showDialogOffline(
        context: Context,
        okClick:(()->Unit)?=null,
        close:(()->Unit)?=null
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.offline_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val tvNotAvailableSmAndPurchases = dialog?.findViewById<TextView>(R.id.tvNotAvailableSmAndPurchases)
        val buttonGood = dialog?.findViewById<CardView>(R.id.btnCloseOfflineDialog)
        val text = context.getString(R.string.shock_mode_and_purchases_will_not_be_available)


        val keyword1 = "Ударный режим"
        val keyword2 = "покупки"

        val spannable = SpannableStringBuilder(text)

        val colorSpan1 = ForegroundColorSpan(ContextCompat.getColor(context,R.color.error))
        val styleSpanBold1 = StyleSpan(Typeface.BOLD)

        val colorSpan2 = ForegroundColorSpan(ContextCompat.getColor(context,R.color.error))
        val styleSpanBold2 = StyleSpan(Typeface.BOLD)

        val start1 = text.indexOf(keyword1)
        val start2 = text.indexOf(keyword2)
        buttonGood?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            okClick?.invoke()
        }
        if (start1 != -1) {
            spannable.setSpan(colorSpan1, start1, start1 + keyword1.length, 0)
            spannable.setSpan(styleSpanBold1, start1, start1 + keyword1.length, 0)
        }

        if (start2 != -1) {
            spannable.setSpan(colorSpan2, start2, start2 + keyword2.length, 0)
            spannable.setSpan(styleSpanBold2, start2, start2 + keyword2.length, 0)
        }
        tvNotAvailableSmAndPurchases?.text = spannable
        dialog?.show()
        dialog?.setOnDismissListener {
            close?.invoke()
        }
    }

    fun showDialogStrikeMode(
        context: Context,
        resultStrikeModeDay: Int,
        layoutInflater: LayoutInflater,
        isSubscribe:Boolean,
        isClose:(()->Unit),
        isPremium:(()->Unit)
    ) {

        if(!isDialogStrikeShow) {
            isDialogStrikeShow = true
            dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            var binding = StrikeModeDialogBinding.inflate(layoutInflater)
            dialog?.setContentView(binding.root)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            strikeModeDayTreatmentResult(resultStrikeModeDay, binding, context,isSubscribe)
            binding?.closeStrikeBtn?.setOnClickListener {
                isDialogStrikeShow = false
                dialog?.dismiss()
                dialog = null
                isClose.invoke()
            }
            binding?.btnPremiumStrikeMode?.setOnClickListener {
                isDialogStrikeShow = false
                dialog?.dismiss()
                dialog = null
                isPremium?.invoke()
            }
            dialog?.show()
        }
    }

    fun showDialogLoadData(
        context: Context
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setCancelable(false)
        dialog?.setContentView(R.layout.load_data_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.show()
    }
    fun closeDialogLoadData() {
        dialog?.dismiss()
        dialog=null
    }

    private fun strikeModeDayTreatmentResult(
        resultStrikeModeDay: Int,
        binding: StrikeModeDialogBinding,
        context: Context,
        isSubscribe:Boolean
    ) {
        val drawableCurrentDay = AppCompatResources.getDrawable(context, R.drawable.border_current_day)
        binding.apply {
            if(isSubscribe){
                oneDayAndropointsText.text = "+2"
                twoDayAndropointsText.text = "+4"
                threeDayAndropointsText.text = "+6"
                fourDayAndropointsText.text = "+8"
                fiveDayAndropointsText.text = "+16"
                sixDayAndropointsText.text = "+24"
                sevenDayAndropointsText.text = "+34"
            }else{
                oneDayAndropointsText.text = "+1"
                twoDayAndropointsText.text = "+2"
                threeDayAndropointsText.text = "+3"
                fourDayAndropointsText.text = "+4"
                fiveDayAndropointsText.text = "+8"
                sixDayAndropointsText.text = "+12"
                sevenDayAndropointsText.text = "+17"
            }
            when (resultStrikeModeDay) {
                1 -> {
                    currentDayOne.background = drawableCurrentDay
                }

                2 -> {
                    currentDayTwo.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                }

                3 -> {
                    currentDayThree.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    pastDayTwo.cardPastDay.visibility = View.VISIBLE
                    pastDayTwo.tvPastDay.text=context.getString(R.string.day_two)
                }

                4 -> {
                    currentDayFour.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    pastDayTwo.cardPastDay.visibility = View.VISIBLE
                    pastDayTwo.tvPastDay.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    pastDayThree.cardPastDay.visibility = View.VISIBLE
                    pastDayThree.tvPastDay.text=context.getString(R.string.day_three)
                }

                5 -> {
                    currentDayFive.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    pastDayTwo.cardPastDay.visibility = View.VISIBLE
                    pastDayTwo.tvPastDay.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    pastDayThree.cardPastDay.visibility = View.VISIBLE
                    pastDayThree.tvPastDay.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    pastDayFour.cardPastDay.visibility = View.VISIBLE
                    pastDayFour.tvPastDay.text=context.getString(R.string.day_four)
                }

                6 -> {
                    currentDaySix.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    pastDayTwo.cardPastDay.visibility = View.VISIBLE
                    pastDayTwo.tvPastDay.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    pastDayThree.cardPastDay.visibility = View.VISIBLE
                    pastDayThree.tvPastDay.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    pastDayFour.cardPastDay.visibility = View.VISIBLE
                    pastDayFour.tvPastDay.text=context.getString(R.string.day_four)
                    cardDayFive.visibility = View.GONE
                    pastDayFive.cardPastDay.visibility = View.VISIBLE
                    pastDayFive.tvPastDay.text=context.getString(R.string.day_five)
                }

                7 -> {
                    currentDaySeven.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    pastDayOne.cardPastDay.visibility = View.VISIBLE
                    pastDayOne.tvPastDay.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    pastDayTwo.cardPastDay.visibility = View.VISIBLE
                    pastDayTwo.tvPastDay.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    pastDayThree.cardPastDay.visibility = View.VISIBLE
                    pastDayThree.tvPastDay.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    pastDayFour.cardPastDay.visibility = View.VISIBLE
                    pastDayFour.tvPastDay.text=context.getString(R.string.day_four)
                    cardDayFive.visibility = View.GONE
                    pastDayFive.cardPastDay.visibility = View.VISIBLE
                    pastDayFive.tvPastDay.text=context.getString(R.string.day_five)
                    cardDaySix.visibility = View.GONE
                    pastDaysix.cardPastDay.visibility = View.VISIBLE
                    pastDaysix.tvPastDay.text=context.getString(R.string.day_six)
                }
            }
        }

    }


    fun showDialogAttention(context: Context, pressButton: () -> Unit) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.attention_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogButton = dialog?.findViewById<CardView>(R.id.btnGood)
        dialogButton?.setOnClickListener {
            pressButton.invoke()
            dialog?.dismiss()
            dialog = null
        }
        dialog?.show()
    }

    fun showDialogAttentionStrikeMode(context: Context, pressButton: (() -> Unit),isClose:(()->Unit)) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.attention_strike_mode_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogButton = dialog?.findViewById<CardView>(R.id.btnTryAgainStrikeMode)
        val dialogClose = dialog?.findViewById<CardView>(R.id.btnOkStrikeMode)
        dialogButton?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            pressButton.invoke()
        }
        dialogClose?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            isClose.invoke()
        }
        dialog?.show()

    }

    fun showDialogSuccessTest(context: Context, close: () -> Unit) {
        if(!isDialogSuccess){
            isDialogSuccess = true
            dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog?.setContentView(R.layout.test_success_dialog)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val dialogButton = dialog?.findViewById<CardView>(R.id.btnContinue)
            val addTextAndropoints = dialog?.findViewById<TextView>(R.id.textViewAddAndropointsSuccess)
            dialogButton?.isClickable = false
            var buttonEnable = false
            var buttonClick = false
            dialogButton?.animate()
                ?.alpha(1f)
                ?.setDuration(3000)
                ?.withEndAction {
                    dialogButton?.isClickable = true
                    buttonEnable = true

                }
            dialogButton?.setOnClickListener {
                if(buttonEnable&&!buttonClick){
                    buttonClick = true
                    dialog?.dismiss()
                    dialog = null
                    close.invoke()
                }
            }
            dialog?.show()
        }

    }

    fun showDialogFailTest(context: Context, correctTest:Int,mistakeTest: Int, size: Int, dateTerm: String,isClose:(()->Unit),isTimerOut:Boolean) {
       dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.test_fail_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogCountMistakes = dialog?.findViewById<TextView>(R.id.tvCountMistake)
        val dialogTerm = dialog?.findViewById<TextView>(R.id.term_fail_test)
        val dialogClose = dialog?.findViewById<ImageButton>(R.id.buttonLockFailDialog)
        dialogClose?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            isClose.invoke()
        }
        var misstakes = 0
        misstakes = if(isTimerOut){
            val mistaketimer = size - (correctTest+mistakeTest)
            mistaketimer + mistakeTest
        }else{
            mistakeTest
        }
        Log.d("misstakeDialog","size:${size} mistakeTest:${misstakes} dateTerm:${dateTerm}")
        val replacementMap = mapOf(
            "6" to (size - misstakes).toString(),
            "10" to size.toString()
        )
        val replacementDatesMap = mapOf(
            "DATA" to dateTerm
        )
        Log.d("misstakeDialog","replacementMap:${replacementMap}")
        dialogCountMistakes?.text = replaceNumbersWithStrings(context.getString(R.string.count_mistake), replacementMap)
        dialogTerm?.text = replaceNumbersWithStrings(
            context.getString(R.string.term_before_fail_test),
            replacementDatesMap
        )
        dialog?.show()

    }

    fun showDialogTerm(context: Context, dateTerm: String) {
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.text_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogTermDescription = dialog?.findViewById<TextView>(R.id.tvTermDescription)
        val replacementMap = mapOf(
            "DATA" to dateTerm,
        )
        dialogTermDescription?.text =
            replaceNumbersWithStrings(
                context.getString(R.string.term_description),
                replacementMap
            )
        dialog?.show()

    }

    fun showDialogBuyAndropoints(
        context: Context,
        watchAd: () -> Unit,
        pay: () -> Unit,
        money: (Int) -> Unit,
        andropoints: (Int) -> Unit
    ) {
        dialog = Dialog(context)
        dialog?.setContentView(R.layout.buy_andropoints_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val btnPay = dialog?.findViewById<CardView>(R.id.btnPay)
        val tvLastPayCost = dialog?.findViewById<TextView>(R.id.tvLastPayCost)
        val cardBuyOneAndropoint = dialog?.findViewById<LinearLayout>(R.id.cardBuyOneAndropoint)
        val cardBuyTenAndropoints = dialog?.findViewById<LinearLayout>(R.id.cardBuyTenAndropoints)
        val cardBuyOneHundredAndropoints = dialog?.findViewById<LinearLayout>(R.id.cardBuyOneHundredAndropoints)
        val cardBuyInfinityAndropoints = dialog?.findViewById<LinearLayout>(R.id.cardBuyInfinityAndropoints)
        val tvLastCountAndropoint = dialog?.findViewById<TextView>(R.id.tvLastCountAndropoint)
        val tvCountAndropointsBuyOneAndropoint = dialog?.findViewById<TextView>(R.id.tvCountAndropointsBuyOneAndropoint)
        val tvCountMoneyBuyOneAndropoint = dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyOneAndropoint)
        val tvCountAndropointsBuyTenAndropoint = dialog?.findViewById<TextView>(R.id.tvCountAndropointsBuyTenAndropoint)
        val tvCountMoneyBuyTenAndropoint = dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyTenAndropoint)
        val tvCountAndropointsOneHundredAndropoint = dialog?.findViewById<TextView>(R.id.tvCountAndropointsOneHundredAndropoint)
        val tvCountMoneyBuyOneHundredAndropoint = dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyOneHundredAndropoint)
        val tvCountAndropointsInfinityAndropoint = dialog?.findViewById<TextView>(R.id.tvCountAndropointsInfinityAndropoint)
        val tvCountMoneyBuyInfinityAndropoint = dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyInfinityAndropoint)
        val btnWatchAd = dialog?.findViewById<CardView>(R.id.btnWatchAd)
            cardBuyOneAndropoint?.setOnClickListener {
                tvLastCountAndropoint?.text = tvCountAndropointsBuyOneAndropoint?.text
                tvLastPayCost?.text = tvCountMoneyBuyOneAndropoint?.text
            }
            cardBuyTenAndropoints?.setOnClickListener {
                tvLastCountAndropoint?.text = tvCountAndropointsBuyTenAndropoint?.text
                tvLastPayCost?.text = tvCountMoneyBuyTenAndropoint?.text
            }
            cardBuyOneHundredAndropoints?.setOnClickListener {
                tvLastCountAndropoint?.text = tvCountAndropointsOneHundredAndropoint?.text
                tvLastPayCost?.text = tvCountMoneyBuyOneHundredAndropoint?.text
            }
            cardBuyInfinityAndropoints?.setOnClickListener {
                tvLastCountAndropoint?.text = tvCountAndropointsInfinityAndropoint?.text
                tvLastPayCost?.text = tvCountMoneyBuyInfinityAndropoint?.text
            }
            btnPay?.setOnClickListener {
                money.invoke(tvLastPayCost?.text.toString().replace("₽", "").toInt())
                andropoints.invoke(
                    tvLastCountAndropoint?.text.toString().replace("₽", "").toInt()
                )
                pay.invoke()
                dialog = null
            }
            btnWatchAd?.setOnClickListener {
                dialog?.dismiss()
                dialog = null
                watchAd.invoke()
            }

        dialog?.show()
    }

    fun showDialogClue(context: Context, text: String,close:(()->Unit)) {
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.text_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogTermDescription = dialog?.findViewById<TextView>(R.id.tvClueDescription)
         val btnClose = dialog?.findViewById<CardView>(R.id.btnCloseClueDialog)
        btnClose?.setOnClickListener {
            dialog.dismiss()
            close?.invoke()
        }
        dialogTermDescription?.text = text
        dialog?.show()
        dialog?.setOnDismissListener {
            close?.invoke()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun showDialogClose(
        context: Context,
        buyCourse: (() -> Unit)? = null,
        dialogDissMiss:(()->Unit),
        themeBuy: Boolean = false,
        themeClose:Boolean = false
    ) {
      dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.close_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogBuyCourse =  dialog?.findViewById<CardView>(R.id.btnBuyCourse)
        val tvCloseText =  dialog?.findViewById<TextView>(R.id.tvCloseText)
        val tvBuy =  dialog?.findViewById<TextView>(R.id.tvBuy)

        if (themeClose) {
            dialogBuyCourse?.visibility=View.GONE
            tvCloseText?.text = context.getString(R.string.theme_close_entirely)
        }
        if (themeBuy) {
            tvBuy?.text = context.getString(R.string.buy_theme)
            tvCloseText?.text = context.getString(R.string.theme_close)
        }else{
            tvBuy?.text = context.getString(R.string.buy_course)
            tvCloseText?.text = context.getString(R.string.course_close)
        }

        dialogBuyCourse?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            buyCourse?.invoke()
        }
        dialog?.setOnDismissListener {
            dialogDissMiss?.invoke()
        }
        dialog?.show()
    }

    fun showDialogBuy(
        context: Context,
        priceRub:Int,
        priceAndropoint:Int?=null,
        pressGoogle: () -> Unit,
        pressAndro: () -> Unit,
        dialogDissMiss:(()->Unit)
    ) {
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.buy_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        val btnTinkoffBuy = dialog?.findViewById<ImageView>(R.id.btnTinkoffBuy)
        val btnGoogleBuy = dialog?.findViewById<LinearLayout>(R.id.btnGoogleBuy)
        val btnAndroBuy = dialog?.findViewById<TextView>(R.id.btnAndroBuy)
        val cardAndropointPossibly = dialog?.findViewById<LinearLayout>(R.id.cardAndropointPossibly)
         val tvPriceToRub = dialog?.findViewById<TextView>(R.id.tvPriceToRub)
        btnGoogleBuy?.setOnClickListener {
            pressGoogle.invoke()
            dialog?.dismiss()
            dialog = null
        }
//        btnAndroBuy
//        btnTinkoffBuy?.setOnClickListener {
//            pressTinkoff.invoke()
//            dialog = null
//        }
        if (priceAndropoint==null) {
            cardAndropointPossibly?.visibility = View.GONE
        }
        tvPriceToRub?.text = priceRub.toString()+"₽"
        btnAndroBuy?.text = priceAndropoint.toString()
        btnAndroBuy?.setOnClickListener {
            pressAndro.invoke()
            dialog?.dismiss()
            dialog = null
        }
        dialog?.setOnDismissListener {
            dialogDissMiss?.invoke()
        }
        dialog?.show()
    }

    private fun replaceNumbersWithStrings(text: String, replacements: Map<String, String>): String {
        var modifiedText = text
        for ((num, replacement) in replacements) {
            modifiedText = modifiedText.replace(num, replacement)
        }
        return modifiedText
    }

    fun showDialogSelectDateForget(
        context: Context,
        requireActivity: FragmentActivity,
        viewLifecycleOwner: LifecycleOwner,
        textQuestion: String,
        selectedDate: (String) -> Unit
    ) {

        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        var date = "02/02/2020"
        dialog?.setContentView(R.layout.forget_date_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val tvQuestionDialogDate = dialog?.findViewById<TextView>(R.id.tvQuestionDialogDate)
        val tvSelectedDateForgetDialog =
            dialog?.findViewById<TextView>(R.id.tvSelectedDateForgetDialog)

        val btnSelectDateForgetDialog = dialog?.findViewById<CardView>(R.id.btnSelectDateForgetDialog)
        val btnReadyForgetDialog = dialog?.findViewById<CardView>(R.id.btnReadyForgetDialog)
        tvQuestionDialogDate?.text = textQuestion
        btnReadyForgetDialog?.setOnClickListener {
            dialog.dismiss()
            selectedDate.invoke(date)

        }

        btnSelectDateForgetDialog?.setOnClickListener {
            if (tvSelectedDateForgetDialog != null) {
                datePicker(requireActivity, viewLifecycleOwner, tvSelectedDateForgetDialog) {
                    date = it
                }
            }
        }

        dialog.show()
    }



    @SuppressLint("ResourceAsColor")
    fun showDialogSelectDate(
        context: Context,
        requireActivity: FragmentActivity,
        viewLifecycleOwner: LifecycleOwner,
        layoutInflater: LayoutInflater,
        isSelectDateAndClue: (String) -> Unit,
        dialogDissMiss:(()->Unit)
    ) {

        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//        val binding = SelectDateDialogBinding.inflate(layoutInflater)
        var date="02/02/2020"
        dialog?.setContentView(R.layout.select_date_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        val btnReadyDialog = dialog?.findViewById<CardView>(R.id.btnReadyDialog)
        val edClueDialog = dialog?.findViewById<EditText>(R.id.edClueDialog)
        val tvErrorClueDateDialog = dialog?.findViewById<TextView>(R.id.tvErrorClueDateDialog)
        val btnSelectDateDialog = dialog?.findViewById<CardView>(R.id.btnSelectDateDialog)
        val tvSelectedDateDialog = dialog?.findViewById<TextView>(R.id.tvSelectedDateDialog)
        val currentTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val textColor: Int = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, R.color.white) // Замените R.color.white на ваш ресурс цвета для темной темы
        } else {
            ContextCompat.getColor(context, R.color.black) // Замените R.color.black на ваш ресурс цвета для светлой темы
        }
        edClueDialog?.setTextColor(textColor)
            btnReadyDialog?.setOnClickListener {


                if (edClueDialog?.text.toString().trim()!=""&& date!="02/02/2020") {
                    val textTemp=edClueDialog?.text.toString().trimEnd()+"!!!TEMPWORD!!!"+date
                    dialog?.dismiss()
                    dialog = null
                    isSelectDateAndClue.invoke(textTemp)
                }else if(edClueDialog?.text.toString().trim()==""){
                    if (edClueDialog != null) {
                        ErrorHelper.showEmailErrorFeedback(
                            context,
                            edClueDialog,
                            errorDrawable =R.drawable.ic_clue_error,
                            oldDrawable=R.drawable.ic_clue_gray
                        )
                    }
                    tvErrorClueDateDialog?.visibility = View.VISIBLE
                    tvErrorClueDateDialog?.postDelayed({tvErrorClueDateDialog?.visibility = View.GONE }, 3000)
                }else if( date=="02/02/2020"){
                    Toast.makeText(context,context.getString(R.string.choose_another_date), Toast.LENGTH_SHORT).show()
                }
            }
            btnSelectDateDialog?.setOnClickListener {
                if (tvSelectedDateDialog != null) {
                    datePicker(requireActivity,viewLifecycleOwner,tvSelectedDateDialog){
                        date=it
                    }
                }
            }


        dialog?.show()
       dialog?.setOnDismissListener {
           dialogDissMiss.invoke()
       }
    }

    fun datePicker(
        requireActivity: FragmentActivity,
        viewLifecycleOwner: LifecycleOwner,
        tvDate: TextView,
        getDate: (String) -> Unit
    ) {
        val datePickerFragment = DatePickerFragment()
        val supportFragmentManager = requireActivity.supportFragmentManager
        supportFragmentManager.setFragmentResultListener("REQUEST_KEY", viewLifecycleOwner) { resultKey, bundle ->
            if (resultKey == "REQUEST_KEY") {
                val dateTemp = bundle.getString("SELECTED_DATE")
                if (dateTemp != null) {
                    getDate.invoke(dateTemp)
                }
                tvDate.text = dateTemp
            }
        }
        datePickerFragment.show(supportFragmentManager, "DatePickerFragment")
    }

    @SuppressLint("ResourceAsColor")
    fun showDialogSelectKeywordForget(
    context: Context,
    textQuestion: String,
    selectedKeyword: (String) -> Unit
    ) {

        val dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        dialog?.setContentView(R.layout.forget_keyword_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val tvQuestionKeywordForgetDialog = dialog?.findViewById<TextView>(R.id.tvQuestionKeywordForgetDialog)
        val edKeywordForgetDialog = dialog?.findViewById<EditText>(R.id.edKeywordForgetDialog)
        val btnReadyKeywordForgetDialog = dialog?.findViewById<CardView>(R.id.btnReadyKeywordForgetDialog)
        val currentTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val textColor: Int = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, R.color.white) // Замените R.color.white на ваш ресурс цвета для темной темы
        } else {
            ContextCompat.getColor(context, R.color.black) // Замените R.color.black на ваш ресурс цвета для светлой темы
        }

        edKeywordForgetDialog?.setTextColor(textColor)
        tvQuestionKeywordForgetDialog?.text = textQuestion
        btnReadyKeywordForgetDialog?.setOnClickListener {
            edKeywordForgetDialog?.text?.toString()?.let { it1 -> selectedKeyword.invoke(it1) }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("ResourceAsColor")
    fun showDialogSelectKeyword(
        context: Context,
        isSelectKeywordAndClue: (String) -> Unit,
        dialogDissMiss:(()->Unit)
    ) {
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)

        dialog?.setContentView(R.layout.select_keyword_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
      val btnSelectKeyword = dialog?.findViewById<CardView>(R.id.btnSelectKeyword)
        var edKeywordDialog = dialog?.findViewById<EditText>(R.id.edKeywordDialog)
        val edClueDialogKeyword = dialog?.findViewById<EditText>(R.id.edClueDialogKeyword)
        val tvErrorKeyword = dialog?.findViewById<TextView>(R.id.tvErrorKeyword)
        val tvErrorClueKeywordDialog = dialog?.findViewById<TextView>(R.id.tvErrorClueKeywordDialog)
        val currentTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        val textColor: Int = if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.black)
        }
        edKeywordDialog?.setTextColor(textColor)
        edClueDialogKeyword?.setTextColor(textColor)
            btnSelectKeyword?.setOnClickListener {
                if (edKeywordDialog?.text.toString().trim()!=""&& edClueDialogKeyword?.text.toString().trim()!=""){
                    val textTemp=edClueDialogKeyword?.text.toString().trimEnd()+"!!!TEMPWORD!!!"+edKeywordDialog?.text.toString().trimEnd()
                    isSelectKeywordAndClue.invoke(textTemp)
                    dialog?.dismiss()
                    dialog = null
                }
                if (edKeywordDialog?.text.toString().trim()==""){
                    if (edKeywordDialog != null) {
                        ErrorHelper.showEmailErrorFeedback(
                            context,
                            edKeywordDialog,
                            errorDrawable =R.drawable.ic_key_error,
                            oldDrawable=R.drawable.ic_key
                        )
                    }
                    tvErrorKeyword?.visibility = View.VISIBLE
                    tvErrorKeyword?.postDelayed({tvErrorKeyword?.visibility = View.GONE }, 3000)
                }
                if (edClueDialogKeyword?.text.toString().trim()==""){
                    if (edClueDialogKeyword != null) {
                        ErrorHelper.showEmailErrorFeedback(
                            context,
                            edClueDialogKeyword,
                            errorDrawable =R.drawable.ic_clue_error,
                            oldDrawable=R.drawable.ic_clue_gray
                        )
                    }
                    tvErrorClueKeywordDialog?.visibility = View.VISIBLE
                    tvErrorClueKeywordDialog?.postDelayed({tvErrorClueKeywordDialog?.visibility = View.GONE }, 3000)
                }
            }

        dialog?.show()
        dialog?.setOnDismissListener {
            dialogDissMiss.invoke()
        }
    }
     fun showDialogBuyAndropointsImplementation(
         context: Context,
         billingManager: BillingManager?,
         ads: () -> Unit
     ) {
        ShowDialogHelper.showDialogBuyAndropoints(context, {
            ads.invoke()
        }, {

        }, {
            Log.d("andropointsIeefffgbbCout", "moneyRub:${it}")
        }, {
            Log.d("andropointsIeefffgbbCout", "andropointsCount:${it}")
            if (it == 1) {
                billingManager?.billingSetup(PayState.ONEANDROPOINTBUY)
            }
            if (it == 10) {
                billingManager?.billingSetup(PayState.TENANDROPOINTBUY)
            }
            if (it == 100) {
                billingManager?.billingSetup(PayState.HUNDREDANDROPOINTBUY)
            }
        })
    }

}