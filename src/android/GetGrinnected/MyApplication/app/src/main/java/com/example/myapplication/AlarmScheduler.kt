package com.example.myapplication

import android.app.PendingIntent

interface AlarmScheduler {
    val AlarmReceiver: Unit

    fun createPendingIntent(reminderItem: ReminderItem): PendingIntent

    fun schedule(reminderItem: ReminderItem)

    fun cancel(reminderItem: ReminderItem)
}