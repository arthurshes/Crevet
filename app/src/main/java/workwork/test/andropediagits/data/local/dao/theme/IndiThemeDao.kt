package workwork.test.andropediagits.data.local.dao.theme

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.indi.IndiLessonContentEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity

@Dao
interface IndiThemeDao {


    @Query("DELETE FROM IndiThemeEntityTable")
    suspend fun deleteAllIndiThemes()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createIndiThemes(indiLessonContentEntity: IndiThemeEntity)

    @Query("SELECT * FROM IndiThemeEntityTable  WHERE uniqueCourseNumber = :uniqueCourseNumber  AND createrToken = :createrToken")
    suspend fun getAllIndiThemes(uniqueCourseNumber:Int,createrToken:String): List<IndiThemeEntity>

    @Delete
    suspend fun deleteIndiThemes(indiLessonContentEntity: IndiThemeEntity)

    @Update
    suspend fun updateIndiThemes(indiLessonContentEntity: IndiThemeEntity)

}