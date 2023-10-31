package workwork.test.andropediagits.presenter.bottomSheet.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.useCases.userLogic.AdsUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import javax.inject.Inject

@HiltViewModel
class VictorineBottomSheetViewModel @Inject constructor(private val courseRepo: CourseRepo, private val themeUseCase: ThemeUseCase, private val adsUseCase: AdsUseCase, private val andropointUseCase: AndropointUseCase): ViewModel() {

    fun adsView(isSuccess: ((ErrorEnum) -> Unit), isVictorine:Boolean){
        viewModelScope.launch {
            adsUseCase.viewAds(isSuccess,isVictorine)
        }
    }

    fun checkLimitActual(isSuccess: ((ErrorEnum) -> Unit), isLimitActual:((Boolean)->Unit)){
        viewModelScope.launch {
            adsUseCase.checkTermLimit(isSuccess, isLimitActual)
        }
    }

    fun minus2HoursTermAds(isSuccess: ((ErrorEnum) -> Unit),uniqueThemeId: Int,remainingHours:((String)->Unit)){
        viewModelScope.launch {
            themeUseCase.watchAdsToDisableDelay(uniqueThemeId,isSuccess,remainingHours)
        }
    }



//    fun howManyTerm(isSuccess:((ErrorEnum)->Unit),isTermEnding:((String)->Unit)){
//        viewModelScope.launch {
//            themeUseCase.howManyTerm(isSuccess,isTermEnding,)
//        }
//    }
//
//    fun checkCurrentThemeTerm(uniqueThemeId: Int, isSuccess: ((ErrorEnum) -> Unit), isNoTerm:((Boolean)->Unit)){
//        viewModelScope.launch {
//            themeUseCase.checkOneThemeTernAndNo(uniqueThemeId, isSuccess, isNoTerm)
//        }
//    }

}