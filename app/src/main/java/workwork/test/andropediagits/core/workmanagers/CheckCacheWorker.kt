package workwork.test.andropediagits.core.workmanagers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters


import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.R
import workwork.test.andropediagits.crashInspector.domain.useCase.CrashUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.CacheUseCase

import java.util.concurrent.TimeoutException
import kotlin.random.Random

@HiltWorker
class CheckCacheWorker @AssistedInject constructor(@Assisted private val cacheUseCase: CacheUseCase, @Assisted val context: Context, @Assisted workerParameters: WorkerParameters, @Assisted private val crashUseCase: CrashUseCase):CoroutineWorker(context,workerParameters) {

    override suspend fun doWork(): Result {
        startForegroundService()
        return try {
            cacheUseCase.checkCacheWorkManager()
            cacheUseCase.checkSubscribesAndBuyCourseFromWorkmanager()
            crashUseCase.checkLocalCrashAndSend()
            Result.success()
        }catch (e:IOException){
            Result.retry()
        }catch (e: HttpException){
            Result.retry()
        }catch (e: TimeoutException){
            Result.retry()
        }catch (e:NullPointerException){
            Result.failure()
        }catch (e:Exception){
            Result.failure()
        }
    }

    private suspend fun startForegroundService(){
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context,"cachecheck_worker")
                    .setSmallIcon(R.drawable.download_image)
                    .setContentText("Проверяем актуальность курсов")
                    .setContentTitle("Проверка актуальности данных")
                    .build()
            )
        )
    }
}