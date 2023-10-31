package workwork.test.andropediagits.data.local.entities.interactive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("interactiveCodeVariantEntityTable")
data class InteractiveCodeVariantEntity(
    @PrimaryKey(autoGenerate = false)
    val variantId:Int,
    val interactiveTestId:Int,
    val taskId:Int,
    val text:String
)
