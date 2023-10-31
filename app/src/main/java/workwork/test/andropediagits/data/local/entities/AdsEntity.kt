package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity("adsTermTableEntity")
data class AdsEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val dateFirstViewAds:String,
    val viewAdsCount:Int = 0,
    val isTerm:Boolean = false
)
