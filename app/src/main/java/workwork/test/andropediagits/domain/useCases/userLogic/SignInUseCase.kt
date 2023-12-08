package workwork.test.andropediagits.domain.useCases.userLogic

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.util.Log

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.core.exception.EmailErrorEnum
import workwork.test.andropediagits.core.exception.EmailIsEmptyException
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.IncorrectPasswordException
import workwork.test.andropediagits.core.exception.NameIsEmptyException
import workwork.test.andropediagits.core.exception.PasswordIsEmptyException
import workwork.test.andropediagits.core.exception.TooBigPhotoException
import workwork.test.andropediagits.core.exception.TooLongPasswordException
import workwork.test.andropediagits.core.exception.TooLongUserNameException
import workwork.test.andropediagits.core.exception.WrongAddressEmialException
import workwork.test.andropediagits.core.mappers.toUserInfoLocalEntity
import workwork.test.andropediagits.data.local.entities.ResetNextEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.DeleteMyAccModel
import workwork.test.andropediagits.data.remote.model.UserSignInModel
import workwork.test.andropediagits.data.remote.model.email.EmailRecoverModel
import workwork.test.andropediagits.data.remote.model.email.RecoverPassState
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.TransactionRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.UpdateUserInfoUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.LanguagesEnum
import workwork.test.andropediagits.domain.useCases.userLogic.validators.EmailValidStates
import workwork.test.andropediagits.domain.useCases.userLogic.validators.EmailValidator
import workwork.test.andropediagits.domain.useCases.userLogic.validators.UserInfoUpdateEnum
import workwork.test.andropediagits.domain.useCases.userLogic.validators.UserInfoValidator
import java.util.Calendar
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.concurrent.TimeoutException
import javax.inject.Inject
import kotlin.Exception

class SignInUseCase @Inject constructor(private val userLogicRepo: UserLogicRepo, private val updateUserInfoUseCase: UpdateUserInfoUseCase, private val emailValidator: EmailValidator, private val transactionRepo: TransactionRepo, private  val userInfoValidator: UserInfoValidator,private val courseRepo: CourseRepo){


