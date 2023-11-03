package workwork.test.andropediagits.domain.useCases.userLogic

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.utils.Constatns
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.updateStates.UpdateThemeState
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.Exception

class VictorineUseCase @Inject constructor(private val updateThemeUseCase: UpdateThemeUseCase, private val courseRepo: CourseRepo, private val transactionRepo: TransactionRepo, private val userLogicRepo: UserLogicRepo){
    //with test

    private var pref: SharedPreferences?=null
    suspend fun getVictorineQuestionsWithuniqueThemeId(uniqueThemeId:Int): List<VictorineEntity> {
        return courseRepo.searchAllVictorinesWithUniqueThemeId(uniqueThemeId)
    }
    //with test
    suspend fun getVictoineAnswerVariantsWithQuestionId(questionId:Int):List<VictorineAnswerVariantEntity>{
        return courseRepo.searchVictorineAnswerVariantsWithQuestionId(questionId)
    }
    //with test
     suspend fun getVictorineQuestWithQuestionId(questionId: Int): VictorineEntity {
        return courseRepo.searchVictorineForQuestionId(questionId)
    }

    suspend fun resetOldVictorineData(uniqueThemeId: Int){
        updateThemeUseCase.updateTheme(uniqueThemeId, UpdateThemeState.RESETVICTORINEDATA)
    }

   suspend fun updateVictorineData(answer:VictorineAnswerVariantEntity, isSuccess:((ErrorEnum)->Unit), isClue:((String)->Unit)?=null){
       try {
           Log.d("victorineTestdfetd",answer.toString())
           if (answer.isCorrectAnswer) {
                   updateThemeUseCase.updateTheme(answer.uniqueThemeId,UpdateThemeState.ISVICTORINECORRECT)
           } else {
               //проверка подписки начало
               val currentTime = courseRepo.getCurrentTime()
               val userInfo = userLogicRepo.getUserInfoLocal()
               val sendSubscribeCheckModel = SendSubscribeCheckModel(
                   currentDate = currentTime.datetime,
                   token = userInfo?.token ?: ""
               )
               val subscribeCheck = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
               //проверка подписки конец
               if (subscribeCheck.subscribeIsActual) {
                       updateThemeUseCase.updateTheme(answer.uniqueThemeId,UpdateThemeState.ISVICTORINEMISTAKE)
//                   if(pref?.getString(Constatns.CLUE_KEY,"")=="false") {
                       val victorinClue = courseRepo.getVictorineClue(
                           questionId = answer.questionId,
                           victorineTestId = answer.vicotineTestId
                       )
                       Log.d("vpkokot3333333bopdrtbkpordtkb",victorinClue.toString())
                       if (victorinClue != null) {
                           isClue?.invoke(victorinClue.clueText)
                       }
//                   }

               } else {
                   ///Проверка подписки промокода начало
                   val myPromCodes = userLogicRepo.getAllMyPromo()
                   if(myPromCodes!=null){
                       val promoCodeModel = PromoCodeModel(token = userInfo?.token ?: "",
                           promoCode = myPromCodes?.promoCode ?: "",
                           dateApi = currentTime.datetime)
                       val responsePromo = userLogicRepo.checkActualMySubscribe(promoCodeModel)
                       if (responsePromo.isActual){
                           if(pref?.getString(Constatns.CLUE_KEY,"")=="true") {
                               val victorinClue = courseRepo.getVictorineClue(
                                   questionId = answer.questionId,
                                   victorineTestId = answer.vicotineTestId
                               )
                               if (victorinClue != null) {
                                   isClue?.invoke(victorinClue.clueText)
                               }
                           }
                       }
                   }
                   ///Проверка подписки промокода конец
                       updateThemeUseCase.updateTheme(answer.uniqueThemeId,UpdateThemeState.ISVICTORINEMISTAKE)
               }
               val buyCourses = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
               if(buyCourses[0].codeAnswer!=707){
                   if(pref?.getString(Constatns.CLUE_KEY,"")=="true") {
                       val victorinClue = courseRepo.getVictorineClue(
                           questionId = answer.questionId,
                           victorineTestId = answer.vicotineTestId
                       )
                       if (victorinClue != null) {
                           isClue?.invoke(victorinClue.clueText)
                       }
                   }
               } else{
                   val buyThemes = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
                   buyThemes.forEach { oneBuyTheme ->
                       if (oneBuyTheme.uniqueThemeId == answer.uniqueThemeId) {
                           if(pref?.getString(Constatns.CLUE_KEY,"")=="true") {
                               val victorinClue = courseRepo.getVictorineClue(
                                   questionId = answer.questionId,
                                   victorineTestId = answer.vicotineTestId
                               )
                               if (victorinClue != null) {
                                   isClue?.invoke(victorinClue.clueText)
                               }
                           }
                       }
                   }
               }
           }
           isSuccess.invoke(ErrorEnum.SUCCESS)
       }catch (e:IOException){
           if(checkSubscibe()){
               isSuccess.invoke(ErrorEnum.OFFLINEMODE)
               return
           }
           if(checkBuyCourse()){
               isSuccess.invoke(ErrorEnum.OFFLINEMODE)
               return
           }
           Log.d("victorineTestdfetd",e.toString())
           isSuccess.invoke(ErrorEnum.NOTNETWORK)
       }catch (e:HttpException){
           Log.d("victorineTestdfetd",e.toString())
           isSuccess.invoke(ErrorEnum.ERROR)
       }catch (e:NullPointerException){
           Log.d("victorineTestdfetd",e.toString())
           isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
       }catch (e:TimeoutException){
           Log.d("victorineTestdfetd",e.toString())
          isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
       }catch (e:Exception){
           Log.d("victorineTestdfetd",e.toString())
           isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
       }
   }

    private suspend fun checkSubscibe():Boolean{
        val userSubscribes = transactionRepo.getSubscribe()
        userSubscribes?.let { sub->
            val currentDateLocal = Date()
            if (sub.date.time>currentDateLocal.time){
                return true
            }
        }
        return false
    }

    private suspend fun checkBuyCourse():Boolean{
        val buyCourses = transactionRepo.getAllMyCourseBuy()
        buyCourses?.let { buyCoursesNotNull ->
            buyCourses.forEach { oneBuyCourse ->
                if (!oneBuyCourse.andropointBuy) {
                    return true
                }
            }
        }
        return false
    }



}