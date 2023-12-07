package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("IndiLessonContentEntityTable")
data class IndiLessonContentEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val createrToken:String,
    val uniqueCourseNumber:Int,
    val themeNumber:Int,
    val themeName:String,
    val lessonNumber:Int,
    val lessonName:String,
    val imageFirst: String?=null,
    val textTitle: String?=null,
    val textFirst: String?=null,
    val CodeFragmentFirst: String?=null,
    val imageSecond: String?=null,
    val textSecond: String?=null,
    val CodeFragmentSecond: String?=null,
    val imageThird: String?=null,
    val textThird: String?=null,
    val CodeFragmentThird: String?=null,
    val imageFourth: String?=null,
    val textFourth: String?=null,
    val CodeFragmentFourth: String?=null,
    val imageFifth: String?=null,
    val textFifth: String?=null,
    val CodeFragmentFifth: String?=null,
    val imageSixth: String?=null,
    val textSixth: String?=null,
    val CodeFragmentSixth: String?=null,
    val imageSeventh: String?=null,
    val textSeventh: String?=null,
    val CodeFragmentSeventh: String?=null,
    val imageEighth: String?=null,
    val textEighth: String?=null,
    val CodeFragmentEighth: String?=null,
    val imageNinth: String?=null,
    val textNinth: String?=null,
    val CodeFragmentNinth: String?=null,
    val imageTenth: String?=null,
    val textTenth: String?=null,
    val CodeFragmentTenth: String?=null
)
