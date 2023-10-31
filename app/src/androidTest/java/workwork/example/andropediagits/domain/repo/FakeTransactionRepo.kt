package workwork.example.andropediagits.domain.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.andropediagits.data.local.entities.SubscribeEntity
import com.example.andropediagits.data.local.entities.course.CourseBuyEntity
import com.example.andropediagits.data.local.entities.theme.ThemeBuyEntity
import com.example.andropediagits.data.local.entities.theme.ThemeEntity
import com.example.andropediagits.data.remote.model.CheckSubscribeBackModel
import com.example.andropediagits.data.remote.model.SendSubscribeCheckModel
import com.example.andropediagits.data.remote.model.SubscribeModel
import com.example.andropediagits.data.remote.model.SubscribtionAnswerModel
import com.example.andropediagits.data.remote.model.TimeAnswerModel
import com.example.andropediagits.data.remote.model.course.CourseBuyModel
import com.example.andropediagits.data.remote.model.theme.ThemeBuyModel
import kotlinx.coroutines.delay
import java.util.Date

class FakeTransactionRepo : TransactionRepo {
    private val fakeTimeAnswerModel = TimeAnswerModel(
        abbreviation = "UTC",
        client_ip = "192.168.1.1",
        datetime = "2023-07-26T12:34:56.789Z",
        day_of_week = 2,
        day_of_year = 207,
        dst = false,
        dst_from = Any(),
        dst_offset = 0,
        dst_until = Any(),
        raw_offset = 0,
        timezone = "GMT",
        unixtime = 1678887296,
        utc_datetime = "2023-07-26T12:34:56.789Z",
        utc_offset = "+00:00",
        week_number = 30
    )
    private val fakeSubscribtionAnswerModel = SubscribtionAnswerModel("", true,3)
    private val fakeCourseBuyList = ArrayList<CourseBuyEntity>()
    private val fakeSubscribe = ArrayList<SubscribeEntity>()
    private val fakeThemeBuy = ArrayList<ThemeBuyModel>()
    private val fakeThemeBuyEntity = ArrayList<ThemeBuyEntity>()

    override suspend fun sendSubscribtionTransaction(subscribeModel: SubscribeModel): SubscribtionAnswerModel {
        // Simulate a delay to mimic network request
        delay(500)
        return fakeSubscribtionAnswerModel
    }

    override suspend fun sendCourseBuy(courseBuyModel: CourseBuyModel): SubscribtionAnswerModel {
        // Simulate a delay to mimic network request
        delay(500)
        return fakeSubscribtionAnswerModel
    }

    override suspend fun checkMySubscribe(sendSubscribeCheckModel: SendSubscribeCheckModel): CheckSubscribeBackModel {
        // Simulate a delay to mimic network request
        delay(500)
        return CheckSubscribeBackModel(true,1,"hello",true)
    }

    override suspend fun checkMyBuyCourse(token: String): List<CourseBuyModel> {
        // Simulate a delay to mimic network request
        delay(500)
        return fakeCourseBuyList.filter { it.token == token }
            .map { courseBuyEntity ->
                CourseBuyModel(
                    courseNumber = courseBuyEntity.courseNumber,
                    dateBuy = "ew",
                    token = courseBuyEntity.token,
                    promoCode = courseBuyEntity.promoCode,
                    transactionId = courseBuyEntity.transactionId,
                    codeAnswer = 43,
                    andropointBuy = courseBuyEntity.andropointBuy
                )
            }
    }

    override suspend fun getCurrentTime(): TimeAnswerModel {
        // Simulate a delay to mimic network request
        delay(500)
        return fakeTimeAnswerModel
    }

    override suspend fun insertSubscribe(subscribeEntity: SubscribeEntity) {
        fakeSubscribe.add( subscribeEntity)
    }

    override suspend fun updateSubscribe(subscribeEntity: SubscribeEntity) {
        fakeSubscribe.add( subscribeEntity)
    }

    override suspend fun deleteSubscribe(subscribeEntity: SubscribeEntity) {
        fakeSubscribe.remove( subscribeEntity)
    }

    override suspend fun getSubscribe(): SubscribeEntity {
        return fakeSubscribe[0]
    }

    override suspend fun insertCourseBuy(courseBuyEntity: CourseBuyEntity) {
        fakeCourseBuyList.add(courseBuyEntity)
    }

    override suspend fun updateBuyCourse(courseBuyEntity: CourseBuyEntity) {
        val index = fakeCourseBuyList.indexOfFirst { it.id == courseBuyEntity.id }
        if (index != -1) {
            fakeCourseBuyList[index] = courseBuyEntity
        }
    }

    override suspend fun deleteBuyCourse(courseBuyEntity: CourseBuyEntity) {
        fakeCourseBuyList.remove(courseBuyEntity)
    }

    override suspend fun getAllMyCourseBuy(): List<CourseBuyEntity> {
        return fakeCourseBuyList
    }

    override suspend fun searchCourseBuyForNumber(courseNumber: Int): CourseBuyEntity {
        return fakeCourseBuyList.find { it.courseNumber == courseNumber }!!
    }

    override suspend fun insertBuyTheme(themeBuyEntity: ThemeBuyEntity) {
        val themeBuyModel = ThemeBuyModel(
            courseNumber = themeBuyEntity.courseNumber,
            themeNumber = themeBuyEntity.themeNumber,
            uniqueThemeId = themeBuyEntity.uniqueThemeId,
            token = themeBuyEntity.token,
            transactionId = themeBuyEntity.transactionId,
            dateBuyApi = ""
        )
        fakeThemeBuy.add(themeBuyModel)
    }

    override suspend fun getAllBuyThemes(): List<ThemeBuyEntity >{
        // Not implemented for the fake repository
        return ArrayList()
    }

    override suspend fun searchBuyTheme(uniqueThemeId: Int): ThemeBuyEntity {
        // Not implemented for the fake repository
        return fakeThemeBuyEntity.find { it.uniqueThemeId == uniqueThemeId }!!

    }

    override suspend fun deleteAllBuyThemes() {
        // Not implemented for the fake repository
    }

    override suspend fun deleteThemeBuy(themeBuyEntity: ThemeBuyEntity) {
        // Not implemented for the fake repository
    }

    override suspend fun getMySubscribe(token: String): SubscribeModel {
        // Simulate a delay to mimic network request
        /*delay(500)
        return SubscribeModel(subscribed = true, token = token)*/
        TODO()
    }

    override suspend fun sendUserThemeBuy(themeBuyModel: ThemeBuyModel): SubscribtionAnswerModel {
        TODO("Not yet implemented")
    }

    override suspend fun checkUserBuyTheme(token: String): List<ThemeBuyModel> {
        return fakeThemeBuy.find { it.token == token } ?.let { listOf(it) }!!
    }
}