package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("adsProviderEntitytTable")
data class AdsProviderEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val selectedGoogle:Boolean,
    val selectedLMyTarger:Boolean
)
