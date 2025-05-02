package com.example.myapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent


abstract class NotificationAlarmScheduler(
    private val context: Context, override val AlarmReceiver: Unit
) : AlarmScheduler {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    override fun createPendingIntent(reminderItem: ReminderItem): PendingIntent {
        val intent = Intent(context, AlarmReceiver::class.java)

        return PendingIntent.getBroadcast(
            context,
            reminderItem.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    override fun schedule(reminderItem: ReminderItem) {
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            reminderItem.time,
            AlarmManager.INTERVAL_DAY,
            createPendingIntent(reminderItem)
        )
    }

    override fun cancel(reminderItem: ReminderItem) {
        alarmManager.cancel(
            createPendingIntent(reminderItem)
        )
    }

    companion object

}

