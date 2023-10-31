package workwork.test.andropediagits.data.local.entities.victorine

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("victorineClueEntity")
data class VictorineClueEntity(
    @PrimaryKey(autoGenerate = true)
    val cluevictorineId:Int?=null,
    val victorineId:Int,
    val questionId:Int,
    val clueText:String,
    val victorineAnswerId:Int,
    val uniqueThemeId:Int
)
