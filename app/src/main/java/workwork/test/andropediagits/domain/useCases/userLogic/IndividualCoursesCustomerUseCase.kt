package workwork.test.andropediagits.domain.useCases.userLogic

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.domain.repo.IndividualCourseCreaterRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class IndividualCoursesCustomerUseCase @Inject constructor(private val individualCourseCreaterRepo: IndividualCourseCreaterRepo, private val userLogicRepo: UserLogicRepo) {



    suspend fun checkMyIndiCourses(isSuccess:((ErrorEnum)->Unit)){
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

    suspend fun downloadIndiCourse(uniqueCourseNumber:Int,createrToken:String,isSuccess:((ErrorEnum)->Unit)){
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

    suspend fun raitingIndiCourse(uniqueCourseNumber:Int,createrToken:String,isSuccess:((ErrorEnum)->Unit),raiting:Float){
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

    suspend fun getCreaterProfile(){

    }

    suspend fun getIndiCourseRatings(){

    }

}