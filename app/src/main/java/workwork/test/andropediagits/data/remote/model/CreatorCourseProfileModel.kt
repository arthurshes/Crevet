package workwork.test.andropediagits.data.remote.model

import android.graphics.Bitmap

data class CreatorCourseProfileModel(
    val name:String,
    val image:Bitmap,
    val instagramLink:String?="",
    val vkLink:String?="",
    val telegramLink:String?="",
    val youtubeLink:String?="",
    val requisits:String
)
