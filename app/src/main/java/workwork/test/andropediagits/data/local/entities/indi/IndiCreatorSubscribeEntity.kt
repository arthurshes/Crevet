package workwork.test.andropediagits.data.local.entities.indi

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("IndiCreatorSubsTableEntity")
data class IndiCreatorSubscribeEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val token:String,
    val successSend:Boolean,
    val dateBuy: Date,
    val term:Int
)
