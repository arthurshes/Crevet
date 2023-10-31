package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.level.LevelDao
import com.example.andropediagits.data.local.entities.levels.LevelEntity
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
class LevelDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var levelDao: LevelDao

    @Before
    fun setup() {
        hiltRule.inject()

        levelDao = database.getRealLevelDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSearchOneLevel()= runTest {
        val testId=1
        val levelEntity1 = LevelEntity(
            uniqueLevelId = testId,
            uniqueThemeId = 101,
            courseNumber = 201,
            themeNumber = 3,
            levelNumber = 1,
            levelName = "This is Level 1",
            lastUpdateDate = Date()
        )

        levelDao.insertLevels(levelEntity1)

        val resultLevels = levelDao.searchOneLevel(testId)

        // Check if the retrieved LevelEntity matches the inserted entity.
        assertThat(resultLevels).isEqualTo(levelEntity1)
    }
    @Test
    fun testSearchThemeLevels() = runTest {
        val uniqueThemeId = 101

        val levelEntity1 = LevelEntity(
            uniqueLevelId = 1,
            uniqueThemeId = uniqueThemeId,
            courseNumber = 201,
            themeNumber = 3,
            levelNumber = 1,
            levelName = "Level 1",
            lastUpdateDate = Date()
        )

        val levelEntity2 = LevelEntity(
            uniqueLevelId = 2,
            uniqueThemeId = uniqueThemeId,
            courseNumber = 201,
            themeNumber = 3,
            levelNumber = 2,
            levelName = "Level 2",
            lastUpdateDate = Date(),
        )

        // Insert the LevelEntity objects into the database.
        levelDao.insertLevels(levelEntity1)
        levelDao.insertLevels(levelEntity2)

        // Retrieve the list of LevelEntity objects from the database by uniqueThemeId.
        val resultLevels = levelDao.searchThemeLevels(uniqueThemeId)

        // Check if the retrieved list contains both inserted LevelEntity objects with the specified uniqueThemeId.
        assertThat(resultLevels).containsExactly(levelEntity1, levelEntity2)
    }

    @Test
    fun testDeleteAllLevelTheme() = runTest {
        val uniqueThemeId = 101

        val levelEntity1 = LevelEntity(
            uniqueLevelId = 1,
            uniqueThemeId = uniqueThemeId,
            courseNumber = 201,
            themeNumber = 3,
            levelNumber = 1,
            levelName = "Level 1",
            lastUpdateDate = Date()
        )

        val levelEntity2 = LevelEntity(
            uniqueLevelId = 2,
            uniqueThemeId = uniqueThemeId,
            courseNumber = 201,
            themeNumber = 3,
            levelNumber = 2,
            levelName = "Level 2",
            lastUpdateDate = Date()
        )

        // Insert the LevelEntity objects into the database.
        levelDao.insertLevels(levelEntity1)
        levelDao.insertLevels(levelEntity2)

        // Delete all LevelEntity objects with the specified uniqueThemeId.
        levelDao.deletAllLevelTheme(uniqueThemeId)

        // Retrieve the list of LevelEntity objects from the database by uniqueThemeId.
        val resultLevels = levelDao.searchThemeLevels(uniqueThemeId)

        // Check if the retrieved list is empty after deletion.
        assertThat(resultLevels).isEmpty()
    }

    @Test
    fun testDeleteAllLevelCourse() = runTest {
        val courseNumber = 201

        val levelEntity1 = LevelEntity(
            uniqueLevelId = 1,
            uniqueThemeId = 101,
            courseNumber = courseNumber,
            themeNumber = 3,
            levelNumber = 1,
            levelName = "Level 1",
            lastUpdateDate = Date()
        )

        val levelEntity2 = LevelEntity(
            uniqueLevelId = 2,
            uniqueThemeId = 102,
            courseNumber = courseNumber,
            themeNumber = 3,
            levelNumber = 2,
            levelName = "Level 2",
            lastUpdateDate = Date()
        )

        // Insert the LevelEntity objects into the database.
        levelDao.insertLevels(levelEntity1)
        levelDao.insertLevels(levelEntity2)

        // Delete all LevelEntity objects with the specified courseNumber.
        levelDao.deleteAllLevelCourse(courseNumber)

        // Retrieve the list of LevelEntity objects from the database by courseNumber.
        val resultLevels = levelDao.searchThemeLevels(courseNumber)

        // Check if the retrieved list is empty after deletion.
        assertThat(resultLevels).isEmpty()
    }
}