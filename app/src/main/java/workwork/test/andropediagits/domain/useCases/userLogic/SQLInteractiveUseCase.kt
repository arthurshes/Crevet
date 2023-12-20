package workwork.test.andropediagits.domain.useCases.userLogic

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.sqlInteractive.SqlTableInteractiveCorrectAnswerEntity
import workwork.test.andropediagits.data.local.entities.sqlInteractive.SqlTableInteractiveEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.Calendar
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class SQLInteractiveUseCase @Inject constructor(private val courseRepo: CourseRepo,private val userLogicRepo: UserLogicRepo,private val transactionRepo: TransactionRepo) {

 suspend fun getThemeQuestionsSql(uniqueThemeID:Int):List<SqlTableInteractiveEntity>{
    return courseRepo.getCurrentThemeSqlInteractiveTasks(uniqueThemeID)
 }

   suspend fun getThemeCorrectAnwer(uniqueThemeID: Int,taskId:Int):SqlTableInteractiveCorrectAnswerEntity{
      return courseRepo.getCorrectAnswerSqlInteractive(taskId,uniqueThemeID)
   }

   suspend fun checkQuery(query:String,uniqueThemeID: Int,taskId: Int,isCorrect:((Boolean)->Unit),isSuccess:((ErrorEnum)->Unit)){
       try{
           val correctAnswerEntity = courseRepo.getCorrectAnswerSqlInteractive(taskId,uniqueThemeID)
           if(query==correctAnswerEntity.correctQuery){
               isCorrect.invoke(true)
           }else{
               isCorrect.invoke(false)
           }
           isSuccess.invoke(ErrorEnum.SUCCESS)
       }catch (e:IOException){
           Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
           if(checkSubscibe()){
               isSuccess.invoke(ErrorEnum.OFFLINEMODE)
               return
           }
           if (checkBuyCourse()){
               isSuccess.invoke(ErrorEnum.OFFLINEMODE)
               return
           }
           isSuccess.invoke(ErrorEnum.NOTNETWORK)
       }catch (e: HttpException){
           Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
           isSuccess.invoke(ErrorEnum.ERROR)
       }catch (e: TimeoutException){
           Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
           isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
       }catch (e:Exception){
           if(e is CancellationException) throw e
           Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
           isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
       }


   }

    private suspend fun checkSubscibe():Boolean{
        val userSubscribes = transactionRepo.getSubscribe()
        userSubscribes?.let { sub->
            val currentDateLocal = Date()
            Log.d("obkobkokoybybybhnb",currentDateLocal.toString())

            val calendar = Calendar.getInstance()
            calendar.time = sub.date
            calendar.add(Calendar.DAY_OF_MONTH,31*sub.term)
            if (calendar.time.time>currentDateLocal.time){
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