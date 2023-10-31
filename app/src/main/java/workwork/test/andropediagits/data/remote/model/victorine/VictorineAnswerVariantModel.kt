package workwork.test.andropediagits.data.remote.model.victorine

import java.util.Date

data class VictorineAnswerVariantModel(
    val isCorrectAnswer: Int,
    val language: String,
    val lastUpdateDate: String,
    val questionId: Int,
    val text: String,
    val uniqueThemeId: Int,
    val vicotineTestId: Int,
    val victorineAnswerId: Int
)
