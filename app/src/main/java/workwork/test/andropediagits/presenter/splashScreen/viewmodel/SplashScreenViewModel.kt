package workwork.test.andropediagits.presenter.splashScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.SplashActionEnum
import workwork.test.andropediagits.domain.useCases.userLogic.CacheUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.SplashScreenUseCase
import javax.inject.Inject

@HiltViewModel
class SplashScreenViewModel@Inject constructor(private val splashScreenUseCase: SplashScreenUseCase, private val cacheUseCase: CacheUseCase): ViewModel() {
     fun checkSplashScreen( result: ((SplashActionEnum) -> Unit)){
         viewModelScope.launch {
             splashScreenUseCase.start({result.invoke(it)})
         }
    }

    fun checkCacheActual(result:((ErrorEnum)->Unit)){
        viewModelScope.launch {
          cacheUseCase.checkCache({
              result.invoke(it)
          })
        }
    }



}