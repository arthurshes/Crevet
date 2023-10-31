package workwork.test.andropediagits.data.remote.model.updateModel

data class UpdateCourseModel(
    val token:String,
    val courseNumber:Int,
    val dateApi:String,
    val isBuyCourse:Boolean?=null,
    val isOpenCourse:Boolean
)
