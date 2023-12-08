package workwork.test.andropediagits.di.userLogicModule


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import workwork.test.andropediagits.core.utils.Constatns.BASE_URL
import workwork.test.andropediagits.data.local.MainDb
import workwork.test.andropediagits.data.remote.LogicUserApiService
import workwork.test.andropediagits.data.remote.TimeApiService
import workwork.test.andropediagits.data.repo.CourseRepoImpl
import workwork.test.andropediagits.data.repo.IndividualCoursesRepoImpl
import workwork.test.andropediagits.data.repo.UserLogicRepoImpl
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.IndividualCoursesRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.validators.UserInfoValidator

import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserLogicModule {

    @Provides
    @Singleton
    fun provideIndiCoursesRepo(mainDb: MainDb,logicUserApiService: LogicUserApiService):IndividualCoursesRepo =
        IndividualCoursesRepoImpl(mainDb, logicUserApiService)


    @Provides
    fun provideUserInfo(): UserInfoValidator =
        UserInfoValidator()

//    @Provides
//    @Singleton
//    fun provideTimer():CustomTimerUtil =
//        CustomTimerUtil()

    @Provides
    @Singleton
    fun provideCourseRepo(mainDb: MainDb, logicUserApiService: LogicUserApiService, timeApiService: TimeApiService): CourseRepo {
        return CourseRepoImpl(mainDb,logicUserApiService,timeApiService)
    }

    @Provides
    fun provideSignInApiService(okHttpClient: OkHttpClient): LogicUserApiService =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(LogicUserApiService::class.java)

    @Provides
    @Singleton
    fun provideUserLogicRepo(signInApiService: LogicUserApiService, mainDb: MainDb, timeApiService: TimeApiService): UserLogicRepo {
        return UserLogicRepoImpl(signInApiService, mainDb,timeApiService)
    }



}