package workwork.test.andropediagits.data.remote.model.updateDates

import java.util.Date

data class LastUpdateVictorineModel(
    val vicotineTestId:Int,
    val uniqueThemeId:Int,
    val questionId:Int,
    val lastUpdateDate: Date,
    val themeNumber:Int,
)
