package workwork.test.andropediagits.data.local.dao.level

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

import androidx.room.Dao
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity

@Dao
interface LevelContentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLevelsContent(themeLevelContentEntity: ThemeLevelContentEntity)

    @Query("DELETE FROM ThemeLevelContentEntityTable")
    suspend fun deleteAllLevelsContent()

    @Query("SELECT * FROM ThemeLevelContentEntityTable")
    suspend fun getAllLevelsContent():List<ThemeLevelContentEntity>


    ////Test Fun 13September
    @Query("SELECT * FROM ThemeLevelContentEntityTable WHERE courseNumber = :courseNumber AND themeNumber = :themeNumber AND levelNumber = :levelNumber")
    suspend fun getNextContentTestFun(courseNumber:Int,themeNumber:Int,levelNumber:Int):ThemeLevelContentEntity

    //with test
    @Query("SELECT * FROM ThemeLevelContentEntityTable WHERE uniqueLevelId = :uniqueLevelId")
    suspend fun searchLevelContents(uniqueLevelId:Int):ThemeLevelContentEntity
    //with test
    @Query("SELECT * FROM ThemeLevelContentEntityTable WHERE uniqueLevelContentId = :uniqueLevelContentId")
    suspend fun searchOneLevelContent(uniqueLevelContentId:Int):ThemeLevelContentEntity

    @Query("DELETE FROM ThemeLevelContentEntityTable WHERE uniqueLevelId = :uniqueLevelId")
    suspend fun deleteAllLevelContentWithUniqueId(uniqueLevelId:Int)

    @Query("DELETE FROM ThemeLevelContentEntityTable WHERE courseNumber = :courseNumber")
    suspend fun deleteAllLevelContentWithCourseNumber(courseNumber:Int)

    @Query("DELETE FROM ThemeLevelContentEntityTable WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun deleteAllThemeLevelContent(uniqueThemeId:Int)



}