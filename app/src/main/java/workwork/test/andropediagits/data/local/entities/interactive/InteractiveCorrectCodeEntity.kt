package workwork.test.andropediagits.data.local.entities.interactive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interactiveCorrectCodeEntityTable")
data class InteractiveCorrectCodeEntity(
    @PrimaryKey(autoGenerate = false)
    val taskId:Int,
    val correctAnswer:String,
    val interactiveTestId:Int,
    val uniqueThemeId:Int
)
