package workwork.test.andropediagits.data.remote.model.interactive

import java.util.Date

data class InteractiveAnswerModel(
    val interactiveTestId:Int,
    val uniqueThemeId:Int,
    val lastUpdateDate: Date,
    val taskId:Int,
    val taskDetailsText:String,
//    val interactiveCodeVariants:List<InterActiveCodeVariantModel>
)
