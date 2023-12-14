package workwork.test.andropediagits.domain.useCases.userLogic.validators


import android.annotation.SuppressLint
import android.util.Log

import okhttp3.internal.isSensitiveHeader
import workwork.test.andropediagits.core.exception.IncorrectPasswordException
import workwork.test.andropediagits.core.exception.PasswordIsEmptyException
import workwork.test.andropediagits.core.exception.TooLongPasswordException
import workwork.test.andropediagits.core.exception.WrongAddressEmialException
import workwork.test.andropediagits.core.utils.Constatns.PASSWORD_MAX_LENGHT
import workwork.test.andropediagits.data.local.entities.ResetNextEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.remote.model.email.EmailSignInModel
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import javax.inject.Inject

class EmailValidator @Inject constructor(private val userLogicRepo: UserLogicRepo){


    suspend fun emailCheckAndSend(email: String, password: String, emailValidStates: EmailValidStates,lang:String,isRegister:((Boolean) -> Unit)?=null,token:((String)->Unit)?=null){
            when(emailValidStates){
                EmailValidStates.CHECKEMAILANDPASSWORD -> {
                    checkEmailAndPasswordLocal(email, password)
                }
                EmailValidStates.SENDEMAIL -> {
                    sendEmailData(email, password,isRegister,lang,token)
                }
            }
    }

    private fun checkEmailAndPasswordLocal(email:String,password:String){
        if (!email.contains("@")){
            throw WrongAddressEmialException()
        }
//        if(email.length==0){
//            throw EmailIsEmptyException()
//        }
        if (password.isEmpty()){
            throw PasswordIsEmptyException()
        }
        if (password.length > PASSWORD_MAX_LENGHT){
            throw TooLongPasswordException()
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private suspend fun sendEmailData(email: String, password: String, isRegister:((Boolean)->Unit)?=null, lang: String,token:((String)->Unit)?=null){
        val currentDate = userLogicRepo.getCurrentTime()
        val emailSignInModel = EmailSignInModel(
            email = email,
            password = password
        )
        Log.d("emailStatesdd","SendEmail")
        val response = userLogicRepo.emailSignIn(emailSignInModel)
        Log.d("emailStatesdd","response:" +response)
        if (response.codeAnswer == 888){
            throw IncorrectPasswordException()
        }

        isRegister?.invoke(response.isRegister)
//            val userInfoLocal = userLogicRepo.getUserInfoLocal().value
//        if(myInfo)
//        userInfoLocal?.let { myInfo->
//            val userInfoEntity = UserInfoEntity(
//                id = myInfo.id,
//                token = response.token,
//                phoneBrand = myInfo.phoneBrand
//            )
//            userLogicRepo.updateUserInfoLocal(userInfoEntity)
//        }
        val atIndex = email.indexOf('@')
        val resetNextEntity = ResetNextEntity(
            email = email
        )
            userLogicRepo.insertReset(resetNextEntity)
        token?.invoke(response.token)
            val userInfoEntity = UserInfoEntity(
                token = response.token,
                lastOnlineDate = currentDate.datetime,
                name = email.substring(0,atIndex),
                userLanguage = lang
            )
        userLogicRepo.insertUserInfoLocal(userInfoEntity)
    }
}

enum class EmailValidStates{
    CHECKEMAILANDPASSWORD,
    SENDEMAIL
}