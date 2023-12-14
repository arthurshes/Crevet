package workwork.test.andropediagits.presenter.signIn.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.EmailErrorEnum
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.CacheUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.SignInUseCase
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val signInUseCase: SignInUseCase, private val cacheUseCase: CacheUseCase, private val userLogicRepo: UserLogicRepo) : ViewModel() {
    var currentState: String = ""



    fun getCurrentTime(currentTime:((String)->Unit)){
        viewModelScope.launch {
            currentTime.invoke(userLogicRepo.getCurrentTime().datetime)
        }
    }
     fun signInEmail(email: String, password: String,lang: String, isRegister:((Boolean)->Unit),result: ((EmailErrorEnum) -> Unit),token:((String)->Unit)) {
         viewModelScope.launch {
             signInUseCase.emailSignIn(
                 {
                     result(it)
                 },
                 email,
                 password,
                 isRegister,
                 lang,
                 token
             )
         }
    }


    fun saveUserInfo(userInfoEntity: UserInfoEntity, result: ((ErrorEnum) -> Unit),lang: String){
       viewModelScope.launch {
           signInUseCase.insertUserInfo(userInfoEntity,{
               result.invoke(it)
           },lang)
       }
    }

     fun loadData(isSuccess:((ErrorEnum)->Unit),lang:String?=null,token:String?=null){
         viewModelScope.launch{
             cacheUseCase.downloaCourse({isSuccess.invoke(it)}, lang,token)
         }
    }

    fun checkSubscribesAndBuyCourse(isSuccess: ((ErrorEnum) -> Unit)){
        viewModelScope.launch {
            cacheUseCase.checkSubscribesAndBuyCourse(isSuccess)
        }
    }

}