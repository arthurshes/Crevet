package workwork.test.andropediagits.domain.useCases.userLogic

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.Calendar
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class HeartsUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo,private val transactionRepo: TransactionRepo){

    suspend fun getHeartUser(isInfinityHearts:((Boolean)->Unit),heartCount:((Int)->Unit),IsSuccess:((ErrorEnum)->Unit)){
        try {
            val myInfo = userLogicRepo.getUserInfoLocal()
            val currentDate = transactionRepo.getCurrentTime()
            val sendSubscribeCheckModel =
                SendSubscribeCheckModel(myInfo.token ?: "", currentDate.datetime)
            val checkSubs = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
            if (checkSubs.subscribeIsActual) {
               isInfinityHearts.invoke(true)
                heartCount.invoke(222)
                IsSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            val myBuyCourses = transactionRepo.checkMyBuyCourse(myInfo.token ?: "")
            if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                isInfinityHearts.invoke(true)
                heartCount.invoke(222)
                IsSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            isInfinityHearts.invoke(false)
            heartCount.invoke(myInfo.heartsCount ?: 0)
            IsSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                isInfinityHearts.invoke(true)
                heartCount.invoke(222)
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            Log.d("vpkokot3333333bopdrtbkpord3333tkb",checkBuyCourse().toString())
            if(checkBuyCourse()){
                isInfinityHearts.invoke(true)
                heartCount.invoke(222)
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun minusHearts(minusHeartCount:Int,IsSuccess:((ErrorEnum)->Unit),isEnding:((Boolean)->Unit)){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            if(userInfo.heartsCount ?: 0>=minusHeartCount) {

                val userSignInModel = UserSignInModel(
                    name = userInfo.name,
                    image = userInfo.image,
                    andropointCount = userInfo.andropointCount,
                    heartsCount = userInfo.heartsCount?.minus(minusHeartCount) ?: 0,
                    token = userInfo.token,
                    userlanguage = userInfo.userLanguage
                )
                userLogicRepo.updateUserInfo(userSignInModel)
                val userInfoEntity = UserInfoEntity(
                    token = userInfo.token,
                    andropointCount = userInfo.andropointCount,
                    heartsCount = userInfo.heartsCount?.minus(minusHeartCount),
                    userLanguage = userInfo.userLanguage,
                    strikeModeDay = userInfo.strikeModeDay,
                    isInfinity = userInfo.isInfinity,
                    lastOnlineDate = userInfo.lastOnlineDate,
                    lastOpenCourse = userInfo.lastOpenCourse,
                    lastOpenTheme = userInfo.lastOpenTheme,
                    image = userInfo.image,
                    name = userInfo.name
                )
                userLogicRepo.updateUserInfoLocal(userInfoEntity)
                isEnding.invoke(false)
            }else{
                isEnding.invoke(true)
            }
            IsSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            Log.d("vpkokot3333333bopdrtbkpord3333tkb",checkBuyCourse().toString())
            if(checkBuyCourse()){
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun buyHearts(heartCount:Int,IsSuccess:((ErrorEnum)->Unit),isHearBuy:((Boolean)->Unit)?=null){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            if(98 >=userInfo.heartsCount ?: 0){
            val userInfoEntity = UserInfoEntity(
                token = userInfo.token,
                andropointCount = userInfo.andropointCount,
                heartsCount = userInfo.heartsCount?.plus(heartCount),
                userLanguage = userInfo.userLanguage,
                strikeModeDay = userInfo.strikeModeDay,
                isInfinity = userInfo.isInfinity,
                lastOnlineDate = userInfo.lastOnlineDate,
                lastOpenCourse = userInfo.lastOpenCourse,
                lastOpenTheme = userInfo.lastOpenTheme,
                image = userInfo.image,
                name = userInfo.name
            )
            userLogicRepo.updateUserInfoLocal(userInfoEntity)
            val userSignInModel = UserSignInModel(
                name = userInfo.name,
                image = userInfo.image,
                andropointCount = userInfo.andropointCount,
                heartsCount = userInfo.heartsCount?.plus(heartCount) ?: 0,
                token = userInfo.token,
                userlanguage = userInfo.userLanguage
            )
            userLogicRepo.updateUserInfo(userSignInModel)
            isHearBuy?.invoke(true)
        }else{
            isHearBuy?.invoke(false)
        }
            IsSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                IsSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("victorineTestdfetd",e.toString())
            IsSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
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

    private suspend fun checkSubscibe():Boolean{
        val sub = transactionRepo.getSubscribe()
        if(sub!=null){
            val currentDateLocal = Date()
            Log.d("obkobkokoybybybhnb",sub.date.toString())

            val calendar = Calendar.getInstance()
            calendar.time = sub.date
            calendar.add(Calendar.DAY_OF_MONTH,31*sub.term)
            if (calendar.time.time>currentDateLocal.time){
                return true
            }
        }

        return false
    }

}