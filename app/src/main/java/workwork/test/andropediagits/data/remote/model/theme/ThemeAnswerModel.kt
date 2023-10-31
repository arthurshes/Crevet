package workwork.test.andropediagits.data.remote.model.theme

import android.graphics.Bitmap



data class ThemeAnswerModel(
    val courseName: String,
    val courseNumber: Int,
    val imageTheme: Bitmap?=null,
    val interactiveQuestionCount: Int,
    val interactiveTestId: Int,
    val isFreeTheme: Int,
    val isFullyPaid: Int,
    val isOpen: Int,
    val language: String,
    val lastThemeCourse: Int,
    val lastUpdateDate: String,
    val lessonsCount: Int,
    val levelsCount: Int,
    val termDateApi: String,
    val termHourse: Int,
    val themeName: String,
    val themeNumber: Int,
    val uniqueThemeId: Int,
    val vicotineTestId: Int,
    val victorineQuestionCount: Int,
    val themePrice:Int?=null,
    val andropointsPrice:Int?=null
)
