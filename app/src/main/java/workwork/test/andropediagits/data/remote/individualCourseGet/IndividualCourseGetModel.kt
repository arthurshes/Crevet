package workwork.test.andropediagits.data.remote.model.individualCourse

data class IndividualCourseGetModel(
    val createrToken:String,
    val coursePrice:String,
    val payRequisits:String,
    val courseName:String,
    val courseDescription:String,
    val uniqueCourseNumber:Int,
    val createrName:String,
    val createrImage:String?=null,
    val versionCourse:Int
)