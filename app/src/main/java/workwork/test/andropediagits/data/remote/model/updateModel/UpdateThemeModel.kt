package workwork.test.andropediagits.data.remote.model.updateModel

data class UpdateThemeModel(
    val themeNumber:Int,
    val courseNumber:Int,
    val uniqueThemeId:Int,
    val victorineId:Int,
    val interactiveId:Int,
    val token:String,
    val dateApi:String,
    val termHourse:Int?=0,
    val isOpenTheme:Boolean,
    val termDateApi:String?="",
    val lasThemePassed:Boolean
)