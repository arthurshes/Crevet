package workwork.test.andropediagits.presenter.forgetScreens.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.email.RecoverPassState
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateCheckSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetMethodGetModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextCheckSendModel
import workwork.test.andropediagits.domain.useCases.userLogic.ResetPasswordUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.SignInUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.ResetSendState
import javax.inject.Inject

@HiltViewModel
class ForgetViewModel  @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase) : ViewModel() {

    fun getUserMethods(email: String, responseMethod:((ResetMethodGetModel)->Unit), isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase.sendReset(email = email,resetSendState = ResetSendState.GETMETHODSRESET,isSuccess, responseMethodCall = responseMethod )
        }
    }

    fun checkResetText(resetTextCheckSendModel: ResetTextCheckSendModel, isCorrect:((Boolean)->Unit)?=null, isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch(Dispatchers.IO){
            resetPasswordUseCase.sendReset(resetSendState = ResetSendState.CHECKRESETTEXT,resetTextCheckSendModel=resetTextCheckSendModel, isCorrectDatas =isCorrect, isSuccess = isSuccess)
        }
    }

    fun checkResetDate(resetDateCheckSendModel: ResetDateCheckSendModel, isCorrect:((Boolean)->Unit)?=null, isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch(Dispatchers.IO){
            resetPasswordUseCase.sendReset(resetSendState = ResetSendState.CHECKRESETDATE,resetDateCheckSendModel=resetDateCheckSendModel, isCorrectDatas =isCorrect, isSuccess = isSuccess)
        }
    }
    fun sendNewPassword(email: String,newPassword:String, isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase.sendReset(resetSendState = ResetSendState.SENDNEWPASSWORD,email=email, isSuccess = isSuccess,newPassword=newPassword)
        }
    }
}