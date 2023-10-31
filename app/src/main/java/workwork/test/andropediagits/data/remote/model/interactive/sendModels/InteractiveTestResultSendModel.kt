package workwork.test.andropediagits.data.remote.model.interactive.sendModels

import java.util.Date

data class InteractiveTestResultSendModel(
    val dateTestApi:String?=null,
    val dateTestLocal:Date?=null,
    val token:String,
    val interactiveTestId:Int,
    val uniqueThemeId:Int,
    val mistakeAnswerCount:Int,
    val correctAnswerCount:Int
)
