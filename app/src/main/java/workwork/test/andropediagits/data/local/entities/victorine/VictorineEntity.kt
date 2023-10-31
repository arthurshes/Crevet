package workwork.test.andropediagits.data.local.entities.victorine

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("victorineEntityTable")
data class VictorineEntity(
    @PrimaryKey(autoGenerate = false)
    val questionId:Int,
    val vicotineTestId:Int,
    val uniqueThemeId:Int,
    val questionText:String,
    val lastUpdateDate: Date,
    val victorineTimeSec:Long
)
