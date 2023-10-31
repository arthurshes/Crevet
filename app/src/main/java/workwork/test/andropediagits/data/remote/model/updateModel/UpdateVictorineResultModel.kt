package workwork.test.andropediagits.data.remote.model.updateModel

data class UpdateVictorineResultModel(
    val uniqueThemeId:Int,
    val themeNumber:Int,
    val courseNumber:Int,
    val dateApi:String,
    val token:String,
    val victorineId:Int,
    val correctAnswer:Int,
    val mistakeAnswer:Int
)
