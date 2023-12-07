package workwork.test.andropediagits.data.repo


import workwork.test.andropediagits.core.model.SendStatus
import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.BillingProviderEntity
import workwork.test.andropediagits.data.local.entities.ResetNextEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.promo.PromoCodeEntity
import workwork.test.andropediagits.data.remote.CheckAdsLimitGetModel
import workwork.test.andropediagits.data.remote.LogicUserApiService
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.remote.model.CheckAdsLimitModel
import workwork.test.andropediagits.data.remote.model.DeleteMyAccModel
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
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import javax.inject.Inject
import kotlin.math.sign

class UserLogicRepoImpl @Inject constructor(val signInApiService: LogicUserApiService, val mainDb: MainDb, val timeApiService: TimeApiService):
    UserLogicRepo {

    private val dao = mainDb.getUserInfoDao()
    private val promoCodeDao = mainDb.getPromoCodeDao()
    private val resetDao = mainDb.getResetDao()
    private val providerDao = mainDb.getProviderDao()

    override suspend fun insertAdsProvider(adsProviderEntity: AdsProviderEntity) {
        providerDao.insertAdsProvider(adsProviderEntity)
    }

    override suspend fun insertBillingProvider(billingProviderEntity: BillingProviderEntity) {
        providerDao.insertBillingProvider(billingProviderEntity)
    }

    override suspend fun updateBillingProvider(billingProviderEntity: BillingProviderEntity) {
        providerDao.updateBillingProvider(billingProviderEntity)
    }

    override suspend fun updateAdsProvider(adsProviderEntity: AdsProviderEntity) {
        providerDao.updateAdsProvider(adsProviderEntity)
    }

    override suspend fun getMyAdsProvider(): AdsProviderEntity {
       return providerDao.getMyAdsProvider()
    }

    override suspend fun getMyBillingProvider(): BillingProviderEntity {
        return providerDao.getMyBillingProvider()
    }

    override suspend fun deleteAdsProvider() {
        providerDao.deleteAdsProvider()
    }

    override suspend fun deletebillingProvider() {
       providerDao.deletebillingProvider()
    }

    override suspend fun deleteMyAccount(DeleteMyAccModel: DeleteMyAccModel): SendStatus {
        return signInApiService.deleteMyAccount(DeleteMyAccModel)
    }

    override suspend fun sendUpdateNewPassword(updateNewPasswordModel: UpdateNewPasswordModel): SendStatus {
       return signInApiService.sendUpdateNewPassword(updateNewPasswordModel)
    }

    override suspend fun getUserCheckMethod(email: String): ResetMethodGetModel {
       return signInApiService.getUserCheckMethod(email)
    }

    override suspend fun checkResetText(resetTextCheckSendModel: ResetTextCheckSendModel): ResetCheckGetModel {
        return signInApiService.checkResetText(resetTextCheckSendModel)
    }

    override suspend fun checkResetDate(resetDateCheckSendModel: ResetDateCheckSendModel): ResetCheckGetModel {
        return signInApiService.checkResetDate(resetDateCheckSendModel)
    }

    override suspend fun insertReset(resetNextEntity: ResetNextEntity) {
        resetDao.insertReset(resetNextEntity)
    }

    override suspend fun deleteReset(resetNextEntity: ResetNextEntity) {
        resetDao.deleteReset(resetNextEntity)
    }

    override suspend fun getReset(): ResetNextEntity {
       return resetDao.getReset()
    }

    override suspend fun deleteAllReset() {
       return resetDao.deleteAllReset()
    }

//    override suspend fun checkResetText(resetTextSendModel: ResetTextSendModel): ResetCheckGetModel {
//        return signInApiService.checkResetText(resetTextSendModel)
//    }
//
//    override suspend fun checkResetDate(resetDateSendModel: ResetDateSendModel): ResetCheckGetModel {
//        return signInApiService.checkResetDate(resetDateSendModel)
//    }

    override suspend fun resetTextSend(resetTextSendModel: ResetTextSendModel): SendStatus {
        return signInApiService.resetTextSend(resetTextSendModel)
    }

    override suspend fun resetDateSend(resetDateSendModel: ResetDateSendModel): SendStatus {
        return signInApiService.resetDateSend(resetDateSendModel)
    }

    override suspend fun deleteAdsTerm(token: String): AdsTermGetModel {
        return signInApiService.deleteAdsTerm(token)
    }

    override suspend fun sendAdsTerm(adsTermUserSendModel: AdsTermUserSendModel): AdsTermGetModel {
        return signInApiService.sendAdsTerm(adsTermUserSendModel)
    }

    override suspend fun checkLimitTwoHoursAds(checkAdsLimitModel: CheckAdsLimitModel): CheckAdsLimitGetModel {
        return signInApiService.checkLimitTwoHoursAds(checkAdsLimitModel)
    }

    override suspend fun checkLimittermAds(checkAdsLimitModel: CheckAdsLimitModel): CheckAdsLimitGetModel {
        return signInApiService.checkLimittermAds(checkAdsLimitModel)
    }

    override suspend fun insertAdsTerm(adsEntity: AdsEntity) {
        dao.insertAdsTerm(adsEntity)
    }

    override suspend fun updateAdsItem(adsEntity: AdsEntity) {
        dao.updateAdsItem(adsEntity)
    }

    override suspend fun getAdsUserTerm(): AdsEntity {
       return dao.getAdsUserTerm()
    }

    override suspend fun deleteUserAds(adsEntity: AdsEntity) {
        dao.deleteUserAds(adsEntity)
    }

    override suspend fun deleteAllUserAds() {
       dao.deleteAllUserAds()
    }

    override suspend fun recoverPasswordForEmail(emailRecoverModel: EmailRecoverModel): RecoverStatusModel {
        return signInApiService.recoverPasswordForEmail(emailRecoverModel)
    }

    override suspend fun checkActualMySubscribe(promoCodeModel: PromoCodeModel): PromoCodeCheckModel {
        return signInApiService.checkActualMySubscribe(promoCodeModel)
    }

    override suspend fun insertPromoCode(promoCodeEntity: PromoCodeEntity) {
       promoCodeDao.insertPromoCode(promoCodeEntity)
    }

    override suspend fun getAllMyPromo(): PromoCodeEntity {
        return promoCodeDao.getAllMyPromo()
    }

    override suspend fun deletePromoCode(promoCodeEntity: PromoCodeEntity) {
        promoCodeDao.deletePromoCode(promoCodeEntity)
    }

    override suspend fun updatePromoCode(promoCodeEntity: PromoCodeEntity) {
        promoCodeDao.updatePromoCode(promoCodeEntity)
    }

    override suspend fun sendPromoCode(promoCodeModel: PromoCodeModel): PromoCodeResponse {
        return signInApiService.sendPromoCode(promoCodeModel)
    }

    override suspend fun emailSignIn(emailSignInModel: EmailSignInModel): EmailSignInAnswerModel {
       return signInApiService.sendEmailData(emailSignInModel)
    }

    override suspend fun sendUserInfo(userSignInModel: UserSignInModel): SendStatus {
       return signInApiService.sendUserInfo(userSignInModel)
    }

    override suspend fun insertUserInfoLocal(userInfoEntity: UserInfoEntity) {
       dao.insertUserInfo(userInfoEntity)
    }

    override suspend fun updateUserInfoLocal(userInfoEntity: UserInfoEntity) {
        dao.updateUserInfo(userInfoEntity)
    }

    override suspend fun deleteUserInfoLocal(userInfoEntity: UserInfoEntity) {
        dao.deleteUserInfo(userInfoEntity)
    }

    override suspend fun getUserInfoLocal(): UserInfoEntity {
        return dao.getUserInfo()
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
        return timeApiService.getCurrentTime()
    }

    override suspend fun getCurrentUserInfo(token: String): UserSignInModel {
        return signInApiService.getMyInfo(token)
    }

    override suspend fun getMyStrikeModeInfo(strikeModeSendModel: StrikeModeSendModel): StrikeModeAnswerModel {
        return signInApiService.getMyStrikeModeInfo(strikeModeSendModel)
    }

    override suspend fun updateUserInfo(userSignInModel: UserSignInModel): SendStatus {
        return signInApiService.updateUserInfo(userSignInModel)
    }

}