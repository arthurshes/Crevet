package workwork.example.andropediagits.domain.useCases.userLogic.privateUseCase

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.AndropointUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class UpdateUserInfoUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    lateinit var updateUserInfoUseCase: UpdateUserInfoUseCase
    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    @Before
    fun setup() {
        hiltRule.inject()
        updateUserInfoUseCase=UpdateUserInfoUseCase(userLogicRepo)
    }
    @Test
    fun testUpdateUserInfo_WithName() = runTest {
        val userInfo = UserInfoEntity(
            name = "John Doe",
            image = null,
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "userToken",
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )

        userLogicRepo.insertUserInfoLocal(userInfo)
        val name = "John Doerrr"

        updateUserInfoUseCase.updateUserInfo(name = name)

        val updatedUserInfo = userLogicRepo.getUserInfoLocal()
        assertEquals(updatedUserInfo.name?.trim(), name)
    }

    @Test
    fun testUpdateUserInfo_WithAndropointCount() = runTest {
        val userInfo = UserInfoEntity(
            name = "John Doere",
            image = null,
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "userToken",
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456)
        userLogicRepo.insertUserInfoLocal(userInfo)
        val andropointCount = 100
        updateUserInfoUseCase.updateUserInfo(andropointCount = andropointCount)

        val updatedUserInfo = userLogicRepo.getUserInfoLocal()

        assertEquals(updatedUserInfo.andropointCount, andropointCount)
    }
    @Test
    fun testUpdateUserInfo_userNull() = runTest {
        val name = "John Doerrr"

        updateUserInfoUseCase.updateUserInfo(name = name)

        val updatedUserInfo = userLogicRepo.getUserInfoLocal()

        assertEquals(updatedUserInfo?.name, name)
    }
}
