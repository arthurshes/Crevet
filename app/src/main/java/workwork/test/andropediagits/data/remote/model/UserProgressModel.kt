package workwork.test.andropediagits.data.remote.model

data class UserProgressModel(
    val lastOpenCourse:Int?=null,
    val lastOpenTheme:Int?=null,
    val termHourse:Int?=null,
    val termDateApi:String?=null,
    val termAds:String?=null,
    val lastCourseThemePasses:Int
)
