package workwork.test.andropediagits.domain.useCases.transactionLogic

import android.annotation.SuppressLint
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.indi.IndiCreatorSubscribeEntity
import workwork.test.andropediagits.data.remote.individualCourseGet.creatorSubscribe.BuyCreatorSubscribeModel
import workwork.test.andropediagits.domain.repo.IndividualCoursesRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeoutException

import javax.inject.Inject

class IndiTransactionUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo,private val individualCoursesRepo: IndividualCoursesRepo) {



    suspend fun buyCreatorSubscribe(isSuccess:((ErrorEnum)->Unit),term:Int,newBuy:Boolean){
        val token = userLogicRepo.getUserInfoLocal().token
        try {
            val currentDate = userLogicRepo.getCurrentTime()
            val existCheck = individualCoursesRepo.getMyIndiSubs()
            if(!existCheck.successSend&&!newBuy){
                val buyCreatorSubscribeModel = BuyCreatorSubscribeModel(
                    token = token,
                    buyDate = currentDate.datetime,
                    term = existCheck.term
                )
                val response = individualCoursesRepo.buyCreatorSubscribe(buyCreatorSubscribeModel)
                if(response.status){
                    val subscribeCreatorSubscribeEntity = IndiCreatorSubscribeEntity(
                        token = token,
                        term = existCheck.term,
                        dateBuy = existCheck.dateBuy,
                        successSend = true
                    )
                    individualCoursesRepo.updateIndiCreaterSubscribe(subscribeCreatorSubscribeEntity)
                }
                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }else if(!existCheck.successSend&&newBuy){
                val buyCreatorSubscribeModel = BuyCreatorSubscribeModel(
                    token = token,
                    buyDate = currentDate.datetime,
                    term = existCheck.term+term
                )
                val response = individualCoursesRepo.buyCreatorSubscribe(buyCreatorSubscribeModel)
                if(response.status){
                    val subscribeCreatorSubscribeEntity = IndiCreatorSubscribeEntity(
                        token = token,
                        term = existCheck.term+term,
                        dateBuy = existCheck.dateBuy,
                        successSend = true
                    )
                    individualCoursesRepo.updateIndiCreaterSubscribe(subscribeCreatorSubscribeEntity)
                }
                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            val buyCreatorSubscribeModel = BuyCreatorSubscribeModel(
                token = token,
                buyDate = currentDate.datetime,
                term = term
            )
           individualCoursesRepo.buyCreatorSubscribe(buyCreatorSubscribeModel)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            subscribeSendError(term, token)
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:Exception){
            subscribeSendError(term, token)
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e: HttpException){
            subscribeSendError(term, token)
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            subscribeSendError(term, token)
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e: TimeoutException){
            subscribeSendError(term, token)
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }
    }

    @SuppressLint("SimpleDateFormat")
    suspend fun checkCreaterSubscribe(isActual:((Boolean)->Unit), isActualToDate:((String)->Unit), isSuccess: ((ErrorEnum) -> Unit),isAccountBanned:((Boolean)->Unit)){
        try {
            val token = userLogicRepo.getUserInfoLocal().token
            val response = individualCoursesRepo.getAndCheckMyCreatorSubscribes(token)
            if(response.statusCode==2106){
                val localSubs = individualCoursesRepo.getMyIndiSubs()
                if(localSubs!=null){
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    val date = dateFormat.parse( response.buyDate)
                    val  indiCreatorSubscribeEntity =  IndiCreatorSubscribeEntity(
                        token = token,
                        term = response.term,
                        dateBuy = date,
                        successSend = true
                    )
                  individualCoursesRepo.insertCreatorSubscribe(indiCreatorSubscribeEntity)
                }
                isAccountBanned.invoke(false)
                isActual.invoke(true)
                isActualToDate.invoke(response.actualToDate)
            }else if(response.statusCode==2925){
                isAccountBanned.invoke(true)
                isActual.invoke(false)
            }else{
                isAccountBanned.invoke(false)
                isActual.invoke(false)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){

            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:Exception){

            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e: HttpException){

            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){

            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e: TimeoutException){

            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }
    }

    private suspend fun subscribeSendError(term: Int,token:String){
        val existCheck = individualCoursesRepo.getMyIndiSubs()
        if(existCheck!=null){
            val subscribeCreatorSubscribeEntity = IndiCreatorSubscribeEntity(
                token = token,
                term = existCheck.term+term,
                dateBuy = existCheck.dateBuy,
                successSend = false
            )
            individualCoursesRepo.updateIndiCreaterSubscribe(subscribeCreatorSubscribeEntity)
        }else{
            val subscribeCreatorSubscribeEntity = IndiCreatorSubscribeEntity(
                token = token,
                term = term,
                dateBuy = Date(),
                successSend = false
            )
            individualCoursesRepo.insertCreatorSubscribe(subscribeCreatorSubscribeEntity)
        }
    }

    suspend fun buyIndiCourse(){

    }

}