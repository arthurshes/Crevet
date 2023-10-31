package workwork.test.andropediagits.data.remote.model.daggerModels

data class DaggerTestModel(
    val uniqueThemeId:Int,
    val themeNumber:Int,
    val courseNumber:Int,
    val testId:Int,
    val tasksDi:List<DaggerTaskClassModel>,
    val tasksClue:List<DaggerClueAnswerModel>,
    val tasksCorrectChains:List<DaggerClassesCorrectModel>
)
