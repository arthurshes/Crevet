package workwork.test.andropediagits.data.local.entities.victorine

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("victorineAnswerVariantEntityTable")
data class VictorineAnswerVariantEntity(
    @PrimaryKey(autoGenerate = false)
    val victorineAnswerId:Int,
    val vicotineTestId:Int,
    val uniqueThemeId:Int,
    val text:String,
    val questionId:Int,
    val isCorrectAnswer:Boolean
)
