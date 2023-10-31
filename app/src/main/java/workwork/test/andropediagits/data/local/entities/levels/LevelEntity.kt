package workwork.test.andropediagits.data.local.entities.levels

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("LevelEntityTableCache")
data class LevelEntity(
    @PrimaryKey(autoGenerate = false)
    val uniqueLevelId:Int,
    val uniqueThemeId:Int,
    val courseNumber:Int,
    val themeNumber:Int,
    val levelNumber:Int,
    val levelName:String,
    val lastUpdateDate:Date,
    val isFav:Boolean = false
//    val levelContent:List<ThemeLevelContentEntity>,
)
