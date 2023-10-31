package workwork.test.andropediagits.data.local.entities.theme

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("themeBuyEntityTable")
data class ThemeBuyEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val courseNumber:Int,
    val themeNumber:Int,
    val uniqueThemeId:Int,
    val date: Date,
    val token:String,
    val transactionId:String
)
