package workwork.test.andropediagits.domain.repo

import androidx.lifecycle.LiveData

import retrofit2.http.Body
import retrofit2.http.Query
import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeBuyEntity
import workwork.test.andropediagits.data.remote.model.CheckSubscribeBackModel
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import workwork.test.andropediagits.data.remote.model.SubscribtionAnswerModel
import workwork.test.andropediagits.data.remote.model.TimeAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel

interface TransactionRepo {

    suspend fun sendSubscribtionTransaction(subscribeModel: SubscribeModel): SubscribtionAnswerModel
    suspend fun sendCourseBuy(courseBuyModel: CourseBuyModel):SubscribtionAnswerModel
    suspend fun checkMySubscribe(sendSubscribeCheckModel: SendSubscribeCheckModel): CheckSubscribeBackModel
    suspend fun checkMyBuyCourse(token: String):List<CourseBuyModel>
    suspend fun getCurrentTime(): TimeAnswerModel

    suspend fun insertSubscribe(subscribeEntity: SubscribeEntity)

    suspend fun updateSubscribe(subscribeEntity: SubscribeEntity)

    suspend fun deleteSubscribe(subscribeEntity: SubscribeEntity)

    suspend fun getSubscribe():SubscribeEntity

    suspend fun insertCourseBuy(courseBuyEntity: CourseBuyEntity)

    suspend fun updateBuyCourse(courseBuyEntity: CourseBuyEntity)

    suspend fun deleteBuyCourse(courseBuyEntity: CourseBuyEntity)

    suspend fun getAllMyCourseBuy():List<CourseBuyEntity>

    suspend fun searchCourseBuyForNumber(courseNumber:Int):CourseBuyEntity

    suspend fun insertBuyTheme(themeBuyEntity: ThemeBuyEntity)

    suspend fun getAllBuyThemes():List<ThemeBuyEntity>

    suspend fun searchBuyTheme(uniqueThemeId:Int):ThemeBuyEntity

    suspend fun deleteAllBuyThemes()

    suspend fun deleteThemeBuy(themeBuyEntity: ThemeBuyEntity)

    suspend fun getMySubscribe(token: String):SubscribeModel

    suspend fun sendUserThemeBuy(themeBuyModel: ThemeBuyModel):SubscribtionAnswerModel

    suspend fun checkUserBuyTheme(
        token: String
    ):List<ThemeBuyModel>

}