package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.mappers.toCourseBuyEntity
import workwork.test.andropediagits.core.mappers.toCourseEntity
import workwork.test.andropediagits.core.mappers.toLevelEntity
import workwork.test.andropediagits.core.mappers.toSubscribeEntity
import workwork.test.andropediagits.core.mappers.toThemeBuyEntity
import workwork.test.andropediagits.core.mappers.toThemeEntity
import workwork.test.andropediagits.core.mappers.toThemeLevelContentEntity
import workwork.test.andropediagits.core.mappers.toVictorineAnswerVariantEntity
import workwork.test.andropediagits.core.mappers.toVictorineClueEntity
import workwork.test.andropediagits.core.mappers.toVictorineEntity
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.data.remote.model.SendSubscribeCheckModel
import workwork.test.andropediagits.data.remote.model.UserProgressModel
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class CacheUseCase @Inject constructor(private val courseRepo: CourseRepo, private val userLogicRepo: UserLogicRepo, private val transactionRepo: TransactionRepo) {

    suspend fun downloaCourse(isSuccess:((ErrorEnum)->Unit), lang:String?=null,token:String?=null){
        try {
//            val userInfo = userLogicRepo.getUserInfoLocal()
       val currentDate = courseRepo.getCurrentTime()
            if(token!=null){
                val cacheResponse = courseRepo.getAllBackendCourse(token = token, lang ?: "rus")
//            Log.d("djdjd33333222jjd",userInfo.token ?: "nullToken")
                Log.d("djdjd33333222jjd","cacheee: "+cacheResponse.themeLessons?.filter {
                    it.levelName == "Обращение к системным службам через контекст"
                })
//                Log.d("djdjd33333222jjd","cacheee: "+cacheResponse.themeLessons)
                cacheResponse.courses?.forEach { oneCourse->
                    courseRepo.insertCourse(oneCourse.toCourseEntity())
                }
                cacheResponse.themes?.forEach { oneTheme->
                    courseRepo.insertTheme(oneTheme.toThemeEntity())
                }
                cacheResponse.themeLessons?.forEach { oneLesson->
                    courseRepo.insertLevels(oneLesson.toLevelEntity())
                }
                cacheResponse.themeLessonContents?.forEach { oneLessonContent->
                    courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
                }
                cacheResponse.victorines?.forEach { oneVictorine->
                    courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
                }
                cacheResponse.victorinesAnswerVariants?.forEach { oneVictorineAnswer->
                    courseRepo.insertVictorineAnswerVariant(oneVictorineAnswer.toVictorineAnswerVariantEntity())
                }
                cacheResponse.victorineClues?.forEach { oneVictorineClue->
                    courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
                }
//            cacheResponse.interactives?.forEach { oneInteractiveTest->
//                courseRepo.insertInteractiveEntity(oneInteractiveTest.toInteractiveEntity())
//            }
//            cacheResponse.interactiveCodeVariants?.forEach { oneInteractiveVariant->
//                courseRepo.insertInteractiveCodeVariant(oneInteractiveVariant.toInteractiveCodeVariantEntity())
//            }
//            cacheResponse.interactiveCorrectAnswers?.forEach { oneInteractiveCorrectAnswer->
//                courseRepo.insertInteractiveCodeCorrectAnswer(oneInteractiveCorrectAnswer.toInteractiveCorrectCodeEntity())
//            }
                updateUserProgress(cacheResponse.userProgress)
                val myBuyCourses = transactionRepo.checkMyBuyCourse(token ?: "")
                if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                    myBuyCourses.forEach { buyCourse->
                        Log.d("vklerfnvlnefwtrnvbIKQewvogiNqw","lastOpenCourse:${cacheResponse.userProgress.lastOpenCourse} buyCourse:${buyCourse.courseNumber} andropointBuy:${buyCourse.andropointBuy} lasOpenTheme:${cacheResponse.userProgress.lastOpenTheme}")
                        if (buyCourse.andropointBuy==0){

                            openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                        }
                    }
                }
                val buyThemes = transactionRepo.checkUserBuyTheme(token ?: "")
                if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                    buyThemes.forEach { oneBuyTheme ->
                        openBuyThemes(oneBuyTheme.uniqueThemeId)
                    }
                }

                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            val userInfoLocal = userLogicRepo.getUserInfoLocal()
            val userInfo = userLogicRepo.getCurrentUserInfo(userInfoLocal.token)
            val isInfinitys = userInfo.isInfinity == 1

            val updateUserLocalInf = UserInfoEntity(
                name = userInfo.name ?: "DefaultName",
                token = userInfoLocal.token,
                image = userInfo.image,
                lastOpenTheme = userInfo.lastThemeNumber ?: 0,
                lastOpenCourse = userInfo.lastCourseNumber ?: 0,
                lastOnlineDate = currentDate.datetime,
                userLanguage = lang,
                andropointCount = userInfo.andropointCount ?: 0,
                strikeModeDay = userInfo.strikeModeDay ?: 0,
                isInfinity = isInfinitys,
            )
            userLogicRepo.updateUserInfoLocal(updateUserLocalInf)




            val cacheResponse = courseRepo.getAllBackendCourse(token = userInfoLocal.token, lang ?: "rus")
//            Log.d("djdjd33333222jjd",userInfo.token ?: "nullToken")
            Log.d("djdjd33333222jjd","cacheee: "+cacheResponse)
            cacheResponse.courses?.forEach { oneCourse->
                courseRepo.insertCourse(oneCourse.toCourseEntity())
            }
            cacheResponse.themes?.forEach { oneTheme->
                courseRepo.insertTheme(oneTheme.toThemeEntity())
            }
            cacheResponse.themeLessons?.forEach { oneLesson->
                courseRepo.insertLevels(oneLesson.toLevelEntity())
            }
            cacheResponse.themeLessonContents?.forEach { oneLessonContent->
                courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
            }
            cacheResponse.victorines?.forEach { oneVictorine->
                courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
            }
            cacheResponse.victorinesAnswerVariants?.forEach { oneVictorineAnswer->
                courseRepo.insertVictorineAnswerVariant(oneVictorineAnswer.toVictorineAnswerVariantEntity())
            }
            cacheResponse.victorineClues?.forEach { oneVictorineClue->
                courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
            }
//            cacheResponse.interactives?.forEach { oneInteractiveTest->
//                courseRepo.insertInteractiveEntity(oneInteractiveTest.toInteractiveEntity())
//            }
//            cacheResponse.interactiveCodeVariants?.forEach { oneInteractiveVariant->
//                courseRepo.insertInteractiveCodeVariant(oneInteractiveVariant.toInteractiveCodeVariantEntity())
//            }
//            cacheResponse.interactiveCorrectAnswers?.forEach { oneInteractiveCorrectAnswer->
//                courseRepo.insertInteractiveCodeCorrectAnswer(oneInteractiveCorrectAnswer.toInteractiveCorrectCodeEntity())
//            }
            updateUserProgress(cacheResponse.userProgress)
            val myBuyCourses = transactionRepo.checkMyBuyCourse(userInfoLocal?.token ?: "")
            if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                myBuyCourses.forEach { buyCourse->
                    Log.d("vklerfnvlnefwtrnvbIKQewvogiNqw","lastOpenCourse:${cacheResponse.userProgress.lastOpenCourse} buyCourse:${buyCourse.courseNumber} andropointBuy:${buyCourse.andropointBuy} lasOpenTheme:${cacheResponse.userProgress.lastOpenTheme}")
                    if (buyCourse.andropointBuy==0){

                            openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                    }
                }
            }
            val buyThemes = transactionRepo.checkUserBuyTheme(userInfoLocal?.token ?: "")
            if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                buyThemes.forEach { oneBuyTheme ->
                    openBuyThemes(oneBuyTheme.uniqueThemeId)
                }
            }

            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("stategyrfgyrefguregu3",e.toString())
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("stategyrfgyrefguregu3",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            Log.d("stategyrfgyrefguregu3",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("stategyrfgyrefguregu3",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun openFirstThemeCourse(lastOpenCourse: Int?) {
        val allThemesCourse = courseRepo.searchThemesWithCourseNumber(lastOpenCourse ?: 1)
        val n = allThemesCourse.minByOrNull { it.themeNumber }
        if(n!=null) {
            val updateThemeEntity = ThemeEntity(
                uniqueThemeId = n.uniqueThemeId,
                lastUpdateDate = Date(),
                themeName = n.themeName,
                courseNumber = n.courseNumber,
                themeNumber = n.themeNumber,
                interactiveCodeMistakes = n.interactiveCodeMistakes,
                interactiveCodeCorrect = n.interactiveCodeCorrect,
                victorineMistakeAnswer = n.victorineMistakeAnswer,
                victorineCorrectAnswer = n.victorineCorrectAnswer,
                victorineDate = n.victorineDate,
                interactiveTestId = n.interactiveTestId,
                interactiveQuestionCount = n.interactiveQuestionCount,
                victorineQuestionCount = n.victorineQuestionCount,
                vicotineTestId = n.vicotineTestId,
                duoDate = n.duoDate,
                isDuoInter = n.isDuoInter,
                isVictorine = n.isVictorine,
                isOpen = true,
                termHourse = n.termHourse,
                termDateApi = n.termDateApi,
                imageTheme = n.imageTheme,
                lessonsCount = n.lessonsCount,
                lastCourseTheme = n.lastCourseTheme,
                isFav = n.isFav,
                isThemePassed = n.isThemePassed,
                possibleToOpenThemeFree = n.possibleToOpenThemeFree,
                themePrice = n.themePrice
            )
            courseRepo.updateTheme(updateThemeEntity)
        }
    }

    suspend fun downloadUpdateLang(isSuccess:((ErrorEnum)->Unit)){
        try {
            courseRepo.deleteAllCourse()
            courseRepo.deleteAllThemes()
            courseRepo.deleteAllLevels()
            courseRepo.deleteAllLevelsContent()
            courseRepo.deleteAllVictorines()
            courseRepo.deleteAllVictorineVariants()
            courseRepo.deleteAllInteractiveTasks()
            courseRepo.deleteAllInteractiveCodeVariants()
            courseRepo.deleteAllInteractiveCorrectAnswers()
            courseRepo.deleteAllVictorineClue()
            val userInfo = userLogicRepo.getUserInfoLocal()
            val cacheResponse = courseRepo.getAllBackendCourse(token = userInfo?.token ?: "",userInfo?.userLanguage ?: "")
            cacheResponse.courses?.forEach { oneCourse->
                courseRepo.insertCourse(oneCourse.toCourseEntity())
            }
            cacheResponse.themes?.forEach { oneTheme->
                courseRepo.insertTheme(oneTheme.toThemeEntity())
            }
            cacheResponse.themeLessons?.forEach { oneLesson->
                courseRepo.insertLevels(oneLesson.toLevelEntity())
            }
            cacheResponse.themeLessonContents?.forEach { oneLessonContent->
                courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
            }
            cacheResponse.victorines?.forEach { oneVictorine->
                courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
            }
            cacheResponse.victorinesAnswerVariants?.forEach { oneVictorineAnswer->
                courseRepo.insertVictorineAnswerVariant(oneVictorineAnswer.toVictorineAnswerVariantEntity())
            }
            cacheResponse.victorineClues?.forEach { oneVictorineClue->
                courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
            }
//            cacheResponse.interactives?.forEach { oneInteractiveTest->
//                courseRepo.insertInteractiveEntity(oneInteractiveTest.toInteractiveEntity())
//            }
//            cacheResponse.interactiveCodeVariants?.forEach { oneInteractiveVariant->
//                courseRepo.insertInteractiveCodeVariant(oneInteractiveVariant.toInteractiveCodeVariantEntity())
//            }
//            cacheResponse.interactiveCorrectAnswers?.forEach { oneInteractiveCorrectAnswer->
//                courseRepo.insertInteractiveCodeCorrectAnswer(oneInteractiveCorrectAnswer.toInteractiveCorrectCodeEntity())
//            }
            updateUserProgress(cacheResponse.userProgress)
            val myBuyCourses = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
            if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                myBuyCourses.forEach { buyCourse->
                    if (buyCourse.andropointBuy==0){

                            openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                    }
                }
            }
            val buyThemes = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
            if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                buyThemes.forEach { oneBuyTheme ->
                    openBuyThemes(oneBuyTheme.uniqueThemeId)
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkSubscibe()){
                Log.d("sppspsp","e.toString()")
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }else{
                isSuccess.invoke(ErrorEnum.NOTNETWORK)
            }
            Log.d("downloadLanfintitoibnoyt",e.toString())
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            Log.d("downloadLanfintitoibnoyt",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            Log.d("downloadLanfintitoibnoyt",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("downloadLanfintitoibnoyt",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun checkCache(isSuccess:((ErrorEnum)->Unit)){
        try {
            val myInfo = userLogicRepo.getUserInfoLocal()
            if(myInfo!=null){
            val notNullCourses = courseRepo.getAllCourses()
            val getAllActualDateBack = courseRepo.getAllCoursesAndThemesUpdateDate()
            if(notNullCourses!=null){
                notNullCourses.forEach { courseLocal ->
                    getAllActualDateBack.lastDatesCourses.forEach { lastUpdateCourse ->
                        if (courseLocal.courseNumber == lastUpdateCourse.courseNumber) {
                            if (courseLocal.lastUpdateDate.time > lastUpdateCourse.lastUpdateDate.time) {
                                courseRepo.deleteAllCourse()
                                courseRepo.deleteAllThemes()
                                courseRepo.deleteAllLevels()
                                courseRepo.deleteAllLevelsContent()
                                courseRepo.deleteAllVictorines()
                                courseRepo.deleteAllVictorineVariants()
                                courseRepo.deleteAllInteractiveTasks()
                                courseRepo.deleteAllInteractiveCodeVariants()
                                courseRepo.deleteAllInteractiveCorrectAnswers()
                                courseRepo.deleteAllVictorineClue()
                               val cacheResponse = courseRepo.getAllBackendCourse(token = myInfo.token ?: "", userLanguage = myInfo.userLanguage ?: "eng")
                                cacheResponse.courses.forEach { oneCourse->
                                    courseRepo.insertCourse(oneCourse.toCourseEntity())
                                }
                                cacheResponse.themeLessons.forEach { oneThemeLesson->
                                    courseRepo.insertLevels(oneThemeLesson.toLevelEntity())
                                }
                                cacheResponse.themes.forEach { oneTheme->
                                    courseRepo.insertTheme(oneTheme.toThemeEntity())
                                }
                                cacheResponse.themeLessonContents.forEach { oneLessonContent->
                                    courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
                                }
                                cacheResponse.victorines.forEach { oneVictorine->
                                    courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
                                }
                                cacheResponse.victorineClues?.forEach { oneVictorineClue->
                                    courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
                                }
                                cacheResponse.victorinesAnswerVariants.forEach { oneVictorineAnswerVatiant->
                                    courseRepo.insertVictorineAnswerVariant(oneVictorineAnswerVatiant.toVictorineAnswerVariantEntity())
                                }

                                updateUserProgress(cacheResponse.userProgress)
                                val myBuyCourses = transactionRepo.checkMyBuyCourse(myInfo?.token ?: "")
                                if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                                    myBuyCourses.forEach { buyCourse->
                                        if (buyCourse.andropointBuy==0){

                                                openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                                        }
                                    }
                                }
                                val buyThemes = transactionRepo.checkUserBuyTheme(myInfo?.token ?: "")
                                if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                                    buyThemes.forEach { oneBuyTheme ->
                                        openBuyThemes(oneBuyTheme.uniqueThemeId)
                                    }
                                }
                            }
                        }
                    }
                }

                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
                val cacheResponse = courseRepo.getAllBackendCourse(token = myInfo.token ?: "", userLanguage = myInfo.userLanguage ?: "eng")
                cacheResponse.courses.forEach { oneCourse->
                    courseRepo.insertCourse(oneCourse.toCourseEntity())
                }
                cacheResponse.themeLessons.forEach { oneThemeLesson->
                    courseRepo.insertLevels(oneThemeLesson.toLevelEntity())
                }
                cacheResponse.themes.forEach { oneTheme->
                    courseRepo.insertTheme(oneTheme.toThemeEntity())
                }
                cacheResponse.themeLessonContents.forEach { oneLessonContent->
                    courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
                }
                cacheResponse.victorines.forEach { oneVictorine->
                    courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
                }
                cacheResponse.victorineClues?.forEach { oneVictorineClue->
                    courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
                }
                cacheResponse.victorinesAnswerVariants.forEach { oneVictorineAnswerVatiant->
                    courseRepo.insertVictorineAnswerVariant(oneVictorineAnswerVatiant.toVictorineAnswerVariantEntity())
                }
//                updateUserProgress(cacheResponse.userProgress)
                updateUserProgress(cacheResponse.userProgress)
                val myBuyCourses = transactionRepo.checkMyBuyCourse(myInfo?.token ?: "")
                if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                    myBuyCourses.forEach { buyCourse->
                        if (buyCourse.andropointBuy==0){

                                openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                        }
                    }
                }
                val buyThemes = transactionRepo.checkUserBuyTheme(myInfo?.token ?: "")
                if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                    buyThemes.forEach { oneBuyTheme ->
                        openBuyThemes(oneBuyTheme.uniqueThemeId)
                    }
                }
                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
        }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkSubscibe()){
                Log.d("sppspsp","e.toString()")
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }else{
                isSuccess.invoke(ErrorEnum.NOTNETWORK)
            }
            Log.d("sppspsp",e.toString())

        }catch (e:HttpException){
            Log.d("sppspsp",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            Log.d("sppspsp",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("sppspsp",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun checkSubscribesAndBuyCourse(isSuccess:((ErrorEnum)->Unit)){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            val mySubsLocal = transactionRepo.getSubscribe()
            val myCourseBuyLocal = transactionRepo.getAllMyCourseBuy()
            val myThemeBuyLocal = transactionRepo.getAllBuyThemes()
            val currentDate = transactionRepo.getCurrentTime()
            if(userInfo!=null) {
                if (mySubsLocal == null) {
                    val mySubscribe = transactionRepo.getMySubscribe(userInfo?.token ?: "")
                    if (mySubscribe.codeAnswer != 707) {
                        transactionRepo.insertSubscribe(mySubscribe.toSubscribeEntity())
                    }
                } else {
                    val sendSubscribeCheckModel = SendSubscribeCheckModel(
                        token = userInfo?.token ?: "",
                        currentDate = currentDate.datetime
                    )
                    val checkSubscribes = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                    if (!checkSubscribes.subscribeIsActual) {
                        transactionRepo.deleteSubscribe(mySubsLocal)
                    }
                }
                if (myCourseBuyLocal.isNullOrEmpty()) {
                    val courseBuyMy = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
                    courseBuyMy.forEach { courseBuy ->
                        if (courseBuy.codeAnswer != 707) {
                            transactionRepo.insertCourseBuy(courseBuy.toCourseBuyEntity())
                        }
                    }
                }
                if (myThemeBuyLocal.isNullOrEmpty()) {
                    val themeBuyMy = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
                    themeBuyMy.forEach { themeBuyOne ->
                        if (themeBuyOne.codeAnswer != 707) {
                            transactionRepo.insertBuyTheme(themeBuyOne.toThemeBuyEntity())
                        }
                    }
                }
                isSuccess.invoke(ErrorEnum.SUCCESS)
                return
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }else{
                Log.d("Splashss",e.toString())
                isSuccess.invoke(ErrorEnum.NOTNETWORK)
            }

        }catch (e:HttpException){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:TimeoutException){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            Log.d("Splashss",e.toString())
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }




    suspend fun checkSubscribesAndBuyCourseFromWorkmanager(){

            val userInfo = userLogicRepo.getUserInfoLocal()
            val mySubsLocal = transactionRepo.getSubscribe()
            val myCourseBuyLocal = transactionRepo.getAllMyCourseBuy()
            val myThemeBuyLocal = transactionRepo.getAllBuyThemes()
            val currentDate = transactionRepo.getCurrentTime()
            if (mySubsLocal==null){
                val mySubscribe = transactionRepo.getMySubscribe(userInfo?.token ?: "")
                if (mySubscribe.codeAnswer==758){
                    transactionRepo.insertSubscribe(mySubscribe.toSubscribeEntity())
                }
            }else{
                val sendSubscribeCheckModel = SendSubscribeCheckModel(
                    token = userInfo?.token ?: "",
                    currentDate = currentDate.datetime
                )
                val checkSubscribes = transactionRepo.checkMySubscribe(sendSubscribeCheckModel)
                if (!checkSubscribes.subscribeIsActual){
                    transactionRepo.deleteSubscribe(mySubsLocal)
                }
            }
            if (myCourseBuyLocal.isNullOrEmpty()){
                val courseBuyMy = transactionRepo.checkMyBuyCourse(userInfo?.token ?: "")
                courseBuyMy.forEach { courseBuy->
                    if (courseBuy.codeAnswer==867){
                        transactionRepo.insertCourseBuy(courseBuy.toCourseBuyEntity())
                    }
                }
            }
        if(myThemeBuyLocal.isNullOrEmpty()){
            val themeBuyMy = transactionRepo.checkUserBuyTheme(userInfo?.token ?: "")
            themeBuyMy.forEach { themeBuyOne->
                if(themeBuyOne.codeAnswer!=707){
                    transactionRepo.insertBuyTheme(themeBuyOne.toThemeBuyEntity())
                }
            }
        }
    }



    suspend fun checkCacheWorkManager(){
        val userInfoLocal = userLogicRepo.getUserInfoLocal()
        userInfoLocal?.let { myInfo ->
            val allCourseLocal = courseRepo.getAllCourses()
            val getAllActualDateBack = courseRepo.getAllCoursesAndThemesUpdateDate()
            allCourseLocal?.let { notNullCourses ->
                notNullCourses.forEach { courseLocal ->
                    getAllActualDateBack.lastDatesCourses.forEach { lastUpdateCourse ->
                        if (courseLocal.courseNumber == lastUpdateCourse.courseNumber) {
                            if (courseLocal.lastUpdateDate.time > lastUpdateCourse.lastUpdateDate.time) {
                                courseRepo.deleteAllCourse()
                                courseRepo.deleteAllThemes()
                                courseRepo.deleteAllLevels()
                                courseRepo.deleteAllLevelsContent()
                                courseRepo.deleteAllVictorines()
                                courseRepo.deleteAllVictorineVariants()
                                courseRepo.deleteAllInteractiveTasks()
                                courseRepo.deleteAllInteractiveCodeVariants()
                                courseRepo.deleteAllInteractiveCorrectAnswers()
                                courseRepo.deleteAllVictorineClue()
                                val cacheResponse = courseRepo.getAllBackendCourse(token = myInfo.token ?: "", userLanguage = myInfo.userLanguage ?: "eng")
                                cacheResponse.courses.forEach { oneCourse->
                                    courseRepo.insertCourse(oneCourse.toCourseEntity())
                                }
                                cacheResponse.themeLessons.forEach { oneThemeLesson->
                                    courseRepo.insertLevels(oneThemeLesson.toLevelEntity())
                                }
                                cacheResponse.themes.forEach { oneTheme->
                                    courseRepo.insertTheme(oneTheme.toThemeEntity())
                                }
                                cacheResponse.themeLessonContents.forEach { oneLessonContent->
                                    courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
                                }
                                cacheResponse.victorines.forEach { oneVictorine->
                                    courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
                                }
                                cacheResponse.victorineClues?.forEach { oneVictorineClue->
                                    courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
                                }
                                cacheResponse.victorinesAnswerVariants.forEach { oneVictorineAnswerVatiant->
                                    courseRepo.insertVictorineAnswerVariant(oneVictorineAnswerVatiant.toVictorineAnswerVariantEntity())
                                }
                                updateUserProgress(cacheResponse.userProgress)
                                val myBuyCourses = transactionRepo.checkMyBuyCourse(userInfoLocal?.token ?: "")
                                if(!myBuyCourses.isNullOrEmpty()&&myBuyCourses[0].codeAnswer != 707){
                                    myBuyCourses.forEach { buyCourse->
                                        if (buyCourse.andropointBuy==0){

                                                openAllThemesBuyCourse(buyCourse.courseNumber, lastThemeNumber = cacheResponse.userProgress.lastOpenTheme ?: 1)

                                        }

                                    }
                                }
                                val buyThemes = transactionRepo.checkUserBuyTheme(userInfoLocal?.token ?: "")
                                if(buyThemes[0].codeAnswer!=707&&!buyThemes.isNullOrEmpty()){
                                    buyThemes.forEach { oneBuyTheme ->
                                        openBuyThemes(oneBuyTheme.uniqueThemeId)
                                    }
                                }
                            }
                        }
                    }
                }
                return
            }
            val cacheResponse = courseRepo.getAllBackendCourse(token = myInfo.token ?: "", userLanguage = myInfo.userLanguage ?: "eng")
            cacheResponse.courses.forEach { oneCourse->
                courseRepo.insertCourse(oneCourse.toCourseEntity())
            }
            cacheResponse.themeLessons.forEach { oneThemeLesson->
                courseRepo.insertLevels(oneThemeLesson.toLevelEntity())
            }
            cacheResponse.themes.forEach { oneTheme->
                courseRepo.insertTheme(oneTheme.toThemeEntity())
            }
            cacheResponse.themeLessonContents.forEach { oneLessonContent->
                courseRepo.insertLevelsContents(oneLessonContent.toThemeLevelContentEntity())
            }
            cacheResponse.victorines.forEach { oneVictorine->
                courseRepo.insertVictorine(oneVictorine.toVictorineEntity())
            }
            cacheResponse.victorineClues?.forEach { oneVictorineClue->
                courseRepo.insertVictorineClue(oneVictorineClue.toVictorineClueEntity())
            }
            cacheResponse.victorinesAnswerVariants.forEach { oneVictorineAnswerVatiant->
                courseRepo.insertVictorineAnswerVariant(oneVictorineAnswerVatiant.toVictorineAnswerVariantEntity())
            }
            updateUserProgress(cacheResponse.userProgress)
        }
    }

    private suspend fun updateUserProgress(userProgressModel: UserProgressModel){
        val allCourses = courseRepo.getAllCourses()
        val allThemes = courseRepo.getAllThemes()
        if(userProgressModel.termAds!=null){
            val adsEntity = AdsEntity(
                dateFirstViewAds = userProgressModel.termAds,
                isTerm = true,
                viewAdsCount = 0
            )
            userLogicRepo.insertAdsTerm(adsEntity)
        }
        allCourses?.forEach { oneCourse->
            if((userProgressModel.lastOpenCourse ?: 0) >= oneCourse.courseNumber){
                val updateCourseEntity = CourseEntity(
                    courseNumber = oneCourse.courseNumber,
                    courseName = oneCourse.courseName,
                    isNetworkConnect = oneCourse.isNetworkConnect,
                    isOpen = true,
                    possibleToOpenCourseFree = oneCourse.possibleToOpenCourseFree,
                    description = oneCourse.description,
                    lastUpdateDate = oneCourse.lastUpdateDate,
                )
                courseRepo.updateCourse(updateCourseEntity)
            }
        }
        allThemes?.forEach { oneTheme->
            Log.d("forkfrofkorfkr","lastThemeNumber:${userProgressModel.lastOpenTheme} lastCourseNumber:${userProgressModel.lastOpenCourse} termApiDate:${userProgressModel.termDateApi} terHourse:${userProgressModel.termHourse}")

            if((userProgressModel?.lastOpenCourse ?: 0) > oneTheme.courseNumber ){
                Log.d("forkfrofkorfkr","oneTheme.courseNumber>userProgressModel?.lastOpenCourse ?: 0")
                val updateTheme = ThemeEntity(
                    uniqueThemeId = oneTheme.uniqueThemeId,
                    lastUpdateDate = Date(),
                    themeName = oneTheme.themeName,
                    courseNumber = oneTheme.courseNumber,
                    themeNumber = oneTheme.themeNumber,
                    interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                    interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                    victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                    victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                    victorineDate = oneTheme.victorineDate,
                    interactiveTestId = oneTheme.interactiveTestId,
                    interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                    victorineQuestionCount = oneTheme.victorineQuestionCount,
                    vicotineTestId = oneTheme.vicotineTestId,
                    duoDate = oneTheme.duoDate,
                    isDuoInter = oneTheme.isDuoInter,
                    isVictorine = oneTheme.isVictorine,
                    isOpen = true,
                    termHourse = oneTheme.termHourse,
                    lessonsCount = oneTheme.lessonsCount,
                    imageTheme = oneTheme.imageTheme,
                    termDateApi = oneTheme.termDateApi,
                    lastCourseTheme = oneTheme.lastCourseTheme,
                    possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                    isFav = oneTheme.isFav,
                    isThemePassed = true,
                    themePrice = oneTheme.themePrice
                )
                courseRepo.updateTheme(updateTheme)
            } else if(oneTheme.courseNumber==userProgressModel?.lastOpenCourse){
                if((userProgressModel.termHourse ?: 0) != 0 && (userProgressModel.termDateApi) != null && oneTheme.themeNumber == userProgressModel.lastOpenTheme){
                    if(userProgressModel.lastCourseThemePasses==1&&oneTheme.lastCourseTheme){
                        val updateTheme = ThemeEntity(
                            uniqueThemeId = oneTheme.uniqueThemeId,
                            lastUpdateDate = Date(),
                            themeName = oneTheme.themeName,
                            courseNumber = oneTheme.courseNumber,
                            themeNumber = oneTheme.themeNumber,
                            interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                            interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                            victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                            victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                            victorineDate = oneTheme.victorineDate,
                            interactiveTestId = oneTheme.interactiveTestId,
                            interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                            victorineQuestionCount = oneTheme.victorineQuestionCount,
                            vicotineTestId = oneTheme.vicotineTestId,
                            duoDate = oneTheme.duoDate,
                            isDuoInter = oneTheme.isDuoInter,
                            isVictorine = oneTheme.isVictorine,
                            isOpen = true,
                            termHourse = userProgressModel.termHourse,
                            lessonsCount = oneTheme.lessonsCount,
                            imageTheme = oneTheme.imageTheme,
                            termDateApi = userProgressModel.termDateApi,
                            lastCourseTheme = oneTheme.lastCourseTheme,
                            possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                            isFav = oneTheme.isFav,
                            isThemePassed = true,
                            themePrice = oneTheme.themePrice
                        )
                        courseRepo.updateTheme(updateTheme)
                        return
                    }
                    Log.d("forkfrofkorfkr","(userProgressModel.termHourse ?: 0) != 0 && (userProgressModel.termHourse) != null && oneTheme.themeNumber == userProgressModel.lastOpenTheme")
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = oneTheme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = oneTheme.themeName,
                        courseNumber = oneTheme.courseNumber,
                        themeNumber = oneTheme.themeNumber,
                        interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                        interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                        victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                        victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                        victorineDate = oneTheme.victorineDate,
                        interactiveTestId = oneTheme.interactiveTestId,
                        interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                        victorineQuestionCount = oneTheme.victorineQuestionCount,
                        vicotineTestId = oneTheme.vicotineTestId,
                        duoDate = oneTheme.duoDate,
                        isDuoInter = oneTheme.isDuoInter,
                        isVictorine = oneTheme.isVictorine,
                        isOpen = true,
                        termHourse = userProgressModel.termHourse,
                        lessonsCount = oneTheme.lessonsCount,
                        imageTheme = oneTheme.imageTheme,
                        termDateApi = userProgressModel.termDateApi,
                        lastCourseTheme = oneTheme.lastCourseTheme,
                        possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                        isFav = oneTheme.isFav,
                        isThemePassed = oneTheme.isThemePassed,
                        themePrice = oneTheme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                    return
                }


                Log.d("forkfrofkorfkr","oneTheme.courseNumber==userProgressModel?.lastOpenCourse")
                            if((userProgressModel?.lastOpenTheme
                                    ?: 0) > oneTheme.themeNumber
                            ){
                                Log.d("forkfrofkorfkr","(userProgressModel?.lastOpenTheme\n" +
                                        "                                    ?: 0) > oneTheme.themeNumber")
                                val updateTheme = ThemeEntity(
                                    uniqueThemeId = oneTheme.uniqueThemeId,
                                    lastUpdateDate = Date(),
                                    themeName = oneTheme.themeName,
                                    courseNumber = oneTheme.courseNumber,
                                    themeNumber = oneTheme.themeNumber,
                                    interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                                    interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                                    victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                                    victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                                    victorineDate = oneTheme.victorineDate,
                                    interactiveTestId = oneTheme.interactiveTestId,
                                    interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                                    victorineQuestionCount = oneTheme.victorineQuestionCount,
                                    vicotineTestId = oneTheme.vicotineTestId,
                                    duoDate = oneTheme.duoDate,
                                    isDuoInter = oneTheme.isDuoInter,
                                    isVictorine = oneTheme.isVictorine,
                                    isOpen = true,
                                    termHourse = oneTheme.termHourse,
                                    lessonsCount = oneTheme.lessonsCount,
                                    imageTheme = oneTheme.imageTheme,
                                    termDateApi = oneTheme.termDateApi,
                                    lastCourseTheme = oneTheme.lastCourseTheme,
                                    possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                                    isFav = oneTheme.isFav,
                                    isThemePassed = true,
                                    themePrice = oneTheme.themePrice
                                )
                                courseRepo.updateTheme(updateTheme)
                            } else if((userProgressModel?.lastOpenTheme
                                    ?: 0) == oneTheme.themeNumber ){
                                Log.d("forkfrofkorfkr","(userProgressModel?.lastOpenTheme\n" +
                                        "                                    ?: 0) == oneTheme.themeNumber")


                                if(userProgressModel.lastCourseThemePasses==1){
                                    val updateTheme = ThemeEntity(
                                        uniqueThemeId = oneTheme.uniqueThemeId,
                                        lastUpdateDate = Date(),
                                        themeName = oneTheme.themeName,
                                        courseNumber = oneTheme.courseNumber,
                                        themeNumber = oneTheme.themeNumber,
                                        interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                                        interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                                        victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                                        victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                                        victorineDate = oneTheme.victorineDate,
                                        interactiveTestId = oneTheme.interactiveTestId,
                                        interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                                        victorineQuestionCount = oneTheme.victorineQuestionCount,
                                        vicotineTestId = oneTheme.vicotineTestId,
                                        duoDate = oneTheme.duoDate,
                                        isDuoInter = oneTheme.isDuoInter,
                                        isVictorine = oneTheme.isVictorine,
                                        isOpen = true,
                                        termHourse = oneTheme.termHourse,
                                        lessonsCount = oneTheme.lessonsCount,
                                        imageTheme = oneTheme.imageTheme,
                                        termDateApi = oneTheme.termDateApi,
                                        lastCourseTheme = oneTheme.lastCourseTheme,
                                        possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                                        isFav = oneTheme.isFav,
                                        isThemePassed = true,
                                        themePrice = oneTheme.themePrice
                                    )
                                    courseRepo.updateTheme(updateTheme)
                                    return
                                }

                                val updateTheme = ThemeEntity(
                                    uniqueThemeId = oneTheme.uniqueThemeId,
                                    lastUpdateDate = Date(),
                                    themeName = oneTheme.themeName,
                                    courseNumber = oneTheme.courseNumber,
                                    themeNumber = oneTheme.themeNumber,
                                    interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                                    interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                                    victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                                    victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                                    victorineDate = oneTheme.victorineDate,
                                    interactiveTestId = oneTheme.interactiveTestId,
                                    interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                                    victorineQuestionCount = oneTheme.victorineQuestionCount,
                                    vicotineTestId = oneTheme.vicotineTestId,
                                    duoDate = oneTheme.duoDate,
                                    isDuoInter = oneTheme.isDuoInter,
                                    isVictorine = oneTheme.isVictorine,
                                    isOpen = true,
                                    termHourse = oneTheme.termHourse,
                                    lessonsCount = oneTheme.lessonsCount,
                                    imageTheme = oneTheme.imageTheme,
                                    termDateApi = oneTheme.termDateApi,
                                    lastCourseTheme = oneTheme.lastCourseTheme,
                                    possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                                    isFav = oneTheme.isFav,
                                    isThemePassed = false,
                                    themePrice = oneTheme.themePrice
                                )
                                courseRepo.updateTheme(updateTheme)
                            }


            }

        }
    }

    private suspend fun openBuyThemes(uniqueId:Int){
        val oneTheme = courseRepo.searchThemeWithUniwueId(uniqueId)
        val updateTheme = ThemeEntity(
            uniqueThemeId = oneTheme.uniqueThemeId,
            lastUpdateDate = Date(),
            themeName = oneTheme.themeName,
            courseNumber = oneTheme.courseNumber,
            themeNumber = oneTheme.themeNumber,
            interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
            interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
            victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
            victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
            victorineDate = oneTheme.victorineDate,
            interactiveTestId = oneTheme.interactiveTestId,
            interactiveQuestionCount = oneTheme.interactiveQuestionCount,
            victorineQuestionCount = oneTheme.victorineQuestionCount,
            vicotineTestId = oneTheme.vicotineTestId,
            duoDate = oneTheme.duoDate,
            isDuoInter = oneTheme.isDuoInter,
            isVictorine = oneTheme.isVictorine,
            isOpen = true,
            termHourse = oneTheme.termHourse,
            lessonsCount = oneTheme.lessonsCount,
            imageTheme = oneTheme.imageTheme,
            termDateApi = oneTheme.termDateApi,
            lastCourseTheme = oneTheme.lastCourseTheme,
            possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
            isFav = oneTheme.isFav,
            isThemePassed = oneTheme.isThemePassed,
            themePrice = oneTheme.themePrice
        )
        courseRepo.updateTheme(updateTheme)
    }
///
    private suspend fun openAllThemesBuyCourse(courseNumber:Int,lastThemeNumber:Int){

            val allThemes = courseRepo.searchThemesWithCourseNumber(courseNumber)
            allThemes.forEach { oneTheme->
                if(oneTheme.themeNumber<lastThemeNumber){
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = oneTheme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = oneTheme.themeName,
                        courseNumber = oneTheme.courseNumber,
                        themeNumber = oneTheme.themeNumber,
                        interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                        interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                        victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                        victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                        victorineDate = oneTheme.victorineDate,
                        interactiveTestId = oneTheme.interactiveTestId,
                        interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                        victorineQuestionCount = oneTheme.victorineQuestionCount,
                        vicotineTestId = oneTheme.vicotineTestId,
                        duoDate = oneTheme.duoDate,
                        isDuoInter = oneTheme.isDuoInter,
                        isVictorine = oneTheme.isVictorine,
                        isOpen = true,
                        termHourse = oneTheme.termHourse,
                        lessonsCount = oneTheme.lessonsCount,
                        imageTheme = oneTheme.imageTheme,
                        termDateApi = oneTheme.termDateApi,
                        lastCourseTheme = oneTheme.lastCourseTheme,
                        possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                        isFav = oneTheme.isFav,
                        isThemePassed = true,
                        themePrice = oneTheme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }else {
                    val updateTheme = ThemeEntity(
                        uniqueThemeId = oneTheme.uniqueThemeId,
                        lastUpdateDate = Date(),
                        themeName = oneTheme.themeName,
                        courseNumber = oneTheme.courseNumber,
                        themeNumber = oneTheme.themeNumber,
                        interactiveCodeMistakes =oneTheme.interactiveCodeMistakes,
                        interactiveCodeCorrect = oneTheme.interactiveCodeCorrect,
                        victorineMistakeAnswer = oneTheme.victorineMistakeAnswer,
                        victorineCorrectAnswer = oneTheme.victorineCorrectAnswer,
                        victorineDate = oneTheme.victorineDate,
                        interactiveTestId = oneTheme.interactiveTestId,
                        interactiveQuestionCount = oneTheme.interactiveQuestionCount,
                        victorineQuestionCount = oneTheme.victorineQuestionCount,
                        vicotineTestId = oneTheme.vicotineTestId,
                        duoDate = oneTheme.duoDate,
                        isDuoInter = oneTheme.isDuoInter,
                        isVictorine = oneTheme.isVictorine,
                        isOpen = true,
                        termHourse = oneTheme.termHourse,
                        lessonsCount = oneTheme.lessonsCount,
                        imageTheme = oneTheme.imageTheme,
                        termDateApi = oneTheme.termDateApi,
                        lastCourseTheme = oneTheme.lastCourseTheme,
                        possibleToOpenThemeFree = oneTheme.possibleToOpenThemeFree,
                        isFav = oneTheme.isFav,
                        isThemePassed = false,
                        themePrice = oneTheme.themePrice
                    )
                    courseRepo.updateTheme(updateTheme)
                }

            }
        }







    @SuppressLint("SuspiciousIndentation")
    private suspend fun checkSubscibe():Boolean{
        val sub = transactionRepo.getSubscribe()

            if(sub!=null){
                val currentDateLocal = Date()


                val calendar = Calendar.getInstance()
                calendar.time = sub.date
                calendar.add(Calendar.DAY_OF_MONTH,31*sub.term)
                if (calendar.time.time>currentDateLocal.time){
                    Log.d("obkobkokoybybybhnb","pooknokn79u797jh975juh8957j")
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