package workwork.example.andropediagits.domain.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.andropediagits.crashInspector.data.local.CrashEntity
import com.example.andropediagits.crashInspector.data.remote.CrashInfoModel
import com.example.andropediagits.crashInspector.data.remote.CrashSendStatus
import com.example.andropediagits.crashInspector.domain.repo.CrashRepo

class FakeCrashRepo:CrashRepo {
    private val crash = mutableListOf<CrashEntity>()

    override suspend fun sendCrashAnalytic(crashInfoModels: List<CrashInfoModel>): CrashSendStatus {
       return CrashSendStatus(true,"hello",122)
    }

    override suspend fun insertExceptionCrash(crashEntity: CrashEntity) {
        crash.add(crashEntity)
    }

    override suspend fun deleteExceptionCrash(crashEntity: CrashEntity) {
        crash.remove(crashEntity)
    }
    override fun getAllCrash(): LiveData<List<CrashEntity>> {
        val liveData = MutableLiveData<List<CrashEntity>>()
        liveData.value = crash.toList() // Создаем копию списка и передаем его через LiveData
        return liveData
    }
}