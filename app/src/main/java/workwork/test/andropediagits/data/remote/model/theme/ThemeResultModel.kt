package workwork.test.andropediagits.data.remote.model.theme

data class ThemeResultModel(
    val expirationDate:String,
    val token:String,
    val transitDelayNextThemeHours:Int,
    val victorineResult: VictorineResultModel,
    val interactiveResult: InteractiveResultModel,
    val errorRate:Int
)
