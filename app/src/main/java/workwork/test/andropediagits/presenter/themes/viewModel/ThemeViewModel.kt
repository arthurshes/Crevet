package workwork.test.andropediagits.presenter.themes.viewModel

import android.icu.number.UnlocalizedNumberFormatter
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.AdsEntity
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.BillingProviderEntity
import workwork.test.andropediagits.data.local.entities.UserInfoEntity
import workwork.test.andropediagits.data.local.entities.theme.ThemeEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.transactionLogic.TransactionUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AdsUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.CacheUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.SignInUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.AddAndropoints
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.LanguagesEnum
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val coursesRepo: CourseRepo,
    savedStateHandle: SavedStateHandle,
    private val userLogicRepo: UserLogicRepo,
    private val themeUseCase: ThemeUseCase,
    private val andropointUseCase: AndropointUseCase,
    private val adsUseCase: AdsUseCase,
    private val transactionUseCase: TransactionUseCase,
    private val signInUseCase: SignInUseCase,
    private val cacheUseCase: CacheUseCase
) : ViewModel() {
    var currentState: String = ""
//    private var _allThemes:MutableLiveData<List<ThemeEntity>> = MutableLiveData()
//    var allThemes: LiveData<List<ThemeEntity>> = _allThemes
    private var courseNumberKey = "countKey"
     var courseNumber = savedStateHandle.getLiveData<Int>(courseNumberKey, -1)

    fun getAdProvider(adsProviderEntity: ((AdsProviderEntity)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            adsProviderEntity.invoke(userLogicRepo.getMyAdsProvider())
        }
    }

    fun selectAdsProvider(adsProviderEntity: AdsProviderEntity){
        viewModelScope.launch(Dispatchers.IO) {
            userLogicRepo.updateAdsProvider(adsProviderEntity)
        }
    }

    fun deleteMyAccount(isSuccess: (ErrorEnum) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.deleteMyAccount { isSuccess.invoke(it) }
        }
    }

    fun getMyAdsProvider(adsProvider:((AdsProviderEntity)->Unit)){
        viewModelScope.launch (Dispatchers.IO){
            adsProvider.invoke(userLogicRepo.getMyAdsProvider())
        }
    }

    fun getMyBillingProvider(billingProvider:((BillingProviderEntity)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            billingProvider.invoke(userLogicRepo.getMyBillingProvider())
        }
    }

    fun exitCurrentAccount( result: (ErrorEnum) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            signInUseCase.exitCurrentAccount(result)
        }
    }

    fun downloadUpdateLang( result: (ErrorEnum) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            cacheUseCase.downloadUpdateLang { result(it) }
        }
    }

    fun langChoose(languagesEnum: LanguagesEnum, isSuccess: ((ErrorEnum) -> Unit)){
        viewModelScope.launch (Dispatchers.IO){
            signInUseCase.languageChoose(isSuccess,languagesEnum)
        }
    }

    fun getCurrentLang(currentLang:((String)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            currentLang.invoke(userLogicRepo.getUserInfoLocal().userLanguage ?: "eng")
        }
    }

    fun buyAndropoints(addAndropoints: AddAndropoints,isSuccess: ((ErrorEnum) -> Unit),){
        viewModelScope.launch(Dispatchers.IO) {
            andropointUseCase.addAndropoint(isSuccess,addAndropoints)
        }
    }

   fun adsView(isSuccess: ((ErrorEnum) -> Unit)){
       viewModelScope.launch(Dispatchers.IO) {
           adsUseCase.viewAds(isSuccess)
       }
   }

    fun checkLimitActual(isSuccess: ((ErrorEnum) -> Unit), isLimitActual:((Boolean)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            adsUseCase.checkTermLimit(isSuccess, isLimitActual)
        }
    }

    fun addAndropointAds(isSuccess: ((ErrorEnum) -> Unit)){
        viewModelScope.launch(Dispatchers.IO) {
            andropointUseCase.addAndropoint(isSuccess,AddAndropoints.ADDANDROPOINTSVIEWAD)
        }
    }

    fun addFavorite(uniqueThemeId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            themeUseCase.themeIsFavorite(uniqueThemeId)
        }
    }

    fun removeFavorite(uniqueThemeId: Int){
        viewModelScope.launch(Dispatchers.IO) {
            themeUseCase.themeNotFavorite(uniqueThemeId)
        }
    }

    fun checkTermTheme(uniqueThemeId: Int, isSuccess: ((ErrorEnum) -> Unit), isNoTerm:((Boolean)->Unit), buyThemeId:((List<Int>)->Unit)?=null) {
        viewModelScope.launch(Dispatchers.IO) {
            themeUseCase.checkOneThemeTernAndNo(uniqueThemeId, { isSuccess(it) },{isNoTerm(it)},{
                if (buyThemeId != null) {
                    buyThemeId(it)
                }
            })
        }
    }
    fun checkThemeBuy(uniqueThemeId: Int, isSuccess: ((ErrorEnum) -> Unit), isBUy:((Boolean)->Unit), buyThemeId:((List<Int>)->Unit)?=null) {
        viewModelScope.launch(Dispatchers.IO) {
            themeUseCase.thisThemeBuy( { isSuccess(it) },{isBUy(it)},uniqueThemeId)
        }
    }
    fun buyTheme(result: (ErrorEnum) -> Unit, isHaveMoney: (BuyForAndropointStates) -> Unit,andropointMinus:Int) {
        viewModelScope.launch(Dispatchers.IO) {
            andropointUseCase.spendAndropoints(SpendAndropointState.THEMEOPENING, { result(it) },{ isHaveMoney(it) }, andropointMinusCount = andropointMinus)
        }
    }

    fun putCourseNumberLocal(numberCourse: Int,themes:((List<ThemeEntity>)->Unit)) {
            Log.d("themeSttvdr","startAllThemesCourse")
        viewModelScope.launch (Dispatchers.IO){
            themes.invoke(coursesRepo.searchThemesWithCourseNumber(numberCourse))
        }

    }
     fun getDataUser(isUserInfo:((UserInfoEntity)->Unit)){
           viewModelScope.launch(Dispatchers.IO) {
               isUserInfo.invoke(userLogicRepo.getUserInfoLocal())
           }
    }

    fun getFavoritesThemes(themes:((List<ThemeEntity>)->Unit)){
        viewModelScope.launch(Dispatchers.IO) {
        themes.invoke(themeUseCase.getAllFavoriteThemes())
        }
    }
  /*  fun putCourseNumberBackend(numberCourse: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            allThemes = logicUserApiService.ser(numberCourse)
        }
    }*/

    fun buyThemeForMoney(uniqueThemeId: Int,isSuccess: (ErrorEnum) -> Unit){
        viewModelScope.launch(Dispatchers.IO) {
            transactionUseCase.themeBuy(uniqueThemeId,isSuccess)
        }
    }
}