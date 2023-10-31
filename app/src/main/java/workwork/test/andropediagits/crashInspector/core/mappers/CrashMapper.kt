package workwork.test.andropediagits.crashInspector.core.mappers


import workwork.test.andropediagits.crashInspector.data.local.CrashEntity
import workwork.test.andropediagits.crashInspector.data.remote.CrashInfoModel

fun CrashInfoModel.toCrashEntity(): CrashEntity {
    return CrashEntity(className = className, dateCrash = dateCrash,
    exception = exception, brandPhone = brandPhone)
}

fun CrashEntity.toCrashInfoModel():CrashInfoModel{
    return CrashInfoModel(
        className, dateCrash, exception,brandPhone
    )
}