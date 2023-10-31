package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.core.utils.Constatns.VICTORINE_REMUNERATION
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import com.example.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import com.example.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import com.example.andropediagits.domain.useCases.userLogic.state.StrikeModeState
import com.example.andropediagits.getOrAwaitValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class AndropointUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var andropointUseCase: AndropointUseCase

    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    @Inject
    @Named("test_crashUseCase")
    lateinit var crashUseCase: CrashUseCase

    @Inject
    @Named("test_updateUserInfoUseCase")
    lateinit var updateUserInfoUseCase: UpdateUserInfoUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        andropointUseCase = AndropointUseCase(userLogicRepo, transactionRepo, updateUserInfoUseCase, crashUseCase)
    }

    @Test
    fun testVictorineProgressAndropointSuccess() = runTest {
        val userInfo = UserInfoEntity(
            name = "John Doe",
            image = null, // Set your Bitmap here
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "userToken",
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )
        val userInfo2 = UserInfoEntity(
            name = "John Doe",
            image = null, // Set your Bitmap here
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "tfy",
            andropointCount = 44,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )
        val isSuccessStates = mutableListOf<ErrorEnum>()

        userLogicRepo.insertUserInfoLocal(userInfo)
        andropointUseCase.victorineProgressAndropoint(
            isSuccess = { isSuccessStates.add(it) }
        )
        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)
            //  userLogicRepo.updateUserInfoLocal(userInfo2)

        assertEquals(
            userLogicRepo.getUserInfoLocal().andropointCount,
            VICTORINE_REMUNERATION
        )

    }
    @Test
    fun testSpendAndropointsSkipDelay() = runTest {
        // Arrange
        val userInfo = UserInfoEntity(
            token = "userToken",
            lastOnlineDate = "2023-08-01",
            strikeModeDay = 3,
            andropointCount = 80
        )
        userLogicRepo.insertUserInfoLocal(userInfo)

        val isSuccessStates = mutableListOf<ErrorEnum>()
        val isAndropointStates = mutableListOf<BuyForAndropointStates>()

        // Act
        andropointUseCase.spendAndropoints(
            spendAndropointState = SpendAndropointState.SKIPDELAY,
            isSuccess = { isSuccessStates.add(it) },
            isAndropointState = { isAndropointStates.add(it) }
        )

        // Assert
     //   assertEquals(isSuccessStates.size, 1)
   //     assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)

        assertEquals(isAndropointStates.size, 1)
        assertEquals(isAndropointStates[0], BuyForAndropointStates.YESMONEY)
        // Check if andropoint count is updated correctly in the repo
        assertEquals(userLogicRepo.getUserInfoLocal().andropointCount, 0)
    }
    /* @Test
    fun testStrikeModeAddAndropointOne() = runBlockingTest {

        val isSuccessStates = mutableListOf<ErrorEnum>()

        // Act
        andropointUseCase.strikeModeAddAndropoint(
            isSuccess = { isSuccessStates.add(it) },
            strikeModeState = StrikeModeState.ONE
        )

        // Assert
        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)
        // Check if andropoint count is updated correctly in the repo
        assertEquals(userLogicRepo.getUserInfoLocal().value?.andropointCount, 2)
    }*/

    // Add similar tests for other StrikeModeState cases
}