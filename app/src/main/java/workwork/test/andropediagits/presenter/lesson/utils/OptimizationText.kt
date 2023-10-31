package workwork.test.andropediagits.presenter.lesson.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import workwork.test.andropediagits.R
import workwork.test.andropediagits.databinding.FragmentLessonBinding


object OptimizationText {
     fun specialCharacterColoring(inputText: String,context: Context): SpannableStringBuilder {
        val words = inputText.split(" ")
        val spannableStringBuilder = SpannableStringBuilder()

        val keywordsColor = ContextCompat.getColor(context, R.color.keywords_color)

        for (word in words) {
            val spannableWord = SpannableString(word)
            val manifestColor = ContextCompat.getColor(context, R.color.manifest_keywords_color)
            when (word) {
                "manifest" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "uses-permission" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "activity" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "intent-filter" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "provider" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "application" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "action" -> spannableWord.setSpan(ForegroundColorSpan(manifestColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "var" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "val" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "while" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "fun" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "when" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "override" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "super" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "init" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "private" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "protected" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "this" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "public" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "lateinit" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "," -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "for" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "in" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "true" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "false" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "null" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "if" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "else" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "return" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "object" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                "import" -> spannableWord.setSpan(ForegroundColorSpan(keywordsColor), 0, word.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                else->{}
            }

            spannableStringBuilder.append(spannableWord).append(" ")
        }

        return spannableStringBuilder
    }

//   fun optimizeCodeFragment(text: String): String {
//        return text.replace(",,", "\n").replace("##", "\t")
//    }

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
    fun colorizeElements(input: String): String {
        val patternsAndColors = mapOf(
            ";;" to "#56864E",
            "^^" to "#F7C067",
            "**" to "#9573A6",
            "[[" to "#5C92BA",
            "%%" to "#7A785E"
        )

        var result = input
        for ((pattern, color) in patternsAndColors) {
            val escapedPattern = Regex.escape(pattern) // Экранируем специальные символы
            val regex = Regex("($escapedPattern)(.*?)($escapedPattern)")
            result = regex.replace(result) {
                val matchedText = it.groupValues[2]
                "<font color=\"$color\">$matchedText</font>"
            }
        }

        return result
    }
     fun TextView.setTextSize(size:String?){
        if(size != null)  this.textSize=size.toFloat()
    }
}