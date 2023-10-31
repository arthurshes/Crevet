package workwork.test.andropediagits.presenter.lesson.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import javax.inject.Inject

@HiltViewModel
class ListLessonsViewModel @Inject constructor(val coursesRepo: CourseRepo, private val andropointUseCase: AndropointUseCase, private val themeUseCase: ThemeUseCase): ViewModel() {
    var currentState: String = ""
//    private var _allLevelsByTheme: MutableLiveData<List<LevelEntity>> = MutableLiveData()
//    var allLevelsByTheme:LiveData<List<LevelEntity>> = _allLevelsByTheme

    fun checkCurrentThemeTerm(uniqueThemeId: Int, isSuccess: ((ErrorEnum) -> Unit), isNoTerm:((Boolean)->Unit)){
        viewModelScope.launch {
            themeUseCase.checkOneThemeTernAndNo(uniqueThemeId, isSuccess, isNoTerm)
        }
    }

    fun howManyTerm(isSuccess:((ErrorEnum)->Unit),isTermEnding:((String)->Unit),themeNumber:Int,courseNumber:Int){
        viewModelScope.launch {
            themeUseCase.howManyTerm(isSuccess,isTermEnding, themeNumber = themeNumber, courseNumber = courseNumber)
        }
    }


    fun thisThemePassed(isPasssed:((Boolean)->Unit),uniqueThemeId: Int){
        viewModelScope.launch {
            Log.d("gprkgrkgorkgprkgPassed3333",themeUseCase.thisThemeisPassed(uniqueThemeId).toString())
            isPasssed.invoke(themeUseCase.thisThemeisPassed(uniqueThemeId))
        }
    }

     fun buyTheme(result: (ErrorEnum) -> Unit, isHaveMoney: (BuyForAndropointStates) -> Unit,andropointMinus:Int) {
        viewModelScope.launch {
            andropointUseCase.spendAndropoints(SpendAndropointState.THEMEOPENING, { result(it) },{ isHaveMoney(it) }, andropointMinusCount = andropointMinus)
        }

    }

     fun putUniqueThemeIdForGetLevels(uniqueThemeId: Int,allLessons:((List<LevelEntity>)->Unit)) {
         viewModelScope.launch {
             allLessons.invoke(coursesRepo.searchAllLevelsTheme(uniqueThemeId))
         }
    }

    fun thisLessonFavorite(uniqueLessonId:Int){
        viewModelScope.launch {
            themeUseCase.lessonIsFavorite(uniqueLessonId)
        }
    }

    fun thisLessonNotFavorite(uniqueLessonId:Int){
        viewModelScope.launch {
            themeUseCase.lessonIsNotFavorite(uniqueLessonId)
        }
    }

    fun getAllFavoriteLessons(uniqueThemeId: Int,favLessons:((List<LevelEntity>)->Unit)){
        viewModelScope.launch {
            favLessons.invoke(themeUseCase.getAllFavoriteLessons(uniqueThemeId))
        }
    }
}