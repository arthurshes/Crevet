package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.updateKey.UpdateKeysDao
import com.example.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import com.example.andropediagits.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
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
class UpdateKeysDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var updateKeysDao: UpdateKeysDao

    @Before
    fun setup() {
        hiltRule.inject()
        updateKeysDao = database.getUpdateKeysDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testGetUpdateKeyWithCourseNumber() = runTest {
        val courseNumber = 301

        val updatesKeyEntity = UpdatesKeyEntity(
            updateKeyId = 2,
            courseNumber = courseNumber,
            uniqueThemeId = 401,
            uniqueLevelId = 501,
            interactiveTestId = 601,
            buyCourseNumber = 383,
            buyThemeUniqueId = 801,
            vicotineTestId = 901,
            updateTime = Date(),
            openCourseNumber = 1001,
            openUniqueThemeId = 1101
        )

        updateKeysDao.insertKey(updatesKeyEntity)

        val resultUpdatesKey = updateKeysDao.getUpdateKeyWithCourseNumber(courseNumber)

        assertThat(resultUpdatesKey).isEqualTo(updatesKeyEntity)
    }
    @Test
    fun testGetUpdateKeyWithUniqueThemeId() = runTest {
        val uniqueThemeId = 401

        val updatesKeyEntity = UpdatesKeyEntity(
            updateKeyId = 2,
            courseNumber = 23,
            uniqueThemeId = uniqueThemeId,
            uniqueLevelId = 501,
            interactiveTestId = 601,
            buyCourseNumber = 383,
            buyThemeUniqueId = 801,
            vicotineTestId = 901,
            updateTime = Date(),
            openCourseNumber = 1001,
            openUniqueThemeId = 1101
        )

        updateKeysDao.insertKey(updatesKeyEntity)

        val resultUpdatesKey = updateKeysDao.getUpdateKeyWithUniqueThemeId(uniqueThemeId)

        assertThat(resultUpdatesKey).isEqualTo(updatesKeyEntity)
    }
    @Test
    fun testGetUpdateKeyWithUniqueLevelId() = runTest {
        val uniqueLevelId = 501

        val updatesKeyEntity = UpdatesKeyEntity(
            updateKeyId = 1,
            courseNumber = 301,
            uniqueThemeId = 401,
            uniqueLevelId = uniqueLevelId,
            interactiveTestId = 601,
            buyCourseNumber = 383,
            buyThemeUniqueId = 801,
            vicotineTestId = 901,
            updateTime = Date(),
            openCourseNumber = 1001,
            openUniqueThemeId = 1101
        )

        updateKeysDao.insertKey(updatesKeyEntity)

        val resultUpdatesKey = updateKeysDao.getUpdateKeyWithUniqueLevelId(uniqueLevelId)

        assertThat(resultUpdatesKey).isEqualTo(updatesKeyEntity)
    }

    @Test
    fun testGetUpdateKeyWithInteractiveId() = runTest {
        val interactiveTestId = 601

        val updatesKeyEntity1 = UpdatesKeyEntity(
            updateKeyId = 1,
            courseNumber = 301,
            uniqueThemeId = 401,
            uniqueLevelId = 501,
            interactiveTestId = interactiveTestId,
            buyCourseNumber = 383,
            buyThemeUniqueId = 801,
            vicotineTestId = 901,
            updateTime = Date(),
            openCourseNumber = 1001,
            openUniqueThemeId = 1101
        )

        val updatesKeyEntity2 = UpdatesKeyEntity(
            updateKeyId = 2,
            courseNumber = 302,
            uniqueThemeId = 402,
            uniqueLevelId = 502,
            interactiveTestId = interactiveTestId,
            buyCourseNumber = 384,
            buyThemeUniqueId = 802,
            vicotineTestId = 902,
            updateTime = Date(),
            openCourseNumber = 1002,
            openUniqueThemeId = 1102
        )

        updateKeysDao.insertKey(updatesKeyEntity1)
        updateKeysDao.insertKey(updatesKeyEntity2)

        val resultUpdatesKeys = updateKeysDao.getUpdateKeyWithInteractiveId(interactiveTestId)

        assertThat(resultUpdatesKeys).containsExactly(updatesKeyEntity1, updatesKeyEntity2)
    }
    @Test
    fun testGetUpdateKeyWithVictorineId() = runTest {
        val victorineTestId = 901

        val updatesKeyEntity1 = UpdatesKeyEntity(
            updateKeyId = 2,
            courseNumber = 301,
            uniqueThemeId = 401,
            uniqueLevelId = 501,
            interactiveTestId = 601,
            buyCourseNumber = 383,
            buyThemeUniqueId = 801,
            vicotineTestId = victorineTestId,
            updateTime = Date(),
            openCourseNumber = 1001,
            openUniqueThemeId = 1101
        )

        val updatesKeyEntity2 = UpdatesKeyEntity(
            updateKeyId = 1,
            courseNumber = 302,
            uniqueThemeId = 402,
            uniqueLevelId = 502,
            interactiveTestId = 602,
            buyCourseNumber = 384,
            buyThemeUniqueId = 802,
            vicotineTestId = victorineTestId,
            updateTime = Date(),
            openCourseNumber = 1002,
            openUniqueThemeId = 1102
        )

        updateKeysDao.insertKey(updatesKeyEntity1)
        updateKeysDao.insertKey(updatesKeyEntity2)

        val resultUpdatesKeys = updateKeysDao.getUpdateKeyWithVictorineId(victorineTestId)

        assertThat(resultUpdatesKeys).containsExactly(updatesKeyEntity1, updatesKeyEntity2)
    }
}