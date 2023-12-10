package workwork.test.andropediagits.presenter.reset.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetDateSendModel
import workwork.test.andropediagits.data.remote.model.resetModelsDemo.ResetTextSendModel
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.ResetPasswordUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.ResetSendState

import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryMethodViewModel  @Inject constructor(private val resetPasswordUseCase: ResetPasswordUseCase, private val userLogicRepo: UserLogicRepo) : ViewModel() {

    fun deleteResetNext(){
        viewModelScope.launch{
            userLogicRepo.deleteAllReset()
        }
    }

    fun getEmail(isEmail:((String)->Unit)){
        viewModelScope.launch {
            isEmail.invoke(userLogicRepo.getReset().email)
        }
    }

    fun sendKeyword(resetTextSendModel: ResetTextSendModel, isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch {
            resetPasswordUseCase.sendReset(resetSendState = ResetSendState.SENDRESETTEXT,resetTextSendModel = resetTextSendModel, isSuccess = isSuccess)
        }
    }

    fun sendDate(resetDateSendModel: ResetDateSendModel, isSuccess:((ErrorEnum)->Unit)){
        viewModelScope.launch {
            resetPasswordUseCase.sendReset(resetSendState = ResetSendState.SENDRESETDATE,resetDateSendModel = resetDateSendModel, isSuccess = isSuccess)
        }
    }

}