package workwork.test.andropediagits.data.local.dao
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity

@Dao
interface UserInfoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdsTerm(adsEntity: AdsEntity)

    @Update
    suspend fun updateAdsItem(adsEntity: AdsEntity)

    @Query("SELECT * FROM adsTermTableEntity")
    suspend fun getAdsUserTerm():AdsEntity

    @Delete
    suspend fun deleteUserAds(adsEntity: AdsEntity)

    @Query("DELETE FROM adsTermTableEntity")
    suspend fun deleteAllUserAds()

    //with test
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserInfo(userInfoEntity: UserInfoEntity)
    //with test
    @Update
    suspend fun updateUserInfo(userInfoEntity: UserInfoEntity)
    //with test
    @Query("SELECT * FROM UserInfoEntityTable")
    suspend fun getUserInfo():UserInfoEntity
    //with test
    @Delete
    suspend fun deleteUserInfo(userInfoEntity: UserInfoEntity)

}