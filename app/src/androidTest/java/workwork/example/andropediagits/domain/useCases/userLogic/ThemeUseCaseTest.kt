package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.entities.UserInfoEntity
import com.example.andropediagits.data.local.entities.theme.ThemeBuyEntity
import com.example.andropediagits.data.local.entities.theme.ThemeEntity
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
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
class ThemeUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb


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

    lateinit var themeUseCase: ThemeUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        themeUseCase = ThemeUseCase(transactionRepo, userLogicRepo, courseRepo, tryAgainUseCase)
    }
    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun testWatchAdsToDisableDelay_WithFullyPaidTheme() = runTest {
        // Arrange
        val uniqueThemeId = 123
        var remainigTermHourse = 4
        val theme =  ThemeEntity(uniqueThemeId = uniqueThemeId, lastUpdateDate = Date(), themeName = "Theme 1", courseNumber = 4, lessonsCount = 5, themeNumber = 1, lastCourseTheme = true, interactiveCodeMistakes = 1, interactiveCodeCorrect = 9, victorineMistakeAnswer = 2, victorineCorrectAnswer = 8, victorineDate = Date(), interactiveTestId = 501, victorineQuestionCount = 3, interactiveQuestionCount = 7, vicotineTestId = 601, duoDate = Date(), isDuoInter = false, isVictorine = true, isOpen = true, isFav = false, termHourse = remainigTermHourse, termDateApi = "2023-07-26", possibleToOpenThemeFree =true,    imageTheme = null)
        val myInfoLocal = UserInfoEntity(token = "dummyToken")
        courseRepo.insertTheme(theme)
        userLogicRepo.insertUserInfoLocal(myInfoLocal)
        var errorEnum: ErrorEnum? =null

        // Act
        themeUseCase.watchAdsToDisableDelay(uniqueThemeId, isSucces = {errorEnum=it}, remainigTermHourse = {theme.termHourse=it})

        // Assert
        assertEquals(errorEnum, ErrorEnum.SUCCESS)
        assertEquals(1,theme.termHourse)
        // Check if theme is still fully paid and open
        val updatedTheme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        assertEquals(updatedTheme.possibleToOpenThemeFree, true)
    }
    @Test
    fun  testThisThemeBuy_Success() = runTest {
        val uniqueThemeId = 123
        val token = "userToken"
        var isSuccessCalled = false
        var isBuyResult: Boolean? = null
        val themeBuy = ThemeBuyEntity(
            courseNumber = 123,
            themeNumber = 456,
            uniqueThemeId = uniqueThemeId,
            date = Date(), // Здесь можно использовать актуальную дату
            token = token,
            transactionId = "transaction123"
        )
        val userInfo = UserInfoEntity(
            name = "John Doe", // Исправлено на "John Doe"
            image = null, // Set your Bitmap here
            userLanguage = "English",
            phoneBrand = "Samsung",
            token =token,
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )
        userLogicRepo.insertUserInfoLocal(userInfo)
        transactionRepo.insertBuyTheme(themeBuy)
        // Run the suspend function within runBlockingTest
        themeUseCase.thisThemeBuy(
            { errorEnum ->
                when (errorEnum) {
                    ErrorEnum.SUCCESS -> {
                        isSuccessCalled = true
                    }
                    // ... Handle other cases ...
                    else -> {}
                }
            },
            { isBuy ->
                isBuyResult = isBuy
            },
            uniqueThemeId,
        )

        // Assertions
        Assert.assertTrue(isSuccessCalled)
       Assert.assertTrue(isBuyResult ?: false)
    }

    @Test
    fun testCheckOneThemeTernAndNo() = runTest {
        val uniqueThemeId = 1
        val termHourse = 10
        val termDateApi = "2023-08-09T12:00:00Z"
        val userInfo = UserInfoEntity(

            name = "John Doe",
            image = null,
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "t",
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )
        userLogicRepo.insertUserInfoLocal(userInfo)
        val theme = ThemeEntity(
            uniqueThemeId = uniqueThemeId,
            lastUpdateDate = Date(),
            themeName = "Theme 1",
            courseNumber = 201,
            lessonsCount = 5,
            themeNumber = 1,

            lastCourseTheme = true,
            interactiveCodeMistakes = 1,
            interactiveCodeCorrect = 9,
            victorineMistakeAnswer = 2,
            victorineCorrectAnswer = 8,
            victorineDate = Date(),
            interactiveTestId = 3,
            victorineQuestionCount = 3,
            interactiveQuestionCount = 7,
            vicotineTestId = 601,
            duoDate = Date(),
            isDuoInter = false,
            isVictorine = true,
            isOpen = true,
            isFav = false,
            termHourse = termHourse,
            termDateApi = termDateApi, possibleToOpenThemeFree = true,    imageTheme = null
        )
        courseRepo.insertTheme(theme)
     //   courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        userLogicRepo.getUserInfoLocal()

        var isSuccess: ErrorEnum? = null
        var isNoTerm: Boolean? = null

        themeUseCase.checkOneThemeTernAndNo(uniqueThemeId, { errorState ->
            isSuccess = errorState
        }, { noTerm ->
            isNoTerm = noTerm
        })

        assertEquals(ErrorEnum.SUCCESS, isSuccess)
        assertEquals(isNoTerm, false)

    }
    @Test
    fun testCheckOneThemeTernAndNo_true() = runTest {
        val uniqueThemeId = 1
        val termHourse = 0
        val termDateApi = "2023-08-09T12:00:00Z"
        val userInfo = UserInfoEntity(
            name = "John Doe", // Исправлено на "John Doe"
            image = null, // Set your Bitmap here
            userLanguage = "English",
            phoneBrand = "Samsung",
            token = "t",
            andropointCount = 0,
            lastOnlineDate = "2023-08-09",
            strikeModeDay = 5,
            lastOpenCourse = 123,
            lastOpenTheme = 456
        )
        userLogicRepo.insertUserInfoLocal(userInfo)
        val theme = ThemeEntity(
            uniqueThemeId = uniqueThemeId,
            lastUpdateDate = Date(),
            themeName = "Theme 1",
            courseNumber = 201,
            lessonsCount = 5,
            themeNumber = 1,
            imageTheme = null,
            lastCourseTheme = true,
            interactiveCodeMistakes = 1,
            interactiveCodeCorrect = 9,
            victorineMistakeAnswer = 2,
            victorineCorrectAnswer = 8,
            victorineDate = Date(),
            interactiveTestId = 3,
            victorineQuestionCount = 3,
            interactiveQuestionCount = 7,
            vicotineTestId = 601,
            duoDate = Date(),
            isDuoInter = false,
            isVictorine = true,
            isOpen = true,
            isFav = false,
            termHourse = termHourse,
            termDateApi = termDateApi, possibleToOpenThemeFree = true
        )
        courseRepo.insertTheme(theme)
     //   courseRepo.searchThemeWithUniwueId(uniqueThemeId)
        userLogicRepo.getUserInfoLocal()

        var isSuccess: ErrorEnum? = null
        var isNoTerm: Boolean? = null

        themeUseCase.checkOneThemeTernAndNo(uniqueThemeId, { errorState ->
            isSuccess = errorState
        }, { noTerm ->
            isNoTerm = noTerm
        })

        assertEquals(ErrorEnum.SUCCESS, isSuccess)
        assertEquals(isNoTerm, true)

    }
}