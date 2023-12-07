package workwork.test.andropediagits.presenter.lesson.victorine.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.core.exception.ErrorStateView
import workwork.test.andropediagits.data.local.entities.victorine.VictorineAnswerVariantEntity
import workwork.test.andropediagits.data.local.entities.victorine.VictorineEntity
import workwork.test.andropediagits.domain.repo.CourseRepo
import workwork.test.andropediagits.domain.repo.UserLogicRepo
import workwork.test.andropediagits.domain.useCases.transactionLogic.TransactionUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.AndropointUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.CourseUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.PromoCodeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.StrikeModeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.ThemeUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.VictorineUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.privateUseCase.TryAgainUseCase
import workwork.test.andropediagits.domain.useCases.userLogic.state.BuyForAndropointStates
import workwork.test.andropediagits.domain.useCases.userLogic.state.SpendAndropointState
import workwork.test.andropediagits.domain.useCases.userLogic.state.StrikeModeState
import javax.inject.Inject

@HiltViewModel
class VictorineViewModel @Inject constructor(private val courseUseCase: CourseUseCase,private val coursesRepo: CourseRepo, private val themeUseCase: ThemeUseCase, private val victorineUseCase: VictorineUseCase, private val tryAgainUseCase: TryAgainUseCase, private val strikeModeUseCase: StrikeModeUseCase, private val andropointUseCase: AndropointUseCase, private  val transactionUseCase: TransactionUseCase,private val promoCodeUseCase: PromoCodeUseCase,private val userLogicRepo: UserLogicRepo): ViewModel() {
//    private var _allVictorineByTheme: MutableLiveData<List<VictorineEntity>> = MutableLiveData()
//    var allVictorineByTheme: List<VictorineEntity>?=null
//    private var _timerValue: MutableLiveData<Long> = MutableLiveData()
//    var timerValue:LiveData<Long>?=null
//     var _allVictorineAnswerVariantByTheme: List<VictorineAnswerVariantEntity>?=null

  fun checkPromoCode(isSucces: (ErrorEnum) -> Unit,isActual: (Boolean) -> Unit){
      viewModelScope.launch {
          val promoCode = userLogicRepo.getAllMyPromo()
          if(promoCode!=null){
              promoCodeUseCase.checkPromoCodeSubscribeActual(promoCode.promoCode,isActual,isSucces)
          }else{
              isSucces.invoke(ErrorEnum.SUCCESS)
              isActual.invoke(false)
          }

      }
  }


    fun victorineExit(uniqueThemeId: Int,isTerm:((Boolean)->Unit),isDateUnlock:((String)->Unit),isSucces: ((ErrorEnum) -> Unit)){
        viewModelScope.launch {
            themeUseCase.termExitVictorine(uniqueThemeId,isTerm,isDateUnlock,isSuccess=isSucces)
        }
    }

    fun thisThemeIsPassed(uniqueThemeId: Int,isPassed:((Boolean)->Unit)){
        viewModelScope.launch {
           isPassed.invoke(themeUseCase.thisThemeisPassed(uniqueThemeId))
        }
    }
    fun getTimeVictorine(victorineTime:((Long)->Unit), uniqueThemeId: Int, isSucces: (ErrorEnum) -> Unit, isTimerStart:((Boolean)->Unit)){
        viewModelScope.launch {
            val theme = coursesRepo.searchAllVictorinesWithUniqueThemeId(uniqueThemeId)
            victorineTime.invoke(theme[0].victorineTimeSec)
            transactionUseCase.checkSubscribeActual({
                isSucces.invoke(it)
            },{
                isTimerStart.invoke(it)
            })

        }
    }


    fun checkCourseBuy(isSucces: (ErrorEnum) -> Unit, isBuy:((Boolean)->Unit)){
        viewModelScope.launch {
            courseUseCase.checkCourseBuy({
                Log.d("startTimerVic2","viewMOdel:${it}")
                  isBuy.invoke(it)
            },{
                isSucces.invoke(it)
            })
        }
    }

      fun resetOlddata(uniqueThemeId: Int){
          viewModelScope.launch {
              victorineUseCase.resetOldVictorineData(uniqueThemeId)
          }
      }


     fun getAllVictorineTheme(uniqueThemeId: Int,allVictorines:((List<VictorineEntity>)->Unit)){
        viewModelScope.launch {
            allVictorines.invoke(coursesRepo.searchAllVictorinesWithUniqueThemeId(uniqueThemeId).shuffled())
        }

    }

