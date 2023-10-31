package workwork.test.andropediagits.data.remote.model.updateDates

import java.util.Date

data class LastUpdateLevelModel(
    val lastUpdateDate: Date,
    val courseNumber:Int,
    val themeNumber:Int,
    val uniqueThemeId:Int,
    val levelNumber:Int,
    val uniqueLeveId:Int
)
