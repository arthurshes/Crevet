package workwork.test.andropediagits.data.remote.model.individualCourse

data class IndividualGetModel(
    val course:IndividualCourseGetModel,
    val themes:List<IndividualThemeGetModel>,
    val lessons:List<IndividualLessonGetModel>,
    val content:List<IndividualLessonContentGetModel>,
    val victorine:IndividualVictorineGetModel,
    val createrToken:String,
    val lastUpdateDate:String,
    val createrName:String,
    val createrImage:String?=null
)
