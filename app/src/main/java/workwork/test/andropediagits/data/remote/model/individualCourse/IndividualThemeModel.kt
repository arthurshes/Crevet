package workwork.test.andropediagits.data.remote.model.individualCourse

import android.graphics.Bitmap

data class IndividualThemeModel(
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val themeName:String,
    val themeImage:Bitmap?=null
)
