package workwork.test.andropediagits.data.local.dao.level

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonEntity


@Dao
interface IndiLevelDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createIndiLesson(indiLessonEntity: IndiLessonEntity)

    @Query("SELECT * FROM IndiLessonEntityTable WHERE  uniqueCourseNumber = :uniqueCourseNumber AND themeNumber = :themeNumber AND createrToken = :createrToken")
    suspend fun getIndiLesson(uniqueCourseNumber:Int,themeNumber:Int,createrToken:String): List<IndiLessonEntity>

    @Delete
    suspend fun deleteLessonIndi(indiLessonEntity: IndiLessonEntity)

    @Update
    suspend fun updateLessonIndi(indiLessonEntity: IndiLessonEntity)

    @Query("DELETE FROM indilessonentitytable")
    suspend fun deleteAllIndiLEssons()

}