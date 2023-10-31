package workwork.test.andropediagits.crashInspector.domain.repo

import androidx.lifecycle.LiveData

import workwork.test.andropediagits.crashInspector.data.local.CrashEntity
import workwork.test.andropediagits.crashInspector.data.remote.CrashInfoModel
import workwork.test.andropediagits.crashInspector.data.remote.CrashSendStatus

interface CrashRepo {

    suspend fun sendCrashAnalytic(crashInfoModels: List<CrashInfoModel>): CrashSendStatus

    suspend fun insertExceptionCrash(crashEntity: CrashEntity)


    suspend fun deleteExceptionCrash(crashEntity: CrashEntity)


    fun getAllCrash(): LiveData<List<CrashEntity>>

}