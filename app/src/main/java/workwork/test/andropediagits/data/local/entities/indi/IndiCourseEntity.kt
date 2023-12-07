package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiCourseEntityTable")
data class IndiCourseEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val createrToken:String,
    val coursePrice:String,
    val payRequisits:String,
    val courseName:String,
    val courseDescription:String,
    val uniqueCourseNumber:Int,
    val createrName:String,
    val createrImage:String?=null,
    val versionCourse:Int
)
