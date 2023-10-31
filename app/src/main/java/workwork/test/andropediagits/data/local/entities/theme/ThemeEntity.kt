package workwork.test.andropediagits.data.local.entities.theme


import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("themeEntityTable")
data class ThemeEntity(
    @PrimaryKey(autoGenerate = false)
    val uniqueThemeId:Int,
    val lastUpdateDate: Date,
    val themeName:String,
    val courseNumber:Int,
    val lessonsCount:Int,
    val themeNumber:Int,
    val imageTheme:Bitmap?,
    val lastCourseTheme:Boolean,
    val interactiveCodeMistakes:Int?=0,
    val interactiveCodeCorrect: Int?=0,
    val victorineMistakeAnswer:Int?=0,
    val victorineCorrectAnswer:Int?=0,
    val victorineDate: Date?=null,
    val interactiveTestId:Int,
    val victorineQuestionCount:Int,
    val interactiveQuestionCount:Int,
    val vicotineTestId:Int,
    val duoDate: Date?=null,
    val isDuoInter:Boolean = false,
    val isVictorine:Boolean = false,
    val isOpen:Boolean = false,
    val isFav:Boolean = false,
    var termHourse:Int?=null,
    val termDateApi:String?=null,
    val possibleToOpenThemeFree:Boolean,
    val isThemePassed:Boolean=false,
    val themePrice:Int?=null,
    val andropointsPrice:Int?=null
)
