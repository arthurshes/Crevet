package workwork.test.andropediagits.presenter.lesson.utils

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import workwork.test.andropediagits.R
import workwork.test.andropediagits.databinding.FragmentLessonBinding


object OptimizationText {




    fun specialCharacterColoring(inputText: String,context: Context): String {
        var result = inputText
        var coloredText=""

        val codeColor = ContextCompat.getColor(context,R.color.white)
        val keywordsColor = ContextCompat.getColor(context, R.color.keywords_color) .toString()
        val manifestColor = ContextCompat.getColor(context, R.color.manifest_keywords_color).toString()
        val annotationColor = ContextCompat.getColor(context,R.color.annotationColor)
        val customColors = mapOf(
            "data" to keywordsColor,
            "void" to keywordsColor,
            "+" to codeColor,
            "-" to codeColor,
            "=" to codeColor,
            "*" to codeColor,
            "println(" to codeColor,
            "{" to codeColor,
            "}" to codeColor,
            "(" to codeColor,
            ")" to codeColor,
            "Log.d(" to codeColor,
            "String" to codeColor,
            "Int" to codeColor,
            ":" to codeColor,
            "Boolean" to codeColor,
            "Float" to codeColor,
            "Double" to codeColor,
            "Context" to codeColor,
            "@Suppress" to annotationColor,
            "@HiltAndroidApp" to annotationColor,
            "@HiltViewModel" to annotationColor,
            "@Module" to annotationColor,
            "@InstallIn" to annotationColor,
            "@Provides" to annotationColor,
            "@AndroidEntryPoint" to annotationColor,
            "navArgs" to manifestColor,
            "viewModels" to manifestColor,
            "@Deprecated" to annotationColor,
            "@Inject" to annotationColor,
            "@Singleton" to annotationColor,
            "forEach" to manifestColor,
            "const" to keywordsColor,
            "operator" to keywordsColor,
            "open" to keywordsColor,
            "class" to keywordsColor,
            "internal" to keywordsColor,
            "interface" to keywordsColor,
            "abstract " to keywordsColor,
            "var" to keywordsColor,        "null" to keywordsColor,
            "val" to keywordsColor ,        "var" to keywordsColor ,
            "while" to keywordsColor,        "fun" to keywordsColor,
            "when" to keywordsColor,        "override" to keywordsColor,
            "super" to keywordsColor,        "init" to keywordsColor,
            "private" to keywordsColor,        "protected" to keywordsColor,
            "this" to keywordsColor,        "public" to keywordsColor,
            "lateinit" to keywordsColor,        "," to keywordsColor,
            "for" to keywordsColor,        "in" to keywordsColor,
            "true" to keywordsColor,        "false" to keywordsColor,
            "null" to keywordsColor,        "if" to keywordsColor,
            "else" to keywordsColor,        "return" to keywordsColor,
            "object" to keywordsColor,        "import" to keywordsColor,
            "manifest" to manifestColor,        "uses-permission" to manifestColor,
            "activity" to manifestColor,        "intent-filter" to manifestColor,
            "provider" to manifestColor,        "application" to manifestColor,
            "action" to manifestColor)
        for ((word, color) in customColors) {
            val escapedWord = Regex.escape(word)
            val regex = Regex("(\\b$escapedWord\\b)")
            result =  regex.replace(result) {
                "<font color=\"$color\">$word</font>"    }
        }
        return result
    }

    fun optimizeCodeFragment(text: String): String {
        return text.replace("##", "<br>").replace(",,,", "\t")
    }

    fun searchTitles(text: String,binding: FragmentLessonBinding?): String {
        val pattern = "=([^=]*)="
        val matcher = pattern.toRegex().findAll(text)
        val extractedList = matcher.map { it.groupValues[1] }.toList()
        binding?.apply {

            if (extractedList.size >= 1) {
                includedFirst.tvTitleSmall.text = extractedList[0]
            }
            if (extractedList.size >= 2) {
                includedSecond.tvTitleSmall.text = extractedList[1]
            }
            if (extractedList.size >= 3) {
                includedThird.tvTitleSmall.text = extractedList[2]
            }
            if (extractedList.size >= 4) {
                includedFourth.tvTitleSmall.text = extractedList[3]
            }
            if (extractedList.size >= 5) {
                includedFifth.tvTitleSmall.text = extractedList[4]
            }
            if (extractedList.size >= 6) {
                includedSixth.tvTitleSmall.text = extractedList[5]
            }
            if (extractedList.size >= 7) {
                includedSeventh.tvTitleSmall.text = extractedList[6]
            }
            if (extractedList.size >= 8) {
                includedEighth.tvTitleSmall.text = extractedList[7]
            }
            if (extractedList.size >= 9) {
                includedNinth.tvTitleSmall.text = extractedList[8]
            }
            if (extractedList.size >= 10) {
                includedTenth.tvTitleSmall.text = extractedList[9]
            }
        }
        val formattedText = text.replace(pattern.toRegex(), "")
        return Html.fromHtml(formattedText, Html.FROM_HTML_MODE_COMPACT).toString()
    }


    fun colorizeElements(input: String, context: Context): String {
        val patternsAndColors = mapOf(
            ";;" to "${ContextCompat.getColor(context, R.color.text_code)}",
            "^^" to "${ContextCompat.getColor(context, R.color.function_code)}",
            "**" to "${ContextCompat.getColor(context, R.color.variable_code)}",
            "[[" to "${ContextCompat.getColor(context, R.color.numbers_code)}",
            "%%" to "${ContextCompat.getColor(context, R.color.comment_code)}"
        )
        var result = input
        for ((pattern, color) in patternsAndColors) {
            val escapedPattern = Regex.escape(pattern) // Экранируем специальные символы
            val regex = Regex("($escapedPattern)(.*?)($escapedPattern)")
            result = regex.replace(result) {
                val matchedText = it.groupValues[2]
                "<font color=\"$color\">$matchedText</font>"
            }
            val regexNumber = Regex("(?<==)\\d+")
            result = regexNumber.replace(result) { matchResult ->
                val digit = matchResult.value
                "<font color=\"#FF0000\">$digit</font>"
            }
        }
        return result
    }

    fun TextView.setTextSize(size:String?){
        if(size != null)  this.textSize=size.toFloat()
    }

}