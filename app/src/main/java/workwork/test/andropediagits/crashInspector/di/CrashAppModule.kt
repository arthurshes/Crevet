package workwork.test.andropediagits.crashInspector.di

import android.content.Context
import androidx.room.Room

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import workwork.test.andropediagits.crashInspector.core.utils.Constants.EXCEPTION_BASE_URL
import workwork.test.andropediagits.crashInspector.data.local.CrashMainDb
import workwork.test.andropediagits.crashInspector.data.remote.CrashApiService
import workwork.test.andropediagits.crashInspector.data.repo.CrashRepoImpl
import workwork.test.andropediagits.crashInspector.domain.repo.CrashRepo
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CrashAppModule {
        @Provides
        fun provideCrashRepo(crashApiService: CrashApiService, crashMainDb: CrashMainDb): CrashRepo {
            return CrashRepoImpl(crashApiService,crashMainDb)
        }

    @Provides
    @Singleton
    fun provideMainDb(@ApplicationContext context:Context):CrashMainDb =
        Room.databaseBuilder(context,CrashMainDb::class.java,"crash_local_db")
            .build()

    @Provides
    @Singleton
    fun provideCrashApiService():CrashApiService =
        Retrofit.Builder()
            .baseUrl(EXCEPTION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CrashApiService::class.java)


}