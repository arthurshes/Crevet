package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.victorine.VictorineDao
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.remote.model.promo.PromoCodeModel
import com.example.andropediagits.data.remote.model.promo.PromoCodeResponse
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import com.example.andropediagits.domain.useCases.userLogic.state.PromoCodeState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.slot
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
class PromoCodeUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    lateinit var promoCodeUseCase: PromoCodeUseCase


    @Before
    fun setup() {
        hiltRule.inject()
       // victorineDao= database.getVictorineDao()
        promoCodeUseCase = PromoCodeUseCase(userLogicRepo)
    }
    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testCheckPromoCodePromoCodeExists() = runTest {
        // Arrange
        val promoCode = "SPECIAL123"
        val currentTime = "2023-08-09T12:00:00"
        val userInfo = UserInfoEntity(token = "userToken")
        val promoCodeModel = PromoCodeModel(
          token = "userToken",
          promoCode = "SPECIAL123",
            currentTime
        )
        userLogicRepo.insertUserInfoLocal(userInfo)
        userLogicRepo.sendPromoCode(promoCodeModel)

        val promoCodeStates = mutableListOf<PromoCodeState>()
        val isSuccessStates = mutableListOf<ErrorEnum>()

        // Act
        promoCodeUseCase.checkPromoCode(
            promoCode,
            { isSuccessStates.add(it) },
            { promoCodeStates.add(it) }
        )

        // Assert
        assertEquals(promoCodeStates.size, 1)
        assertEquals(promoCodeStates[0], PromoCodeState.PROMOEXISTSUCCESS)

        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)
    }
    @Test
    fun testCheckPromoCodePromoCodeDoesNotExist() = runTest {
        // Arrange
        val promoCode = "INVALID123"
        val userInfo = UserInfoEntity(token = "userTokenyy")
        val promoCodeModel = PromoCodeModel(
            token = "userToken",
            promoCode = "INVALID123",
            "443434"
        )
        userLogicRepo.insertUserInfoLocal(userInfo)
        userLogicRepo.sendPromoCode(promoCodeModel)

        val promoCodeStates = mutableListOf<PromoCodeState>()
        val isSuccessStates = mutableListOf<ErrorEnum>()

        // Act
        promoCodeUseCase.checkPromoCode(
            promoCode,
            { isSuccessStates.add(it) },
            { promoCodeStates.add(it) }
        )

        // Assert
        assertEquals(promoCodeStates.size, 1)
        assertEquals(promoCodeStates[0], PromoCodeState.PROMONOTEXIST)

        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)
    }
}