package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.remote.model.CheckAdsLimitModel
import workwork.test.andropediagits.data.remote.model.adsTerm.AdsTermUserSendModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class AdsUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo, private val transactionRepo: TransactionRepo) {

    @SuppressLint("SimpleDateFormat", "SuspiciousIndentation")
    suspend fun viewAds(isSuccess:((ErrorEnum)->Unit), isVictorineView:Boolean = false){
        try{
          val myAdsViews = userLogicRepo.getAdsUserTerm()
            if(isVictorineView){
                if(myAdsViews==null){
                    val currentTime = userLogicRepo.getCurrentTime()
                    val token = userLogicRepo.getUserInfoLocal().token
                    val adsEntity = AdsEntity(
                        dateFirstViewAds = currentTime.datetime,
                        viewAdsCount = 0,
                        isTerm = true
                    )
                    userLogicRepo.insertAdsTerm(adsEntity)
                    val adsTermUserSendModel = AdsTermUserSendModel(
                        adsTermDate = currentTime.datetime,
                        token = token
                    )
                    userLogicRepo.sendAdsTerm(adsTermUserSendModel)

                }else{
                    val currentTime = userLogicRepo.getCurrentTime()
                    val token = userLogicRepo.getUserInfoLocal().token
                    val adsEntity = AdsEntity(
                        dateFirstViewAds = currentTime.datetime,
                        viewAdsCount = 0,
                        id = myAdsViews.id,
                        isTerm = true
                    )
                    userLogicRepo.updateAdsItem(adsEntity)
                    val adsTermUserSendModel = AdsTermUserSendModel(
                        adsTermDate = currentTime.datetime,
                        token = token
                    )
                    userLogicRepo.sendAdsTerm(adsTermUserSendModel)
                }
                return
            }
            if(myAdsViews==null){
                val currentTime = userLogicRepo.getCurrentTime()
                val adsEntity = AdsEntity(
                    dateFirstViewAds = currentTime.datetime,
                    viewAdsCount = 1
                  )
                userLogicRepo.insertAdsTerm(adsEntity)
            }else{
                val checkAdsLimitModel = CheckAdsLimitModel(
                    dateTerm = myAdsViews.dateFirstViewAds
                )
                val response = userLogicRepo.checkLimitTwoHoursAds(checkAdsLimitModel)
                if(response.isLimitActual==true){
                    if(myAdsViews.viewAdsCount==0){
                        val currentTime = userLogicRepo.getCurrentTime()
                        val adsEntity = AdsEntity(
                            dateFirstViewAds = currentTime.datetime,
                            viewAdsCount = 1,
                            id = myAdsViews.id
                        )
                        userLogicRepo.updateAdsItem(adsEntity)
                    }
                    if(myAdsViews.viewAdsCount<4){
                        val adsEntity = AdsEntity(
                            dateFirstViewAds = myAdsViews.dateFirstViewAds,
                            viewAdsCount = myAdsViews.viewAdsCount+1,
                            id = myAdsViews.id
                        )
                        userLogicRepo.updateAdsItem(adsEntity)
                    } else {
                        val currentTime = userLogicRepo.getCurrentTime()
                        val token = userLogicRepo.getUserInfoLocal().token
                        val adsEntity = AdsEntity(
                            dateFirstViewAds = myAdsViews.dateFirstViewAds,
                            viewAdsCount = 0,
                            id = myAdsViews.id,
                            isTerm = true
                        )
                        userLogicRepo.updateAdsItem(adsEntity)
                        val adsTermUserSendModel = AdsTermUserSendModel(
                            adsTermDate = currentTime.datetime,
                            token = token
                        )
                        userLogicRepo.sendAdsTerm(adsTermUserSendModel)
                    }
                }else{
                    val currentTime = userLogicRepo.getCurrentTime()
                    val adsEntity = AdsEntity(
                        dateFirstViewAds = currentTime.datetime,
                        viewAdsCount = 1,
                        id = myAdsViews.id
                    )
                    userLogicRepo.updateAdsItem(adsEntity)
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
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: HttpException){
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
    suspend fun checkTermLimit(isSuccess: ((ErrorEnum) -> Unit),isLimitActual:((Boolean)->Unit)){
        try{
            val myAdsViews = userLogicRepo.getAdsUserTerm()
            if(myAdsViews==null){
                isLimitActual.invoke(true)
            } else if(!myAdsViews.isTerm){
                isLimitActual.invoke(true)
            }
            else{
                val checkAdsLimitModel = CheckAdsLimitModel(
                    dateTerm = myAdsViews.dateFirstViewAds
                )
                val response = userLogicRepo.checkLimittermAds(checkAdsLimitModel)
                if(response.isLimitActual){
                    val token = userLogicRepo.getUserInfoLocal().token
                    userLogicRepo.deleteAdsTerm(token)
                }
                isLimitActual.invoke(response.isLimitActual)
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
        }catch (e: HttpException){
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