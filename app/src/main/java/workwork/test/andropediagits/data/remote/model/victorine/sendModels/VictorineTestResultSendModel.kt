package workwork.test.andropediagits.data.remote.model.victorine.sendModels

import java.util.Date

data class VictorineTestResultSendModel(
    val token:String,
    val uniqueThemeId:Int,
    val vicotineTestId:Int,
    val mistakeAnswerCount:Int,
    val correctAnswerCount:Int,
    val dateTestApi:String?=null,
    val dateTestLocal: Date?=null
)
