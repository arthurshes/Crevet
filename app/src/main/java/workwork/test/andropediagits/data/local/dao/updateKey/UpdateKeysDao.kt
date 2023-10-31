package workwork.test.andropediagits.data.local.dao.updateKey
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity


@Dao
interface UpdateKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(updatesKeyEntity: UpdatesKeyEntity)

    @Query("SELECT * FROM updateEntityKeyTable")
    suspend fun getAllUpdatesKeys():List<UpdatesKeyEntity>

    @Delete
    suspend fun deleteUpdateKey(updatesKeyEntity: UpdatesKeyEntity)

    @Update
    suspend fun updateUpdateKey(updatesKeyEntity: UpdatesKeyEntity)
    //with test
    @Query("SELECT * FROM updateEntityKeyTable WHERE courseNumber = :courseNumber")
    suspend fun getUpdateKeyWithCourseNumber(courseNumber:Int):UpdatesKeyEntity
    //with test
    @Query("SELECT * FROM updateEntityKeyTable WHERE uniqueThemeId = :uniqueThemeId")
    suspend fun getUpdateKeyWithUniqueThemeId(uniqueThemeId:Int):UpdatesKeyEntity
    //with test
    @Query("SELECT * FROM updateEntityKeyTable WHERE uniqueLevelId = :uniqueLevelId")
    suspend fun getUpdateKeyWithUniqueLevelId(uniqueLevelId:Int):UpdatesKeyEntity
    //with test
    @Query("SELECT * FROM updateEntityKeyTable WHERE interactiveTestId = :interactiveTestId")
    suspend fun getUpdateKeyWithInteractiveId(interactiveTestId:Int):List<UpdatesKeyEntity>

    @Query("SELECT * FROM updateEntityKeyTable WHERE vicotineTestId = :vicotineTestId")
    suspend fun getUpdateKeyWithVictorineId(vicotineTestId:Int):List<UpdatesKeyEntity>
}
