package workwork.test.andropediagits.core.exception

import okio.IOException
import retrofit2.HttpException

class ExceptionCustomHandle {
    fun <T> handleException(tryBlock: () -> T, catchBlock: (Exception) -> Unit,catchHttpBlock:(HttpException)->Unit,catchIoBlock:(IOException)->Unit): T? {
        return try {
            tryBlock()
        } catch (e:HttpException){
            catchHttpBlock(e)
            null
        } catch (e:IOException){
            catchIoBlock(e)
            null
        }
    }
}



