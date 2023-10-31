package workwork.test.andropediagits.domain.useCases.userLogic

import android.util.Log
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateCourseModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.Exception

class CourseUseCase @Inject constructor(private val courseRepo: CourseRepo, private val transactionRepo: TransactionRepo, private val userLogicRepo: UserLogicRepo, private val tryAgainUseCase: TryAgainUseCase) {

//    suspend fun thisCourseBuy(isSuccess:((ErrorEnum)->Unit), courseNumber:Int, isBuy:((Boolean)->Unit)){
//        try {
//            val myInfo = userLogicRepo.getUserInfoLocal()
//            val myBuyCourses = transactionRepo.checkMyBuyCourse(myInfo?.token ?: "")
//            if(myBuyCourses.isEmpty()){
//                isBuy.invoke(false)
//            }
//            myBuyCourses.forEach { buyCourse->
//                if (buyCourse.codeAnswer == 707){
//                    isBuy.invoke(false)
//                }
//                if (buyCourse.courseNumber==courseNumber){
//                    isBuy.invoke(true)
//                }
//
//            }
//            isSuccess.invoke(ErrorEnum.SUCCESS)
//        }catch (e:IOException){
//            localCheckBuyCourse(courseNumber,{success->
//                when(success){
//                    ErrorEnum.NOTNETWORK -> TODO()
//                    ErrorEnum.ERROR -> isSuccess.invoke(ErrorEnum.ERROR)
//                    ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorEnum.SUCCESS)
//                    ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//                    ErrorEnum.TIMEOUTERROR -> TODO()
//                    ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
//                    ErrorEnum.OFFLINEMODE -> TODO()
//                    ErrorEnum.OFFLINETHEMEBUY -> TODO()
//                }
//            },
//                { buyCourses->
//                    isBuy.invoke(buyCourses)
//                }
//                )
//            if(checkSubscibe()){
//                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
//                return
//            }
//            isSuccess.invoke(ErrorEnum.NOTNETWORK)
//        }catch (e:HttpException){
//             isSuccess.invoke(ErrorEnum.ERROR)
//        }catch (e:TimeoutException){
//              isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
//        }catch (e:NullPointerException){
//               isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
//        }catch (e:Exception){
//            localCheckBuyCourse(courseNumber,{success->
//                when(success){
//                    ErrorEnum.NOTNETWORK -> TODO()
//                    ErrorEnum.ERROR -> isSuccess.invoke(ErrorEnum.ERROR)
//                    ErrorEnum.SUCCESS -> isSuccess.invoke(ErrorEnum.SUCCESS)
//                    ErrorEnum.UNKNOWNERROR -> isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//                    ErrorEnum.TIMEOUTERROR -> TODO()
//                    ErrorEnum.NULLPOINTERROR -> isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
//                    ErrorEnum.OFFLINEMODE -> TODO()
//                    ErrorEnum.OFFLINETHEMEBUY -> TODO()
//                }
//            },
//                { buyCourses->
//                    isBuy.invoke(buyCourses)
//                }
//            )
//            Log.d("cccccoo",e.toString())
//            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//        }
//    }

    suspend fun tryAgainCourseSend(isSuccess:((ErrorEnum)->Unit),buyThemesId:((List<Int>)->Unit)?=null){
        tryAgainUseCase.tryAgainSendProgres({state->
               isSuccess.invoke(state)
        },{themesIds->
            buyThemesId?.invoke(themesIds)
        })
    }

