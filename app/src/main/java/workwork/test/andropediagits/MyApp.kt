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


@HiltAndroidApp
class MyApp:Application(){

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


}





