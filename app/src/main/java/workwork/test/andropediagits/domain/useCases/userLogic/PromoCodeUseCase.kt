package workwork.test.andropediagits.domain.useCases.userLogic

import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.promo.PromoCodeEntity
import workwork.test.andropediagits.data.remote.model.promo.PromoCodeModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.state.PromoCodeState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class PromoCodeUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo,private val transactionRepo: TransactionRepo){

    suspend fun checkPromoCode(promoCode:String, isSuccess:((ErrorEnum)->Unit), isPromoState:((PromoCodeState)->Unit)){
        try {
            val currentTime = userLogicRepo.getCurrentTime()
            val userInfo = userLogicRepo.getUserInfoLocal()
            Log.d("promoDialdodod","userTokenLocal:${userInfo.token}")
                val promoCodeModel = PromoCodeModel(userInfo?.token ?: "", promoCode, currentTime.datetime)
                val response = userLogicRepo.sendPromoCode(promoCodeModel)
                if (response.promoExist) {
                    if(response.userPromoExist){
                        isPromoState.invoke(PromoCodeState.PROMOUSEREXIST)
                    }else{
                        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val date = dateFormat.parse( response.promoDate)
                        val promoCodeEntity = PromoCodeEntity(
                            promoId = userInfo?.token + promoCode + currentTime.datetime,
                            promoCode = promoCode,
                            token = userInfo?.token ?: "",
                            promoDate = date
                        )
                        userLogicRepo.insertPromoCode(promoCodeEntity)
                        isPromoState.invoke(PromoCodeState.PROMOEXISTSUCCESS)
                    }
                } else if (!response.promoExist) {
                    isPromoState.invoke(PromoCodeState.PROMONOTEXIST)
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
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("promoDialdodod","promooException:${e.toString()}")
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            Log.d("promoDialdodod","promooException:${e.toString()}")
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }



    suspend fun checkPromoCodeSubscribeActual(promoCode:String,isActual:((Boolean)->Unit),isSuccess:((ErrorEnum)->Unit)){
        try {
            val currentTime = userLogicRepo.getCurrentTime()
            val userInfo = userLogicRepo.getUserInfoLocal()
            userInfo?.let { myInfo ->
                val promoCodeModel = PromoCodeModel(myInfo?.token ?: "", promoCode, currentTime.datetime)
                val response = userLogicRepo.checkActualMySubscribe(promoCodeModel)
                isActual.invoke(response.isActual)
//                isSuccess.invoke(ErrorEnum.SUCCESS)
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
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
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

//    private suspend fun checkAndGeIdtLocalBuyThemes(isThemesId:((List<Int>)->Unit)?=null,isBuy:((Boolean)->Unit)){
//        val buyThemes = transactionRepo.getAllBuyThemes()
//        buyThemes?.let { buyThemesNotNull->
//            val buyThemesIdList = ArrayList<Int>()
//            buyThemesNotNull.forEach { oneBuyTheme->
//                buyThemesIdList.add(oneBuyTheme.uniqueThemeId)
//            }
//            isThemesId?.invoke(buyThemesIdList)
//            isBuy.invoke(true)
//        }
//        isBuy.invoke(false)
//    }

}