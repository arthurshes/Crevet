package workwork.example.andropediagits.domain.useCases.userLogic

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.core.utils.CustomTimerUtil
import com.example.andropediagits.data.local.MainDb
import com.example.andropediagits.data.local.dao.theme.ThemeDao
import com.example.andropediagits.data.local.dao.victorine.VictorineDao
import com.example.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineClueEntity
import com.example.andropediagits.data.local.entities.victorine.VictorineEntity
import com.example.andropediagits.data.remote.model.SendSubscribeCheckModel
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.repo.TransactionRepo
import com.example.andropediagits.domain.repo.UserLogicRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import com.example.andropediagits.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.*
import java.util.Date
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
@HiltAndroidTest
class VictorineUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_dbversion")
    lateinit var database: MainDb

    private lateinit var victorineDao: VictorineDao

    @Inject
    @Named("test_courseRepo")
    lateinit var courseRepo: CourseRepo

    @Inject
    @Named("test_transactionRepo")
    lateinit var transactionRepo: TransactionRepo

    @Inject
    @Named("test_userLogicRepo")
    lateinit var userLogicRepo: UserLogicRepo

    lateinit var victorineUseCase: VictorineUseCase

    @Inject
    @Named("test_updateThemeUseCase")
    lateinit var updateThemeUseCase: UpdateThemeUseCase

    @Inject
    @Named("test_customTimerUtil")
    lateinit var CustomTimerUtil: CustomTimerUtil

    @Before
    fun setup() {
        hiltRule.inject()
        victorineDao = database.getVictorineDao()
        victorineUseCase =
            VictorineUseCase(
                updateThemeUseCase,
                courseRepo,
                transactionRepo,
                userLogicRepo,
                CustomTimerUtil
            )
    }

    @After
    fun teardown() {
        database.close()
    }


    @Test
    fun testGetVictorineQuestionsWithUniqueThemeId() = runTest {
        val uniqueThemeIdToTest = 123

        val test1 = VictorineEntity(1, 1, uniqueThemeIdToTest, "hello", Date(), 3)
        val test2 = VictorineEntity(2, 3, uniqueThemeIdToTest, "hello", Date(), 3)
        val test3 = VictorineEntity(3, 3, 334, "hello", Date(), 3)

        courseRepo.insertVictorine(test1)
        courseRepo.insertVictorine(test2)
        val resultLiveData =
            victorineUseCase.getVictorineQuestionsWithuniqueThemeId(uniqueThemeIdToTest)

        assertThat(resultLiveData).contains(test1)
        assertThat(resultLiveData).doesNotContain(test3)

    }

    @Test
    fun testGetVictoineAnswerVariantsWithQuestionId() = runTest {
        val idTest = 1

        val test1 = VictorineAnswerVariantEntity(1, 1, 34, "hello", idTest, true)
        val test2 = VictorineAnswerVariantEntity(2, 1, 32, "hello", 32, true)

        courseRepo.insertVictorineAnswerVariant(test1)
        courseRepo.insertVictorineAnswerVariant(test2)
        courseRepo.searchVictorineAnswerVariantsWithQuestionId(idTest)
        val resultLiveData =
            victorineUseCase.getVictoineAnswerVariantsWithQuestionId(idTest)

        assertThat(resultLiveData).containsExactly(test1)
        assertThat(resultLiveData).isNotEqualTo(test2)
    }

    @Test
    fun testGetVictorineQuestWithQuestionId() = runTest {
        val idTest = 1

        val test1 = VictorineEntity(idTest, 1, 32, "hello", Date(), 4)
        val test2 = VictorineEntity(idTest, 2, 34, "hello", Date(), 3)
        courseRepo.insertVictorine(test1)
        courseRepo.insertVictorine(test2)
        courseRepo.searchVictorineForQuestionId(idTest)
        val resultLiveData =
            victorineUseCase.getVictorineQuestWithQuestionId(idTest)

        assertThat(resultLiveData).isEqualTo(test1)
        assertThat(resultLiveData).isNotEqualTo(test2)
    }

    @Test
    fun testUpdateVictorineData() {
        runBlocking {
            val answer = VictorineAnswerVariantEntity(14, 1, 34, "Clue text goes here", 14, false)
            val answer1 = VictorineClueEntity(14, 1, 14, "Clue text goes here", 14, 34)
            val answer2 = VictorineEntity(14, 1, 34, "Clue text goes here", Date(), 7)
            courseRepo.insertVictorine(answer2)
            courseRepo.insertVictorineAnswerVariant(answer)
            courseRepo.insertVictorineClue(answer1)

            var isSuccessResult: ErrorEnum? = null
            var isClueResult: String? = null
            var sendSubscribeCheckModel =
                SendSubscribeCheckModel(currentDate = "hello", token = "name")
            val subscribeCheck = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
            if (subscribeCheck.subscribeIsActual) {
                victorineUseCase.updateVictorineData(
                    answer,
                    { isSuccessResult = it },
                    { isClueResult = it })
                assertEquals(ErrorEnum.SUCCESS, isSuccessResult)
                assertThat("Clue text goes here").isEqualTo(isClueResult)
            }
        }
    }
}
