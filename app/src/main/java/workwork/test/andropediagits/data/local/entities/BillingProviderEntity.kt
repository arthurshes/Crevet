package workwork.test.andropediagits.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("billingProviderTableEntity")
data class BillingProviderEntity(
    @PrimaryKey(autoGenerate = true)
    var id:Int?=null,
    val selectedGoogleBilling:Boolean,
    val selectedRuService:Boolean
)
