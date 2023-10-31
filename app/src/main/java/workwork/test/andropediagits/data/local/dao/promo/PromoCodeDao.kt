package workwork.test.andropediagits.data.local.dao.promo
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import workwork.test.andropediagits.data.local.entities.promo.PromoCodeEntity

@Dao
interface PromoCodeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPromoCode(promoCodeEntity: PromoCodeEntity)

    @Query("SELECT * FROM promoCodeEntotyTable")
    suspend fun getAllMyPromo():PromoCodeEntity

    @Delete
    suspend fun deletePromoCode(promoCodeEntity: PromoCodeEntity)

    @Update
    suspend fun updatePromoCode(promoCodeEntity: PromoCodeEntity)

}