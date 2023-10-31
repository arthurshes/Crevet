package workwork.test.andropediagits.data.remote.model.interactive

import java.util.Date

data class InteractiveCorrectCodeModel(
    val correctAnswer:String,
    val interactiveTestId:Int,
    val uniqueThemeId:Int,
    val lastUpdateDate: Date,
    val taskId:Int
)
