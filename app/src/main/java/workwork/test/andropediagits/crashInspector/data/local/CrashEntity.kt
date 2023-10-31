package workwork.test.andropediagits.crashInspector.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("crashEntityTable")
data class CrashEntity (
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val className:String,
    val dateCrash:Date,
    val exception:String,
    val brandPhone:String
    )