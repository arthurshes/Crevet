package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineClueEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.data.remote.model.victorine.VictorineAnswerModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineAnswerVariantModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineClueModel
import java.text.SimpleDateFormat


fun VictorineAnswerModel.toVictorineEntity(): VictorineEntity {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    val date = dateFormat.parse(lastUpdateDate)
    return VictorineEntity(questionId = questionId,
        questionText = questionText,
        uniqueThemeId = uniqueThemeId,
        vicotineTestId = vicotineTestId,
        lastUpdateDate = date,
        victorineTimeSec = victorineTimeSec.toLong() ///Тестовое нужно убрать потом
    )
}




fun VictorineAnswerVariantModel.toVictorineAnswerVariantEntity(): VictorineAnswerVariantEntity {
    val isCorrectAnswers = isCorrectAnswer == 1
    return VictorineAnswerVariantEntity(
        vicotineTestId = vicotineTestId,
        victorineAnswerId = victorineAnswerId,
        text = text,
        uniqueThemeId = uniqueThemeId,
        questionId = questionId,
        isCorrectAnswer = isCorrectAnswers
    )
}

fun VictorineClueModel.toVictorineClueEntity(): VictorineClueEntity {
    return VictorineClueEntity(
        questionId = questionId,
        uniqueThemeId = uniqueThemeId,
        clueText = clueText,
        victorineAnswerId = victorineAnswerId,
        victorineId = vicotineTestId,
    )
}