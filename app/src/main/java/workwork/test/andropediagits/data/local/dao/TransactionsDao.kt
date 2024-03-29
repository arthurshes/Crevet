package workwork.test.andropediagits.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseBuyEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiCreatorSubscribeEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeBuyEntity

@Dao
interface TransactionsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyIndiCourse(indiCourseBuyEntity: IndiCourseBuyEntity)

    @Delete
    suspend fun deleteIndiCourse(indiCourseBuyEntity: IndiCourseBuyEntity)

    @Query("SELECT * FROM indiCourseBuyTable")
    suspend fun getAllBuyIndiCourses():List<IndiCourseBuyEntity>

    @Query("SELECT * FROM indiCourseBuyTable WHERE creatorToken = :creatorToken")
    suspend fun getAllBuyIndiCoursesForCreator(creatorToken:String):List<IndiCourseBuyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCreatorSubscribe(creatorSubscribeEntity: IndiCreatorSubscribeEntity)

    @Query("SELECT * FROM IndiCreatorSubsTableEntity")
    suspend fun getMyIndiSubs():IndiCreatorSubscribeEntity

    @Query("DELETE FROM IndiCreatorSubsTableEntity")
    suspend fun deleteIndiCreaterSubscribe()

    @Update
    suspend fun updateIndiCreaterSubscribe(indiCreatorSubscribeEntity:IndiCreatorSubscribeEntity)


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubscribe(subscribeEntity: SubscribeEntity)

    @Update
    suspend fun updateSubscribe(subscribeEntity: SubscribeEntity)

    @Delete
    suspend fun deleteSubscribe(subscribeEntity: SubscribeEntity)

    @Query("SELECT * FROM subscribeUserEntityTable")
    suspend fun getAllMySubs():SubscribeEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyCourse(courseBuyEntity: CourseBuyEntity)

    @Update
    suspend fun updateBuyCourse(courseBuyEntity: CourseBuyEntity)

    @Delete
    suspend fun deleteBuyCourse(courseBuyEntity: CourseBuyEntity)

    @Query("SELECT * FROM CourseBuyEntityTable")
    suspend fun getAllMyCourseBuy():List<CourseBuyEntity>
    //with test
    @Query("SELECT * FROM CourseBuyEntityTable WHERE courseNumber = :courseNumber")
    suspend fun searchCourseBuyForNumber(courseNumber:Int):CourseBuyEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBuyTheme(themeBuyEntity: ThemeBuyEntity)

    @Query("SELECT * FROM themeBuyEntityTable")
    suspend fun getAllBuyThemes():List<ThemeBuyEntity>
    //with test
    @Query("SELECT * FROM themeBuyEntityTable WHERE uniqueThemeId = :uniqueThemeId")
   suspend fun searchBuyTheme(uniqueThemeId:Int):ThemeBuyEntity

    @Query("DELETE FROM themeBuyEntityTable")
    suspend fun deleteAllBuyThemes()

    @Delete
    suspend fun deleteThemeBuy(themeBuyEntity: ThemeBuyEntity)

}