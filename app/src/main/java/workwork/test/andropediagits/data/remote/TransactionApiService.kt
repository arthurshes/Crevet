package workwork.test.andropediagits.data.remote


import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import workwork.test.andropediagits.data.remote.model.CheckSubscribeBackModel
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import workwork.test.andropediagits.data.remote.model.SubscribtionAnswerModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel

interface TransactionApiService {

    @POST("user/subscribe/send")
    suspend fun sendUserSubscribe(@Body subscribeModel: SubscribeModel): SubscribtionAnswerModel

    @POST("user/courseBuy/send")
    suspend fun sendUserCourseBuy(@Body courseBuyModel: CourseBuyModel):SubscribtionAnswerModel

    @POST("user/themeBuy/send")
    suspend fun sendUserThemeBuy(@Body themeBuyModel: ThemeBuyModel):SubscribtionAnswerModel

    @POST("user/subscribe/check")
    suspend fun checkUserSubscribe(
        @Body sendSubscribeCheckModel: SendSubscribeCheckModel
    ): CheckSubscribeBackModel

    @GET("user/courseBuy/check")
    suspend fun checkUserBuyCourse(
        @Query("token") token: String
    ):List<CourseBuyModel>

    @GET("user/themeBuy/check")
    suspend fun checkUserBuyTheme(
        @Query("token") token: String
    ):List<ThemeBuyModel>

    @GET("user/subscribe/get")
    suspend fun getMySubscribe(@Query("token")token: String):SubscribeModel

}