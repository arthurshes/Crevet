package workwork.test.andropediagits.data.local.entities

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("UserInfoEntityTable")
data class UserInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val token:String,
    val name:String?=null,
    val image:Bitmap?=null,
    val userLanguage:String?=null,
    val phoneBrand:String?=null,
    var andropointCount:Int?=0,
    val lastOnlineDate:String?=null,
    val strikeModeDay:Int?=0,
    val lastOpenCourse:Int?=0,
    val lastOpenTheme:Int?=0,
    val isInfinity:Boolean?=false,
    val heartsCount:Int?=0
)
