package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiVictorineAnswerEntityTable")
data class IndiVictorineAnswerVarEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val variantText:String,
    val isCorrectVariant:Boolean,
    val questionNumber:Int
)
