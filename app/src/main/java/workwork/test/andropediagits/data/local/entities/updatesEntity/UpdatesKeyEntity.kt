package workwork.test.andropediagits.data.local.entities.updatesEntity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("updateEntityKeyTable")
data class UpdatesKeyEntity(
    @PrimaryKey(autoGenerate = true)
    val updateKeyId:Int?=null,
    val courseNumber:Int?=null,
    val uniqueThemeId:Int?=null,
    val uniqueLevelId:Int?=null,
    val interactiveTestId:Int?=null,
    val buyCourseNumber:Int?=null,
    val buyThemeUniqueId:Int?=null,
    val vicotineTestId:Int?=null,
    val subscribeTrasaction:String?=null,
    val updateNameBoolean:Boolean ?= false,
    val updateTime:Date,
    val openCourseNumber:Int?=null,
    val openUniqueThemeId:Int?=null
)