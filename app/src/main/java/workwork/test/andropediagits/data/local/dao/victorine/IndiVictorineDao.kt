package workwork.test.andropediagits.data.local.dao.victorine

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import workwork.test.andropediagits.data.local.entities.indi.IndiVIctorineClueEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineAnswerVarEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineQuestionEntity

@Dao
interface IndiVictorineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndiQuestion(indiVictorineQuestionEntity: IndiVictorineQuestionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndiClue(indiVIctorineClueEntity: IndiVIctorineClueEntity)

    @Query("DELETE FROM IndiVictorineClueEntityTable WHERE createrToken = :createrToken AND uniqueCourseNumber =:uniqueCourseNumber")
    suspend fun deleteIndiVictorineClue(createrToken:String,uniqueCourseNumber:Int)

    @Query("DELETE FROM IndiVictorineAnswerEntityTable WHERE createrToken = :createrToken AND uniqueCourseNumber =:uniqueCourseNumber")
    suspend fun deleteIndiVictorineAnswers(createrToken:String,uniqueCourseNumber:Int)

    @Query("DELETE FROM IndiVictorineEntityTable WHERE createrToken = :createrToken AND uniqueCourseNumber =:uniqueCourseNumber")
    suspend fun deleteIndiVicotineQuestions(createrToken:String,uniqueCourseNumber:Int)

    @Query("SELECT * FROM IndiVictorineClueEntityTable WHERE createrToken =:createrToken AND uniqueCourseNumber =:uniqueCourseNumber AND themeNumber =:themeNumber AND questionNumber = :questionNumber ")
    suspend fun getIndiClueTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int,questionNumber:Int):List<IndiVIctorineClueEntity>


    @Query("SELECT * FROM IndiVictorineAnswerEntityTable WHERE createrToken =:createrToken AND uniqueCourseNumber =:uniqueCourseNumber AND themeNumber =:themeNumber AND questionNumber = :questionNumber ")
    suspend fun getIndiAnswerVartionsTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int,questionNumber:Int):List<IndiVictorineAnswerVarEntity>

    @Query("SELECT * FROM IndiVictorineEntityTable WHERE createrToken =:createrToken AND uniqueCourseNumber =:uniqueCourseNumber AND themeNumber =:themeNumber")
    suspend fun getIndiQuestionsTheme(createrToken:String,uniqueCourseNumber:Int,themeNumber:Int):List<IndiVictorineQuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIndiAnswerVariant(indiVictorineAnswerVarEntity: IndiVictorineAnswerVarEntity)

}