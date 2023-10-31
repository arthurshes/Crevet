package workwork.test.andropediagits.data.remote.model.theme

import android.support.v4.app.INotificationSideChannel

data class VictorineResultModel(
    val datePassing:String,
    val token:String,
    val misstakeAnswer:Int,
    val correctAnswer:Int,
    val andropointsAdd:Int
)
