package workwork.test.andropediagits.data.local.entities.sqlInteractive

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("SqlTableInteractiveCorrectAnswerEntity")
data class SqlTableInteractiveCorrectAnswerEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val uniqueThemeId:Int,
    val testId:Int,
    val correctQuery:String,
    val correctIndexDataForList:Int
)