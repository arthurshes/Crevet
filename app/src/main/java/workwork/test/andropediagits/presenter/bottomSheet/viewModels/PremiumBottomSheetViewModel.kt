package workwork.test.andropediagits.presenter.bottomSheet.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import workwork.test.andropediagits.core.exception.ErrorEnum
import workwork.test.andropediagits.domain.useCases.transactionLogic.TransactionUseCase
import javax.inject.Inject

@HiltViewModel
class PremiumBottomSheetViewModel @Inject constructor(private val transactionUseCase: TransactionUseCase): ViewModel(){

    fun buySubscribe(isSuccess:((ErrorEnum)->Unit), subsTerm:Int){
        viewModelScope.launch(Dispatchers.IO) {
            transactionUseCase.subscribeBuy(subsTerm,isSuccess)
        }
    }

}