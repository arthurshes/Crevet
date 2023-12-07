package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiThemeEntityTable")
data class IndiThemeEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val themeName:String,
    val themeImage:String?=null
)
