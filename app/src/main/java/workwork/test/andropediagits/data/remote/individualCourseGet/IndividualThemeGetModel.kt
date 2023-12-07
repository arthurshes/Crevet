package workwork.test.andropediagits.data.remote.model.individualCourse

import android.graphics.Bitmap

data class IndividualThemeGetModel(
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val themeName:String,
    val themeImage:String?=null
)
