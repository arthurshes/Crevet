package workwork.test.andropediagits.presenter.lesson.viewmodel

import android.annotation.SuppressLint
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
import workwork.test.andropediagits.data.local.entities.levels.LevelEntity
import workwork.test.andropediagits.data.local.entities.levels.ThemeLevelContentEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import javax.inject.Inject

@HiltViewModel
class LessonViewModel @Inject constructor(
    private val coursesRepo: CourseRepo,
    private val savedStateHandle: SavedStateHandle,
    private val themeUseCase: ThemeUseCase
) : ViewModel() {
    var currentState: String = ""
    private var dataKey = "IntKey"
    private var countKey = "countKey"
    private var maxLevelNumberWithIndex = 0
    private var minLeveNumberWithIndex = 0
    // Используйте значение по умолчанию, если оно не было установлено
    private var currentIndex=0
    //  private var countLesson = savedStateHandle.getLiveData<Int>(countKey, -1)
    //  private set


    private var allLevelsByTheme:List<LevelEntity> = emptyList()
    // var currentLevel: LiveData<ThemeLevelContentEntity>? = null
private var countLesson:Int = 0

    fun termExistCheckLocal(uniqueThemeId: Int,isExist:((Boolean)->Unit)){
        viewModelScope.launch{
            val theme = coursesRepo.searchThemeWithUniwueId(uniqueThemeId)
            if(theme.termDateApi==null&&theme.termHourse==null){
                isExist.invoke(true)
            }else{
                isExist.invoke(false)
            }
        }
    }

    fun howManyTerm(isSuccess:((ErrorEnum)->Unit), isTermEnd:((String)->Unit),themeNumber:Int,courseNumber: Int){
        viewModelScope.launch{
            themeUseCase.howManyTerm(isSuccess, isTermEnd, themeNumber = themeNumber, courseNumber = courseNumber)
        }
    }

    fun checkVictorineExistTheme(victorineExist:((Boolean)->Unit),uniqueThemeId: Int){
        viewModelScope.launch{
            themeUseCase.thisThemeVictorineYes(victorineExist,uniqueThemeId)
        }
    }

    fun test() {
        viewModelScope.launch {
            //    coursesRepo.searchAllLevelsTheme()
        }
    }

      @SuppressLint("SuspiciousIndentation")
     suspend fun putUniqueThemeIdForGetLevels(uniqueThemeId: Int,minNumber:((Int)->Unit),maxNumber:((Int)->Unit)) {
          Log.d("firjfirjfirjfijrfjri","uniqueThemeID:${uniqueThemeId}")

       allLevelsByTheme = coursesRepo.searchAllLevelsTheme(uniqueThemeId)
         maxLevelNumberWithIndex = allLevelsByTheme.maxByOrNull { it.levelNumber }?.levelNumber ?: 0
       minLeveNumberWithIndex = allLevelsByTheme.minByOrNull { it.levelNumber }?.levelNumber ?: 0
          minNumber.invoke(minLeveNumberWithIndex)
          maxNumber.invoke(maxLevelNumberWithIndex)
          countLesson = allLevelsByTheme.size
          Log.d("firjfirjfirjfijrfjri","countSize:${countLesson}")
    }
//
//   suspend fun putUniqueLevelIdForGetContents(uniqueLevelId: Int):ThemeLevelContentEntity?  {
//
//
////        currentIndex=allLevelsByTheme.indexOfFirst { it.uniqueLevelId == uniqueLevelId } ?: 0
////        Log.d("firjfirjfirjfijrfjri",currentIndex.toString())
////        // currentLevel = coursesRepo.searchOneLevelContent(currentIndex)
//        return coursesRepo.searchOneLevelContent(uniqueLevelId)
//    }

//    suspend fun getCurrentContent(): ThemeLevelContentEntity? {
//        // if (currentLevel != null && currentIndex.value!! >= 0 && currentIndex.value!! < currentLevel!!.value!!.size) {
//        return coursesRepo.searchOneLevelContent(currentIndex)
//        // }
//        //return null
//    }

    /*   fun getVictorineContent(): ThemeLevelContentEntity? {
           if (allContentBylevel != null && currentIndex.value!! >= 0 && currentIndex.value!! < allContentBylevel!!.value!!.size) {
               return allContentBylevel!!.value!![currentIndex.value!!-1]
           }
           return null
       }*/
    suspend fun getNextContent(courseNumber:Int,themeNumber:Int,LevelNumber:Int,isVictorine:(()->Unit)?=null,isText:(()->Unit)?=null,LastLesson:(()->Unit)?=null): ThemeLevelContentEntity? {


        if(maxLevelNumberWithIndex==LevelNumber){
            LastLesson?.invoke()
        }
        Log.d("firjfirjfirjfijrfjri","courseNumberViewModel:${courseNumber},themeNumber:${themeNumber},levelNUmber:${LevelNumber}")
        if(maxLevelNumberWithIndex>=LevelNumber){

//            Log.d("firjfirjfirjfijrfjri", "boolean view ints: "+smallAndBigInt(LevelNumber,countLesson).toString())
            isText?.invoke()
            return coursesRepo.getNextContentTestFun(courseNumber,themeNumber,LevelNumber)

        }else{
            isVictorine?.invoke()
            Log.d("firjfirjfirjfijrfjri","isVicotorineInViewModel:${isVictorine}")
        }
//        Log.d("firjfirjfirjfijrfjri","checknotwork:${smallAndBigInt(LevelNumber,countLesson).toString()}")
         return null
    }

//    private fun smallAndBigInt(small:Int,big:Int):Boolean{
//        return big>=small
//    }

//    private fun smallAndBigIntTest(lesso:Int,big:Int):Boolean{
////        return big>=small
//
//    }



    suspend fun getPreviousContent(courseNumber:Int,themeNumber:Int,LevelNumber:Int): ThemeLevelContentEntity? {
        /*  var currentIndexValue = currentIndex.value ?: 0
          if (currentLevel != null && currentIndex.value!! - 1 >= 0) {
              savedStateHandle[dataKey] = --currentIndexValue
              return currentLevel!!.value!![currentIndex.value!!]
          }
          return null*/
        if(LevelNumber>=minLeveNumberWithIndex){

            return coursesRepo.getNextContentTestFun(courseNumber,themeNumber,LevelNumber)
        }
            return null
    }
}