     fun getAllQuestionAnswerVariants(questionId:Int,victorineTestId:Int,allVictorinesAnswer:((List<VictorineAnswerVariantEntity>)->Unit)){
        viewModelScope.launch {
            allVictorinesAnswer.invoke(coursesRepo.searchVictorineAnswerVariantsWithQuestionId(questionId,victorineTestId))
        }

    }



//   suspend fun getVictoineAnswerVariantsWithQuestionId(questionID:Int):List<VictorineAnswerVariantEntity>{
//            return victorineUseCase.getVictoineAnswerVariantsWithQuestionId(questionID)
//    }
//
//  suspend   fun putUniqueThemeIdForGetVictorine(uniqueThemeId:Int){
//            _allVictorineByTheme.postValue( coursesRepo.searchAllVictorinesWithUniqueThemeId(uniqueThemeId))
//         Log.d("vicotinresViewModel",
//             coursesRepo.searchAllVictorinesWithUniqueThemeId(uniqueThemeId).value.toString()
//         )
//    }
//    suspend fun putUniqueThemeIdForGetCVictorineAnswerVariant(uniqueThemeId:Int){
//            _allVictorineAnswerVariantByTheme = coursesRepo.searchVictorineAnswerVariantsWithQuestionId(uniqueThemeId)
//    }
    fun checkTestResult(uniqueThemeId: Int, result: (ErrorStateView) -> Unit, dateUnlock: (String) -> Unit, correctCount:Int, misstakesAnswersC:Int, isTimerOut:Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            themeUseCase.checkTestResults(uniqueThemeId,{result(it)},{dateUnlock(it)}, correctAnswerTstCo = correctCount, misstakesAnswersC = misstakesAnswersC, isTimerOut = isTimerOut)
        }
    }
     fun addAndropoints(result: (ErrorEnum) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            andropointUseCase.victorineProgressAndropoint { result(it) }
        }
    }
    fun checkAnswer(answer: VictorineAnswerVariantEntity, result:((ErrorEnum)->Unit), isClue:((String)->Unit)?=null){
        viewModelScope.launch {
            victorineUseCase.updateVictorineData(answer,{result(it)},{
                if (isClue != null) {
                    isClue(it)
                }
            })
        }

    }
    fun tryAgainSendProgress( result: (ErrorEnum) -> Unit,buyThemeId: ((List<Int>) -> Unit)?=null){
        viewModelScope.launch(Dispatchers.IO) {
            tryAgainUseCase.tryAgainSendProgres({result(it)},{
                if (buyThemeId != null) {
                    buyThemeId(it)
                }
            })
        }
    }
    fun strikeModeProgress( result: (ErrorEnum) -> Unit,strikeModeDay:(Int)->Unit,buyThemeId: ((List<Int>) -> Unit)?=null){
        viewModelScope.launch(Dispatchers.IO) {
         strikeModeUseCase.strikeModeMainFun({ result(it) }, { strikeModeDay(it) },
             {
                 if (buyThemeId != null) {
                     buyThemeId(it)
                 }
             })
        }
    }
//    fun startTimer( result: ((ErrorEnum) -> Unit),uniqueThemeId:Int,isSubscribe:((Boolean)->Unit),isEnding:((Boolean)->Unit)) {
//       viewModelScope.launch {
//           victorineUseCase.victorineStart({ result(it) },uniqueThemeId, { isSubscribe(it) }, { isEnding(it) }, {   timerValue=it
//           })
//       }
// }
    fun strikeModeAndropointProgress(result: (ErrorEnum) -> Unit, strikeModeState: StrikeModeState, buyThemeId: ((List<Int>) -> Unit)?=null){
        viewModelScope.launch(Dispatchers.IO) {
         andropointUseCase.strikeModeAddAndropoint({result(it)},strikeModeState,{
             if (buyThemeId != null) {
                 buyThemeId(it)
             }
         })
        }
    }
    /*fun sendVictorineProgressCash(uniqueThemeId:VictorineAnswerVariantEntity){
        viewModelScope.launch(Dispatchers.IO) {
           //  coursesRepo.sendMyProgress(uniqueThemeId)
        }
    }*/

    fun checSubscribe(isActual:((Boolean)->Unit),isSucces:((ErrorEnum)->Unit)){
        viewModelScope.launch {
              transactionUseCase.checkSubscribeActual({errore->
                  isSucces.invoke(errore)
              },{ actual->
                  isActual.invoke(actual)
              })
        }
    }

}

