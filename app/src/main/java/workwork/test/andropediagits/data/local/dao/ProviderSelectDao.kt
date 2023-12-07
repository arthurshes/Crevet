package workwork.test.andropediagits.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.BillingProviderEntity

@Dao
interface ProviderSelectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdsProvider(adsProviderEntity: AdsProviderEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBillingProvider(billingProviderEntity: BillingProviderEntity)

    @Update
    suspend fun updateBillingProvider(billingProviderEntity: BillingProviderEntity)

    @Update
    suspend fun updateAdsProvider(adsProviderEntity: AdsProviderEntity)

    @Query("SELECT * FROM adsProviderEntitytTable")
    suspend fun getMyAdsProvider():AdsProviderEntity

    @Query("SELECT * FROM billingProviderTableEntity")
    suspend fun getMyBillingProvider():BillingProviderEntity

    @Query("DELETE FROM adsProviderEntitytTable")
    suspend fun deleteAdsProvider()

    @Query("DELETE FROM billingProviderTableEntity")
    suspend fun deletebillingProvider()
}