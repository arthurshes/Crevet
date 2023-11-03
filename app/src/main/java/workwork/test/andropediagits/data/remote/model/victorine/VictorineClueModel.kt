package workwork.test.andropediagits.data.remote.model.victorine

import java.util.Date

data class VictorineClueModel(
    val vicotineTestId:Int,
    val uniqueThemeId:Int,
    val questionId:Int,
    val lastUpdateDate: Date,
    val clueText:String,
)
