package workwork.test.andropediagits.data.remote.model.updateModel

data class UpdateInteractiveResultModel(
    val uniqueThemeId:Int,
    val themeNumber:Int,
    val courseNumber:Int,
    val dateApi:String,
    val token:String,
    val interactiveId:Int,
    val correctAnswer:Int,
    val mistakeAnswer:Int
)
