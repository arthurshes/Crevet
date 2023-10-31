package workwork.test.andropediagits.domain.useCases.userLogic

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.UpdateNewPasswordModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.state.ResetSendState
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val transactionRepo: TransactionRepo,private val userLogicRepo: UserLogicRepo){

    suspend fun sendReset(email:String?=null,resetSendState: ResetSendState,isSuccess:((ErrorEnum)->Unit),resetDateSendModel: ResetDateSendModel?=null,resetTextSendModel: ResetTextSendModel?=null,isCorrectDatas:((Boolean)->Unit)?=null,responseMethodCall:((ResetMethodGetModel)->Unit)?=null,resetDateCheckSendModel: ResetDateCheckSendModel?=null,resetTextCheckSendModel: ResetTextCheckSendModel?=null,newPassword:String?=null){
        when(resetSendState){
            ResetSendState.SENDRESETTEXT -> {
                sendTextReset(resetTextSendModel,isSuccess)
            }
            ResetSendState.SENDRESETDATE -> {
                sendDateReset(resetDateSendModel,isSuccess)
            }

            ResetSendState.CHECKRESETTEXT -> {
                checkTextReset(resetTextCheckSendModel,isSuccess,isCorrectDatas)
            }
            ResetSendState.CHECKRESETDATE -> {
               checkDateReset(resetDateCheckSendModel,isSuccess,isCorrectDatas)
            }

            ResetSendState.GETMETHODSRESET -> {
                getMethodReset(email,isSuccess,responseMethodCall)
            }

            ResetSendState.SENDNEWPASSWORD -> {
                sendNewPassword(email ?: "",newPassword ?: "",isSuccess)
            }
        }
    }

    private suspend fun sendNewPassword(email: String,newPassword: String,isSuccess: ((ErrorEnum) -> Unit)){
        try {
            val updateNewPasswordModel = UpdateNewPasswordModel(
                newPassword, email
            )
            userLogicRepo.sendUpdateNewPassword(updateNewPasswordModel)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e: IOException) {
            if (checkSubscibe()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        } catch (e: TimeoutException) {
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        } catch (e: HttpException) {
            isSuccess.invoke(ErrorEnum.ERROR)
        } catch (e: NullPointerException) {
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        } catch (e: Exception) {
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun getMethodReset(email: String?=null,isSuccess: ((ErrorEnum) -> Unit),responseCall:((ResetMethodGetModel)->Unit)?=null){
        try {
            val response = email?.let { userLogicRepo.getUserCheckMethod(it) }
            if (response != null) {
                responseCall?.invoke(response)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e: IOException) {
            if (checkSubscibe()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        } catch (e: TimeoutException) {
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        } catch (e: HttpException) {
            isSuccess.invoke(ErrorEnum.ERROR)
        } catch (e: NullPointerException) {
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        } catch (e: Exception) {
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun checkTextReset(resetCheckTextSendModel: ResetTextCheckSendModel?=null,isSuccess: ((ErrorEnum) -> Unit),isCorrect:((Boolean)->Unit)?=null) {
        try {
            val response = resetCheckTextSendModel?.let { userLogicRepo.checkResetText(it) }
            if(response?.codeAnswer==2002){
                isCorrect?.invoke(true)
            }
            if(response?.codeAnswer==2005){
                isCorrect?.invoke(false)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        } catch (e: IOException) {
            if (checkSubscibe()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()) {
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        } catch (e: TimeoutException) {
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        } catch (e: HttpException) {
            isSuccess.invoke(ErrorEnum.ERROR)
        } catch (e: NullPointerException) {
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        } catch (e: Exception) {
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun checkDateReset(resetDateCheckSendModel: ResetDateCheckSendModel?=null,isSuccess: ((ErrorEnum) -> Unit),isCorrect:((Boolean)->Unit)?=null){
        try {
            val response = resetDateCheckSendModel?.let { userLogicRepo.checkResetDate(it) }
            if(response?.codeAnswer==2002){
                isCorrect?.invoke(true)
            }
            if(response?.codeAnswer==2005){
                isCorrect?.invoke(false)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("tktktktktktasmt",e.toString())
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: TimeoutException){
            Log.d("tktktktktktasmt",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e: HttpException){
            Log.d("tktktktktktasmt",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("tktktktktktasmt",e.toString())
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            Log.d("tktktktktktasmt",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun sendTextReset(resetTextSendModel: ResetTextSendModel?=null,isSuccess: ((ErrorEnum) -> Unit)){
        try{
            if (resetTextSendModel != null) {
                userLogicRepo.resetTextSend(resetTextSendModel)
            }
            userLogicRepo.deleteAllReset()
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
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e: HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun sendDateReset(resetDateSendModel: ResetDateSendModel?=null,isSuccess: ((ErrorEnum) -> Unit)){
      try{
          if (resetDateSendModel != null) {
              userLogicRepo.resetDateSend(resetDateSendModel)
          }
          userLogicRepo.deleteAllReset()
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
          isSuccess.invoke(ErrorEnum.NOTNETWORK)
      }catch (e: TimeoutException){
          isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
      }catch (e: HttpException){
          isSuccess.invoke(ErrorEnum.ERROR)
      }catch (e:NullPointerException){
          isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
      }catch (e:Exception){
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

