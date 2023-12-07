package workwork.test.andropediagits.data.repo

import androidx.lifecycle.LiveData

import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeBuyEntity
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.remote.TransactionApiService
import workwork.test.andropediagits.data.remote.model.CheckSubscribeBackModel
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import workwork.test.andropediagits.data.remote.model.SubscribtionAnswerModel
import workwork.test.andropediagits.data.remote.model.TimeAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import javax.inject.Inject


class TransactionRepoImpl  @Inject constructor(val transactionApiService: TransactionApiService, val timeApiService: TimeApiService, val mainDb: MainDb):
    TransactionRepo {

   private val transactionsDao = mainDb.getTransactionDao()

    override suspend fun sendSubscribtionTransaction(subscribeModel: SubscribeModel): SubscribtionAnswerModel {
       return transactionApiService.sendUserSubscribe(subscribeModel)
    }

    override suspend fun sendCourseBuy(courseBuyModel: CourseBuyModel): SubscribtionAnswerModel {
        return transactionApiService.sendUserCourseBuy(courseBuyModel)
    }

    override suspend fun checkMySubscribe(sendSubscribeCheckModel: SendSubscribeCheckModel): CheckSubscribeBackModel {
        return transactionApiService.checkUserSubscribe(sendSubscribeCheckModel)
    }

    override suspend fun checkMyBuyCourse(token: String): List<CourseBuyModel> {
        return transactionApiService.checkUserBuyCourse(token)
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
       return timeApiService.getCurrentTime()
    }

    override suspend fun insertSubscribe(subscribeEntity: SubscribeEntity) {
        transactionsDao.insertSubscribe(subscribeEntity)
    }

    override suspend fun updateSubscribe(subscribeEntity: SubscribeEntity) {
       transactionsDao.updateSubscribe(subscribeEntity)
    }

    override suspend fun deleteSubscribe(subscribeEntity: SubscribeEntity) {
        transactionsDao.deleteSubscribe(subscribeEntity)
    }

    override suspend fun getSubscribe(): SubscribeEntity {
        return transactionsDao.getAllMySubs()
    }

    override suspend fun insertCourseBuy(courseBuyEntity: CourseBuyEntity) {
        transactionsDao.insertBuyCourse(courseBuyEntity)
    }

    override suspend fun updateBuyCourse(courseBuyEntity: CourseBuyEntity) {
        transactionsDao.updateBuyCourse(courseBuyEntity)
    }

    override suspend fun deleteBuyCourse(courseBuyEntity: CourseBuyEntity) {
        transactionsDao.deleteBuyCourse(courseBuyEntity)
    }

    override suspend fun getAllMyCourseBuy(): List<CourseBuyEntity> {
        return transactionsDao.getAllMyCourseBuy()
    }

    override suspend fun searchCourseBuyForNumber(courseNumber: Int): CourseBuyEntity {
        return transactionsDao.searchCourseBuyForNumber(courseNumber)
    }

    override suspend fun insertBuyTheme(themeBuyEntity: ThemeBuyEntity) {
        transactionsDao.insertBuyTheme(themeBuyEntity)
    }

    override suspend fun getAllBuyThemes(): List<ThemeBuyEntity> {
        return transactionsDao.getAllBuyThemes()
    }

    override suspend fun searchBuyTheme(uniqueThemeId: Int): ThemeBuyEntity {
        return transactionsDao.searchBuyTheme(uniqueThemeId)
    }

    override suspend fun deleteAllBuyThemes() {
       transactionsDao.deleteAllBuyThemes()
    }

    override suspend fun deleteThemeBuy(themeBuyEntity: ThemeBuyEntity) {
       transactionsDao.deleteThemeBuy(themeBuyEntity)
    }

    override suspend fun getMySubscribe(token: String): SubscribeModel {
        return transactionApiService.getMySubscribe(token)
    }

    override suspend fun sendUserThemeBuy(themeBuyModel: ThemeBuyModel): SubscribtionAnswerModel {
        return transactionApiService.sendUserThemeBuy(themeBuyModel)
    }

    override suspend fun checkUserBuyTheme(token: String): List<ThemeBuyModel> {
        return transactionApiService.checkUserBuyTheme(token)
    }


}