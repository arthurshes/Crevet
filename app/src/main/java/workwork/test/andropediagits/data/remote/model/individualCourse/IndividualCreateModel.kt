package workwork.test.andropediagits.data.remote.model.individualCourse

data class IndividualCreateModel(
    val course:IndividualCourseGetModel,
    val themes:List<IndividualThemeGetModel>,
    val lessons:List<IndividualLessonGetModel>,
    val content:List<IndividualLessonContentGetModel>,
    val victorine:IndividualVictorineGetModel,
    val createrToken:String,
    val lastUpdateDate:String
)
