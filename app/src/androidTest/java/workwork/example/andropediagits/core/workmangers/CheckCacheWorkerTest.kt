package workwork.example.andropediagits.core.workmangers

import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.example.andropediagits.core.workmanagers.CheckCacheWorker
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase
import com.example.andropediagits.di.TestAppModule
import com.example.andropediagits.domain.useCases.userLogic.CacheUseCase
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.Executor
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
@UninstallModules(TestAppModule::class) // Дополнительно, если требуется отключить модули Hilt в тесте
class MyWorkerTest {

  /*  private lateinit var worker: CheckCacheWorker

    @Inject
    lateinit var cacheUseCase: CacheUseCase

    @Inject
    lateinit var context: Context

    @Inject
    lateinit var workerParameters: WorkerParameters

    @Inject
    lateinit var crashUseCase: CrashUseCase

    @Before
    fun setup() {
        // Initialize Hilt
        DaggerWorkerTest_HiltComponents_TestSingletonComponent.builder()
            .applicationContext(ApplicationProvider.getApplicationContext())
            .build()
            .inject(this)

        // Create the worker
        worker = CheckCacheWorker(cacheUseCase, context, workerParameters, crashUseCase)
    }

    @Test
    fun testDoWork_Success() {
        runBlockingTest {
            // Mock successful calls of cacheUseCase.checkCacheWorkManager() and other functions if needed.
            // For example, you can use `whenever` from Mockito.

            // Act
            val result = worker.doWork()

            // Assert
            assertEquals(Result.success(), result)
        }
    }

    @Test
    fun testDoWork_RetryOnError() {
        runBlockingTest {
            // Mock the cacheUseCase.checkCacheWorkManager() and other functions to throw IOException, HttpException, or other exceptions if needed.
            // For example, you can use `whenever` from Mockito.

            // Act
            val result = worker.doWork()

            // Assert
            assertEquals(Result.retry(), result)
        }
    }*/
}
