package workwork.example.andropediagits.data.local.dao

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.entities.UserInfoEntity
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
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class UserInfoDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var userInfoDao: UserInfoDao

    @Before
    fun setup() {
    hiltRule.inject()

        userInfoDao = database.getUserInfoDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertUserInfo() = runTest {
        val userInfoEntity = UserInfoEntity(
            name = "John Doe",
            image = null,
            userLanguage = "en",
            token = "example_token",
            andropointCount = 100,
            lastOnlineDate = "2023-07-26",
            strikeModeDay = 5,
            lastOpenCourse = 101,
            lastOpenTheme = 201
        )

        userInfoDao.insertUserInfo(userInfoEntity)

        val resultUserInfo = userInfoDao.getUserInfo()

        assertThat(resultUserInfo).isEqualTo(userInfoEntity)
    }

    @Test
    fun testUpdateUserInfo() = runTest {
        val userInfoEntity = UserInfoEntity(
            name = "John Doe",
            image = null,
            userLanguage = "en",
            token = "example_token",
            andropointCount = 100,
            lastOnlineDate = "2023-07-26",
            strikeModeDay = 5,
            lastOpenCourse = 101,
            lastOpenTheme = 201
        )

        userInfoDao.insertUserInfo(userInfoEntity)

        val updatedUserInfo = userInfoEntity.copy(
            name = "Jane Doe",
            andropointCount = 200
        )

        userInfoDao.updateUserInfo(updatedUserInfo)

        val resultUserInfo = userInfoDao.getUserInfo()

        assertThat(resultUserInfo).isEqualTo(updatedUserInfo)
    }
    @Test
    fun testGetUserInfo() = runTest {
        val userInfoEntity = UserInfoEntity(
            name = "John Doe",
            image = null,
            userLanguage = "en",
            token = "example_token",
            andropointCount = 100,
            lastOnlineDate = "2023-07-26",
            strikeModeDay = 5,
            lastOpenCourse = 101,
            lastOpenTheme = 201
        )

        userInfoDao.insertUserInfo(userInfoEntity)

        val resultUserInfo = userInfoDao.getUserInfo()

        assertThat(resultUserInfo).isEqualTo(userInfoEntity)
    }

    @Test
    fun testDeleteUserInfo() = runTest {
        val userInfoEntity = UserInfoEntity(
            name = "John Doe",
            image = null,
            userLanguage = "en",
            token = "example_token",
            andropointCount = 100,
            lastOnlineDate = "2023-07-26",
            strikeModeDay = 5,
            lastOpenCourse = 101,
            lastOpenTheme = 201
        )

        userInfoDao.insertUserInfo(userInfoEntity)

        val resultUserInfoBeforeDelete = userInfoDao.getUserInfo()
        assertThat(resultUserInfoBeforeDelete).isEqualTo(userInfoEntity)

        userInfoDao.deleteUserInfo(userInfoEntity)

        val resultUserInfoAfterDelete = userInfoDao.getUserInfo()
        assertThat(resultUserInfoAfterDelete).isNull()
    }

}