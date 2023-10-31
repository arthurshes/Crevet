package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("resetNextTableEntity")
data class ResetNextEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val email:String
)
