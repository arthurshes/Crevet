package workwork.test.andropediagits.data.local.entities.sqlInteractive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("SqlTableInteractiveEntity")
data class SqlTableInteractiveEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val uniqueThemeId:Int,
    val testId:Int,
    val questionText:String
)