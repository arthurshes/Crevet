package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.theme.ThemeDao
import com.example.andropediagits.data.local.entities.theme.ThemeEntity
import com.example.andropediagits.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ThemeDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var themeDao: ThemeDao

    @Before
    fun setup() {
        hiltRule.inject()

        themeDao = database.getLevelDao()
    }

    @After
    fun tearDown() {
        database.close()
    }




    @Test
    fun testSearchThemeWithUniqueId() = runTest {
        val uniqueThemeId = 101

        val theme = ThemeEntity(
            uniqueThemeId = uniqueThemeId,
            lastUpdateDate = Date(),
            themeName = "Theme 3",
            courseNumber = 201,
            lessonsCount = 10, themeNumber = 3, imageTheme =null, lastCourseTheme = true, interactiveCodeMistakes = 2, interactiveCodeCorrect = 8, victorineMistakeAnswer = 1, victorineCorrectAnswer = 9, victorineDate = Date(), interactiveTestId = 501, victorineQuestionCount = 5, interactiveQuestionCount = 10, vicotineTestId = 601, duoDate = Date(), isDuoInter = true, isVictorine = true, isOpen = false, isFav = true, termHourse = 20,  termDateApi = "2023-07-26", possibleToOpenThemeFree =true)

        themeDao.insertTheme(theme)

        val resultTheme = themeDao.searchThemeWithUniqueId(uniqueThemeId)

        assertThat(resultTheme).isEqualTo(theme)
    }
    @Test
    fun testSearchThemesWithCourseNumber() = runTest {
        val courseNumber = 201

        val theme1 = ThemeEntity(uniqueThemeId = 101, lastUpdateDate = Date(), themeName = "Theme 1", courseNumber = courseNumber, lessonsCount = 5, themeNumber = 1, imageTheme =null, lastCourseTheme = true, interactiveCodeMistakes = 1, interactiveCodeCorrect = 9, victorineMistakeAnswer = 2, victorineCorrectAnswer = 8, victorineDate = Date(), interactiveTestId = 501, victorineQuestionCount = 3, interactiveQuestionCount = 7, vicotineTestId = 601, duoDate = Date(), isDuoInter = false, isVictorine = true, isOpen = true, isFav = false, termHourse = 15,  termDateApi = "2023-07-26", possibleToOpenThemeFree =true)

        val theme2 = ThemeEntity(uniqueThemeId = 102, lastUpdateDate = Date(), themeName = "Theme 2", courseNumber = courseNumber, lessonsCount = 8, themeNumber = 2, imageTheme = null, lastCourseTheme = false, interactiveCodeMistakes = 2, interactiveCodeCorrect = 8, victorineMistakeAnswer = 3, victorineCorrectAnswer = 7, victorineDate = Date(), interactiveTestId = 502, victorineQuestionCount = 4, interactiveQuestionCount = 6, vicotineTestId = 602, duoDate = Date(), isDuoInter = true, isVictorine = false, isOpen = false, isFav = true, termHourse = 12, termDateApi = "2023-07-27", possibleToOpenThemeFree =true)

        themeDao.insertTheme(theme1)
        themeDao.insertTheme(theme2)

        val resultThemes = themeDao.searchThemesWithCourseNumber(courseNumber)

        assertThat(resultThemes).containsExactly(theme1, theme2)
    }
    @Test
    fun testSearchThemeWithInteractiveId() = runTest {
        val interactiveTestId = 501

        val theme1 = ThemeEntity(
            uniqueThemeId = 101,
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
            interactiveTestId = interactiveTestId,
            victorineQuestionCount = 3,
            interactiveQuestionCount = 7,
            vicotineTestId = 601,
            duoDate = Date(),
            isDuoInter = false,
            isVictorine = true,
            isOpen = true,
            isFav = false,
            termHourse = 15,
            termDateApi = "2023-07-26", possibleToOpenThemeFree =true
        )

        val theme2 = ThemeEntity(
            uniqueThemeId = 102,
            lastUpdateDate = Date(),
            themeName = "Theme 2",
            courseNumber = 201,
            lessonsCount = 8,
            themeNumber = 2,
            imageTheme = null,
            lastCourseTheme = false,
            interactiveCodeMistakes = 2,
            interactiveCodeCorrect = 8,
            victorineMistakeAnswer = 3,
            victorineCorrectAnswer = 7,
            victorineDate = Date(),
            interactiveTestId = 502,
            victorineQuestionCount = 4,
            interactiveQuestionCount = 6,
            vicotineTestId = 602,
            duoDate = Date(),
            isDuoInter = true,
            isVictorine = false,
            isOpen = false,
            isFav = true,
            termHourse = 12,
            termDateApi = "2023-07-27", possibleToOpenThemeFree =true
        )

        themeDao.insertTheme(theme1)
        themeDao.insertTheme(theme2)

        val resultTheme = themeDao.searchThemeWithInteractiveId(interactiveTestId)

        assertThat(resultTheme).isEqualTo(theme1)
    }
    @Test
    fun testSearchThemeWithVictorineTestId() = runTest {
        val victorineTestId = 601

        val theme1 = ThemeEntity(
            uniqueThemeId = 101,
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
            interactiveTestId = 501,
            victorineQuestionCount = 3,
            interactiveQuestionCount = 7,
            vicotineTestId = victorineTestId,
            duoDate = Date(),
            isDuoInter = false,
            isVictorine = true,
            isOpen = true,
            isFav = false,
            termHourse = 15,
            termDateApi = "2023-07-26", possibleToOpenThemeFree =true
        )

        val theme2 = ThemeEntity(
            uniqueThemeId = 102,
            lastUpdateDate = Date(),
            themeName = "Theme 2",
            courseNumber = 201,
            lessonsCount = 8,
            themeNumber = 2,
            imageTheme = null,
            lastCourseTheme = false,
            interactiveCodeMistakes = 2,
            interactiveCodeCorrect = 8,
            victorineMistakeAnswer = 3,
            victorineCorrectAnswer = 7,
            victorineDate = Date(),
            interactiveTestId = 502,
            victorineQuestionCount = 4,
            interactiveQuestionCount = 6,
            vicotineTestId = 602,
            duoDate = Date(),
            isDuoInter = true,
            isVictorine = false,
            isOpen = false,
            isFav = true,
            termHourse = 12,
            termDateApi = "2023-07-27", possibleToOpenThemeFree =true
        )

        themeDao.insertTheme(theme1)
        themeDao.insertTheme(theme2)

        val resultTheme = themeDao.searchThemeWithVictorineTestId(victorineTestId)

        assertThat(resultTheme).isEqualTo(theme1)
    }
}