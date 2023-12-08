package workwork.test.andropediagits.presenter.courses.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.userAgent
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.AdsProviderEntity
import workwork.test.andropediagits.data.local.entities.course.CourseEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.transactionLogic.TransactionUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.CourseUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.PromoCodeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.PromoCodeState
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(private val userLogicRepo: UserLogicRepo,private val coursesRepo: CourseRepo, private val promoCodeUseCase: PromoCodeUseCase, private val courseUseCase: CourseUseCase, private val andropointUseCase: AndropointUseCase, private val themeUseCase: ThemeUseCase, private val transactionUseCase: TransactionUseCase) : ViewModel() {
    var currentState: String = ""
    private var _allCourses:MutableLiveData<List<CourseEntity>> = MutableLiveData()
    var allCourses: LiveData<List<CourseEntity>> = _allCourses

    init {
//        _allCourses.postValue( savedStateHandle.get("courses")?: emptyList<CourseEntity>())
        initialCourse()
    }

    fun selectAdsProvider(adsProviderEntity: AdsProviderEntity){
        viewModelScope.launch {
            userLogicRepo.insertAdsProvider(adsProviderEntity)
        }
    }

    fun buyCourseAndropoint(isSuccess: (ErrorEnum) -> Unit, andropointMinus:Int, isAndropointState:((BuyForAndropointStates)->Unit)){
        viewModelScope.launch {
            andropointUseCase.spendAndropoints(SpendAndropointState.COURSEOPENING,isSuccess,isAndropointState, andropointMinusCount = andropointMinus)
        }
    }

    fun buyCourseAndropointOpen(isSuccess: (ErrorEnum) -> Unit,courseNumber: Int){
        viewModelScope.launch {
            courseUseCase.courseBuyAndropoint(isSuccess,courseNumber)
        }
    }

    fun checkAllCourseThemesTerm(courseNumber: Int, isSuccess:((ErrorEnum)->Unit), isTermActual:((Boolean)->Unit)){
        viewModelScope.launch {
            themeUseCase.checkAllCourseThemesTerm(courseNumber, isSuccess, isTermActual)
        }
    }

     fun initialCourse(){
        viewModelScope.launch {
            _allCourses.postValue(coursesRepo.getAllCourses())
        }
    }


     fun checkPromoCode(promoCode: String, result: (ErrorEnum) -> Unit, promoState: (PromoCodeState) -> Unit) {
        viewModelScope.launch {
            promoCodeUseCase.checkPromoCode(promoCode, { result(it) }, { promoState(it) })
        }
    }

//    fun checkCourseBuy(result: (ErrorEnum) -> Unit, isBuy: (Boolean) -> Unit, courseNumber: Int) {
//        viewModelScope.launch {
//            courseUseCase.thisCourseBuy({ result(it) }, courseNumber, { isBuy(it) })
//        }
//    }
//
//    fun buyCourse(result: (ErrorEnum) -> Unit, isHaveMoney: (BuyForAndropointStates) -> Unit) {
//        viewModelScope.launch(Dispatchers.IO) {
//            andropointUseCase.spendAndropoints(SpendAndropointState.COURSEOPENING, { result(it) },{ isHaveMoney(it) })
//        }
//    }

    fun buyCourseForMoney(isSuccess: (ErrorEnum) -> Unit, courseNumber: Int){
        viewModelScope.launch {
            transactionUseCase.courseBuy(courseNumber, isSuccess)
        }
    }

}