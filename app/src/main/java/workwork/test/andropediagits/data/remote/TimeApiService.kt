package workwork.test.andropediagits.data.remote


import retrofit2.http.GET
import workwork.test.andropediagits.data.remote.model.TimeAnswerModel

interface TimeApiService {

    @GET("currentTime/time/get")
    suspend fun getCurrentTime(): TimeAnswerModel

}