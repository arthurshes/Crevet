package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.entities.course.CourseBuyEntity
import com.example.andropediagits.data.local.entities.theme.ThemeBuyEntity
import com.example.andropediagits.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TransactionsDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var transactionsDao: TransactionsDao

    @Before
    fun setup() {
        hiltRule.inject()

        transactionsDao = database.getTransactionDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSearchCourseBuyForNumber() = runTest {
        val courseNumber = 301
        val courseBuyEntity = CourseBuyEntity(
           id = 1,
            courseNumber=courseNumber,
            date = Date(),
            token = "hello",
            transactionId = "hello",
            andropointBuy = true
        )

        transactionsDao.insertBuyCourse(courseBuyEntity)

        val resultCourseBuy = transactionsDao.searchCourseBuyForNumber(courseNumber)

        assertThat(resultCourseBuy).isEqualTo(courseBuyEntity)
    }
    @Test
    fun testSearchBuyTheme() = runTest {
        val uniqueThemeId = 401

        val themeBuyEntity = ThemeBuyEntity(
            id = 1,
            courseNumber = 9,
            themeNumber = 3,
            uniqueThemeId = uniqueThemeId,
            date = Date(),
            token = "hello",
            transactionId = "hello",
        )

        transactionsDao.insertBuyTheme(themeBuyEntity)

        val resultThemeBuy = transactionsDao.searchBuyTheme(uniqueThemeId)

        assertThat(resultThemeBuy).isEqualTo(themeBuyEntity)
    }
}