    private suspend fun localCheckBuyCourse(courseNumber:Int,isSuccess:((ErrorEnum)->Unit),isBuy:((Boolean)->Unit)){
        try {
            val courseLocal = transactionRepo.searchCourseBuyForNumber(courseNumber)
            courseLocal?.let { courseBuy->
                if (courseBuy.courseNumber==courseNumber) {
                    isBuy.invoke(true)
                }
            }
            isBuy.invoke(false)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException) {
             isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun courseBuyAndropoint(isSuccess:((ErrorEnum)->Unit),courseNumber: Int,isCoursePremium:((Boolean)->Unit)?=null){
        try {
            val currentCourse = courseRepo.searchCourseWithNumber(courseNumber)
            if (currentCourse.possibleToOpenCourseFree) {
                val userInfo = userLogicRepo.getUserInfoLocal()
                val promoCode = userLogicRepo.getAllMyPromo()
                promoCode?.promoCode?.let { promo ->
                    val courseBuyEntity = CourseBuyEntity(
                        courseNumber = courseNumber,
                        token = userInfo?.token ?: "",
                        date = Date(),
                        transactionId = (userInfo?.token + courseNumber.toString()) ?: "",
                        andropointBuy = true
                    )
                    transactionRepo.insertCourseBuy(courseBuyEntity)
                    val currentDateApi = courseRepo.getCurrentTime()
                    val courseBuyModel = CourseBuyModel(
                        token = userInfo?.token ?: "",
                        courseNumber = courseNumber,
                        dateBuy = currentDateApi.datetime,
                        promoCode = promo,
                        transactionId = (userInfo?.token + courseNumber.toString())
                            ?: "",
                        andropointBuy = true
                    )
                    transactionRepo.sendCourseBuy(courseBuyModel)
                    ////Открытие самого курса
                    val courseEntity = CourseEntity(
                        courseName = currentCourse.courseName,
                        courseNumber = currentCourse.courseNumber,
                        possibleToOpenCourseFree = currentCourse.possibleToOpenCourseFree,
                        description = currentCourse.description,
                        isNetworkConnect = currentCourse.isNetworkConnect,
                        lastUpdateDate = currentCourse.lastUpdateDate,
                        isOpen = true
                    )
                    courseRepo.updateCourse(courseEntity)
                    ////Открытие самого курса конец
                    ////ОТправка открытия курса
                    val updateCourseModel = UpdateCourseModel(
                        courseNumber = courseNumber,
                        isOpenCourse = true,
                        token = userInfo?.token ?: "",
                        isBuyCourse = false,
                        dateApi = currentDateApi.datetime
                    )

                    val updateAnswerModel = UpdateAnswerModel(
                        updateCourses = updateCourseModel,
                        token = userInfo?.token ?: "",
                        AndropointCount = userInfo?.andropointCount ?: 0
                    )
                    courseRepo.sendMyProgress(updateAnswerModel)
                    ////ОТправка открытия курса конец
//                    isSuccess.invoke(ErrorEnum.SUCCESS)
                }
                val courseBuyEntity = CourseBuyEntity(
                    courseNumber = courseNumber,
                    token = userInfo?.token ?: "",
                    date = Date(),
                    transactionId = (userInfo?.token + courseNumber.toString()) ?: "",
                    andropointBuy = true
                )
                transactionRepo.insertCourseBuy(courseBuyEntity)
                val currentDateApi = courseRepo.getCurrentTime()
                val courseBuyModel = CourseBuyModel(
                    token = userInfo?.token ?: "",
                    courseNumber = courseNumber,
                    dateBuy = currentDateApi.datetime,
                    transactionId = (userInfo?.token + courseNumber.toString())
                        ?: "",
                    andropointBuy = true
                )
                transactionRepo.sendCourseBuy(courseBuyModel)
                ////Открытие самого курса
                val courseEntity = CourseEntity(
                    courseName = currentCourse.courseName,
                    courseNumber = currentCourse.courseNumber,
                    possibleToOpenCourseFree = currentCourse.possibleToOpenCourseFree,
                    description = currentCourse.description,
                    isNetworkConnect = currentCourse.isNetworkConnect,
                    lastUpdateDate = currentCourse.lastUpdateDate,
                    isOpen = true
                )
                courseRepo.updateCourse(courseEntity)
                ////Открытие самого курса конец
                ///Отправка прогресса
                val updateCourseModel = UpdateCourseModel(
                    courseNumber = courseNumber,
                    isOpenCourse = true,
                    token = userInfo?.token ?: "",
                    isBuyCourse = false,
                    dateApi = currentDateApi.datetime
                )
                val updateAnswerModel = UpdateAnswerModel(
                    updateCourses = updateCourseModel,
                    token = userInfo?.token ?: "",
                    AndropointCount = userInfo?.andropointCount ?: 0
                )
                courseRepo.sendMyProgress(updateAnswerModel)
                ///Отправка прогресса конец
                isCoursePremium?.invoke(false)

            } else if (currentCourse.possibleToOpenCourseFree){
                isCoursePremium?.invoke(true)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if (checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
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

}