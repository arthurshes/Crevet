package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.SplashActionEnum
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import java.util.Calendar
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class SplashScreenUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo, private val tryAgainUseCase: TryAgainUseCase, private val updateUserInfoUseCase: UpdateUserInfoUseCase, private val transactionRepo: TransactionRepo) {

    @SuppressLint("SuspiciousIndentation")
    suspend fun start(isSuccess:((SplashActionEnum)->Unit), buyThemesId:((List<Int>)->Unit)?=null) {
        try {
            val userInfoLocal = userLogicRepo.getUserInfoLocal()
              if (userInfoLocal!=null){
                  Log.d("splash",userInfoLocal.token)
                  tryAgainUseCase.tryAgainSendProgres({ errorState->
                      when(errorState){
                          ErrorEnum.NOTNETWORK -> {
                              isSuccess.invoke(SplashActionEnum.TRYAGAINSNACk)
                          }
                          ErrorEnum.ERROR ->          isSuccess.invoke(SplashActionEnum.TRYAGAINSNACk)
                          ErrorEnum.SUCCESS ->           Log.d("splash","success")
                          ErrorEnum.UNKNOWNERROR ->    isSuccess.invoke(SplashActionEnum.ERRORSCREEN)
                          ErrorEnum.TIMEOUTERROR ->    isSuccess.invoke(SplashActionEnum.LONGWAITSERVER)
                          ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(SplashActionEnum.ERRORSCREEN)
                          ErrorEnum.OFFLINEMODE -> isSuccess.invoke(SplashActionEnum.OFFLINEMODE)
                          ErrorEnum.OFFLINETHEMEBUY -> isSuccess.invoke(SplashActionEnum.OFFLINEBUYTHEME)
                      }
                  },{uniqueThemes->
                      buyThemesId?.invoke(uniqueThemes)
                  })
                  if(userLogicRepo.getReset()!=null){
                      isSuccess.invoke(SplashActionEnum.RESETDATASCREEN)
                      return
                  }

///
                  isSuccess.invoke(SplashActionEnum.HOMESCREEN)

              }else{
                  Log.d("splash","signin")
                  isSuccess.invoke(SplashActionEnum.SIGNINSCREEN)
              }
        }catch (e:IOException){
            if(checkBuyCourse()){
                isSuccess.invoke(SplashActionEnum.OFFLINEMODE)
                return
            }
            if(checkSubscibe()){
                Log.d("Splashss","subsdirifri")
                isSuccess.invoke(SplashActionEnum.OFFLINEMODE)
                return
            }else{
                Log.d("Splashss",e.toString())
                isSuccess.invoke(SplashActionEnum.TRYAGAINSNACk)
            }

        }catch (e:HttpException){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(SplashActionEnum.TRYAGAINSNACk)
        }catch (e: TimeoutException){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(SplashActionEnum.LONGWAITSERVER)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("Splashss",e.toString())
            isSuccess.invoke(SplashActionEnum.ERRORSCREEN)
        }
    }

    private suspend fun checkSubscibe():Boolean{
        val sub = transactionRepo.getSubscribe()
        if(sub!=null){
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