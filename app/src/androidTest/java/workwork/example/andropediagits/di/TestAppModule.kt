package workwork.example.andropediagits.di

import android.content.Context
import androidx.room.Room
import com.example.andropediagits.core.utils.CustomTimerUtil
import com.example.andropediagits.crashInspector.domain.repo.CrashRepo
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase_Factory
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.FakeCourseRepo
import com.example.andropediagits.domain.repo.FakeCrashRepo
import com.example.andropediagits.domain.repo.FakeTransactionRepo
import com.example.andropediagits.domain.repo.FakeUserLogicRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import com.example.andropediagits.domain.useCases.userLogic.validators.EmailValidator
import com.example.andropediagits.domain.useCases.userLogic.validators.UserInfoValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    /*@Provides@Named("test_tryAgainUseCase")fun provideTryAgainUseCase(@Named("test_courseRepo")courseRepo: CourseRepo,@Named("test_transactionRepo")transactionRepo: TransactionRepo,@Named("test_userLogicRepo")userLogicRepo: UserLogicRepo): TryAgainUseCase {
        return TryAgainUseCase(courseRepo,transactionRepo,userLogicRepo)
    }*/

    @Provides
    @Named("test_dbversion")
    fun provideInMemoryMainDb(@ApplicationContext context: Context): MainDb =
        Room.inMemoryDatabaseBuilder(context, MainDb::class.java)
            .allowMainThreadQueries()
            .build()

    @Provides
    @Named("test_userLogicRepo")
    fun provideUserLogicRepo(): UserLogicRepo {
        return FakeUserLogicRepo()
    }

    @Provides
    @Named("test_transactionRepo")
    fun provideTransactionRepo(): TransactionRepo {
        return FakeTransactionRepo()
    }

    @Provides
    @Named("test_courseRepo")
    fun provideCourseRepo(): CourseRepo {
        return FakeCourseRepo()
    }

    @Provides
    @Named("test_crashRepo")
    fun provideCrashRepo(): CrashRepo {
        return FakeCrashRepo()
    }

    @Provides
    @Named("test_updateThemeUseCase")
    fun provideUpdateThemeUseCase(@Named("test_courseRepo") courseRepo: CourseRepo): UpdateThemeUseCase {
        return UpdateThemeUseCase(courseRepo)
    }

    @Provides
    @Named("test_tryAgainUseCase")
    fun provideTryAgainUseCase(
        @Named("test_courseRepo") courseRepo: CourseRepo,
        @Named("test_transactionRepo") transactionRepo: TransactionRepo,
        @Named("test_userLogicRepo") userLogicRepo: UserLogicRepo
    ): TryAgainUseCase {
        return TryAgainUseCase(courseRepo, transactionRepo, userLogicRepo)
    }


    @Provides
    @Named("test_updateUserInfoUseCase")
    fun provideUpdateUserInfoUseCase(@Named("test_userLogicRepo") userLogicRepo: UserLogicRepo): UpdateUserInfoUseCase {
        return UpdateUserInfoUseCase(userLogicRepo)
    }

    @Provides
    @Named("test_emailValidator")
    fun provideEmailValidator(@Named("test_userLogicRepo") userLogicRepo: UserLogicRepo): EmailValidator {
        return EmailValidator(userLogicRepo)
    }

    @Provides
    @Named("test_crashUseCase")
    fun provideCrashUseCase(@Named("test_crashRepo") crashRepo: CrashRepo): CrashUseCase {
        return CrashUseCase(crashRepo)
    }
    @Provides
    @Named("test_userInfoValidator")
    fun provideUserInfoValidator(): UserInfoValidator {
        return UserInfoValidator()
    }
    @Provides
    @Named("test_customTimerUtil")
    fun provideTimer(): CustomTimerUtil =
        CustomTimerUtil()

}
