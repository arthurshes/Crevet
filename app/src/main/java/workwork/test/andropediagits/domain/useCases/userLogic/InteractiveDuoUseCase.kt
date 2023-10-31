package workwork.test.andropediagits.domain.useCases.userLogic


import okio.IOException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCodeVariantEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveCorrectCodeEntity
import workwork.test.andropediagits.data.local.entities.interactive.InteractiveEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateThemeState
import java.util.Date
import javax.inject.Inject

class InteractiveDuoUseCase @Inject constructor(private val courseRepo: CourseRepo, private val updateThemeUseCase: UpdateThemeUseCase) {
    //with test
    suspend fun getInteractiveTaksWithUniqueThemeId(uniqueThemeId:Int): List<InteractiveEntity> {
        return courseRepo.searchInteractiveTaskWithUniqueThemeId(uniqueThemeId)
    }
    //with test
    suspend fun getInteractiveCodeVariantsWithTaskId(taskId:Int): List<InteractiveCodeVariantEntity> {
        return courseRepo.searchInteractiveCodeVariantsWithTaskId(taskId)
    }
    //with test
    suspend fun getInteractiveCorrectCodeWithTaskId(taskId: Int): InteractiveCorrectCodeEntity {
        return courseRepo.searchInteractiveTaskCorrectAnswerWithTaskId(taskId)
    }
    //with test
    suspend fun getAllInteractiveCorrectCodesWithUniqueThemeId(uniqueThemeId: Int): List<InteractiveCorrectCodeEntity> {
        return courseRepo.searchInteractiveTaskCorrectAnswersWithUniqueThemeId(uniqueThemeId)
    }

    suspend fun checkCodeAnswer(answerCode:String,taskId:Int,isCorrect:((Boolean)->Unit),isSuccess:((ErrorEnum)->Unit)){
        try {
            val currentCorrectAnswer =
                courseRepo.searchInteractiveTaskCorrectAnswerWithTaskId(taskId)
            currentCorrectAnswer?.let { correctAnswer ->
                if (answerCode == correctAnswer.correctAnswer) {
                    isCorrect.invoke(true)
                    updateThemeUseCase.updateTheme(correctAnswer.uniqueThemeId, UpdateThemeState.ISDUOCORRECT)
                } else {
                    isCorrect.invoke(false)
                    updateThemeUseCase.updateTheme(correctAnswer.uniqueThemeId,UpdateThemeState.ISDUOMISTAKE)
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

}