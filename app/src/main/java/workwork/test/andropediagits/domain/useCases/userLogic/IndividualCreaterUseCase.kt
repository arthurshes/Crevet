package workwork.test.andropediagits.domain.useCases.userLogic

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.indi.IndiCourseEntity
import workwork.test.andropediagits.data.local.entities.indi.IndiThemeEntity
import workwork.test.andropediagits.domain.repo.IndividualCourseCreaterRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.state.IndiCourseModerationStatus
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class IndividualCreaterUseCase @Inject constructor(private val individualCourseCreaterRepo: IndividualCourseCreaterRepo, private val userLogicRepo: UserLogicRepo) {

    suspend fun getIncomeData(){

    }

    suspend fun checkCourseModerationStatus(status:((IndiCourseModerationStatus)->Unit), isSuccess: (ErrorEnum) -> Unit, uniqueCourseNumber: Int){
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
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun createCourse(isSuccess:((ErrorEnum)->Unit),courseName:String,courseDescription:String,coursePriceDollar:String,myRequisits:String){
        try {
            val token = userLogicRepo.getUserInfoLocal()
            val uniqueCourseNumber = individualCourseCreaterRepo.getUnuiqueCourseNumber(token = token.token)
            val indiCourseEntity = IndiCourseEntity(
                createrToken = token.token,
                coursePrice = coursePriceDollar,
                courseDescription = courseDescription,
                courseName = courseName,
                payRequisits = myRequisits,
                createrName = token.name ?: "Default",
                versionCourse = 1,
                uniqueCourseNumber = uniqueCourseNumber.uniqueCourseNumber
            )
            individualCourseCreaterRepo.createIndiCourse(indiCourseEntity)
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
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun createIndiThemeCourse(uniqueCourseNumber:Int,isSuccess:((ErrorEnum)->Unit),themeName:String){
        try {
            val userInfo = userLogicRepo.getUserInfoLocal()
            val indiThemes = individualCourseCreaterRepo.getAllIndiThemes(uniqueCourseNumber,userInfo.token)?.maxOfOrNull {
                it.themeNumber
            }
            val indiThemeEntity = IndiThemeEntity(
                createrToken = userInfo.token,
                themeName = themeName,
                themeNumber = indiThemes ?: 1,
                uniqueCourseNumber = uniqueCourseNumber
            )
            individualCourseCreaterRepo.createIndiThemes(indiThemeEntity)
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
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun createIndiLesson(isSuccess:((ErrorEnum)->Unit)){
        try {

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
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun createIndiLessonContent(isSuccess:((ErrorEnum)->Unit)){
        try {

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
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }




}