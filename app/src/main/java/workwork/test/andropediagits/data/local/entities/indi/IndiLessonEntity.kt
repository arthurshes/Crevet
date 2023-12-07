package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiLessonEntityTable")
data class IndiLessonEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val themeName:String,
    val lessonNumber:Int,
    val lessonName:String
)
