package workwork.test.andropediagits.data.remote.model.course



data class CourseAnswerModel(
    val courseName: String,
    val courseNumber: Int,
    val description: String,
    val isClosing: Int,
    val isFullyPaid: Int,
    val language: String,
    val lastUpdateDate: String,
    val possibleToOpenCourseFree: Int,
    val themesCount: Int,
    val coursePriceRub:Int?=null,
    val coursePriceAndropoint:Int?=null
)