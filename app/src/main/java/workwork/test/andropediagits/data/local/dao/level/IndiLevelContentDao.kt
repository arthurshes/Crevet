package workwork.test.andropediagits.data.local.dao.level

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity

@Dao
interface IndiLevelContentDao {

    @Query("DELETE FROM IndiLessonContentEntityTable")
    suspend fun deleteAllIndiLEssonsContent()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createIndiLessonContent(indiLessonContentEntity: IndiLessonContentEntity)

    @Query("SELECT * FROM indilessoncontententitytable WHERE lessonNumber = :lessonNumber AND uniqueCourseNumber = :uniqueCourseNumber AND themeNumber = :themeNumber AND createrToken = :createrToken")
    suspend fun getIndiLessonContent(lessonNumber:Int,uniqueCourseNumber:Int,themeNumber:Int,createrToken:String): IndiLessonContentEntity

    @Delete
    suspend fun deleteLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity)

    @Update
    suspend fun updateLessonContentIndi(indiLessonContentEntity: IndiLessonContentEntity)
}