package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.victorine.VictorineDao
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.remote.model.promo.PromoCodeResponse
import com.example.andropediagits.data.remote.model.strike.StrikeModeSendModel
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject
import javax.inject.Named


@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class StrikeModeUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb

    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    lateinit var strikeModeUseCase: StrikeModeUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        strikeModeUseCase = StrikeModeUseCase(userLogicRepo, transactionRepo)
    }
    @After
    fun teardown() {
        database.close()
    }
    @Test
    fun testStrikeModeMainFunSuccess() = runTest {
        val userInfo = UserInfoEntity(token = "userToken", lastOnlineDate = "2023-08-01", strikeModeDay = 3)
        val userStrikeModel = StrikeModeSendModel(
            token = "userToken",
            lastDateApi ="ewew" ,
            currentDateApi = "fef"
        )

        userLogicRepo.insertUserInfoLocal(userInfo)
        userLogicRepo.getMyStrikeModeInfo(userStrikeModel)


        val isSuccessStates = mutableListOf<ErrorEnum>()
        val strikeModeDayValues = mutableListOf<Int>()

        strikeModeUseCase.strikeModeMainFun(
            { isSuccessStates.add(it) },
            { strikeModeDayValues.add(it) },
            null
        )

            //assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)

        assertEquals(strikeModeDayValues.size, 1)
        assertEquals(strikeModeDayValues[0], 3)
    }
}