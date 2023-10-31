package workwork.test.andropediagits.data.local.dao.course
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import workwork.test.andropediagits.data.local.entities.course.CourseEntity

@Dao
interface CoursesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(courseEntity: CourseEntity)

    @Query("SELECT * FROM CourseEntityTable")
    suspend fun getAllMyCourses():List<CourseEntity>

    @Update
    suspend fun updateCourse(courseEntity: CourseEntity)

    @Delete
    suspend fun deleteCourse(courseEntity: CourseEntity)

    @Query("DELETE FROM CourseEntityTable")
    suspend fun deleteAllCourse()

    @Query("SELECT * FROM CourseEntityTable WHERE courseNumber = :courseNumber")
    suspend fun searchCourseWithNumber(courseNumber:Int): CourseEntity


}