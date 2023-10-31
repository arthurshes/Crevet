package workwork.test.andropediagits.crashInspector.data.local
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CrashDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExceptionCrash(crashEntity: CrashEntity)

    @Delete
    suspend fun deleteExceptionCrash(crashEntity: CrashEntity)

    @Query("SELECT * FROM crashEntityTable")
    fun getAllCrash():LiveData<List<CrashEntity>>


}