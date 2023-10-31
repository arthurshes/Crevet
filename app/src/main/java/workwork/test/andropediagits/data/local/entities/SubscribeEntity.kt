package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("subscribeUserEntityTable")
data class SubscribeEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val date:Date,
    val term:Int,
    val token:String,
    val transactionId:String
)
