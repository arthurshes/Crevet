package workwork.test.andropediagits.data.remote.model

import android.graphics.Bitmap
import java.util.Date

data class UserSignInModel(
    val name:String?=null,
    val image:Bitmap?=null,
    val token:String,
    val userlanguage:String?=null,
    val andropointCount:Int?=null,
    val strikeModeDay:Int?=null,
    val lastCourseNumber:Int?=null,
    val lastThemeNumber:Int?=null,
    val isInfinity:Int?=null,
    val heartsCount:Int
)
