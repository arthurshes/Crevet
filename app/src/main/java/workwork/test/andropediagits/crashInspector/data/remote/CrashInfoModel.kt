package workwork.test.andropediagits.crashInspector.data.remote

import java.util.Date

data class CrashInfoModel(
    val className:String,
    val dateCrash: Date,
    val brandPhone:String,
    val exception:String
)
