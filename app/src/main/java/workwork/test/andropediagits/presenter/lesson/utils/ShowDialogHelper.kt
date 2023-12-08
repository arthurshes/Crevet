package workwork.test.andropediagits.presenter.lesson.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Typeface
import android.media.Image
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
import org.w3c.dom.Text

import workwork.test.andropediagits.R
import workwork.test.andropediagits.databinding.StrikeModeDialogBinding
import workwork.test.andropediagits.domain.googbilling.BillingManager
import workwork.test.andropediagits.domain.googbilling.PayState
import workwork.test.andropediagits.presenter.reset.DatePickerFragment

object ShowDialogHelper {
    private var dialog: Dialog? = null
    private var isDialogStrikeShow = false
    private var isDialogSuccess = false
    private var dialogChooseWay = false
    private var isDialogReplay = false
    private var isAndropointShow = false
    private var isClueShow = false
   private var isShowDialogBuy = false
    private var isCloseDialog = false
    private var isFailDialog = false
    private var isDeleteDialog = false


    fun showDialogDeleteDataAcc(context: Context,delete:(()->Unit),close: (() -> Unit)){
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.delete_account_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if(!isDeleteDialog){
            isDeleteDialog = true
            val okButton = dialog?.findViewById<CardView>(R.id.btnDeleteAccount)
            val cancelBtn = dialog?.findViewById<TextView>(R.id.tv_delete_acc_cancel)
            okButton?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
                delete.invoke()
            }
            cancelBtn?.setOnClickListener {
                dialog?.dismiss()
                dialog = null
            }
            dialog?.show()
        }
        dialog?.setOnDismissListener {
            isDeleteDialog = false
            close.invoke()
        }
    }

    fun showDialogChooseWay(    context: Context,
                                googleAd: () -> Unit, ruAd: () -> Unit,    googlePay: (() -> Unit)? = null,
                                ruPay: (() -> Unit)? = null,    choiceAd: Boolean, currentGoogleSelect: Boolean? = false
    ) {
        if (!dialogChooseWay) {
        val currentTheme =
            context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            dialogChooseWay = true
            var isGoogleWay = false
            var isRuWay = false
        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.choose_way_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        val btnGoogleChoice = dialog?.findViewById<LinearLayout>(R.id.btnGoogleChoice)
        val btnContinueChooseWay = dialog?.findViewById<CardView>(R.id.btnContinueChooseWay)
        val tvWay = dialog?.findViewById<TextView>(R.id.tvWay)

        val tvGoogle = dialog?.findViewById<TextView>(R.id.tvGoogle)
            val tvRu = dialog?.findViewById<TextView>(R.id.tvRu)
        val btnRuChoice = dialog?.findViewById<LinearLayout>(R.id.btnRuChoice)
            if (currentGoogleSelect == true) {
            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
            } else {                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)
            }
            btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
                btnContinueChooseWay?.isClickable = true
        } else {
                btnContinueChooseWay?.isClickable = true
            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
            btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
            btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
        } else {                btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background) }
            btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
        }
        if (choiceAd) {            btnGoogleChoice?.setOnClickListener {
            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {                        btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
            }                    else {

                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)                    }
            btnGoogleChoice.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
            btnContinueChooseWay?.alpha = 1.0F
            btnContinueChooseWay?.isClickable = true
            isGoogleWay = true
            isRuWay = false
        }
            btnRuChoice?.setOnClickListener {
            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
                btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
            } else {
                btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)
            }
            btnRuChoice.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
            btnContinueChooseWay?.alpha = 1.0F
            btnContinueChooseWay?.isClickable = true
            isGoogleWay = false
                isRuWay = true
            }
            btnContinueChooseWay?.setOnClickListener {                dialog?.dismiss()
                dialog = null

                if(currentGoogleSelect==false&&!isGoogleWay&&!isRuWay){
                    ruAd.invoke()
                }else

                if(currentGoogleSelect==true&&!isGoogleWay&&!isRuWay){
                    googleAd.invoke()
                }else if (isGoogleWay) {
                    googleAd.invoke()
                }
                if(isRuWay){
                    ruAd.invoke()
                }
            }        } else {
            tvWay?.text = context.getString(R.string.select_pay_type)
            tvGoogle?.text = "Google pay"
            tvRu?.text = context.getString(R.string.yukassa)
            btnGoogleChoice?.setOnClickListener {
                btnContinueChooseWay?.alpha = 1.0F
                btnContinueChooseWay?.isClickable = true
                isGoogleWay = true            }
            btnRuChoice?.setOnClickListener {                btnContinueChooseWay?.alpha = 1.0F
                btnContinueChooseWay?.isClickable = true
                isGoogleWay = false
            }
            btnContinueChooseWay?.setOnClickListener {
                dialog?.dismiss()
                dialog = null
                if (isGoogleWay) {                    googlePay?.invoke()
                } else {                    ruPay?.invoke()
                }            }
        }    }
        dialog?.show()
        dialog?.setOnDismissListener {
            dialogChooseWay = false
        }
    }



