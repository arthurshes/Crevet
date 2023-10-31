package workwork.test.andropediagits.data.local.entities.course

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("CourseBuyEntityTable")
data class CourseBuyEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val courseNumber:Int,
    val promoCode:String?=null,
    val andropointBuy:Boolean,
    val date: Date,
    val token:String,
    val transactionId:String
)
