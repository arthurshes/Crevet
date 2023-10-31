package workwork.test.andropediagits.core.utils

import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.Timer

object CustomTimerUtil{

    private var _timerValue: MutableLiveData<Long> = MutableLiveData()
    var timerValue: LiveData<Long> = _timerValue
    private
    var timer: CountDownTimer?=null
    fun startTimer(timeSeconds: Long, isEnding: (() -> Unit)? = null) {
        timer = object : CountDownTimer(timeSeconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {            _timerValue.value = millisUntilFinished / 1000        }        override fun onFinish() {            isEnding?.invoke()        }    }
        timer?.start()
    }

//    fun startTimer(timeSeconds:Long,isEnding:((Boolean)->Unit)){

    fun stopTimer(){
        timer?.cancel()
    }

}