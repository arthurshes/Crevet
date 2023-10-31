package workwork.test.andropediagits.core.mappers


import workwork.test.andropediagits.data.local.entities.SubscribeEntity
import workwork.test.andropediagits.data.remote.model.SubscribeModel
import java.util.Date

fun SubscribeModel.toSubscribeEntity(): SubscribeEntity {
    return SubscribeEntity(
        token = token,
        date = dateBuyForDate ?: Date(),
        transactionId = transactionId,
        term = term
    )
}