//    fun showDialogChooseWay(    context: Context,
//                                googleAd: () -> Unit, ruAd: () -> Unit,    googlePay: (() -> Unit)? = null,
//                                ruPay: (() -> Unit)? = null,    choiceAd: Boolean, currentGoogleSelect: Boolean?=false
//    ) {
//        if (!isDialogSuccess) {
//        val currentTheme =  context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//        isDialogSuccess = true
//        var isGoogleWay = false
//        dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//        dialog?.setContentView(R.layout.choose_way_dialog)
//        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//        dialog?.setCancelable(false)
//        val btnGoogleChoice = dialog?.findViewById<LinearLayout>(R.id.btnGoogleChoice)
//        val btnContinueChooseWay = dialog?.findViewById<CardView>(R.id.btnContinueChooseWay)
//        val tvWay = dialog?.findViewById<TextView>(R.id.tvWay)
//        val tvGoogle = dialog?.findViewById<TextView>(R.id.tvGoogle)
//        val tvRu = dialog?.findViewById<TextView>(R.id.tvRu)
//        val btnRuChoice = dialog?.findViewById<LinearLayout>(R.id.btnRuChoice)
//        if (currentGoogleSelect == true) {
//            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
//                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
//            } else {
//                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)
//            }
//            btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
//        } else {
//            if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
//                btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
//            } else {
//                btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)
//            }
//            btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
//        }
//        if (choiceAd) {
//            btnGoogleChoice?.setOnClickListener {
//            if (!isGoogleWay) {
//
//                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
//                btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
//                } else { btnRuChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background) }
//                btnGoogleChoice.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)                }
//            btnContinueChooseWay?.alpha = 1.0F
//            btnContinueChooseWay?.isClickable = true
//            isGoogleWay = true            }
//            btnRuChoice?.setOnClickListener {
//                if (isGoogleWay) {
//                if (currentTheme == Configuration.UI_MODE_NIGHT_YES) {
//                    btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_night)
//                } else {
//                    btnGoogleChoice?.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background)
//                }
//                    btnRuChoice.background = ContextCompat.getDrawable(context, R.drawable.button_edges_background_yellow)
//            }
//                btnContinueChooseWay?.alpha = 1.0F
//                btnContinueChooseWay?.isClickable = true
//                isGoogleWay = false
//            }
//            btnContinueChooseWay?.setOnClickListener {                dialog?.dismiss()
//                dialog = null
//                if (isGoogleWay) {
//                    googleAd.invoke()
//                } else {
//                    ruAd.invoke()
//                }
//            }        } else {
//            tvWay?.text = context.getString(R.string.select_pay_type)
//            tvGoogle?.text = "Google pay"
//            tvRu?.text = context.getString(R.string.yukassa)
//            btnGoogleChoice?.setOnClickListener {
//                btnContinueChooseWay?.alpha = 1.0F
//                btnContinueChooseWay?.isClickable = true
//                isGoogleWay = true            }
//            btnRuChoice?.setOnClickListener {                btnContinueChooseWay?.alpha = 1.0F
//                btnContinueChooseWay?.isClickable = true
//                isGoogleWay = false
//            }
//            btnContinueChooseWay?.setOnClickListener {
//                dialog?.dismiss()
//                dialog = null
//                if (isGoogleWay) {
//                    googlePay?.invoke()
//                } else {
//                    ruPay?.invoke()                }
//            }        }
//    }
//        dialog?.show()
//    }


