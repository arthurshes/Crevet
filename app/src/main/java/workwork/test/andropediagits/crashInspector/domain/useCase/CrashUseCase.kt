package workwork.test.andropediagits.crashInspector.domain.useCase

import android.os.Build

import okio.IOException
import retrofit2.HttpException
import workwork.test.andropediagits.crashInspector.core.mappers.toCrashInfoModel
import workwork.test.andropediagits.crashInspector.data.local.CrashEntity
import workwork.test.andropediagits.crashInspector.data.remote.CrashInfoModel
import workwork.test.andropediagits.crashInspector.domain.repo.CrashRepo
import java.util.Date
import java.util.concurrent.TimeoutException
import javax.inject.Inject

class CrashUseCase @Inject constructor(val crashRepo: CrashRepo) {

    suspend fun sendCrash(className:String,exception:String){
        val brandPhone = Build.MANUFACTURER
        try {
            val crashInfoModel = CrashInfoModel(
                className = className,
                exception = exception,
                dateCrash = Date(),
                brandPhone = brandPhone
            )
            val crashList = ArrayList<CrashInfoModel>()
            crashList.add(crashInfoModel)
            crashRepo.sendCrashAnalytic(
                crashList
            )
        }catch (e:IOException){
            val crashEntity = CrashEntity(
                className = className,
                exception = exception,
                dateCrash = Date(),
                brandPhone = brandPhone
            )
            crashRepo.insertExceptionCrash(crashEntity)
        }catch (e:HttpException){
            val crashEntity = CrashEntity(
                className = className,
                exception = exception,
                dateCrash = Date(),
                brandPhone = brandPhone
            )
            crashRepo.insertExceptionCrash(crashEntity)
        }catch (e:TimeoutException){
            val crashEntity = CrashEntity(
                className = className,
                exception = exception,
                dateCrash = Date(),
                brandPhone = brandPhone
            )
            crashRepo.insertExceptionCrash(crashEntity)
        }catch (e:Exception){
            val crashEntity = CrashEntity(
                className = className,
                exception = exception,
                dateCrash = Date(),
                brandPhone = brandPhone
            )
            crashRepo.insertExceptionCrash(crashEntity)
        }
    }

    suspend fun checkLocalCrashAndSend(){
        val crashInfoLocal = crashRepo.getAllCrash().value
        crashInfoLocal?.let { crashLocal->
            val crashArrayList = ArrayList<CrashInfoModel>()
            crashLocal.forEach { oneCras->
                crashArrayList.add(oneCras.toCrashInfoModel())
            }
            crashRepo.sendCrashAnalytic(crashArrayList)
        }
    }

}