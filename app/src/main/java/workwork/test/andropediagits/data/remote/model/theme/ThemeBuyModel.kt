package workwork.test.andropediagits.data.remote.model.theme

import java.util.Date

data class ThemeBuyModel(
    val token:String,
    val dateBuyApi:String,
    val dateBuyForDate: Date?=null,
    val uniqueThemeId:Int,
    val themeNumber:Int,
    val courseNumber:Int,
    val codeAnswer:Int?=null,
    val promoCode:String?=null,
    val transactionId:String
)