//    fun showDialogChooseWay(    context: Context,
//                                googleAd: () -> Unit,    ruAd: () -> Unit,
//                                googlePay: (() -> Unit)?=null,
//                                ruPay:( () -> Unit)?=null,
//                                choiceAd: Boolean,currentGoogleSelect:Boolean?=null)
//
//    {
//        if (!isDialogSuccess) {
//            val currentTheme = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
//            isDialogSuccess = true
//            var isGoogleWay = false
//            dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
//            dialog?.setContentView(R.layout.choose_way_dialog)
//            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//            dialog?.setCancelable(false)
//
//            val btnGoogleChoice = dialog?.findViewById<LinearLayout>(R.id.btnGoogleChoice)
//            val btnContinueChooseWay = dialog?.findViewById<CardView>(R.id.btnContinueChooseWay)
//            val tvWay = dialog?.findViewById<TextView>(R.id.tvWay)
//            val tvGoogle = dialog?.findViewById<TextView>(R.id.tvGoogle)
//            val tvRu = dialog?.findViewById<TextView>(R.id.tvRu)
//            val btnRuChoice = dialog?.findViewById<LinearLayout>(R.id.btnRuChoice)
//            if(currentGoogleSelect == true){
//                if(currentTheme == Configuration.UI_MODE_NIGHT_YES){
//                    btnRuChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_night )
//                }else{
//                    btnRuChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background )
//                }
//                btnGoogleChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_yellow )
//            }else{
//                if(currentTheme == Configuration.UI_MODE_NIGHT_YES){
//                    btnGoogleChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_night )
//                }else{
//                    btnGoogleChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background )
//                }
//                btnRuChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_yellow )
//            }
//            if (choiceAd) {
//                btnGoogleChoice?.setOnClickListener {
//                    if(!isGoogleWay){
//                        if(currentTheme == Configuration.UI_MODE_NIGHT_YES){
//                            btnRuChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_night )
//                        }else{
//                            btnRuChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background )
//                        }
//                        btnGoogleChoice.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_yellow )
//                    }
//                    btnContinueChooseWay?.alpha= 1.0F
//                    btnContinueChooseWay?.isClickable= true
//                    isGoogleWay = true
//                }
//                btnRuChoice?.setOnClickListener {
//                    if(isGoogleWay){
//                        if(currentTheme == Configuration.UI_MODE_NIGHT_YES){
//                            btnGoogleChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_night )
//                        }else{
//                            btnGoogleChoice?.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background )
//                        }
//                        btnRuChoice.background = ContextCompat.getDrawable(context,R.drawable.button_edges_background_yellow )
//                    }
//                    btnContinueChooseWay?.alpha= 1.0F
//                    btnContinueChooseWay?.isClickable= true
//                    isGoogleWay = false
//
//                }
//                btnContinueChooseWay?.setOnClickListener {
//                    dialog?.dismiss()
//                    dialog = null
//                    if (isGoogleWay) {
//                        googleAd.invoke()                }
//                    else {
//                        ruAd.invoke()
//                    }
//                }
//            }else{
//                tvWay?.text = context.getString(R.string.select_pay_type)
//                tvGoogle?.text = "Google pay"
//                tvRu?.text = context.getString(R.string.yukassa)
//                btnGoogleChoice?.setOnClickListener {
//                    btnContinueChooseWay?.alpha= 1.0F
//                    btnContinueChooseWay?.isClickable= true
//                    isGoogleWay = true            }
//                btnRuChoice?.setOnClickListener {                btnContinueChooseWay?.alpha= 1.0F
//                    btnContinueChooseWay?.isClickable= true
//                    isGoogleWay = false
//                }
//                btnContinueChooseWay?.setOnClickListener {
//                    dialog?.dismiss()
//                    dialog = null
//                    if (isGoogleWay) {
//                        googlePay?.invoke()
//                    }else {                    ruPay?.invoke()
//                    }            }
//            }    }
//        dialog?.show()}


    fun supportDialog(context: Context,clickTikTok:(()->Unit),clickYoutube:(()->Unit),clickTelegram:(()->Unit),dialogClose:(()->Unit)){
        dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.social_network_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val btnTelegram = dialog?.findViewById<ImageView>(R.id.btnTelegram)
        val btnYoutube = dialog?.findViewById<ImageView>(R.id.btnYouTube)
        val btnTiktok = dialog?.findViewById<ImageView>(R.id.btnTikTok)
        btnTelegram?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            clickTelegram.invoke()
        }
        btnYoutube?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            clickYoutube.invoke()
        }
        btnTiktok?.setOnClickListener {
            dialog?.dismiss()
            dialog = null
            clickTikTok.invoke()
        }
        dialog?.setOnDismissListener {
            dialogClose.invoke()
        }
        dialog?.show()
    }

    fun showDialogUnknownError(
        context: Context,
        pressButton: (() -> Unit),
        close: () -> Unit
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
        dialog?.setOnDismissListener {
            close.invoke()
        }
    }

    fun showDialogTimeOutError(
        context: Context,
        pressButton: (() -> Unit),
        close: () -> Unit
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
        dialog?.setOnDismissListener {
            close.invoke()
        }
    }

    fun showDialogNotNetworkError(
        context: Context,
        pressButton: (() -> Unit),
        dialogDissMiss: () -> Unit
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
        dialog?.setOnDismissListener {
            dialogDissMiss.invoke()
        }
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

        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.strike_mode_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if(!isDialogStrikeShow) {

            val closeStrikeBtn = dialog.findViewById<ImageView>(R.id.closeStrikeBtn)
            val btnPremiumStrikeMode = dialog.findViewById<LinearLayout>(R.id.btnPremiumStrikeMode)


            isDialogStrikeShow = true

            strikeModeDayTreatmentResult(resultStrikeModeDay,dialog ,context,isSubscribe)

            dialog.show()
            closeStrikeBtn.setOnClickListener {
                isClose.invoke()
                dialog.dismiss()
            }
            btnPremiumStrikeMode.setOnClickListener {
                dialog.dismiss()
                isPremium.invoke()
            }
        }

        dialog?.setOnDismissListener {
            isDialogStrikeShow = false
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

    @SuppressLint("SuspiciousIndentation")
    private fun strikeModeDayTreatmentResult(
        resultStrikeModeDay: Int,
        dialog:Dialog,
        context: Context,
        isSubscribe:Boolean
    ) {

        val drawableCurrentDay = AppCompatResources.getDrawable(context, R.drawable.border_current_day)
        val oneDayAndropointsText = dialog.findViewById<TextView>(R.id.oneDayAndropointsText)
        val twoDayAndropointsText = dialog.findViewById<TextView>(R.id.twoDayAndropointsText)
        val threeDayAndropointsText = dialog.findViewById<TextView>(R.id.threeDayAndropointsText)
        val fourDayAndropointsText = dialog.findViewById<TextView>(R.id.fourDayAndropointsText)
        val fiveDayAndropointsText = dialog.findViewById<TextView>(R.id.fiveDayAndropointsText)
        val sixDayAndropointsText = dialog.findViewById<TextView>(R.id.sixDayAndropointsText)
        val sevenDayAndropointsText = dialog.findViewById<TextView>(R.id.sevenDayAndropointsText)
        val currentDayOne = dialog.findViewById<LinearLayout>(R.id.currentDayOne)
        val currentDayTwo = dialog.findViewById<LinearLayout>(R.id.currentDayTwo)
        val currentDayThree = dialog.findViewById<LinearLayout>(R.id.currentDayThree)
        val currentDayFour = dialog.findViewById<LinearLayout>(R.id.currentDayFour)
        val currentDayFive = dialog.findViewById<LinearLayout>(R.id.currentDayFive)
        val currentDaySix = dialog.findViewById<LinearLayout>(R.id.currentDaySix)
        val currentDaySeven = dialog.findViewById<LinearLayout>(R.id.currentDaySeven)
        val cardDayOne = dialog.findViewById<CardView>(R.id.cardDayOne)
        val cardDayTwo = dialog.findViewById<CardView>(R.id.cardDayTwo)
        val cardDayThree = dialog.findViewById<CardView>(R.id.cardDayThree)
        val cardDayFour = dialog.findViewById<CardView>(R.id.cardDayFour)
        val cardDayFive = dialog.findViewById<CardView>(R.id.cardDayFive)
        val cardDaySix = dialog.findViewById<CardView>(R.id.cardDaySix)
        val cardDaySeven = dialog.findViewById<CardView>(R.id.cardDaySeven)

        val cardPastDayOne = dialog.findViewById<CardView>(R.id.pastDayOne)
        val cardPastDayTwo = dialog.findViewById<CardView>(R.id.pastDayTwo)
        val cardPastDayThree = dialog.findViewById<CardView>(R.id.pastDayThree)
        val cardPastDayFour = dialog.findViewById<CardView>(R.id.pastDayFour)
        val cardPastDayFive = dialog.findViewById<CardView>(R.id.pastDayFive)
        val cardPastDaySix = dialog.findViewById<CardView>(R.id.pastDaysix)
        val cardPastDaySeven = dialog.findViewById<CardView>(R.id.pastDaySeven)
        ////TextPast
        val tvPastDayOne = cardPastDayOne.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDayTwo = cardPastDayTwo.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDayThree = cardPastDayThree.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDayFour = cardPastDayFour.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDayFive = cardPastDayFive.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDaySix = cardPastDaySix.findViewById<TextView>(R.id.tvPastDay)
        val tvPastDaySeven = cardPastDaySeven.findViewById<TextView>(R.id.tvPastDay)



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
                    cardPastDayOne.visibility = View.VISIBLE
                    tvPastDayOne.text=context.getString(R.string.day_one)
                }

                3 -> {
                    currentDayThree.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    cardPastDayOne.visibility = View.VISIBLE
                     tvPastDayOne.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    cardPastDayTwo.visibility = View.VISIBLE
                   tvPastDayTwo.text=context.getString(R.string.day_two)
                }

                4 -> {
                    currentDayFour.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    cardPastDayOne.visibility = View.VISIBLE
                    tvPastDayOne.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    cardPastDayTwo.visibility = View.VISIBLE
                    tvPastDayTwo.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    cardPastDayThree.visibility = View.VISIBLE
                    tvPastDayThree.text=context.getString(R.string.day_three)
                }

                5 -> {
                    currentDayFive.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    cardPastDayOne.visibility = View.VISIBLE
                    tvPastDayOne.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    cardPastDayTwo.visibility = View.VISIBLE
                    tvPastDayTwo.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    cardPastDayThree.visibility = View.VISIBLE
                    tvPastDayThree.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    cardPastDayFour.visibility = View.VISIBLE
                    tvPastDayFour.text=context.getString(R.string.day_four)
                }

                6 -> {
                    currentDaySix.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    cardPastDayOne.visibility = View.VISIBLE
                    tvPastDayOne.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    cardPastDayTwo.visibility = View.VISIBLE
                    tvPastDayTwo.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    cardPastDayThree.visibility = View.VISIBLE
                    tvPastDayThree.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    cardPastDayFour.visibility = View.VISIBLE
                    tvPastDayFour.text=context.getString(R.string.day_four)
                    cardDayFive.visibility = View.GONE
                    cardPastDayFive.visibility = View.VISIBLE
                    tvPastDayFive.text=context.getString(R.string.day_five)

                }

                7 -> {
                    currentDaySeven.background = drawableCurrentDay
                    cardDayOne.visibility = View.GONE
                    cardPastDayOne.visibility = View.VISIBLE
                    tvPastDayOne.text=context.getString(R.string.day_one)
                    cardDayTwo.visibility = View.GONE
                    cardPastDayTwo.visibility = View.VISIBLE
                    tvPastDayTwo.text=context.getString(R.string.day_two)
                    cardDayThree.visibility = View.GONE
                    cardPastDayThree.visibility = View.VISIBLE
                    tvPastDayThree.text=context.getString(R.string.day_three)
                    cardDayFour.visibility = View.GONE
                    cardPastDayFour.visibility = View.VISIBLE
                    tvPastDayFour.text=context.getString(R.string.day_four)
                    cardDayFive.visibility = View.GONE
                    cardPastDayFive.visibility = View.VISIBLE
                    tvPastDayFive.text=context.getString(R.string.day_five)
                    cardDaySix.visibility = View.GONE
                    cardPastDaySix.visibility = View.VISIBLE
                    tvPastDaySix.text=context.getString(R.string.day_six)
                }

        }

    }


    fun showDialogAttention(context: Context, pressButton: () -> Unit,close: () -> Unit) {
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
        dialog?.setOnDismissListener {
            close.invoke()
        }
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

    fun showDialogReplayTest(context: Context,close: () -> Unit,mistakeTest: Int,size: Int){
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.test_replay_dialog_result)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if (!isDialogReplay){
            isDialogReplay = true


            val dialogButton = dialog?.findViewById<CardView>(R.id.btnContinueReplay)
            val success_text_result = dialog?.findViewById<TextView>(R.id.success_text_result_replay)
            success_text_result?.text = context.getString(R.string.count_mistake).replace("6", (size - mistakeTest).toString()).replace("10", size.toString())
            dialogButton?.setOnClickListener {
                    dialog.dismiss()
            }
            dialog.show()
        }
        dialog?.setOnDismissListener {
            isDialogReplay = false
            close.invoke()
        }
    }

    fun showDialogSuccessTest(context: Context, close: () -> Unit,mistakeTest: Int, size: Int) {
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.setContentView(R.layout.test_success_dialog)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if(!isDialogSuccess){
            isDialogSuccess = true
            val dialogButton = dialog.findViewById<CardView>(R.id.btnContinue)
            val success_text_result = dialog.findViewById<TextView>(R.id.success_text_result)
            val addTextAndropoints = dialog.findViewById<TextView>(R.id.textViewAddAndropointsSuccess)
            success_text_result?.text = context.getString(R.string.count_mistake).replace("6", (size - mistakeTest).toString()).replace("10", size.toString())
            dialogButton?.isClickable = false
            var buttonEnable = false
            var buttonClick = false
            dialogButton?.animate()
                ?.alpha(1f)
                ?.setDuration(3000)
                ?.withEndAction {
                    dialogButton.isClickable = true
                    buttonEnable = true

                }
            dialogButton?.setOnClickListener {
                if(buttonEnable&&!buttonClick){
                    dialogButton.isClickable = false
                    buttonEnable = false
                    buttonClick = true
                    dialog.dismiss()

                }
            }
            dialog?.show()
        }
        dialog?.setOnDismissListener {
            isDialogSuccess = false
            close.invoke()
        }
    }

    fun showDialogFailTest(context: Context, correctTest:Int,mistakeTest: Int, size: Int, dateTerm: String,isClose:(()->Unit),isTimerOut:Boolean) {
       val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.test_fail_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.setCancelable(false)
        if(!isFailDialog){
            isFailDialog = true
            val dialogCountMistakes = dialog?.findViewById<TextView>(R.id.tvCountMistake)
            val dialogTerm = dialog?.findViewById<TextView>(R.id.term_fail_test)
            val dialogClose = dialog?.findViewById<ImageButton>(R.id.buttonLockFailDialog)
            dialogClose?.setOnClickListener {
                dialogClose.isClickable = false
                dialog.dismiss()
            }
            var misstakes = 0
            misstakes = if(isTimerOut){
                val mistaketimer = size - (correctTest+mistakeTest)
                mistaketimer + mistakeTest
            }else{
                mistakeTest
            }

            Log.d("misstakeDialog","size:${size} mistakeTest:${misstakes} dateTerm:${dateTerm}")
            val test=context.getString(R.string.count_mistake).replace("6", (size - misstakes).toString())
            dialogCountMistakes?.text = test.replace("10", size.toString())
//        dialogCountMistakes?.text = context.getString(R.string.count_mistake).replace("6", (size - misstakes).toString())
//        dialogCountMistakes?.text = context.getString(R.string.count_mistake).replace("10", size.toString())
//        dialogCountMistakes?.text = context.getString(R.string.count_mistake).replace("6", (size - misstakes).toString()).replace("10", size.toString())
            dialogTerm?.text =   context.getString(R.string.term_before_fail_test).replace("DATA",dateTerm.toString())
            dialog?.show()
        }
       dialog?.setOnDismissListener {
           isFailDialog = false
           isClose.invoke()
       }

    }

    fun showDialogTerm(context: Context, dateTerm: String) {
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.text_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val dialogTermDescription = dialog?.findViewById<TextView>(R.id.tvTermDescription)
        dialogTermDescription?.text =
            context.getString(R.string.term_description).replace("DATA",dateTerm.toString())
        dialog?.show()

    }

    @SuppressLint("SuspiciousIndentation")
    fun showDialogBuyAndropoints(
        context: Context,
        watchAd: () -> Unit,
        pay: () -> Unit,
        money: (String) -> Unit,
        andropoints: (String) -> Unit
    ) {
        if(!isAndropointShow) {
            dialog = Dialog(context)
            isAndropointShow = true
            dialog?.setContentView(R.layout.buy_andropoints_dialog)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val btnPay = dialog?.findViewById<CardView>(R.id.btnPay)
            val tvLastPayCost = dialog?.findViewById<TextView>(R.id.tvLastPayCost)
            val cardBuyOneAndropoint = dialog?.findViewById<LinearLayout>(R.id.cardBuyOneAndropoint)
            val cardBuyTenAndropoints =
                dialog?.findViewById<LinearLayout>(R.id.cardBuyTenAndropoints)
            val cardBuyOneHundredAndropoints =
                dialog?.findViewById<LinearLayout>(R.id.cardBuyOneHundredAndropoints)
            val cardBuyInfinityAndropoints =
                dialog?.findViewById<LinearLayout>(R.id.cardBuyInfinityAndropoints)
            val tvLastCountAndropoint = dialog?.findViewById<TextView>(R.id.tvLastCountAndropoint)
            val tvCountAndropointsBuyOneAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountAndropointsBuyOneAndropoint)
            val tvCountMoneyBuyOneAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyOneAndropoint)
            val tvCountAndropointsBuyTenAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountAndropointsBuyTenAndropoint)
            val tvCountMoneyBuyTenAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyTenAndropoint)
            val tvCountAndropointsOneHundredAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountAndropointsOneHundredAndropoint)
            val tvCountMoneyBuyOneHundredAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyOneHundredAndropoint)
            val tvCountAndropointsInfinityAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountAndropointsInfinityAndropoint)
            val tvCountMoneyBuyInfinityAndropoint =
                dialog?.findViewById<TextView>(R.id.tvCountMoneyBuyInfinityAndropoint)
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
                money.invoke(tvLastPayCost?.text.toString().replace("$", ""))
                andropoints.invoke(
                    tvLastCountAndropoint?.text.toString().replace("$", "")
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
        dialog?.setOnDismissListener {
            isAndropointShow = false
        }
    }

    fun showDialogClue(context: Context, text: String,close:(()->Unit)) {
        val dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog?.setContentView(R.layout.text_dialog)
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        if(!isClueShow){
            isClueShow = true
            val dialogTermDescription = dialog?.findViewById<TextView>(R.id.tvClueDescription)
            val btnClose = dialog?.findViewById<CardView>(R.id.btnCloseClueDialog)
            btnClose?.setOnClickListener {
                dialog.dismiss()
                close?.invoke()
            }
            dialogTermDescription?.text = text
            dialog?.show()
        }

        dialog?.setOnDismissListener {
            isClueShow = false
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
        if(!isCloseDialog) {
            isCloseDialog = true
            dialog = Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog?.setContentView(R.layout.close_dialog)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val dialogBuyCourse = dialog?.findViewById<CardView>(R.id.btnBuyCourse)
            val tvCloseText = dialog?.findViewById<TextView>(R.id.tvCloseText)
            val tvBuy = dialog?.findViewById<TextView>(R.id.tvBuy)

            if (themeClose) {
                dialogBuyCourse?.visibility = View.GONE
                tvCloseText?.text = context.getString(R.string.theme_close_entirely)
            }
            if (themeBuy) {
                tvBuy?.text = context.getString(R.string.buy_theme)
                tvCloseText?.text = context.getString(R.string.theme_close)
            } else {
                tvBuy?.text = context.getString(R.string.buy_course)
                tvCloseText?.text = context.getString(R.string.course_close)
            }

            dialogBuyCourse?.setOnClickListener {
                dialog?.dismiss()
                dialog = null
                buyCourse?.invoke()
            }
            dialog?.show()
        }
        dialog?.setOnDismissListener {
            dialogDissMiss?.invoke()
            isCloseDialog = false
        }

    }

    fun showDialogBuy(
        context: Context,
        priceRub:Int,
        priceAndropoint:Int?=null,
        pressGoogle: () -> Unit,
        pressAndro: () -> Unit,
        dialogDissMiss:(()->Unit)
    ) {
        if(!isShowDialogBuy){
            isShowDialogBuy = true
            dialog = Dialog(context,android.R.style.Theme_Black_NoTitleBar_Fullscreen)
            dialog?.setContentView(R.layout.buy_dialog)
            dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val btnGoogleBuy = dialog?.findViewById<LinearLayout>(R.id.btnGoogleBuy)
            val btnAndroBuy = dialog?.findViewById<TextView>(R.id.btnAndroBuy)
            val cardAndropointPossibly = dialog?.findViewById<LinearLayout>(R.id.cardAndropointPossibly)
            val tvPriceToRub = dialog?.findViewById<TextView>(R.id.tvPriceToRub)
            btnGoogleBuy?.setOnClickListener {
                pressGoogle.invoke()
                dialog?.dismiss()
                dialog = null
            }
            if (priceAndropoint==null) {
                cardAndropointPossibly?.visibility = View.GONE
            }
            tvPriceToRub?.text = priceRub.toString()+"$"
            btnAndroBuy?.text = priceAndropoint.toString()
            btnAndroBuy?.setOnClickListener {
                pressAndro.invoke()
                dialog?.dismiss()
                dialog = null
            }
            dialog?.show()
        }

        dialog?.setOnDismissListener {
            isShowDialogBuy = false
            dialogDissMiss?.invoke()
        }

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
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.black)
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
            ContextCompat.getColor(context, R.color.white)
        } else {
            ContextCompat.getColor(context, R.color.black)
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
        showDialogBuyAndropoints(context, {
            ads.invoke()
        }, {

        }, {
            Log.d("andropointsIeefffgbbCout", "moneyRub:${it}")
        }, {
            Log.d("andropointsIeefffgbbCout", "andropointsCount:${it}")
            if (it == "1") {
                billingManager?.billingSetup(PayState.ONEANDROPOINTBUY)
            }
            if (it == "10") {
                billingManager?.billingSetup(PayState.TENANDROPOINTBUY)
            }
            if (it == "100") {
                billingManager?.billingSetup(PayState.HUNDREDANDROPOINTBUY)
            }
            if(it=="∞"){
                billingManager?.billingSetup(PayState.THOUSANDANDROPOINTBUY)
            }
        })
    }

}