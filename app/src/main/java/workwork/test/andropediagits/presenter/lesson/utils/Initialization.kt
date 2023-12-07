package workwork.test.andropediagits.presenter.lesson.utils

import android.annotation.SuppressLint
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
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import coil.load
import dagger.Provides
import dagger.hilt.EntryPoint
import dagger.hilt.android.scopes.ActivityScoped
import workwork.test.andropediagits.presenter.lesson.utils.OptimizationText.setTextSize
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import workwork.test.andropediagits.R
import workwork.test.andropediagits.core.utils.Constatns

import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.databinding.FragmentLessonBinding
import workwork.test.andropediagits.presenter.lesson.utils.OptimizationText.optimizeCodeFragment
import java.util.regex.Pattern
import javax.inject.Inject

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
               val textReplace = text.replace(Regex("\\d+\\.?(?==)"), "")

                textView.text = OptimizationText.searchTitles(textReplace)

                textView.setTextSize(pref?.getString(Constatns.TEXT_KEY, "20"))
            } else {
                textView.visibility = View.GONE
                textView.text = ""
            }

            if (CodeFragment != null) {

                card.visibility = View.VISIBLE
                CodeFragment.replace("%%", "")
                CodeFragment.replace("^^", "")
                CodeFragment.replace(";;", "")
                CodeFragment.replace("**", "")
                CodeFragment.replace("[[", "")
                val keywords = OptimizationText.optimizeCodeFragment(CodeFragment)
                val keywords1 = OptimizationText.colorizeElements(keywords, context)
                val keywords2 = OptimizationText.specialCharacterColoring(keywords1, context)
                val html = Html.fromHtml(keywords2.toString(), Html.FROM_HTML_MODE_LEGACY)
                val spannableString = SpannableString(html)

                // Цвет для ключевых слов (например, fun, val)
                val annotationColor = ContextCompat.getColor(context,R.color.annotationColor)

//                applyColorToText(spannableString, "@", annotationColor)
//                applyColorToText(spannableString, "@InstallIn", annotationColor)
                val keywords23345 = arrayOf("@Module", "@InstallIn", "@Deprecated", "@Provides", "@Inject","@HiltAndroidApp","@AndroidEntryPoint","@HiltViewModel","@GET","@Query","@Delete","@POST","@DELETE","@PUT","@Path","@Insert","@Update","@Suppress","@SuppressLint","@Singleton","@Test","@RunWith","@VisibleForTesting","@Named","@SmallTest","@ExperimentalCoroutinesApi","@HiltAndroidTest","@Before","@After","@EntryPoint","@ActivityScoped","@ViewModelInject",)
                applyColorToText(spannableString, keywords23345, annotationColor)
                val keywords2334335 = arrayOf("buildscript","dependencies","plugins","jvmTarget","pluginManagement","repositories","dependencyResolutionManagement","repositoriesMode","rootProject")
                applyColorToText(spannableString, keywords2334335,  ContextCompat.getColor(context, R.color.variable_code))
                // Цвет для строк


                // Цвет для чисел
                val numberColor = context.getColor(R.color.numbers_code)
                applyColorToNumbers(spannableString, numberColor)
                val stringColor = context.getColor(R.color.text_code)
                applyColorToTextBetweenQuotes(spannableString, stringColor)

                // Цвет для комментариев
                val commentColor = context.getColor(R.color.comment_code)
                applyColorToTextStartingWith(spannableString, "//", commentColor)

                val regex = Regex("(val|var|fun)\\s(\\w+)")
                val matches = regex.findAll(html)
                for (match in matches) {
                    val start = match.range.first + 4
                    val end = match.range.last
                    if (end + 1 <= spannableString.length) {
                        val color = when (match.groupValues[1]) {
                            "val" -> ContextCompat.getColor(context, R.color.variable_code)
                            "var" -> ContextCompat.getColor(context, R.color.variable_code)
                            "fun" -> ContextCompat.getColor(context, R.color.function_code)
                            else -> ContextCompat.getColor(context, R.color.function_code)
                        }
                        spannableString.setSpan(ForegroundColorSpan(color), start, end + 1, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
                    } else {
                        // Обработка случая, когда end + 1 превышает длину строки
                        // Можете вывести сообщение об ошибке или выполнить другие действия по вашему усмотрению
                    }
                }

//                textCode.text = spannableStringtextCode.setTextSize(pref?.getString(Constatns.CODE_KEY, "20"))]
                textCode.text = spannableString
                textCode.setTextSize(pref?.getString(Constatns.CODE_KEY, "20"))
            } else {
                card.visibility = View.GONE
                textCode.text = ""
            }

        }
    }
    private fun applyColorToText(spannableString: SpannableString, keywords: Array<String>, color: Int) {
        for (keyword in keywords) {
            var startIndex = spannableString.indexOf(keyword)
            while (startIndex != -1) {
                val endIndex = startIndex + keyword.length
                spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                startIndex = spannableString.indexOf(keyword, endIndex)
            }
        }
    }

    private fun applyColorToTextBetweenQuotes(spannableString: SpannableString, color: Int) {
        val regex = "\"[^\"]*\"".toRegex()
        val matches = regex.findAll(spannableString)

        for (match in matches) {
            val startIndex = match.range.first
            val endIndex = match.range.last + 1
            spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun applyColorToNumbers(spannableString: SpannableString, color: Int) {
        val regex = "\\b\\d+\\b".toRegex()
        val matches = regex.findAll(spannableString)

        for (match in matches) {
            val startIndex = match.range.first
            val endIndex = match.range.last + 1
            spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun applyColorToTextStartingWith(spannableString: SpannableString, prefix: String, color: Int) {
        var startIndex = spannableString.indexOf(prefix)
        while (startIndex != -1) {
            val endIndex = spannableString.indexOf("\n", startIndex)
            if (endIndex == -1) {
                break
            }
            spannableString.setSpan(ForegroundColorSpan(color), startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            val nextStartIndex = spannableString.indexOf(prefix, endIndex)
            if (nextStartIndex == -1) {
                break
            }
            startIndex = nextStartIndex
        }
    }
    /* private fun afterPoint(
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
     }*/
    /* private fun quotesColorize(
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
     }*/


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