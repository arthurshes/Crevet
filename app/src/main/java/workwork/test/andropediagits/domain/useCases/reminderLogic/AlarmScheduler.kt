package workwork.test.andropediagits.domain.useCases.reminderLogic

interface AlarmScheduler {

    fun schedule(item: AlarmItem)
    fun cancel(item: AlarmItem)

}