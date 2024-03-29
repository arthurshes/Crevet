package workwork.test.andropediagits.domain.useCases.transactionLogic


import android.util.Log
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.mappers.toThemeBuyEntity
import workwork.test.andropediagits.core.utils.ErrorLamdaRes
import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.local.entities.course.CourseBuyEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.local.entities.updatesEntity.UpdatesKeyEntity
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import workwork.test.andropediagits.data.remote.model.course.CourseBuyModel
import workwork.test.andropediagits.data.remote.model.theme.ThemeBuyModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateAnswerModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateCourseModel
import workwork.test.andropediagits.data.remote.model.updateModel.UpdateThemeModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.Calendar
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.collections.ArrayList

class TransactionUseCase @Inject constructor(private val transactionRepo: TransactionRepo, private val userLogicRepo: UserLogicRepo, private val courseRepo: CourseRepo) {


    suspend fun themeBuy(uniqueThemeId:Int,isSucces:((ErrorEnum)->Unit)){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            val token = userInfo.token
            val theme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            val themeNumber = theme.themeNumber
            val courseNumber = theme.courseNumber
            val currentTime = transactionRepo.getCurrentTime()
            val promoLocalInfo = userLogicRepo.getAllMyPromo()
            promoLocalInfo?.let {promoCode->
                val themeBuyModel = ThemeBuyModel(
                    promoCode = promoCode.promoCode,
                    dateBuyApi = currentTime.datetime,
                    dateBuyForDate = Date(),
                    token = token,
                    uniqueThemeId = uniqueThemeId,
                    themeNumber = themeNumber,
                    courseNumber = courseNumber,
                    transactionId = token+courseNumber.toString()+uniqueThemeId.toString()+currentTime.datetime
                )
                transactionRepo.insertBuyTheme(themeBuyModel.toThemeBuyEntity())
                transactionRepo.sendUserThemeBuy(themeBuyModel)
                val themeEntity = ThemeEntity(
                    uniqueThemeId = theme?.uniqueThemeId ?: 0,
                    themeNumber = theme?.themeNumber ?: 0,
                    courseNumber = theme?.courseNumber?: 0,
                    lastCourseTheme = theme?.lastCourseTheme?:false,
                    lastUpdateDate = theme?.lastUpdateDate ?: Date(),
                    isOpen = true,
                    possibleToOpenThemeFree = theme?.possibleToOpenThemeFree ?: true,
                    isDuoInter = theme?.isDuoInter ?: false,
                    isVictorine = theme?.isVictorine?: false,
                    isFav = theme?.isFav?: false,
                    termHourse = null,
                    termDateApi = null,
                    victorineDate = theme?.victorineDate,
                    vicotineTestId = theme?.vicotineTestId?: 0,
                    victorineQuestionCount = theme?.victorineQuestionCount?: 0,
                    victorineCorrectAnswer = theme?.victorineCorrectAnswer,
                    victorineMistakeAnswer = theme?.victorineMistakeAnswer,
                    interactiveQuestionCount = theme?.interactiveQuestionCount?: 0,
                    themeName = theme?.themeName?: "",
                    imageTheme = theme?.imageTheme,
                    interactiveTestId = theme?.interactiveTestId?: 0,
                    interactiveCodeCorrect = theme?.interactiveCodeCorrect,
                    interactiveCodeMistakes = theme?.interactiveCodeMistakes,
                    duoDate = theme?.duoDate,
                    lessonsCount = theme?.lessonsCount?: 0,
                    isThemePassed = theme.isThemePassed,
                    themePrice = theme.themePrice
                )
                courseRepo.updateTheme(themeEntity)
//                val updateThemeModel = UpdateThemeModel(
//                    uniqueThemeId = theme?.uniqueThemeId ?: 0,
//                    themeNumber =theme?.themeNumber ?: 0,
//                    isOpenTheme = true,
//                    courseNumber = theme?.courseNumber ?: 0,
//                    termDateApi = null,
//                    termHourse = null,
//                    victorineId = 2,
//                    interactiveId = 3,
//                    dateApi = currentTime.datetime,
//                    token = userInfo?.token ?: ""
//                )
//                val updateAnswerModel = UpdateAnswerModel(
//                    updateThemes = updateThemeModel,
//                    token = userInfo?.token ?: "",
//                    AndropointCount =  userInfo?.andropointCount ?: 0
//                )
//                courseRepo.sendMyProgress(updateAnswerModel)
                isSucces.invoke(ErrorEnum.SUCCESS)
                return
            }
            val themeEntity = ThemeEntity(
                uniqueThemeId = theme?.uniqueThemeId ?: 0,
                themeNumber = theme?.themeNumber ?: 0,
                courseNumber = theme?.courseNumber?: 0,
                lastCourseTheme = theme?.lastCourseTheme?:false,
                lastUpdateDate = theme?.lastUpdateDate ?: Date(),
                isOpen = true,
                possibleToOpenThemeFree = theme?.possibleToOpenThemeFree ?: true,
                isDuoInter = theme?.isDuoInter ?: false,
                isVictorine = theme?.isVictorine?: false,
                isFav = theme?.isFav?: false,
                termHourse = null,
                termDateApi = null,
                victorineDate = theme?.victorineDate,
                vicotineTestId = theme?.vicotineTestId?: 0,
                victorineQuestionCount = theme?.victorineQuestionCount?: 0,
                victorineCorrectAnswer = theme?.victorineCorrectAnswer,
                victorineMistakeAnswer = theme?.victorineMistakeAnswer,
                interactiveQuestionCount = theme?.interactiveQuestionCount?: 0,
                themeName = theme?.themeName?: "",
                imageTheme = theme?.imageTheme,
                interactiveTestId = theme?.interactiveTestId?: 0,
                interactiveCodeCorrect = theme?.interactiveCodeCorrect,
                interactiveCodeMistakes = theme?.interactiveCodeMistakes,
                duoDate = theme?.duoDate,
                lessonsCount = theme?.lessonsCount?: 0,
                themePrice = theme.themePrice,
                isThemePassed = theme.isThemePassed
            )
            courseRepo.updateTheme(themeEntity)
//            val updateThemeModel = UpdateThemeModel(
//                uniqueThemeId = theme?.uniqueThemeId ?: 0,
//                themeNumber =theme?.themeNumber ?: 0,
//                isOpenTheme = true,
//                courseNumber = theme?.courseNumber ?: 0,
//                termDateApi = null,
//                termHourse = null,
//                victorineId = 2,
//                interactiveId = 3,
//                dateApi = currentTime.datetime,
//                token = userInfo?.token ?: ""
//            )
//            val updateAnswerModel = UpdateAnswerModel(
//                updateThemes = updateThemeModel,
//                token = userInfo?.token ?: "",
//                AndropointCount =  userInfo?.andropointCount ?: 0
//            )
//            courseRepo.sendMyProgress(updateAnswerModel)
            val themeBuyModel = ThemeBuyModel(
                dateBuyApi = currentTime.datetime,
                dateBuyForDate = Date(),
                token = token,
                uniqueThemeId = uniqueThemeId,
                themeNumber = themeNumber,
                courseNumber = courseNumber,
                transactionId = token+courseNumber.toString()+uniqueThemeId.toString()+currentTime.datetime
            )
            transactionRepo.insertBuyTheme(themeBuyModel.toThemeBuyEntity())
            transactionRepo.sendUserThemeBuy(themeBuyModel)
            isSucces.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            withContext(NonCancellable){
                saveErrorUpdate(uniqueThemeId = uniqueThemeId)
            }

            if(checkSubscibe()){
                isSucces.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                isSucces.invoke(ErrorEnum.OFFLINEMODE)
                return
            }

            isSucces.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:Exception){
            withContext(NonCancellable){
                saveErrorUpdate(uniqueThemeId = uniqueThemeId)
            }
            if(e is CancellationException) throw e
            isSucces.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e:HttpException){
            withContext(NonCancellable){
                saveErrorUpdate(uniqueThemeId = uniqueThemeId)
            }
            isSucces.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            withContext(NonCancellable){
                saveErrorUpdate(uniqueThemeId = uniqueThemeId)
            }
            isSucces.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            withContext(NonCancellable){
                saveErrorUpdate(uniqueThemeId = uniqueThemeId)
            }
            isSucces.invoke(ErrorEnum.TIMEOUTERROR)
        }
    }

    suspend fun subscribeBuy(term:Int,isSuccess:((ErrorEnum)->Unit)){
        try {
            val currentTime = transactionRepo.getCurrentTime()
            val promocode = userLogicRepo.getAllMyPromo()
            val token = userLogicRepo.getUserInfoLocal().token
           if(promocode!=null){
                val myLocalSub = transactionRepo.getSubscribe()
                if(myLocalSub!=null){
                    val updateSub = SubscribeEntity(
                        term = myLocalSub.term+term,
                        date = myLocalSub.date,
                        id = myLocalSub.id,
                        token = token,
                        transactionId = currentTime.datetime + token + term.toString()
                    )
                    transactionRepo.updateSubscribe(updateSub)
                }
                val saveSubscribeEntity = SubscribeEntity(
                    term = term,
                    token = token,
                    date = Date(),
                    transactionId = currentTime.datetime + token + term.toString()
                )
                transactionRepo.insertSubscribe(saveSubscribeEntity)
                offTermAllThemes()
                val subscribe = SubscribeModel(
                    dateBuy = currentTime.datetime,
                    promoCode = promocode.promoCode,
                    token = token,
                    term = term,
                    transactionId = currentTime.datetime + token + term.toString()
                )
                transactionRepo.sendSubscribtionTransaction(subscribe)
                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            val myLocalSub = transactionRepo.getSubscribe()
            if(myLocalSub!=null){
                val updateSub = SubscribeEntity(
                    term = myLocalSub.term+term,
                    date = myLocalSub.date,
                    id = myLocalSub.id,
                    token = token,
                    transactionId = currentTime.datetime + token + term.toString()
                )
                transactionRepo.updateSubscribe(updateSub)
            }
            val saveSubscribeEntity = SubscribeEntity(
                term = term,
                token = token,
                date = Date(),
                transactionId = currentTime.datetime + token + term.toString()
            )
            transactionRepo.insertSubscribe(saveSubscribeEntity)
            offTermAllThemes()
            val subscribe = SubscribeModel(
                dateBuy = currentTime.datetime,
                token = token,
                term = term,
                transactionId = currentTime.datetime + token + term.toString()
            )
            transactionRepo.sendSubscribtionTransaction(subscribe)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            withContext(NonCancellable){
                saveErrorUpdate(subscribe = "obkotkbokb")
            }

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
            withContext(NonCancellable){
                saveErrorUpdate(subscribe = "obkotkbokb")
            }
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            withContext(NonCancellable){
                saveErrorUpdate(subscribe = "obkotkbokb")
            }
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e:TimeoutException){
            withContext(NonCancellable){
                saveErrorUpdate(subscribe = "obkotkbokb")
            }
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            withContext(NonCancellable){
                saveErrorUpdate(subscribe = "obkotkbokb")
            }
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }
    }


    ////Отключает задержку во всех темах
    private suspend fun offTermAllThemes(){
        val allCourses = courseRepo.getAllCourses()
        val userInfo = userLogicRepo.getUserInfoLocal()
        val listUpdateThemes = ArrayList<UpdateThemeModel>()
        val currentTime = courseRepo.getCurrentTime()
        allCourses?.forEach { oneCourse->
            val allCourseTheme = courseRepo.searchThemesWithCourseNumber(oneCourse.courseNumber)
            allCourseTheme?.forEach { oneTheme ->
                    oneTheme.termHourse?.let {
                        val updateThemeEntity = ThemeEntity(
                            uniqueThemeId = oneTheme.uniqueThemeId,
                            themeName = oneTheme.themeName,
                            imageTheme = oneTheme.imageTheme,
                            isDuoInter = oneTheme.isDuoInter,
                            termDateApi = null,
                            termHourse = null,
                            victorineDate = oneTheme.victorineDate,
                            interactiveTestId = oneTheme.interactiveTestId,
                            vicotineTestId = oneTheme.vicotineTestId,
                            interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                            victorineQuestionCount = oneTheme.victorineQuestionCount,
                            victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                            victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                            interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                            interactiveCodeMistakes = oneTheme.interactiveCodeMistakes,
                            isOpen = oneTheme.isOpen,
                            duoDate = oneTheme.duoDate,
                            lessonsCount = oneTheme.lessonsCount,
                            lastCourseTheme = oneTheme.lastCourseTheme,
                            courseNumber = oneTheme.courseNumber,
                            isVictorine = oneTheme.isVictorine,
                            lastUpdateDate = oneTheme.lastUpdateDate,
                            themeNumber = oneTheme.themeNumber,
                            possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                            themePrice = oneTheme.themePrice,
                            isThemePassed = oneTheme.isThemePassed
                        )
                        courseRepo.updateTheme(updateThemeEntity)

//                        val updateThemeModel = UpdateThemeModel(
//                            uniqueThemeId = oneTheme.uniqueThemeId,
//                            themeNumber = oneTheme.themeNumber,
//                            isOpenTheme = oneTheme.isOpen,
//                            courseNumber = oneTheme.courseNumber,
//                            termDateApi = null,
//                            termHourse = null,
//                            victorineId = oneTheme.vicotineTestId,
//                            interactiveId = oneTheme.interactiveTestId,
//                            dateApi = currentTime.datetime,
//                            token = userInfo?.token ?: ""
//                        )
//                        listUpdateThemes.add(updateThemeModel)
                    }


            }

        }

        val updateThemeModel = UpdateThemeModel(
            uniqueThemeId = 0,
            themeNumber =0,
            isOpenTheme = true,
            courseNumber = 0,
            termDateApi = null,
            termHourse = null,
            victorineId = 2,
            interactiveId = 3,
            dateApi = currentTime.datetime,
            token = userInfo?.token ?: "",
            lasThemePassed = false
        )
        val updateAnswerModel = UpdateAnswerModel(
            updateThemes = updateThemeModel,
            token = userInfo?.token ?: "",
            AndropointCount = userInfo?.andropointCount ?: 0)
        courseRepo.sendMyProgress(updateAnswerModel)
//        val updateAnswerModel = UpdateAnswerModel(
//            updateThemes = listUpdateThemes,
//            token = userInfo?.token ?: ""
//        )
//        courseRepo.sendMyProgress(updateAnswerModel)


    }

    suspend fun courseBuy(courseNumber:Int,isSuccess: ((ErrorEnum) -> Unit)){
        try {
            val currentTime = transactionRepo.getCurrentTime()
            val currentCourse = courseRepo.searchCourseWithNumber(courseNumber)
            val myInfo = userLogicRepo.getUserInfoLocal()
            val promoInfoLocal = userLogicRepo.getAllMyPromo()
            isSuccess.invoke(ErrorEnum.SUCCESS)
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
                isSuccess.invoke(ErrorEnum.SUCCESS)
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
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            withContext(NonCancellable){
                saveErrorUpdate(courseNumber = courseNumber)
            }

            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            withContext(NonCancellable){
                saveErrorUpdate(courseNumber = courseNumber)
            }
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            withContext(NonCancellable){
                saveErrorUpdate(courseNumber = courseNumber)
            }
            if(e is CancellationException) throw e

            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }catch (e:TimeoutException){
            withContext(NonCancellable){
                saveErrorUpdate(courseNumber = courseNumber)
            }
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            withContext(NonCancellable){
                saveErrorUpdate(courseNumber = courseNumber)
            }
//            saveErrorUpdate(courseNumber = courseNumber)
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
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



    private suspend fun saveErrorUpdate(subscribe:String?=null,courseNumber: Int?=null,uniqueThemeId: Int?=null){
        courseNumber?.let {courNotnull->
            val updateKeyEntity = UpdatesKeyEntity(
                buyCourseNumber = courNotnull,
                updateTime = Date(),
                buyCourseAndropoint = false
            )
            courseRepo.insertKey(updateKeyEntity)
        }
        subscribe?.let { sub->
            val updateKeyEntity = UpdatesKeyEntity(
                updateTime = Date(),
                subscribeTrasaction = sub
            )
            courseRepo.insertKey(updateKeyEntity)
        }
        uniqueThemeId?.let { themeNotNull->
           val updateKeyEntity = UpdatesKeyEntity(
                buyThemeUniqueId = uniqueThemeId,
                updateTime = Date()
            )
            courseRepo.insertKey(updateKeyEntity)
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



    suspend fun checkSubscribeActual(isSuccess:((ErrorEnum)->Unit),isActual:((Boolean)->Unit)){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal().token
            val currentTime = transactionRepo.getCurrentTime()
            val checkBlank = SendSubscribeCheckModel(
                token = userInfo,
                currentTime.datetime
            )
            val response = transactionRepo.checkMySubscribe(checkBlank)
            isActual.invoke(response.subscribeIsActual)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d(
                "victorineTestResultStateStrikeResultTreaAndropoint",
                e.toString()
            )
            if(checkSubscibe()){
                isActual.invoke(true)
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                isActual.invoke(false)
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            e.printStackTrace()
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d(
                "victorineTestResultStateStrikeResultTreaAndropoint",
                e.toString()
            )
            e.printStackTrace()
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d(
                "victorineTestResultStateStrikeResultTreaAndropoint",
                e.toString()
            )
            e.printStackTrace()
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun checkCourseBuy(token: String, isSuccess: ((ErrorLamdaRes) -> Unit), isBuy:((Boolean)->Unit)){
//        val response = transactionRepo.checkMyBuyCourse(token)
        
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

}