package workwork.test.andropediagits.presenter.bottomSheet.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.userLogic.AdsUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import javax.inject.Inject

@HiltViewModel
class VictorineBottomSheetViewModel @Inject constructor(private val andropointUseCase: AndropointUseCase,private val themeUseCase: ThemeUseCase, private val adsUseCase: AdsUseCase,private val courseRepo: CourseRepo,private val userLogicRepo: UserLogicRepo): ViewModel() {

    fun getMyAdsProvider(adsProvider:((AdsProviderEntity)->Unit)){
        viewModelScope.launch {
            adsProvider.invoke(userLogicRepo.getMyAdsProvider())
        }
    }

    fun termExistCheckLocal(uniqueThemeId: Int,isExist:((Boolean)->Unit)){
        viewModelScope.launch {
            val theme = courseRepo.searchThemeWithUniwueId(uniqueThemeId)
            if(theme.termDateApi==null&&theme.termHourse==null){
                isExist.invoke(true)
            }else{
                isExist.invoke(false)
            }
        }
    }

    fun spendAndropoints(minusAndropoint:Int,isSucces: (ErrorEnum) -> Unit,isAndropointState:((BuyForAndropointStates)->Unit)){
        viewModelScope.launch {
            andropointUseCase.spendAndropoints(SpendAndropointState.SKIPDELAY,isSucces,isAndropointState, andropointMinusCount = minusAndropoint)
        }
    }

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
}