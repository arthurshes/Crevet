package workwork.test.andropediagits.presenter.lesson.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import androidx.cardview.widget.CardView
import androidx.preference.PreferenceManager
import coil.load
import workwork.test.andropediagits.presenter.lesson.utils.OptimizationText.setTextSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.utils.Constatns

import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.databinding.FragmentLessonBinding

object Initialization {
    private var pref: SharedPreferences?=null



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
            Log.d("inititnit39394949",text.toString())
            if (text != null) {
                textView.visibility = View.VISIBLE
                textView.text = OptimizationText.searchTitles(text,binding)
//                textView.text = text
                textView.setTextSize(pref?.getString(Constatns.TEXT_KEY, "30"))
            } else {
                textView.visibility = View.GONE
                textView.text = ""
            }

            if (CodeFragment != null) {
                card.visibility = View.VISIBLE
                val keywords = OptimizationText.optimizeCodeFragment(CodeFragment)
                val keywords1 = OptimizationText.colorizeElements(keywords)
                val keywords2 = OptimizationText.specialCharacterColoring(keywords1, context)
                textCode.text = Html.fromHtml(keywords2.toString(), Html.FROM_HTML_MODE_LEGACY)
                textCode.setTextSize(pref?.getString(Constatns.CODE_KEY, "30"))
            } else {
                card.visibility = View.GONE
                textCode.text = ""
            }

        }
    }
     fun initAllViews(content: ThemeLevelContentEntity, binding: FragmentLessonBinding?, context: Context) {
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