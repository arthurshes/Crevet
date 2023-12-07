package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiVictorineClueEntityTable")
data class IndiVIctorineClueEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val questionNumber:Int,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val clueText:String
)
