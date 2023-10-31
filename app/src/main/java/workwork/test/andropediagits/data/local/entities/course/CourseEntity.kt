package workwork.test.andropediagits.data.local.entities.course

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("CourseEntityTable")
data class CourseEntity(
    @PrimaryKey(autoGenerate = false)
    val courseNumber:Int,
    val isOpen:Boolean,
    val possibleToOpenCourseFree:Boolean = true,
    val courseName:String,
    val description:String?=null,
    val lastUpdateDate: Date,
    val isNetworkConnect:Boolean = true,
    val coursePriceRub:Int?=null,
    val coursePriceAndropoint:Int?=null
)
