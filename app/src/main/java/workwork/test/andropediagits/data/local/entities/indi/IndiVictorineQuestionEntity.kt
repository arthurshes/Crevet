package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiVictorineEntityTable")
data class IndiVictorineQuestionEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val questionNumber:Int,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val questionText:String

)
