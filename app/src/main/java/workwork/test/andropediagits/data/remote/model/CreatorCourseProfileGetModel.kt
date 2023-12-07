package workwork.test.andropediagits.data.remote.model

import android.graphics.Bitmap
import workwork.test.andropediagits.data.remote.model.individualCourse.IndividualCourseGetModel

data class CreatorCourseProfileGetModel(
    val name:String,
    val image: String,
    val instagramLink:String?="",
    val vkLink:String?="",
    val telegramLink:String?="",
    val youtubeLink:String?="",
    val hisCourses:List<IndividualCourseGetModel>
)