    suspend fun deleteMyAccount(isSuccess: (ErrorEnum) -> Unit){
        try{
            val token = userLogicRepo.getUserInfoLocal()
            val deleteMyAccModel = DeleteMyAccModel(token.token)
            val response = userLogicRepo.deleteMyAccount(deleteMyAccModel)
            if(response.codeAnswer==212){
                courseRepo.deleteAllCourse()
                courseRepo.deleteAllThemes()
                courseRepo.deleteAllLevels()
                courseRepo.deleteAllLevelsContent()
                courseRepo.deleteAllVictorineClue()
                courseRepo.deleteAllVictorines()
                courseRepo.deleteAllVictorineVariants()
                userLogicRepo.deleteUserInfoLocal(token)
                val subscribes = transactionRepo.getSubscribe()
                if(subscribes!=null){
                    transactionRepo.deleteSubscribe(subscribes)
                }
                val themeBuy = transactionRepo.getAllBuyThemes()
                if(!themeBuy.isNullOrEmpty()){
                    themeBuy.forEach { buyTheme->
                        transactionRepo.deleteThemeBuy(buyTheme)
                    }
                }
                val buyCourses = transactionRepo.getAllMyCourseBuy()
                if(!buyCourses.isNullOrEmpty()){
                    buyCourses.forEach { buyCourse->
                        transactionRepo.deleteBuyCourse(buyCourse)
                    }
                }
            }
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }


    suspend fun exitCurrentAccount(isSuccess: (ErrorEnum) -> Unit){
        try{
            val current = userLogicRepo.getUserInfoLocal()
            val promo = userLogicRepo.getAllMyPromo()
            userLogicRepo.deleteAllUserAds()
            userLogicRepo.deleteAllReset()
            userLogicRepo.deleteUserInfoLocal(current)
            if(promo!=null){
                userLogicRepo.deletePromoCode(promo)
            }
            transactionRepo.deleteAllBuyThemes()
            val courseBuy = transactionRepo.getAllMyCourseBuy()
            if(!courseBuy.isNullOrEmpty()){
                courseBuy.forEach { oneCourse->
                    transactionRepo.deleteBuyCourse(oneCourse)
                }
            }
            val subs = transactionRepo.getSubscribe()
            if(subs!=null){
                transactionRepo.deleteSubscribe(subs)
            }
            courseRepo.deleteAllCourse()
            courseRepo.deleteAllThemes()
            courseRepo.deleteAllLevels()
            courseRepo.deleteAllLevelsContent()
            courseRepo.deleteAllVictorineClue()
            courseRepo.deleteAllVictorines()
            courseRepo.deleteAllVictorineVariants()
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    @SuppressLint("SuspiciousIndentation")
    suspend fun insertUserInfo(userInfoEntity: UserInfoEntity, isSuccess: ((ErrorEnum) -> Unit), lang:String?=null){
        try {
            val currentDate = courseRepo.getCurrentTime()
            userLogicRepo.insertUserInfoLocal(userInfoEntity)
            val userSignInModel = UserSignInModel(
                token = userInfoEntity.token,
                name = userInfoEntity.name
            )
          val response =  userLogicRepo.sendUserInfo(userSignInModel)
            if(response.codeAnswer==212){
                val userInfo = userLogicRepo.getCurrentUserInfo(userInfoEntity.token)
                val isInfinitys = userInfo.isInfinity == 1
                val updateUserLocalInf = UserInfoEntity(
                    name = userInfo.name ?: "DefaultName",
                    token = userInfo.token,
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
            }
           isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun recoverPassword(email: String,code:String?=null,newPassword:String?=null,isSuccess:((RecoverPassState)->Unit),language:String?=null){
        try {
            if(email.isEmpty()){
                isSuccess.invoke(RecoverPassState.EMAILISEMPTY)
                return
            }
            val emailRecoverModel = EmailRecoverModel(
                email = email,
                code = code ?: "",
                newPassword = newPassword ?: "",
                lang = language ?: "eng"
            )
            val response = userLogicRepo.recoverPasswordForEmail(emailRecoverModel)
            if(response.codeAnswer == 103){
                isSuccess.invoke(RecoverPassState.CODESENDEMAIL)
                return
            }
            if (response.codeAnswer==508){
                isSuccess.invoke(RecoverPassState.EMAILISNOTEXIST)
                return
            }
            if (response.codeAnswer==124){
                isSuccess.invoke(RecoverPassState.CODEEXIST)
                return
            }
            if (response.codeAnswer==222){
                isSuccess.invoke(RecoverPassState.CORRECTCODE)
                return
            }
            if(response.codeAnswer==989){
                isSuccess.invoke(RecoverPassState.INCORRECTCODE)
                return
            }
            if (response.codeAnswer==206){
                isSuccess.invoke(RecoverPassState.PASSWORDCHANGE)
                return
            }
            if (response.codeAnswer==606){
                isSuccess.invoke(RecoverPassState.ERROR)
                return
            }
        }catch (e:IOException){
            Log.d("recoverCodeeee",e.toString())
            isSuccess.invoke(RecoverPassState.NOTNETWORK)
        }catch (e:TimeoutException){
            isSuccess.invoke(RecoverPassState.TIMEOUTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(RecoverPassState.ERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(RecoverPassState.NULLPOINTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(RecoverPassState.UNKNOWNERROR)
        }
    }

    suspend fun emailSignIn(isSuccess:((EmailErrorEnum)->Unit), email:String, password:String, isRegister:((Boolean)->Unit), lang: String){
        try {
            emailValidator.emailCheckAndSend(email = email, password = password,EmailValidStates.CHECKEMAILANDPASSWORD,lang)
            emailValidator.emailCheckAndSend(email = email, password = password, EmailValidStates.SENDEMAIL,lang,isRegister)
            Log.d("emailStatesdd","success")
            isSuccess.invoke(EmailErrorEnum.SUCCESS)
        }catch (e:IOException){
            Log.d("emailStatesdd","NOTNETWORK")
            isSuccess.invoke(EmailErrorEnum.NOTNETWORK)
        }catch (e:TimeoutException){
            Log.d("emailStatesdd","TIMEOUTERROR")
            isSuccess.invoke(EmailErrorEnum.TIMEOUTERROR)
        }catch (e:HttpException){
            Log.d("emailStatesdd","ERROR")
            isSuccess.invoke(EmailErrorEnum.ERROR)
        }catch (e: WrongAddressEmialException){
            Log.d("emailStatesdd","WrongAddressEmail")
            isSuccess.invoke(EmailErrorEnum.WrongAddressEmail)
        }catch (e: TooLongPasswordException){
            Log.d("emailStatesdd","TooLongPassword")
            isSuccess.invoke(EmailErrorEnum.TooLongPassword)
        }catch (e: IncorrectPasswordException){
            Log.d("emailStatesdd","IncorrectPassword")
            isSuccess.invoke(EmailErrorEnum.IncorrectPassword)
        }catch (e:NullPointerException){

            Log.d("emailStatesdd","NULLPOINTERROR: "+e)
            isSuccess.invoke(EmailErrorEnum.NULLPOINTERROR)
        }catch (e: PasswordIsEmptyException){
            Log.d("emailStatesdd","PasswordIsEmpty")
            isSuccess.invoke(EmailErrorEnum.PasswordIsEmpty)
        }catch (e: EmailIsEmptyException){
            Log.d("emailStatesdd","EmailIsEmpty")
            isSuccess.invoke(EmailErrorEnum.EmailIsEmpty)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("s8888222sss",e.toString())
            Log.d("emailStatesdd","vUNKNOWNERROR")
            isSuccess.invoke(EmailErrorEnum.UNKNOWNERROR)
        }
    }

    suspend fun updateUserInfo(name:String?=null, image: Bitmap?=null,lang:String?=null, isSuccess:((UserInfoUpdateEnum)->Unit),buyThemesId:((List<Int>)->Unit)?=null,token: String?=null){
        try {
            userInfoValidator.validateImageAndName(name = name ?: "", image = image)
            updateUserInfoUseCase.updateUserInfo(name = name, image = image, lang = lang,token=token)
            isSuccess.invoke(UserInfoUpdateEnum.SUCCESS)
        }catch (e:IOException){
            if (checkSubscibe()){
                isSuccess.invoke(UserInfoUpdateEnum.OFFLINE)
                return
            }
            if (checkBuyCourse()){
                isSuccess.invoke(UserInfoUpdateEnum.OFFLINE)
                return
            }
            checkAndGeIdtLocalBuyThemes({
                buyThemesId?.invoke(it)
            },{ isBuy->
                if (isBuy){
                    isSuccess.invoke(UserInfoUpdateEnum.OFFLINETHEME)
                }
            })
            isSuccess.invoke(UserInfoUpdateEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(UserInfoUpdateEnum.ERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(UserInfoUpdateEnum.TIMEOUTERROR)
        }catch (e:NullPointerException){
            isSuccess.invoke(UserInfoUpdateEnum.NULLPOINTERROR)
        }catch (e: TooLongUserNameException){
            isSuccess.invoke(UserInfoUpdateEnum.TooLongUserName)
        }catch (e: TooBigPhotoException){
            isSuccess.invoke(UserInfoUpdateEnum.TooBigPhoto)
        }catch (e: NameIsEmptyException){
            isSuccess.invoke(UserInfoUpdateEnum.NameIsEmpty)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            Log.d("s8888222sss",e.toString())
            isSuccess.invoke(UserInfoUpdateEnum.UNKNOWNERROR)
        }
    }

    suspend fun languageChoose(isSuccess:((ErrorEnum)->Unit),language: LanguagesEnum){
        try{
            when(language){
                LanguagesEnum.ENGLISH -> {
                    updateUserInfoUseCase.updateUserInfo( lang = "eng")
                }
                LanguagesEnum.RUSSIAN -> {
                    updateUserInfoUseCase.updateUserInfo( lang = "rus")
                }
                LanguagesEnum.GERMANY -> {
                    updateUserInfoUseCase.updateUserInfo( lang = "ger")
                }
                LanguagesEnum.HINDI -> {
                    updateUserInfoUseCase.updateUserInfo( lang = "hindi")
                }
            }
              isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            if(checkSubscibe()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
            if(checkBuyCourse()){
                isSuccess.invoke(ErrorEnum.OFFLINEMODE)
                return
            }
          isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:NullPointerException){
            isSuccess.invoke(ErrorEnum.NULLPOINTERROR)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }


    suspend fun getMyInfo(token:String,isSuccess:((ErrorEnum)->Unit)){
        try {
            val myInfoBackend = userLogicRepo.getCurrentUserInfo(token)
            userLogicRepo.insertUserInfoLocal(myInfoBackend.toUserInfoLocalEntity())
            isSuccess.invoke(ErrorEnum.SUCCESS)
        }catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        } catch (e:IOException){
            isSuccess.invoke(ErrorEnum.NOTNETWORK)
        }catch (e:HttpException){
            isSuccess.invoke(ErrorEnum.ERROR)
        }catch (e:TimeoutException){
            isSuccess.invoke(ErrorEnum.TIMEOUTERROR)
        }catch (e:Exception){
            if(e is CancellationException) throw e
            isSuccess.invoke(ErrorEnum.UNKNOWNERROR)
        }
    }

    private suspend fun checkSubscibe():Boolean{
        val sub = transactionRepo.getSubscribe()

        if(sub!=null){
            val currentDateLocal = Date()


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