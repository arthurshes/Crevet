package workwork.test.andropediagits.data.remote.model.updateDates

import java.util.Date

data class LastUpdateInteractiveModel(
    val lastUpdateDate: Date,
    val themeNumber:Int,
    val uniqueThemeId:Int,
    val taskId:Int,
    val interactiveTestId:Int
)
