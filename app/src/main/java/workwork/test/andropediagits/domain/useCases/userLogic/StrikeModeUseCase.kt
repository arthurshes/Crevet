package workwork.test.andropediagits.domain.useCases.userLogic

import android.graphics.Bitmap
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.data.remote.model.strike.StrikeModeSendModel
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class StrikeModeUseCase @Inject constructor(private val repo: UserLogicRepo, private val transactionRepo: TransactionRepo) {

    private fun infinityToInt(infinitiyBool:Boolean):Int{
        if(infinitiyBool){
            return 1
        } else {
            return 0
        }
    }

    suspend fun strikeModeMainFun(isSuccess: ((ErrorEnum) -> Unit), strikeModeDay:((Int)->Unit), buyThemeId:((List<Int>)->Unit)?=null){
        try {
            val userInfoLocal = repo.getUserInfoLocal()
            val currentDateApi = repo.getCurrentTime()

//            val userStrikeModel = StrikeModeSendModel(
//                token = userInfoLocal?.token ?: "",
//                lastDateApi = userInfoLocal?.lastOnlineDate ?: currentDateApi.datetime ,
//                currentDateApi = currentDateApi.datetime
//            )
                        val userStrikeModel = StrikeModeSendModel(
                token = userInfoLocal?.token ?: "",
                lastDateApi = userInfoLocal.lastOnlineDate ?: "" ,
                currentDateApi = currentDateApi.datetime
            )
            val strikeModeResponse = repo.getMyStrikeModeInfo(userStrikeModel)
            Log.d("strikeModeUSeCase",strikeModeResponse.strikeModeDay.toString())
            strikeModeDay.invoke(strikeModeResponse.strikeModeDay)
            updateStrikeDay( {
                when(it){
                    ErrorEnum.NOTNETWORK -> isSuccess.invoke(ErrorEnum.NOTNETWORK)
                    ErrorEnum.ERROR -> isSuccess.invoke(ErrorEnum.ERROR)
                    ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorEnum.SUCCESS)
                    ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
                    ErrorEnum.TIMEOUTERROR -> isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
                    ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
                    ErrorEnum.OFFLINEMODE -> isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                    ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            },
               strikeModeResponse.strikeModeDay,
                currentDateApi.datetime,{
                       buyThemeId?.invoke(it)
                })
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemeId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
                }
            })
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("victorineTestResultStateSimpleTreamtResult",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun updateStrikeDay(isSuccess: ((ErrorEnum) -> Unit), strikeDay:Int,lastOnlieDate:String,buyThemes:((List<Int>)->Unit)?=null){
        try {
            val currentUserInfoLocal = repo.getUserInfoLocal()
            currentUserInfoLocal?.let { myInfo->
                val updateUserInfo = UserInfoEntity(
                    token = myInfo.token,
                    name = myInfo.name,
                    image = myInfo.image,
                    userLanguage = myInfo.userLanguage,
                    andropointCount = myInfo.andropointCount,
                    lastOnlineDate = lastOnlieDate,
                    strikeModeDay = strikeDay,
                    lastOpenCourse = myInfo.lastOpenCourse,
                    lastOpenTheme = myInfo.lastOpenTheme,
                    phoneBrand = myInfo.phoneBrand
                )
                repo.updateUserInfoLocal(updateUserInfo)

                val userSignInModel = UserSignInModel(
                    name = myInfo.name,
                    token = myInfo.token ?: "",
                    image = myInfo.image,
                    userlanguage = myInfo.userLanguage,
                    andropointCount = myInfo.andropointCount,
                    strikeModeDay = strikeDay,
                    lastCourseNumber = myInfo.lastOpenCourse,
                    lastThemeNumber = myInfo.lastOpenTheme,
                    isInfinity = infinityToInt(myInfo.isInfinity ?: false)
                )
                repo.updateUserInfo(userSignInModel)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if (checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemes?.invoke(it)
            },{ isBuy->
               if (isBuy){
                   isSuccess.invoke(ErrorEnum.OFFLINETHEMEBUY)
               }
            })
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
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

    private suspend fun checkAndGeIdtLocalBuyThemes(isThemesId:((List<Int>)->Unit)?=null,isBuy:((Boolean)->Unit)){
        val buyThemes = transactionRepo.getAllBuyThemes()
        buyThemes?.let { buyThemesNotNull->
            val buyThemesIdList = ArrayList<Int>()
            buyThemesNotNull.forEach { oneBuyTheme->
                buyThemesIdList.add(oneBuyTheme.uniqueThemeId)
            }
            isThemesId?.invoke(buyThemesIdList)
            isBuy.invoke(true)
        }
        isBuy.invoke(false)
    }

}