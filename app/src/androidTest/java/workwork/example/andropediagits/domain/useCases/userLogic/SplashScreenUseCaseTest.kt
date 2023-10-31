package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.SplashActionEnum
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.UserInfoDao
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class SplashScreenUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    lateinit var splashScreenUseCase: SplashScreenUseCase

    @Inject
    @Named("test_tryAgainUseCase")
    lateinit var tryAgainUseCase: TryAgainUseCase
    @Inject
    @Named("test_updateUserInfoUseCase")
    lateinit var updateUserInfoUseCase: UpdateUserInfoUseCase
    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo

    @Before
    fun setup() {
        hiltRule.inject()
        splashScreenUseCase = SplashScreenUseCase(userLogicRepo, tryAgainUseCase, updateUserInfoUseCase,transactionRepo)
    }

    @Test
    fun testStart_Success() = runBlocking {
        var actionResult: SplashActionEnum? = null

        // Act
        splashScreenUseCase.start(
            isSuccess = { actionResult = it },
            buyThemesId = null
        )

        // Assert
        assertEquals(actionResult, SplashActionEnum.SIGNINSCREEN)

    }

}