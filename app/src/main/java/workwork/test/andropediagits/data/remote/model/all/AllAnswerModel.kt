package workwork.test.andropediagits.data.remote.model.all

import workwork.test.andropediagits.data.remote.model.UserProgressModel
import workwork.test.andropediagits.data.remote.model.course.CourseAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.LevelThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeAnswerModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeLevelContentModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineAnswerModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineAnswerVariantModel
import workwork.test.andropediagits.data.remote.model.victorine.VictorineClueModel

data class AllAnswerModel(
    val courses:List<CourseAnswerModel>,
    val themes:List<ThemeAnswerModel>,
    val victorines:List<VictorineAnswerModel>,
    val victorinesAnswerVariants:List<VictorineAnswerVariantModel>,
    val victorineClues:List<VictorineClueModel>?=null,
    val themeLessons:List<LevelThemeAnswerModel>,
    val themeLessonContents:List<ThemeLevelContentModel>,
//    val interactives:List<InteractiveAnswerModel>?=null,
//    val interactiveCodeVariants:List<InterActiveCodeVariantModel>?=null,
//    val interactiveCorrectAnswers:List<InteractiveCorrectCodeModel>?=null,
    val userProgress: UserProgressModel
)
