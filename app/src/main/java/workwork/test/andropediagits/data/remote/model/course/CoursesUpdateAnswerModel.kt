package workwork.test.andropediagits.data.remote.model.course

data class CoursesUpdateAnswerModel(
    val updateCourse:Boolean,
    val updateTheme:Boolean,
    val updateLevels:Boolean,
    val updateCourseNumbers:List<Int>?=null,
    val updateUniqueThemeId:List<Int>?=null,
    val updateLevelUniqueId:List<Int>?=null
)
