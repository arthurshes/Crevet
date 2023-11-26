package workwork.test.andropediagits.data.remote.model.updateModel

data class UpdateAnswerModel(
    val updateThemes:UpdateThemeModel?=null,
//    val updateInteractiveResult:List<UpdateInteractiveResultModel>?=null,
    val updateCourses:UpdateCourseModel?=null,
//    val updateVictorineResult:List<UpdateVictorineResultModel>?=null,
    val AndropointCount:Int?=0,
    val token:String
)
