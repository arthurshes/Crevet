package workwork.test.andropediagits.data.local.entities.interactive

import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.Date

@Entity("interactiveEntityTable")
data class InteractiveEntity(
    @PrimaryKey(autoGenerate = false)
    val taskId:Int,
    val interactiveTestId:Int,
    val uniqueThemeId:Int,
    val taskDetailsText:String,
    val lastUpdateDate: Date
)
