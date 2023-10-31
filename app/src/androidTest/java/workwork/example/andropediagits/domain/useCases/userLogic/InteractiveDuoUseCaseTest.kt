package workwork.example.andropediagits.domain.useCases.userLogic

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.example.andropediagits.core.exception.ErrorEnum
import com.example.andropediagits.core.exception.SplashActionEnum
import com.example.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import com.example.andropediagits.data.local.entities.interactive.InteractiveEntity
import com.example.andropediagits.domain.repo.CourseRepo
import com.example.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import com.example.andropediagits.getOrAwaitValue
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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
class InteractiveDuoUseCaseTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_courseRepo")
    lateinit var courseRepo: CourseRepo

    lateinit var interactiveDuoUseCase: InteractiveDuoUseCase

    @Inject
    @Named("test_updateThemeUseCase")
    lateinit var updateThemeUseCase: UpdateThemeUseCase

    @Before
    fun setup() {
        hiltRule.inject()
        interactiveDuoUseCase = InteractiveDuoUseCase(courseRepo, updateThemeUseCase)
    }

    @Test
    fun testGetInteractiveTaksWithUniqueThemeId() = runTest {
        val uniqueThemeIdToTest = 1

        val test1 = InteractiveEntity(1, 1, uniqueThemeIdToTest, "hello", Date())
        val test2 = InteractiveEntity(2, 1, uniqueThemeIdToTest, "hello", Date())

        courseRepo.insertInteractiveEntity(test1)
        courseRepo.insertInteractiveEntity(test2)
        courseRepo.searchInteractiveTaskWithUniqueThemeId(uniqueThemeIdToTest)
        val resultLiveData = interactiveDuoUseCase.getInteractiveTaksWithUniqueThemeId(uniqueThemeIdToTest)

        Truth.assertThat(resultLiveData).containsExactly(test1, test2)
    }
    @Test
    fun testGetInteractiveCodeVariantsWithTaskId() = runTest {
        val idTest = 1

        val test1 = InteractiveCodeVariantEntity(3, 1, idTest, "hello")
        val test2 = InteractiveCodeVariantEntity(4, 1, idTest, "hello")

        courseRepo.insertInteractiveCodeVariant(test1)
        courseRepo.insertInteractiveCodeVariant(test2)
        courseRepo.searchInteractiveTaskCorrectAnswerWithTaskId(idTest)
        val resultLiveData = interactiveDuoUseCase.getInteractiveCodeVariantsWithTaskId(idTest)

        Truth.assertThat(resultLiveData).containsExactly(test1, test2)
    }
    @Test
    fun testGetInteractiveCorrectCodeWithTaskId() = runTest {

        val idTest = 1

        val test1 = InteractiveCorrectCodeEntity(idTest, "", 323, 322)
        val test2 = InteractiveCorrectCodeEntity(4, "", 55, 32)

        courseRepo.insertInteractiveCodeCorrectAnswer(test1)
        courseRepo.insertInteractiveCodeCorrectAnswer(test2)
        courseRepo.searchInteractiveTaskCorrectAnswerWithTaskId(idTest)
        val resultLiveData = interactiveDuoUseCase.getInteractiveCorrectCodeWithTaskId(idTest)

        Truth.assertThat(resultLiveData).isEqualTo(test1)
        Truth.assertThat(resultLiveData).isNotEqualTo(test2)
    }
    @Test
    fun testAllInteractiveCorrectCodesWithUniqueThemeId() = runTest {

        val idTest = 1

        val test1 = InteractiveCorrectCodeEntity(6, "", 323, idTest)
        val test2 = InteractiveCorrectCodeEntity(4, "", 55, idTest)

        courseRepo.insertInteractiveCodeCorrectAnswer(test1)
        courseRepo.insertInteractiveCodeCorrectAnswer(test2)
        courseRepo.searchInteractiveTaskCorrectAnswersWithUniqueThemeId(idTest)
        val resultLiveData = interactiveDuoUseCase.getAllInteractiveCorrectCodesWithUniqueThemeId(idTest)

        Truth.assertThat(resultLiveData).containsExactly(test1, test2)
    }

    @Test
    fun checkCodeAnswerTestCorrectAnswer() = runTest {
        // Arrange
        val taskId = 123
        val answerCode = "correct_answer_code"
        var isCorrectResult: Boolean? = null
        // Установите courseRepoStub так, чтобы searchInteractiveTaskCorrectAnswerWithTaskId возвращал правильный ответ
        courseRepo.insertInteractiveCodeCorrectAnswer(InteractiveCorrectCodeEntity(taskId=taskId,correctAnswer= answerCode,interactiveTestId=2,uniqueThemeId=3))

        // Act
        interactiveDuoUseCase.checkCodeAnswer(answerCode, taskId, { isCorrect -> isCorrectResult = isCorrect }) {
            // Не интересует данный тест, можно оставить пустым.
        }

        // Assert
        assertEquals(true, isCorrectResult)
    }
}