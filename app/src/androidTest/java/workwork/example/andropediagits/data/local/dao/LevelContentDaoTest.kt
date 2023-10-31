package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.level.LevelContentDao
import com.example.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
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
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class LevelContentDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var levelContentDao: LevelContentDao

    @Before
    fun setup() {
        hiltRule.inject()
        levelContentDao = database.getLevelContentDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testSearchLevelContents() = runTest {
        val uniqueLevelId = 1
        val themeLevelContentEntity1 = ThemeLevelContentEntity(
            uniqueLevelContentId = 1,
            courseNumber = 101,
            themeNumber = 201,
            levelNumber = 3,
            uniqueLevelId = uniqueLevelId,
            uniqueThemeId = 401,
            lastUpdateDate = Date(),
            contentIndexList = 1,
            imageFirst = null,
            textTitle = "Title",
            textFirst = "First Text",
            CodeFragmentFirst = null,
            imageSecond = null,
            textSecond = "Second Text",
            CodeFragmentSecond = "Code2",
            imageThird = null,
            textThird = "Third Text",
            CodeFragmentThird = null,
            imageFourth = null,
            textFourth = "Fourth Text",
            CodeFragmentFourth = "Code4",
            imageFifth = null,
            textFifth = "Fifth Text",
            CodeFragmentFifth = null,
            imageSixth = null,
            textSixth = "Sixth Text",
            CodeFragmentSixth = "Code6",
            imageSeventh = null,
            textSeventh = "Seventh Text",
            CodeFragmentSeventh = null,
            imageEighth = null,
            textEighth = "Eighth Text",
            CodeFragmentEighth = "Code8",
            imageNinth = null,
            textNinth = "Ninth Text",
            CodeFragmentNinth = null,
            imageTenth = null,
            textTenth = "Tenth Text",
            CodeFragmentTenth = "Code10"
        )
        val themeLevelContentEntity2 = ThemeLevelContentEntity(uniqueLevelContentId = 2, courseNumber = 101, themeNumber = 201, levelNumber = 3, uniqueLevelId = uniqueLevelId, uniqueThemeId = 401, lastUpdateDate = Date(), contentIndexList = 1, imageFirst = null, textTitle = "Title", textFirst = "First Text", CodeFragmentFirst = null, imageSecond = null, textSecond = "Second Text", CodeFragmentSecond = "Code2", imageThird =null, textThird = "Third Text", CodeFragmentThird = null, imageFourth = null, textFourth = "Fourth Text", CodeFragmentFourth = "Code4", imageFifth = null, textFifth = "Fifth Text", CodeFragmentFifth = null, imageSixth = null, textSixth = "Sixth Text", CodeFragmentSixth = "Code6", imageSeventh =null, textSeventh = "Seventh Text", CodeFragmentSeventh = null, imageEighth = null, textEighth = "Eighth Text", CodeFragmentEighth = "Code8", imageNinth = null, textNinth = "Ninth Text", CodeFragmentNinth = null, imageTenth = null, textTenth = "Tenth Text", CodeFragmentTenth = "Code10")

        levelContentDao.insertLevelsContent(themeLevelContentEntity1)
        levelContentDao.insertLevelsContent(themeLevelContentEntity2)

        val resultLevelContents = levelContentDao.searchLevelContents(uniqueLevelId)

        assertThat(resultLevelContents).isEqualTo(themeLevelContentEntity1)
        assertThat(resultLevelContents).isEqualTo(themeLevelContentEntity2)
    }

    @Test
    fun testSearchOneLevelContent() = runTest {
        val testId = 123
        val themeLevelContentEntity = ThemeLevelContentEntity(
            uniqueLevelContentId = testId,
            courseNumber = 101,
            themeNumber = 201,
            levelNumber = 3,
            uniqueLevelId = 3,
            uniqueThemeId = 401,
            lastUpdateDate = Date(),
            contentIndexList = 1,
            imageFirst =null,
            textTitle = "Title",
            textFirst = "First Text",
            CodeFragmentFirst = null,
            imageSecond = null,
            textSecond = "Second Text",
            CodeFragmentSecond = "Code2",
            imageThird = null,
            textThird = "Third Text",
            CodeFragmentThird = null,
            imageFourth =null,
            textFourth = "Fourth Text",
            CodeFragmentFourth = "Code4",
            imageFifth = null,
            textFifth = "Fifth Text",
            CodeFragmentFifth = null,
            imageSixth = null,
            textSixth = "Sixth Text",
            CodeFragmentSixth = "Code6",
            imageSeventh = null,
            textSeventh = "Seventh Text",
            CodeFragmentSeventh = null,
            imageEighth =null,
            textEighth = "Eighth Text",
            CodeFragmentEighth = "Code8",
            imageNinth = null,
            textNinth = "Ninth Text",
            CodeFragmentNinth = null,
            imageTenth = null,
            textTenth = "Tenth Text",
            CodeFragmentTenth = "Code10"
        )

        levelContentDao.insertLevelsContent(themeLevelContentEntity)

        val resultThemeLevelContent =
            levelContentDao.searchOneLevelContent(testId)

        assertThat(resultThemeLevelContent).isEqualTo(themeLevelContentEntity)
    }
}