package workwork.test.andropediagits.domain.repo

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import workwork.test.andropediagits.core.model.SendStatus
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.ResetNextEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.promo.PromoCodeEntity
import workwork.test.andropediagits.data.remote.CheckAdsLimitGetModel
import workwork.test.andropediagits.data.remote.model.CheckAdsLimitModel
import workwork.test.andropediagits.data.remote.model.EmailSignInAnswerModel
import workwork.test.andropediagits.data.remote.model.StrikeModeAnswerModel
import workwork.test.andropediagits.data.remote.model.TimeAnswerModel
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.data.remote.model.adsTerm.AdsTermGetModel
import workwork.test.andropediagits.data.remote.model.adsTerm.AdsTermUserSendModel
import workwork.test.andropediagits.data.remote.model.email.EmailRecoverModel
import workwork.test.andropediagits.data.remote.model.email.EmailSignInModel
import workwork.test.andropediagits.data.remote.model.email.RecoverStatusModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeCheckModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeResponse
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetCheckGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.UpdateNewPasswordModel
import workwork.test.andropediagits.data.remote.model.strike.StrikeModeSendModel

interface UserLogicRepo {

    suspend fun sendUpdateNewPassword(updateNewPasswordModel: UpdateNewPasswordModel):SendStatus

    suspend fun getUserCheckMethod(email:String): ResetMethodGetModel



    suspend fun checkResetText( resetTextCheckSendModel: ResetTextCheckSendModel): ResetCheckGetModel


    suspend fun checkResetDate( resetDateCheckSendModel: ResetDateCheckSendModel):ResetCheckGetModel

    suspend fun insertReset(resetNextEntity: ResetNextEntity)


    suspend fun deleteReset(resetNextEntity: ResetNextEntity)


    suspend fun getReset(): ResetNextEntity


    suspend fun deleteAllReset()


    suspend fun resetTextSend( resetTextSendModel: ResetTextSendModel):SendStatus


    suspend fun resetDateSend( resetDateSendModel: ResetDateSendModel):SendStatus


    suspend fun deleteAdsTerm (token:String): AdsTermGetModel


    suspend fun sendAdsTerm(adsTermUserSendModel: AdsTermUserSendModel): AdsTermGetModel

    suspend fun checkLimitTwoHoursAds(checkAdsLimitModel: CheckAdsLimitModel): CheckAdsLimitGetModel

    suspend fun checkLimittermAds(checkAdsLimitModel: CheckAdsLimitModel): CheckAdsLimitGetModel

    suspend fun insertAdsTerm(adsEntity: AdsEntity)


    suspend fun updateAdsItem(adsEntity: AdsEntity)


    suspend fun getAdsUserTerm(): AdsEntity


    suspend fun deleteUserAds(adsEntity: AdsEntity)


    suspend fun deleteAllUserAds()

    suspend fun recoverPasswordForEmail(emailRecoverModel: EmailRecoverModel): RecoverStatusModel
    suspend fun checkActualMySubscribe(promoCodeModel: PromoCodeModel): PromoCodeCheckModel
    suspend fun insertPromoCode(promoCodeEntity: PromoCodeEntity)

    suspend fun getAllMyPromo():PromoCodeEntity

    suspend fun deletePromoCode(promoCodeEntity: PromoCodeEntity)

    suspend fun updatePromoCode(promoCodeEntity: PromoCodeEntity)
    suspend fun sendPromoCode(promoCodeModel: PromoCodeModel): PromoCodeResponse
    suspend fun emailSignIn(emailSignInModel: EmailSignInModel): EmailSignInAnswerModel
    suspend fun sendUserInfo(userSignInModel: UserSignInModel): SendStatus
    suspend fun insertUserInfoLocal(userInfoEntity: UserInfoEntity)
    suspend fun updateUserInfoLocal(userInfoEntity: UserInfoEntity)
    suspend fun deleteUserInfoLocal(userInfoEntity: UserInfoEntity)
    suspend fun getUserInfoLocal():UserInfoEntity

    suspend fun getCurrentTime(): TimeAnswerModel

    suspend fun getCurrentUserInfo(token:String):UserSignInModel

    suspend fun getMyStrikeModeInfo(strikeModeSendModel: StrikeModeSendModel): StrikeModeAnswerModel

    suspend fun updateUserInfo(userSignInModel: UserSignInModel):SendStatus
}