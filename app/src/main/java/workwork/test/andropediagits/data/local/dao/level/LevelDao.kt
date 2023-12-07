package workwork.test.andropediagits.data.local.dao.level

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLevels(levelEntity: LevelEntity)

    @Query("DELETE FROM LevelEntityTableCache")
    suspend fun deleteAllLevels()

    @Query("SELECT * FROM LevelEntityTableCache WHERE levelNumber = :levelNumber AND themeNumber = :themeNumber AND courseNumber = :courseNumber")
    suspend fun getSearchLevelTest(levelNumber:Int,themeNumber:Int,courseNumber:Int): LevelEntity

    @Query("SELECT * FROM LevelEntityTableCache")
    suspend fun getAllLevels():List<LevelEntity>
    //with test
    @Query("SELECT * FROM LevelEntityTableCache WHERE uniqueLevelId = :uniqueLevelId")
    suspend fun searchOneLevel(uniqueLevelId:Int):LevelEntity
    //with test
    @Query("SELECT * FROM LevelEntityTableCache WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun searchThemeLevels(uniqueThemeId:Int):List<LevelEntity>
    //with test
    @Query("DELETE FROM LevelEntityTableCache WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun deletAllLevelTheme(uniqueThemeId:Int)
    //with test
    @Query("DELETE FROM LevelEntityTableCache WHERE courseNumber = :courseNumber")
    suspend fun deleteAllLevelCourse(courseNumber:Int)


    @Update
    suspend fun updateLesson(levelEntity: LevelEntity)

    @Query("SELECT * FROM LevelEntityTableCache  WHERE isFav = 1 AND uniqueThemeId = :uniqueThemeId")
    suspend fun getAllFavoriteLessons(uniqueThemeId: Int):List<LevelEntity>

}