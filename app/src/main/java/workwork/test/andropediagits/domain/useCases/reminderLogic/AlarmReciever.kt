package workwork.test.andropediagits.domain.useCases.reminderLogic

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReciever:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val message = intent?.getStringExtra("EXTRA_MESSAGE") ?: return

    }
}