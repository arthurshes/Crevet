package workwork.example.andropediagits.data.local.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.interactive.InteractiveDao
import com.example.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveEntity
import com.example.andropediagits.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
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
@HiltAndroidTest
@SmallTest
class InteractiveDaoTest {

    // This rule is used to make sure that Room executes all tasks immediately on the same thread.
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_dbversion")
     lateinit var database: MainDb
    private lateinit var interactiveDao: InteractiveDao

    @Before
    fun setup() {
         hiltRule.inject()
        interactiveDao = database.getInteractiveDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun testSearchInteractiveTaskWithTaskId() = runTest {

        val interactiveEntity = InteractiveEntity(taskId = 1, interactiveTestId = 1,uniqueThemeId = 1, taskDetailsText = "dsds", lastUpdateDate = Date())

        interactiveDao.insertInteractiveEntity(interactiveEntity)

        val resultInteractiveTask = interactiveDao.searchInteractiveTaskWithTaskId(1)

        assertThat(resultInteractiveTask).isEqualTo(interactiveEntity)
    }
    @Test
    fun testSearchInteractiveTasksWithInteractiveTestId() = runTest {
        val interactiveEntity1 = InteractiveEntity(taskId = 3, interactiveTestId = 2,uniqueThemeId = 3, taskDetailsText = "dsdwrrs", lastUpdateDate = Date())
        val interactiveEntity2 = InteractiveEntity(taskId = 2, interactiveTestId = 3,uniqueThemeId = 2, taskDetailsText = "dsds", lastUpdateDate = Date())

        interactiveDao.insertInteractiveEntity(interactiveEntity1)
        interactiveDao.insertInteractiveEntity(interactiveEntity2)


        val resultInteractiveTasks = interactiveDao.searchInteractiveTasksWithInteractiveTestId(2)


        assertThat(resultInteractiveTasks).containsExactly(interactiveEntity1)
    }
    @Test
    fun testSearchInteractiveTaskWithUniqueThemeId() = runTest {
        val interactiveEntity1 = InteractiveEntity(taskId = 3, interactiveTestId = 2,uniqueThemeId = 3, taskDetailsText = "dsdwrrs", lastUpdateDate = Date())
        val interactiveEntity2 = InteractiveEntity(taskId = 2, interactiveTestId = 3,uniqueThemeId = 3, taskDetailsText = "dsds", lastUpdateDate = Date())

        interactiveDao.insertInteractiveEntity(interactiveEntity1)
        interactiveDao.insertInteractiveEntity(interactiveEntity2)

        val resultInteractiveTasks = interactiveDao.searchInteractiveTaskWithUniqueThemeId(3)

        assertThat(resultInteractiveTasks).containsExactly(interactiveEntity1, interactiveEntity2)
    }
    @Test
    fun testSearchInteractiveCodeVariantWithVariantId() = runTest {
        val testId = 1
        val interactiveCodeVariantEntity = InteractiveCodeVariantEntity(variantId = 1, interactiveTestId = testId,taskId = 1, text = "else")

        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity)

        val resultInteractiveCodeVariant = interactiveDao.searchInteractiveCodeVariantWithVariantId(testId)

        assertThat(resultInteractiveCodeVariant).isEqualTo(interactiveCodeVariantEntity)
    }
    @Test
    fun testSearchInteractiveCodeVariantsWithTaskId() = runTest {
        val testId = 1
        val interactiveCodeVariantEntity1 = InteractiveCodeVariantEntity(variantId = 1, interactiveTestId = testId, taskId = 1, text = "else")
        val interactiveCodeVariantEntity2 = InteractiveCodeVariantEntity(variantId = 2, interactiveTestId = testId, taskId = 1, text = "else") // Changed variantId to 2

        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity1)
        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity2)


        val resultInteractiveCodeVariants = interactiveDao.searchInteractiveCodeVariantsWithTaskId(testId)

        assertThat(resultInteractiveCodeVariants).containsExactly(interactiveCodeVariantEntity1, interactiveCodeVariantEntity2)
    }

    @Test
    fun testSearchInteractiveCodeVariantsWithInteractiveTestId() = runTest {
        val interactiveTestId = 1
        val interactiveCodeVariantEntity1 = InteractiveCodeVariantEntity(variantId = 1, interactiveTestId = interactiveTestId, taskId = 1, text = "else")
        val interactiveCodeVariantEntity2 = InteractiveCodeVariantEntity(variantId = 2, interactiveTestId = interactiveTestId, taskId = 2, text = "if")

        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity1)
        interactiveDao.insertInteractiveCodeVariant(interactiveCodeVariantEntity2)

        val resultInteractiveCodeVariants = interactiveDao.searchInteractiveCodeVariantsWithInteractiveTestId(interactiveTestId)

        assertThat(resultInteractiveCodeVariants).containsExactly(interactiveCodeVariantEntity1, interactiveCodeVariantEntity2)
    }
    @Test
    fun testSearchInteractiveTaskCorrectAnswerWithTaskId() = runTest {
        val taskId = 1

        val interactiveCorrectCodeEntity = InteractiveCorrectCodeEntity(taskId = taskId, correctAnswer = "hello", interactiveTestId = 1,uniqueThemeId =1)

        interactiveDao.insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity)

        val resultInteractiveCorrectCode = interactiveDao.searchInteractiveTaskCorrectAnswerWithTaskId(taskId)

        assertThat(resultInteractiveCorrectCode).isEqualTo(interactiveCorrectCodeEntity)
    }
    @Test
    fun testSearchInteractiveTaskCorrectAnswersWithUniqueThemeId() = runTest {
        val uniqueThemeId = 1
        val interactiveCorrectCodeEntity1 = InteractiveCorrectCodeEntity(taskId = 1, correctAnswer = "hello", interactiveTestId = 1,uniqueThemeId =uniqueThemeId)
        val interactiveCorrectCodeEntity2 = InteractiveCorrectCodeEntity(taskId = 1, correctAnswer = "hello", interactiveTestId = 1,uniqueThemeId =uniqueThemeId)

        interactiveDao.insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity1)
        interactiveDao.insertInteractiveCodeCorrectAnswer(interactiveCorrectCodeEntity2)

        val resultInteractiveCorrectCodes = interactiveDao.searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId)

        assertThat(resultInteractiveCorrectCodes).containsExactly(interactiveCorrectCodeEntity1, interactiveCorrectCodeEntity2)
    }

}