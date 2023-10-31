package workwork.test.andropediagits.data.local.entities.reset

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import workwork.test.andropediagits.data.local.entities.ResetNextEntity

@Dao
interface ResetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReset(resetNextEntity: ResetNextEntity)

    @Delete
    suspend fun deleteReset(resetNextEntity: ResetNextEntity)

    @Query("SELECT * FROM resetNextTableEntity")
    suspend fun getReset():ResetNextEntity

    @Query("DELETE FROM resetNextTableEntity")
    suspend fun deleteAllReset()

}