package workwork.test.andropediagits.crashInspector.data.remote


import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface CrashApiService {

    @POST("user/crash/send")
    suspend fun sendCrashAnalytic(@Body crashInfoModels: List<CrashInfoModel>):CrashSendStatus

}