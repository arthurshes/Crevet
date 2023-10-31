package workwork.test.andropediagits.data.local.dao.theme
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity


@Dao
interface ThemeDao {

    @Query("SELECT * FROM themeEntityTable WHERE courseNumber = :courseNumber AND isThemePassed = 0")
    suspend fun getAllNotPassedThemesCourse(courseNumber:Int):List<ThemeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTheme(themeEntity: ThemeEntity)

    @Update
    suspend fun updateTheme(themeEntity: ThemeEntity)

    @Delete
    suspend fun deleteTheme(themeEntity: ThemeEntity)

    @Query("SELECT * FROM themeEntityTable WHERE isFav = 1")
    suspend fun getAllFavoriteThemes():List<ThemeEntity>

    @Query("SELECT * FROM themeEntityTable")
    suspend fun getAllMyThemes():List<ThemeEntity>
    //with test
    @Query("SELECT * FROM themeEntityTable WHERE uniqueThemeId = :uniqueThemeId")
     suspend fun searchThemeWithUniqueId(uniqueThemeId:Int):ThemeEntity
    //with test
    @Query("SELECT * FROM themeEntityTable WHERE courseNumber = :courseNumber")
    suspend fun searchThemesWithCourseNumber(courseNumber:Int):List<ThemeEntity>

    @Query("DELETE FROM themeEntityTable")
    suspend fun deleteAllThemes()
    //with test
    @Query("SELECT * FROM themeEntityTable WHERE interactiveTestId = :interactiveTestId")
    suspend fun searchThemeWithInteractiveId(interactiveTestId:Int):ThemeEntity
    //with test
    @Query("SELECT * FROM themeEntityTable WHERE vicotineTestId = :vicotineTestId")
    suspend fun searchThemeWithVictorineTestId(vicotineTestId:Int):ThemeEntity

    @Query("DELETE FROM themeEntityTable WHERE courseNumber = :courseNumber")
    suspend fun deleteAllThemesCourse(courseNumber:Int)
}

