package workwork.test.andropediagits.core.module

import android.content.Context
import androidx.room.Room

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.core.utils.Constatns.MY_API_KEY
import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.remote.interceptor.HeaderInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
        OkHttpClient.Builder()
            .callTimeout(90,TimeUnit.SECONDS)
            .connectTimeout(40, TimeUnit.SECONDS)
            .readTimeout(50, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(HeaderInterceptor(MY_API_KEY))
            .addInterceptor(httpLoggingInterceptor)
            .build()

    @Provides
    @Singleton
    fun provideMainDb(@ApplicationContext context:Context): MainDb =
        Room.databaseBuilder(context,MainDb::class.java,"andropedia.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTimeApiService(okHttpClient: OkHttpClient): TimeApiService =
        Retrofit.Builder()
            .baseUrl(Constatns.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TimeApiService::class.java)

}