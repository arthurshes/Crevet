package workwork.test.andropediagits.crashInspector.data.repo

import androidx.lifecycle.LiveData

import workwork.test.andropediagits.crashInspector.data.local.CrashEntity
import workwork.test.andropediagits.crashInspector.data.local.CrashMainDb
import workwork.test.andropediagits.crashInspector.data.remote.CrashApiService
import workwork.test.andropediagits.crashInspector.data.remote.CrashInfoModel
import workwork.test.andropediagits.crashInspector.data.remote.CrashSendStatus
import workwork.test.andropediagits.crashInspector.domain.repo.CrashRepo
import javax.inject.Inject

class CrashRepoImpl @Inject constructor(val crashApiService: CrashApiService, crashMainDb: CrashMainDb):
    CrashRepo {

    private val crashDao = crashMainDb.getCrashDao()

    override suspend fun sendCrashAnalytic(crashInfoModels: List<CrashInfoModel>): CrashSendStatus {
        return crashApiService.sendCrashAnalytic(crashInfoModels)
    }

    override suspend fun insertExceptionCrash(crashEntity: CrashEntity) {
        crashDao.insertExceptionCrash(crashEntity)
    }

    override suspend fun deleteExceptionCrash(crashEntity: CrashEntity) {
        crashDao.deleteExceptionCrash(crashEntity)
    }

    override fun getAllCrash(): LiveData<List<CrashEntity>> {
        return crashDao.getAllCrash()
    }

}