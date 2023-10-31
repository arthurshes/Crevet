package workwork.test.andropediagits.presenter.lesson.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.google.android.material.textfield.TextInputEditText
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.utils.Constatns

object ErrorHelper {
    fun showEmailErrorFeedback(
        context: Context,
        editText: EditText,
        errorDrawable: Int?=null,
        oldDrawable: Int?=null,
        text: String="",
        isNeedDrawable:Boolean=true,
        isNeedToast:Boolean=true
    ) {
        val originalHintColor = editText.currentHintTextColor
        val originalBackground = editText.background

        editText.setHintTextColor(ContextCompat.getColor(context, R.color.error))
        if (isNeedDrawable) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, errorDrawable!!), null, null, null)
        }
        editText.background = ContextCompat.getDrawable(context, R.drawable.edit_text_background_error)
        if (isNeedToast) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            editText.setHintTextColor(originalHintColor)
            if (isNeedDrawable) {
                editText.setCompoundDrawablesRelativeWithIntrinsicBounds(ContextCompat.getDrawable(context, oldDrawable!!), null, null, null)
            }
            editText.background = originalBackground
        }, Constatns.ERROR_TIME)
    }
}