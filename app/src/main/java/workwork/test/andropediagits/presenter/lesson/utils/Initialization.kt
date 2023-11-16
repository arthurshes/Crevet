package workwork.test.andropediagits.presenter.lesson.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import coil.load
import workwork.test.andropediagits.presenter.lesson.utils.OptimizationText.setTextSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.utils.Constatns

import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.databinding.FragmentLessonBinding
import java.util.regex.Pattern

object Initialization {
    private var pref: SharedPreferences? = null
    private fun processContent(
        imageView: ImageView,
        textView: TextView,
        card: CardView,
        textCode: TextView,
        imageLoad: Bitmap?,
        text: String?,
        CodeFragment: String?,
        binding: FragmentLessonBinding?,
        context: Context
    ) {
        CoroutineScope(Dispatchers.Main).launch {
            pref = PreferenceManager.getDefaultSharedPreferences(context)

            if (imageLoad != null) {
                imageView.visibility = View.VISIBLE
                imageView.load(imageLoad)
            } else {
                imageView.visibility = View.GONE
                imageView.load("")
            }
            if (text != null) {
                textView.visibility = View.VISIBLE
                textView.text = OptimizationText.searchTitles(text, binding)
                textView.setTextSize(pref?.getString(Constatns.TEXT_KEY, "20"))
            } else {
                textView.visibility = View.GONE
                textView.text = ""
            }

            if (CodeFragment != null) {
                card.visibility = View.VISIBLE
                val keywords = OptimizationText.optimizeCodeFragment(CodeFragment)
                val keywords1 = OptimizationText.colorizeElements(keywords, context)
                val keywords2 = OptimizationText.specialCharacterColoring(keywords1, context)
                val html = Html.fromHtml(keywords2.toString(), Html.FROM_HTML_MODE_LEGACY)
                val textViewTSec = colorizeNumbers(html, context)
                val spannableString = SpannableString(textViewTSec)
                val regex = Regex("(val|var|fun)\\s(\\w+)")
                val matches = regex.findAll(textViewTSec)
                for (match in matches) {
                    val start = match.range.first + 4
                    val end = match.range.last
                    val color = when (match.groupValues[1]) {
                        "val" -> ContextCompat.getColor(context, R.color.variable_code)
                        "var" -> ContextCompat.getColor(context, R.color.variable_code)
                        "fun" -> ContextCompat.getColor(context, R.color.function_code)
                        else -> ContextCompat.getColor(context, R.color.function_code)
                    }
                    spannableString.setSpan(ForegroundColorSpan(color), start, end + 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                quotesColorize(textViewTSec, spannableString, context)
                afterPoint(spannableString,textViewTSec,context)
                beforePoint(spannableString,textViewTSec,context)
                textCode.text = spannableString
                textCode.setTextSize(pref?.getString(Constatns.CODE_KEY, "20"))
            } else {
                card.visibility = View.GONE
                textCode.text = ""
            }

        }
    }
    private fun afterPoint(
        spannableString: SpannableString,
        textViewTSec: SpannableString,
        context: Context
    ) {
        val pattern = Pattern.compile("\\.(\\w+)(\\()", Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(textViewTSec)

        if (matcher.find()) {
            val startIndex = matcher.start(1)
            val endIndex = matcher.end(1)
            val color = ContextCompat.getColor(context, R.color.function_code)
            spannableString.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun beforePoint(
        spannableString: SpannableString,
        textViewTSec: SpannableString,
        context: Context
    ) {
        val dotIndex = textViewTSec.indexOf('.')
        val spaceBeforeDotIndex = textViewTSec.lastIndexOf(' ', dotIndex)
        if (spaceBeforeDotIndex != -1) {
            val startIndex = spaceBeforeDotIndex + 1
            val endIndex = dotIndex.coerceAtMost(spannableString.length - 1)

            val color = ContextCompat.getColor(context, R.color.variable_code)
            spannableString.setSpan(
                ForegroundColorSpan(color),
                startIndex,
                endIndex,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }
    private fun quotesColorize(
        text: SpannableString,
        spannableString: SpannableString,
        context: Context
    ) {
        var startIndex = text.indexOf('"')
        if (startIndex != -1) {
            var endIndex = text.indexOf('"', startIndex + 1)
            if (endIndex != -1) {
                while (startIndex in 0 until endIndex) {
                    spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_code)), startIndex, startIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_code)), endIndex, endIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    spannableString.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.text_code)), startIndex + 1, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    startIndex = text.indexOf('"', endIndex + 1)
                    endIndex = text.indexOf('"', startIndex + 1)
                }
            }
        }
    }


    private fun colorizeNumbers(html: Spanned, context: Context): SpannableString {
        val spannableString = SpannableString(html)
        val regex = Regex("[=+\\-*/(,.ln]\\s*(-?\\d+)(,?)")
        val matcher = regex.toPattern().matcher(spannableString)
        while (matcher.find()) {
            val start = matcher.start(1)
            val end = matcher.end(1)
            spannableString.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(context, R.color.numbers_code)), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            if (matcher.group(2)?.isNotEmpty() == true) {
                spannableString.setSpan(
                    ForegroundColorSpan(ContextCompat.getColor(context, R.color.numbers_code)), end, end + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannableString
    }


    fun initAllViews(
        content: ThemeLevelContentEntity,
        binding: FragmentLessonBinding?,
        context: Context
    ) {
        binding?.apply {
            CoroutineScope(Dispatchers.Main).launch {
                processContent(
                    imFirst,
                    includedFirst.tvText,
                    includedFirst.included.cardCodeFragment,
                    includedFirst.included.tvCode,
                    content.imageFirst,
                    content.textFirst,
                    content.CodeFragmentFirst,
                    binding,
                    context
                )
                processContent(
                    includedSecond.im,
                    includedSecond.tvText,
                    includedSecond.included.cardCodeFragment,
                    includedSecond.included.tvCode,
                    content.imageSecond,
                    content.textSecond,
                    content.CodeFragmentSecond,
                    binding,
                    context
                )
                processContent(
                    includedThird.im,
                    includedThird.tvText,
                    includedThird.included.cardCodeFragment,
                    includedThird.included.tvCode,
                    content.imageThird,
                    content.textThird,
                    content.CodeFragmentThird,
                    binding,
                    context
                )
                processContent(
                    includedFourth.im,
                    includedFourth.tvText,
                    includedFourth.included.cardCodeFragment,
                    includedFourth.included.tvCode,
                    content.imageFourth,
                    content.textFourth,
                    content.CodeFragmentFourth,
                    binding,
                    context
                )
                processContent(
                    includedFifth.im,
                    includedFifth.tvText,
                    includedFifth.included.cardCodeFragment,
                    includedFifth.included.tvCode,
                    content.imageFifth,
                    content.textFifth,
                    content.CodeFragmentFifth,
                    binding,
                    context
                )
                processContent(
                    includedSixth.im,
                    includedSixth.tvText,
                    includedSixth.included.cardCodeFragment,
                    includedSixth.included.tvCode,
                    content.imageSixth,
                    content.textSixth,
                    content.CodeFragmentSixth,
                    binding,
                    context
                )

                processContent(
                    includedSeventh.im,
                    includedSeventh.tvText,
                    includedSeventh.included.cardCodeFragment,
                    includedSeventh.included.tvCode,
                    content.imageSeventh,
                    content.textSeventh,
                    content.CodeFragmentSeventh,
                    binding,
                    context
                )
                processContent(
                    includedEighth.im,
                    includedEighth.tvText,
                    includedEighth.included.cardCodeFragment,
                    includedEighth.included.tvCode,
                    content.imageEighth,
                    content.textEighth,
                    content.CodeFragmentEighth,
                    binding,
                    context
                )
                processContent(
                    includedNinth.im,
                    includedNinth.tvText,
                    includedNinth.included.cardCodeFragment,
                    includedNinth.included.tvCode,
                    content.imageNinth,
                    content.textNinth,
                    content.CodeFragmentNinth,
                    binding,
                    context
                )
                processContent(
                    includedTenth.im,
                    includedTenth.tvText,
                    includedTenth.included.cardCodeFragment,
                    includedTenth.included.tvCode,
                    content.imageTenth,
                    content.textTenth,
                    content.CodeFragmentTenth,
                    binding,
                    context
                )
            }
        }
    }
}