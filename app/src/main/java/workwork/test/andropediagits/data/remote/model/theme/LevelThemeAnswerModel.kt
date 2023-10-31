package workwork.test.andropediagits.data.remote.model.theme


import java.util.Date

data class LevelThemeAnswerModel(

    val levelName:String,
    val lastUpdateDate: Date,
    val levelNumber:Int,
    val courseNumber:Int,
    val themeNumber:Int,
    val uniqueLevelId:Int,
    val uniqueThemeId:Int,
//    val levelContent:List<ThemeLevelContentModel>
//     val imagesUrl:List<String>?=null,
//    val codeFragments:List<String>?=null
)