package workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase

import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
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
                        if( courseBuyNotNull.andropointBuy){
                            val themesCourse = courseRepo.searchThemesWithCourseNumber(courseBuyNotNull.courseNumber)
                            val n = themesCourse.minByOrNull { it.themeNumber }
                            val courseBuyModel = CourseBuyModel(
                                dateBuy = currentDate.datetime,
                                courseNumber = courseBuyNotNull.courseNumber,
                                token = userLocalInfo?.token ?: "",
                                transactionId = courseBuyNotNull.transactionId,
                                andropointBuy =1,
                                andropointMinus = key.courseAndropointCount,

                            )
                            transactionRepo.sendCourseBuy(courseBuyModel)
                            val updateCourseModel = UpdateCourseModel(
                                courseNumber = courseBuyNotNull.courseNumber,
                                isOpenCourse = true,
                                token = userLocalInfo?.token ?: "",
                                isBuyCourse = false,
                                dateApi = currentDate.datetime,
                                courseFirstTheme  = n?.themeNumber ?: 0
                            )

                            val updateAnswerModel = UpdateAnswerModel(
                                updateCourses = updateCourseModel,
                                token = userLocalInfo?.token ?: "",
                                AndropointCount = userLocalInfo?.andropointCount ?: 0
                            )
                            courseRepo.sendMyProgress(updateAnswerModel)
                        }else{
                            val courseBuyModel = CourseBuyModel(
                                dateBuy = currentDate.datetime,
                                courseNumber = courseBuyNotNull.courseNumber,
                                token = userLocalInfo?.token ?: "",
                                transactionId = courseBuyNotNull.transactionId,
                                andropointBuy =0
                            )
                            transactionRepo.sendCourseBuy(courseBuyModel)
                            courseBuyMoney(courseBuyNotNull.courseNumber)
                        }

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
            ////
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
private  suspend fun courseBuyMoney(courseNumber:Int){

        val currentTime = transactionRepo.getCurrentTime()
        val currentCourse = courseRepo.searchCourseWithNumber(courseNumber)
        val myInfo = userLogicRepo.getUserInfoLocal()
        val promoInfoLocal = userLogicRepo.getAllMyPromo()

        promoInfoLocal?.let { promocode ->
            val courseBuyEntity = CourseBuyEntity(
                token = myInfo.token,
                courseNumber = courseNumber,
                promoCode = promocode.promoCode,
                date = Date(),
                transactionId = myInfo.token+courseNumber.toString(),
                andropointBuy = false
            )
            transactionRepo.insertCourseBuy(courseBuyEntity)
            val courseBuy = CourseBuyModel(
                token = myInfo.token,
                promoCode = promocode.promoCode,
                courseNumber = courseNumber,
                dateBuy = currentTime.datetime,
                transactionId = myInfo.token + courseNumber.toString(),
                andropointBuy = 0
            )
            transactionRepo.sendCourseBuy(courseBuy)
            val updateCourseModel = UpdateCourseModel(
                courseNumber = courseNumber,
                isBuyCourse = true,
                isOpenCourse = true,
                dateApi = currentTime.datetime,
                token = myInfo.token ?: ""
            )
            val updateAnswerModel = UpdateAnswerModel(
                updateCourses = updateCourseModel,
                token = myInfo.token ?: "",
                AndropointCount = myInfo.andropointCount ?: 0
            )
            courseRepo.sendMyProgress(updateAnswerModel)
            val courses = courseRepo.getAllCourses()
            courses?.forEach { oneCourse->
                if (oneCourse.courseNumber == courseNumber){
                    val updateCourseEntity = CourseEntity(
                        isOpen = true,
                        courseNumber = oneCourse.courseNumber,
                        description = oneCourse.description,
                        courseName = oneCourse.courseName,
                        isNetworkConnect = oneCourse.isNetworkConnect,
                        possibleToOpenCourseFree = oneCourse.possibleToOpenCourseFree,
                        lastUpdateDate = oneCourse.lastUpdateDate
                    )
                    courseRepo.updateCourse(updateCourseEntity)
                    openAllThemesCourse(oneCourse.courseNumber,true)
                }
//                    if (oneCourse.possibleToOpenCourseFree){
//                        val updateCourseEntity = CourseEntity(
//                            isOpen = true,
//                            courseNumber = oneCourse.courseNumber,
//                            description = oneCourse.description,
//                            courseName = oneCourse.courseName,
//                            isNetworkConnect = oneCourse.isNetworkConnect,
//                            possibleToOpenCourseFree = oneCourse.possibleToOpenCourseFree,
//                            lastUpdateDate = oneCourse.lastUpdateDate
//                        )
//                        courseRepo.updateCourse(updateCourseEntity)
//                        openAllThemesCourse(oneCourse.courseNumber,false)
//                    }
            }

            return
        }
        val courseBuyEntity = CourseBuyEntity(
            token = myInfo.token,
            courseNumber = courseNumber,
            date = Date(),
            transactionId = myInfo.token+courseNumber.toString(),
            andropointBuy = false
        )
        transactionRepo.insertCourseBuy(courseBuyEntity)
        val courseBuy = CourseBuyModel(
            token = myInfo.token,
            courseNumber = courseNumber,
            dateBuy = currentTime.datetime,
            transactionId = myInfo.token + courseNumber.toString(),
            andropointBuy = 0
        )
        transactionRepo.sendCourseBuy(courseBuy)
        val updateCourseModel = UpdateCourseModel(
            courseNumber = courseNumber,
            isBuyCourse = true,
            isOpenCourse = true,
            dateApi = currentTime.datetime,
            token = myInfo.token ?: ""
        )
        val updateAnswerModel = UpdateAnswerModel(
            updateCourses = updateCourseModel,
            token = myInfo.token ?: "",
            AndropointCount = myInfo?.andropointCount ?: 0
        )
        courseRepo.sendMyProgress(updateAnswerModel)
        val courses = courseRepo.getAllCourses()
        courses?.forEach { oneCourse->
            if (oneCourse.courseNumber == courseNumber){
                val updateCourseEntity = CourseEntity(
                    isOpen = true,
                    courseNumber = oneCourse.courseNumber,
                    description = oneCourse.description,
                    courseName = oneCourse.courseName,
                    isNetworkConnect = oneCourse.isNetworkConnect,
                    possibleToOpenCourseFree = oneCourse.possibleToOpenCourseFree,
                    lastUpdateDate = oneCourse.lastUpdateDate
                )
                courseRepo.updateCourse(updateCourseEntity)
                openAllThemesCourse(oneCourse.courseNumber,true)
            }
            if (oneCourse.possibleToOpenCourseFree){
                val updateCourseEntity = CourseEntity(
                    isOpen = true,
                    courseNumber = oneCourse.courseNumber,
                    description = oneCourse.description,
                    courseName = oneCourse.courseName,
                    isNetworkConnect = oneCourse.isNetworkConnect,
                    possibleToOpenCourseFree = oneCourse.possibleToOpenCourseFree,
                    lastUpdateDate = oneCourse.lastUpdateDate
                )
                courseRepo.updateCourse(updateCourseEntity)
                openAllThemesCourse(oneCourse.courseNumber,false)
            }
        }


}

    private suspend fun openAllThemesCourse(courseNumber: Int,isBuyCourse:Boolean){
        val userInfo = userLogicRepo.getUserInfoLocal()
        val allThemes = courseRepo.searchThemesWithCourseNumber(courseNumber)
        val currentTime = courseRepo.getCurrentTime().datetime
        allThemes?.forEach { oneTheme->
            val updateThemeEntity = ThemeEntity(
                uniqueThemeId = oneTheme.uniqueThemeId,
                themeNumber = oneTheme.themeNumber,
                courseNumber = oneTheme.courseNumber,
                themeName = oneTheme.themeName,
                imageTheme = oneTheme.imageTheme,
                lastUpdateDate = oneTheme.lastUpdateDate,
                victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                interactiveCodeMistakes = oneTheme.interactiveCodeMistakes,
                victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                lastCourseTheme = oneTheme.lastCourseTheme,
                lessonsCount = oneTheme.lessonsCount,
                victorineQuestionCount = oneTheme.victorineQuestionCount,
                victorineDate = oneTheme.victorineDate,
                vicotineTestId = oneTheme.vicotineTestId,
                interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                interactiveTestId = oneTheme.interactiveTestId,
                isOpen = true,
                termHourse = null,
                termDateApi = null,
                isDuoInter = oneTheme.isDuoInter,
                duoDate = oneTheme.duoDate,
                possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                isVictorine = oneTheme.isVictorine,
                isFav = oneTheme.isFav,
                isThemePassed = oneTheme.isThemePassed,
                themePrice = oneTheme.themePrice
            )
            courseRepo.updateTheme(updateThemeEntity)
            if(isBuyCourse==true&&oneTheme.lastCourseTheme){
                if(oneTheme.isThemePassed){
                    val updateThemeModel = UpdateThemeModel(
                        courseNumber = oneTheme.courseNumber,
                        uniqueThemeId = oneTheme.uniqueThemeId,
                        themeNumber = oneTheme.themeNumber,
                        termDateApi = null,
                        termHourse = null,
                        token = userInfo?.token ?: "",
                        isOpenTheme = true,
                        interactiveId = oneTheme.interactiveTestId,
                        victorineId = oneTheme.vicotineTestId,
                        dateApi = currentTime,
                        lasThemePassed = true
                    )
                    val updateAnswerModel = UpdateAnswerModel(
                        token = userInfo?.token ?: "",
                        updateThemes = updateThemeModel,
                        AndropointCount = userInfo?.andropointCount ?: 0
                    )
                    courseRepo.sendMyProgress(updateAnswerModel)
                }else{
                    val updateThemeModel = UpdateThemeModel(
                        courseNumber = oneTheme.courseNumber,
                        uniqueThemeId = oneTheme.uniqueThemeId,
                        themeNumber = oneTheme.themeNumber,
                        termDateApi = null,
                        termHourse = null,
                        token = userInfo?.token ?: "",
                        isOpenTheme = true,
                        interactiveId = oneTheme.interactiveTestId,
                        victorineId = oneTheme.vicotineTestId,
                        dateApi = currentTime,
                        lasThemePassed = false
                    )
                    val updateAnswerModel = UpdateAnswerModel(
                        token = userInfo?.token ?: "",
                        updateThemes = updateThemeModel,
                        AndropointCount = userInfo?.andropointCount ?: 0
                    )
                    courseRepo.sendMyProgress(updateAnswerModel)
                }

            }
        }
    }

}