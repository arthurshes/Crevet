package workwork.test.andropediagits

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters

import dagger.hilt.android.HiltAndroidApp
import workwork.test.andropediagits.core.workmanagers.CheckCacheWorker
import workwork.test.andropediagits.crashInspector.domain.useCase.CrashUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.CacheUseCase
import javax.inject.Inject

@HiltAndroidApp
class MyApp:Application(),Configuration.Provider{

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "cachecheck_worker",
                "Скачивание курсов",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = ContextCompat.getSystemService(
                applicationContext,
                NotificationManager::class.java
            )
            notificationManager?.createNotificationChannel(channel)
        }
    }

    @Inject
    lateinit var cacheWorkerFactory: CheckCacheWorkerFactory



    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(cacheWorkerFactory)
            .build()
}



class CheckCacheWorkerFactory @Inject constructor(private val cacheUseCase: CacheUseCase, private val crashUseCase: CrashUseCase):WorkerFactory(){
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = CheckCacheWorker(cacheUseCase, appContext,workerParameters,crashUseCase)

}

