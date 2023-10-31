package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("ReminderTableEntity")
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val remindTime:LocalDateTime,
    val remindMessage:String
)
