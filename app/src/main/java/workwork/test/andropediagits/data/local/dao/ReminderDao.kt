package workwork.test.andropediagits.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.ReminderEntity


@Dao
interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminderEntity: ReminderEntity)

    @Delete
    suspend fun deleteReminder(reminderEntity: ReminderEntity)

    @Update
    suspend fun updateReminder(reminderEntity: ReminderEntity)

    @Query("SELECT * FROM ReminderTableEntity")
    suspend fun getMyReminds():ReminderEntity

    @Query("DELETE FROM ReminderTableEntity")
    suspend fun deleteAllReminds()
}