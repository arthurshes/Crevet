package workwork.test.andropediagits.data.local.entities.levels

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

import java.util.Date

@Entity("ThemeLevelContentEntityTable")
data class ThemeLevelContentEntity(
    @PrimaryKey(autoGenerate = false)
    val uniqueLevelContentId: Int,
    val courseNumber:Int,
    val themeNumber:Int,
    val levelNumber:Int,
    val uniqueLevelId:Int,
    val uniqueThemeId:Int,
    val lastUpdateDate: Date,
    val contentIndexList:Int,
   // val text:String,
    val imageFirst: Bitmap?,
    val textTitle: String?,
    val textFirst: String?,
    val CodeFragmentFirst: String?,
    val imageSecond: Bitmap?,
    val textSecond: String?,
    val CodeFragmentSecond: String?,
    val imageThird: Bitmap?,
    val textThird: String?,
    val CodeFragmentThird: String?,
    val imageFourth: Bitmap?,
    val textFourth: String?,
    val CodeFragmentFourth: String?,
    val imageFifth: Bitmap?,
    val textFifth: String?,
    val CodeFragmentFifth: String?,
    val imageSixth: Bitmap?,
    val textSixth: String?,
    val CodeFragmentSixth: String?,
    val imageSeventh: Bitmap?,
    val textSeventh: String?,
    val CodeFragmentSeventh: String?,
    val imageEighth: Bitmap?,
    val textEighth: String?,
    val CodeFragmentEighth: String?,
    val imageNinth: Bitmap?,
    val textNinth: String?,
    val CodeFragmentNinth: String?,
    val imageTenth: Bitmap?,
    val textTenth: String?,
    val CodeFragmentTenth: String?,
)
