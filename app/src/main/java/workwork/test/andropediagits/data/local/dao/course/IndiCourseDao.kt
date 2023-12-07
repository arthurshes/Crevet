package workwork.test.andropediagits.data.local.dao.course

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity

@Dao
interface IndiCourseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createIndiCourse(indiCourseEntity: IndiCourseEntity)

    @Query("SELECT * FROM indicourseentitytable")
    suspend fun getMyIndiCourse():IndiCourseEntity

    @Query("SELECT * FROM indicourseentitytable WHERE createrToken = :createrToken AND uniqueCourseNumber = :uniqueCourseNumber")
    suspend fun getIndiCourseWithCreaterToken(createrToken:String,uniqueCourseNumber:Int):IndiCourseEntity

    @Delete
    suspend fun deleteMyIndiCourse(indiCourseEntity: IndiCourseEntity)

    @Update
    suspend fun updateMyIndiCours(indiCourseEntity: IndiCourseEntity)

    @Query("DELETE FROM IndiCourseEntityTable")
    suspend fun deleteAllCourseIndi()
}