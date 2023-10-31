package workwork.test.andropediagits.data.local.entities.promo

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("promoCodeEntotyTable")
data class PromoCodeEntity(
    @PrimaryKey(autoGenerate = false)
    val promoId:String,
    val promoCode:String,
    val token:String,
    val promoDate:Date
)
