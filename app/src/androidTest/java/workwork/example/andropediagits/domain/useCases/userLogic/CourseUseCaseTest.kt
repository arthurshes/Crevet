package workwork.example.andropediagits.domain.useCases.userLogic


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.crashInspector.domain.useCase.CrashUseCase
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.local.entities.course.CourseBuyEntity
import com.example.andropediagits.data.local.entities.course.CourseEntity
import com.example.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import com.example.andropediagits.data.remote.model.course.CourseBuyModel
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

class CourseUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var courseUseCase: CourseUseCase

    @Inject
    @Named("test_courseRepo")
    lateinit var courseRepo: CourseRepo

    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo

    @Inject
    @Named("test_tryAgainUseCase")
    lateinit var tryAgainUseCase: TryAgainUseCase

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    @Before
    fun setup() {
        hiltRule.inject()
        courseUseCase = CourseUseCase(courseRepo, transactionRepo, userLogicRepo, tryAgainUseCase)
    }

    @Test
    fun testThisCourseBuy() = runTest {

        val isSuccessStates = mutableListOf<ErrorEnum>()
        val isBuyStates = mutableListOf<Boolean>()
        val courseBuyModel = CourseBuyEntity(
            courseNumber = 3,
            token = "userToken",
            promoCode = "promoCode",
            transactionId = "4",
            andropointBuy = true,
            date = Date()
        )
        val userInfo = UserInfoEntity(
            token = "userToken",
            lastOnlineDate = "2023-08-01",
            strikeModeDay = 3,
            andropointCount = 80
        )

        userLogicRepo.insertUserInfoLocal(userInfo)
        transactionRepo.insertCourseBuy(courseBuyModel)
        // Act
        courseUseCase.thisCourseBuy(
            isSuccess = { isSuccessStates.add(it) },
            courseNumber = 3,
            isBuy = { isBuyStates.add(it) }
        )

        // Assert
        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)

        assertEquals(isBuyStates.size, 1)
        assertEquals(isBuyStates[0], true)
    }

    @Test
    fun testTryAgainCourseSend() = runTest {
        val isSuccessStates = mutableListOf<ErrorEnum>()
        val buyThemesIdStates = mutableListOf<List<Int>>()
        val key = UpdatesKeyEntity(
            updateKeyId = 1,
            courseNumber = 123,
            uniqueThemeId = 456,
            uniqueLevelId = 789,
            interactiveTestId = 987,
            buyCourseNumber = 654,
            buyThemeUniqueId = 321,
            vicotineTestId = 555,
            subscribeTrasaction = "transaction123",
            updateNameBoolean = true,
            updateTime = Date(),
            openCourseNumber = 789,
            openUniqueThemeId = 654
        )
        courseRepo.insertKey(key)

        courseUseCase.tryAgainCourseSend(
            isSuccess = { isSuccessStates.add(it) },
            buyThemesId = { buyThemesIdStates.add(it) }
        )

        assertEquals(isSuccessStates.size, 1)
        assertEquals(isSuccessStates[0], ErrorEnum.SUCCESS)
        // assertEquals(buyThemesIdStates.size, 1)
    }
    //with test
    @Test
    fun testCourseBuyAndropoint_possibleToOpenCourseFree() = runTest {
        val courseNumber = 1

        var isSuccessResult: ErrorEnum? = null
        var isCoursePremiumResult: Boolean? = null

        val courseEntity = CourseEntity(
            courseNumber = courseNumber,
            isOpen = true,
            possibleToOpenCourseFree = true,
            courseName = "Sample Course",
            description = "This is a sample course description.",
            lastUpdateDate = Date(),
            isNetworkConnect = false
        )
        courseRepo.insertCourse(courseEntity)
        courseUseCase.courseBuyAndropoint(
            isSuccess = { isSuccessResult = it },
            courseNumber,
            isCoursePremium = { isCoursePremiumResult = it })

        assertEquals(isSuccessResult, ErrorEnum.SUCCESS)
        assertEquals(isCoursePremiumResult, false)
    }
    @Test
    fun testCourseBuyAndropoint_isFullyPaid() = runTest {
        val courseNumber = 1

        var isSuccessResult: ErrorEnum? = null
        var isCoursePremiumResult: Boolean? = null

        val courseEntity = CourseEntity(
            courseNumber = courseNumber,
            isOpen = true,
            possibleToOpenCourseFree = false,
            courseName = "Sample Course",
            description = "This is a sample course description.",
            lastUpdateDate = Date(),
            isNetworkConnect = false
        )
        courseRepo.insertCourse(courseEntity)
        courseUseCase.courseBuyAndropoint(
            isSuccess = { isSuccessResult = it },
            courseNumber,
            isCoursePremium = { isCoursePremiumResult = it })

        assertEquals(isSuccessResult, ErrorEnum.SUCCESS)
        assertEquals(isCoursePremiumResult, true)
    }
}