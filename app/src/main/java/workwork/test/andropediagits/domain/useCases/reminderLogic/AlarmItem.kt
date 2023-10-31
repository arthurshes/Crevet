package workwork.test.andropediagits.domain.useCases.reminderLogic

import android.os.Message
import java.time.LocalDateTime

data class AlarmItem(
    val time:LocalDateTime,
    val message: String
)
