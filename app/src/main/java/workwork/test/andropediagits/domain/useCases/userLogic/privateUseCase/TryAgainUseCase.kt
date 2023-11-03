package workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase

import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateCourseModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateThemeModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.ArrayList
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject


class TryAgainUseCase @Inject constructor(private val courseRepo: CourseRepo, private val transactionRepo: TransactionRepo, private val userLogicRepo: UserLogicRepo) {

    suspend fun tryAgainSendProgres(isSuccess: ((ErrorEnum) -> Unit)?=null, buyThemesId:((List<Int>)->Unit)?=null){
        var successFlag = true
        Log.d("lllltryagain","startastt")
        try {
            val updateKeys = courseRepo.getAllUpdatesKeys() ?: return
            updateKeys?.let { nonulKey ->
                val updateThemeModelArray = ArrayList<UpdateThemeModel>()
                val updateCourseModelArray = ArrayList<UpdateCourseModel>()
                val userLocalInfo = userLogicRepo.getUserInfoLocal()
                val currentDate = courseRepo.getCurrentTime()
                nonulKey.forEach { key ->
                    val themeEntity =
                        key.uniqueThemeId?.let { courseRepo.searchThemeWithUniwueId(it) }
                    val themeBuy = key.buyThemeUniqueId?.let { transactionRepo.searchBuyTheme(it) }
                    themeBuy?.let { themeBuyNotNull->
                        val themeBuyModel = ThemeBuyModel(
                            dateBuyApi = currentDate.datetime,
                            dateBuyForDate = themeBuyNotNull.date,
                            token = userLocalInfo?.token ?: "",
                            uniqueThemeId = themeBuyNotNull.uniqueThemeId,
                            themeNumber = themeBuyNotNull.themeNumber,
                            courseNumber = themeBuyNotNull.courseNumber,
                            transactionId = themeBuyNotNull.transactionId
                        )
                        transactionRepo.sendUserThemeBuy(themeBuyModel)
                    }

                    key.subscribeTrasaction?.let { notNull->
                        val mySub = transactionRepo.getSubscribe()
                        val updateSubModel = SubscribeModel(
                            token = userLocalInfo?.token ?: "",
                            term = mySub?.term ?: 0,
                            dateBuy = currentDate.datetime,
                            dateBuyForDate = mySub?.date ?: Date(),
                            transactionId = mySub?.transactionId ?: ""
                        )
                        transactionRepo.sendSubscribtionTransaction(updateSubModel)
                        offTermAllThemes()
                    }

//                    key.updateNameBoolean?.let { notNullName->
//
//                    }

                    val courseBuy = key.buyCourseNumber?.let {
                        transactionRepo.searchCourseBuyForNumber(it)
                    }
                    val course = key.courseNumber?.let {
                        courseRepo.searchCourseWithNumber(it)
                    }
                    courseBuy?.let { courseBuyNotNull->
                        val courseBuyModel = CourseBuyModel(
                            dateBuy = currentDate.datetime,
                            courseNumber = courseBuyNotNull.courseNumber,
                            token = userLocalInfo?.token ?: "",
                            transactionId = courseBuyNotNull.transactionId,
                            andropointBuy = courseBuyNotNull.andropointBuy
                        )
                        transactionRepo.sendCourseBuy(courseBuyModel)
                    }

                        val courseModel = UpdateCourseModel(
                           dateApi = currentDate.datetime,
                           token = userLocalInfo?.token ?: "",
                           courseNumber = course?.courseNumber ?: 0,
                          isOpenCourse = course?.isOpen ?: false
                     )
                        val updateThemeModel = UpdateThemeModel(
                            uniqueThemeId = themeEntity?.uniqueThemeId ?: 0,
                            interactiveId = themeEntity?.interactiveTestId ?: 0,
                            victorineId = themeEntity?.vicotineTestId ?: 0,
                            themeNumber = themeEntity?.themeNumber ?: 0,
                            courseNumber = themeEntity?.courseNumber ?: 0,
                            isOpenTheme = themeEntity?.isOpen ?: false ,
                            token = userLocalInfo?.token ?: "",
                            dateApi = currentDate.datetime,
                            termDateApi = themeEntity?.termDateApi,
                            termHourse = themeEntity?.termHourse,
                            lasThemePassed = false
                        )
                        val updateAnswerModel = UpdateAnswerModel(
                            updateCourses = courseModel,
                            updateThemes = updateThemeModel,
                            token = userLocalInfo?.token ?: "",
                            AndropointCount = userLocalInfo?.andropointCount
                        )
                        courseRepo.sendMyProgress(updateAnswerModel)
                        if (successFlag) {
                            courseRepo.deleteUpdateKey(key)
                        }

                }

            }
            isSuccess?.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("Splashsst",e.toString())
            successFlag = false
            val subscribeInfoLocal = transactionRepo.getSubscribe()
             subscribeInfoLocal?.let { sub->
                 val currentDateLocal = Date()
                 Log.d("obkobkokoybybybhnb",currentDateLocal.toString())

                 val calendar = Calendar.getInstance()
                 calendar.time = sub.date
                 calendar.add(Calendar.DAY_OF_MONTH,31*sub.term)
                 if (calendar.time.time>currentDateLocal.time){
                     isSuccess?.invoke(ErrorEnum.OFFLINEMODE)
                     return
                 }
             }
             if(checkBuyCourse()){
                 isSuccess?.invoke(ErrorEnum.OFFLINEMODE)
                 return
             }
            checkAndGeIdtLocalBuyThemes({
                buyThemesId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess?.invoke(ErrorEnum.OFFLINETHEMEBUY)
                    return@checkAndGeIdtLocalBuyThemes
                }
            })
            isSuccess?.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: HttpException){
            Log.d("Splashsst",e.toString())
            successFlag = false
            isSuccess?.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("Splashsst",e.toString())
            isSuccess?.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("Splashsst",e.toString())
            isSuccess?.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("Splashsst",e.toString())
            successFlag = false
            isSuccess?.invoke(ErrorEnum.UNKNOWNERROR)
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

    ////Отключает задержку во всех темах
    private suspend fun offTermAllThemes(){
        val userInfo = userLogicRepo.getUserInfoLocal()
                val currentTime = courseRepo.getCurrentTime()
                            val updateThemeModel = UpdateThemeModel(
                        uniqueThemeId =0,
                        themeNumber = 0,
                        isOpenTheme = true,
                        courseNumber =0,
                        termDateApi = null,
                        termHourse = null,
                        victorineId = 0,
                        interactiveId = 0,
                        dateApi = currentTime.datetime,
                        token = userInfo?.token ?: "",
                                lasThemePassed = false
                    )
                val updateAnswerModel = UpdateAnswerModel(
            updateThemes = updateThemeModel,
            token = userInfo?.token ?: ""
        )
        courseRepo.sendMyProgress(updateAnswerModel)
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