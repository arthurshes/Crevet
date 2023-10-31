package workwork.test.andropediagits.core.utils

sealed class ErrorLamdaRes(val message:String?=null){
    class Success:ErrorLamdaRes()
    class Error(message: String):ErrorLamdaRes(message)
}
