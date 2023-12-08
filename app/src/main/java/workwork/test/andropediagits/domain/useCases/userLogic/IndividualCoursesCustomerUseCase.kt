package workwork.test.andropediagits.domain.useCases.userLogic

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.mappers.toIndiCourseEntity
import workwork.test.andropediagits.core.mappers.toIndiLessonContentEntity
import workwork.test.andropediagits.core.mappers.toIndiLessonEntity
import workwork.test.andropediagits.core.mappers.toIndiThemeEntity
import workwork.test.andropediagits.core.utils.Constatns.INDI_COURSE_NOT_BUY
import workwork.test.andropediagits.core.utils.Constatns.INDI_COURSE_RAITING_NOT_VISIBLE
import workwork.test.andropediagits.data.local.entities.indi.IndiVIctorineClueEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineAnswerVarEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiVictorineQuestionEntity
import workwork.test.andropediagits.data.remote.individualCourseGet.SnedRatingIndiModel
import workwork.test.andropediagits.domain.repo.IndividualCoursesRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class IndividualCoursesCustomerUseCase @Inject constructor(private val individualCoursesRepo: IndividualCoursesRepo, private val userLogicRepo: UserLogicRepo) {



//    suspend fun getMyBuyIndiCourses(isSuccess:((ErrorEnum)->Unit)){
//        try{
//            val userInfo = userLogicRepo.getUserInfoLocal()
//
//        }catch (e:IOException){
//            isSuccess.invoke(ErrorEnum.NOTNETWORK)
//        }catch (e: HttpException){
//            isSuccess.invoke(ErrorEnum.ERROR)
//        }catch (e:NullPointerException){
//            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
//        }catch (e: TimeoutException){
//            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
//        }catch (e:Exception){
//            if(e is CancellationException) throw e
//            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//        }
//    }

    suspend fun downloadIndiCourse(uniqueCourseNumber:Int,createrToken:String,isSuccess:((ErrorEnum)->Unit),isDownload:((Boolean)->Unit)){
       try{
           val userInfo = userLogicRepo.getUserInfoLocal()
           val checkBuyIndiCourses = individualCoursesRepo.checkOneIndiCourseBuy(userInfo.token,createrToken,uniqueCourseNumber)
          if(checkBuyIndiCourses.codeAnswer!=INDI_COURSE_NOT_BUY){
              isDownload.invoke(true)
              val courseDownloadReponse = individualCoursesRepo.downloadOneIndiCourse(createrToken,uniqueCourseNumber)

              individualCoursesRepo.createIndiCourse(courseDownloadReponse.course.toIndiCourseEntity())
             courseDownloadReponse.themes.forEach { oneIndiTheme->
                 individualCoursesRepo.createIndiThemes(oneIndiTheme.toIndiThemeEntity())
             }
              courseDownloadReponse.lessons.forEach { oneIndiLesson->
                  individualCoursesRepo.createIndiLesson(oneIndiLesson.toIndiLessonEntity())
              }
              courseDownloadReponse.content.forEach { oneIndiContent->
                  individualCoursesRepo.createIndiLessonContent(oneIndiContent.toIndiLessonContentEntity())
              }
              courseDownloadReponse.victorine.victorineQuestions.forEach { oneIndiQuestion->
                  val indiVictorineQuestionEntity = IndiVictorineQuestionEntity(
                      questionText = oneIndiQuestion.questionText,
                      questionNumber = oneIndiQuestion.questionNumber,
                      uniqueCourseNumber = oneIndiQuestion.uniqueCourseNumber,
                      createrToken = oneIndiQuestion.createrToken,
                      themeNumber = oneIndiQuestion.themeNumber
                  )
                  individualCoursesRepo.insertIndiQuestion(indiVictorineQuestionEntity)
              }
              courseDownloadReponse.victorine.victorineClues?.forEach { oneVictorineCLue->
                val indiVIctorineClueEntity = IndiVIctorineClueEntity(
                    questionNumber = oneVictorineCLue.questionNumber,
                    themeNumber = oneVictorineCLue.themeNumber,
                    clueText = oneVictorineCLue.clueText,
                    createrToken = oneVictorineCLue.createrToken,
                    uniqueCourseNumber = oneVictorineCLue.uniqueCourseNumber
                )
                  individualCoursesRepo.insertIndiClue(indiVIctorineClueEntity)
              }
              courseDownloadReponse.victorine.victorineAnswerVariants.forEach { oneVictorineVAr->
                  val isCorrect = oneVictorineVAr.isCorrectVariant == 1
                  val indiVictorineAnswerVarEntity = IndiVictorineAnswerVarEntity(
                      questionNumber = oneVictorineVAr.questionNumber,
                      variantText = oneVictorineVAr.variantText,
                      themeNumber = oneVictorineVAr.themeNumber,
                      uniqueCourseNumber = oneVictorineVAr.uniqueCourseNumber,
                      createrToken = oneVictorineVAr.createrToken,
                      isCorrectVariant = isCorrect
                  )
                  individualCoursesRepo.insertIndiAnswerVariant(indiVictorineAnswerVarEntity)
              }
          }else{
              isDownload.invoke(false)
          }
           isSuccess.invoke(ErrorEnum.SUCCESS)
       }catch (e:IOException){
           isSuccess.invoke(ErrorEnum.NOTNETWORK)
       }catch (e: HttpException){
           isSuccess.invoke(ErrorEnum.ERROR)
       }catch (e:NullPointerException){
           isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
       }catch (e: TimeoutException){
           isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
       }catch (e:Exception){
           if(e is CancellationException) throw e
           isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
       }
    }

    suspend fun checkRatingVisibleOrNo(raitingVisible:((Boolean)->Unit),uniqueCourseNumber:Int,createrToken:String,isSuccess:((ErrorEnum)->Unit)){
        try{
            val userInfo = userLogicRepo.getUserInfoLocal()
            val responseCheck = individualCoursesRepo.checkUserSendRaitingThisCourse(userInfo.token,createrToken,uniqueCourseNumber)
            if(responseCheck.codeAnswer!=INDI_COURSE_RAITING_NOT_VISIBLE){
               raitingVisible.invoke(true)
            }else{
                raitingVisible.invoke(false)
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)

        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun raitingIndiCourse(uniqueCourseNumber:Int,createrToken:String,isSuccess:((ErrorEnum)->Unit),raiting:Float,raitingText:String?=null){
        try{
            val userInfo = userLogicRepo.getUserInfoLocal()
            val currentDate = userLogicRepo.getCurrentTime()
            val snedRatingIndiModel = SnedRatingIndiModel(
                creatorToken = createrToken,
                uniqueCourseNumber = uniqueCourseNumber,
                raiting = raiting,
                raitingText = raitingText,
                raitingDate = currentDate.datetime,
                token = userInfo.token
            )
            individualCoursesRepo.sendRatingIndiCourse(snedRatingIndiModel)
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun getCreaterProfile(createrToken:String,isSuccess:((ErrorEnum)->Unit)){
        try{

        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e: HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e: TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

//    suspend fun getIndiCourseRatings(createrToken:String,uniqueCourseNumber:Int,isSuccess:((ErrorEnum)->Unit)){
//        try{
//
//        }catch (e:IOException){
//            isSuccess.invoke(ErrorEnum.NOTNETWORK)
//        }catch (e: HttpException){
//            isSuccess.invoke(ErrorEnum.ERROR)
//        }catch (e:NullPointerException){
//            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
//        }catch (e: TimeoutException){
//            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
//        }catch (e:Exception){
//            if(e is CancellationException) throw e
//            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
//        }
//    }

}