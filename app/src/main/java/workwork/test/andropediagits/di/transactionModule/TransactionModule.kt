package workwork.test.andropediagits.di.transactionModule


import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import workwork.test.andropediagits.core.utils.Constatns.BASE_URL
import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.remote.TransactionApiService
import workwork.test.andropediagits.data.repo.TransactionRepoImpl
import workwork.test.andropediagits.domain.repo.TransactionRepo
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TransactionModule {



    @Provides
    fun provideTransactionApiService(okHttpClient: OkHttpClient): TransactionApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(TransactionApiService::class.java)

    @Provides
    @Singleton
    fun provideTransactionRepo(transactionApiService: TransactionApiService, timeApiService: TimeApiService, mainDb: MainDb): TransactionRepo {
        return TransactionRepoImpl(transactionApiService,timeApiService,mainDb)
    }

}