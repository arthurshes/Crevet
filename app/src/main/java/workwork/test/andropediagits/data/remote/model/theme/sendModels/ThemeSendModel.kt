package workwork.test.andropediagits.data.remote.model.theme.sendModels

import java.util.Date

data class ThemeSendModel(
    val token:String,
    val uniqueThemeId:Int,
    val isClosing:Boolean,
    val dateUpdateLocal:Date?=null,
    val dateUpdateApi:String?=null,
    val correctAnswerProcent:Int?=null
)
