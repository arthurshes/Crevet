package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("indiCourseBuyTable")
data class IndiCourseBuyEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val uniqueCourseNumber:Int,
    val creatorToken:String
)