package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.victorine.VictorineDao
import com.example.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineEntity
import com.example.andropediagits.getOrAwaitValue
import com.google.common.truth.Truth
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
class VictorineDaoTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb
    private lateinit var victorineDao: VictorineDao

    @Before
    fun setup() {
        hiltRule.inject()

        victorineDao = database.getVictorineDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testSearchVictorinesForVictorineTestId() = runTest {
        val idTest = 1
        val userInfoEntity = VictorineEntity(323, idTest, 32, "hello", Date(),22)
        victorineDao.insertVictorine(userInfoEntity)

        val resultUserInfo =
            victorineDao.searchVictorinesForVictorineTestId(idTest)

       assertThat(resultUserInfo).containsExactly(userInfoEntity)
    }

    @Test
    fun testSearchVictorineForQuestionId() = runTest {
        val idTest = 1
        val victorineEntity = VictorineEntity(idTest, 1, 32, "hello", Date(),22)
        victorineDao.insertVictorine(victorineEntity)

        val resultUserInfo = victorineDao.searchVictorineForQuestionId(idTest)

        assertThat(resultUserInfo).isEqualTo(victorineEntity)
    }

    @Test
    fun testSearchAllVictorinesWithUniqueThemeId() = runTest {
        val idTest = 1
        val victorineEntity = VictorineEntity(1, 1, idTest, "hello", Date(),22)
        victorineDao.insertVictorine(victorineEntity)

        val resultUserInfo =
            victorineDao.searchAllVictorinesWithUniqueThemeId(idTest)

        assertThat(resultUserInfo).containsExactly(victorineEntity)
    }

    @Test
    fun testSearchVictorineAnswerVariantWithVictorineAnswerId() = runTest {
        val idTest = 1
        val victorineAnswerVariantEntity =
            VictorineAnswerVariantEntity(idTest, 1, 34, "Clue text goes here", 14, false)

        victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity)

        val resultUserInfo =
            victorineDao.searchVictorineAnswerVariantWithVictorineAnswerId(idTest)

        assertThat(resultUserInfo).containsExactly(victorineAnswerVariantEntity)
    }

    @Test
    fun testSearchVictorineAnswerVariantsWithQuestionId() = runTest {
        val idTest = 1
        val victorineAnswerVariantEntity =
            VictorineAnswerVariantEntity(4, 1, 34, "Clue text goes here", idTest, false)

        victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity)

        val resultUserInfo = victorineDao.searchVictorineAnswerVariantsWithQuestionId(idTest)

        assertThat(resultUserInfo).containsExactly(victorineAnswerVariantEntity)
    }

    @Test
    fun testDeleteAllVictorineVariants() = runTest {
        val victorineAnswerVariantEntity1 = VictorineAnswerVariantEntity(1, 1, 34, "Variant 1", 3, true)
        val victorineAnswerVariantEntity2 = VictorineAnswerVariantEntity(2, 1, 34, "Variant 2", 3, false)
        val victorineAnswerVariantEntity3 = VictorineAnswerVariantEntity(3, 1, 34, "Variant 3", 3, true)
        val delete = victorineDao.getAllVictorines()
        victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity1)
        victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity2)
        victorineDao.insertVictorineAnswerVariant(victorineAnswerVariantEntity3)

        victorineDao.deleteAllVictorineVariants()

        val finalCount = victorineDao.getAllVictorines()
        assertThat(finalCount).isEqualTo(delete)
    }
}