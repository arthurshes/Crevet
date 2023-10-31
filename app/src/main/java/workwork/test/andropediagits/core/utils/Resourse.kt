package workwork.test.andropediagits.core.utils

sealed class Resourse<T>(
    val data:T?=null,
    val message:String?=null
){

    class Success<T>(data: T):Resourse<T>(data)

    class Loading<T>:Resourse<T>()

    class Error<T>(message: String):Resourse<T>(message = message)

}
