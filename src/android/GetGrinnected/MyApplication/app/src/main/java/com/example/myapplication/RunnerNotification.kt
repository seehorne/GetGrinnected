package com.example.myapplication

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

class RunnerNotifier(
    private val notificationManager: NotificationManager,
    private val context: Context
) : Notifier(notificationManager) {

    override val notificationChannelId: String = "runner_channel_id"
    override val notificationChannelName: String = "Running Notification"
    override val notificationId: Int = 200

    override fun buildNotification(): Notification {
        return NotificationCompat.Builder(context, notificationChannelId)
            .setContentTitle(getNotificationTitle())
            .setContentText(getNotificationMessage())
            .setSmallIcon(android.R.drawable.btn_star)
            .build()
    }

    override fun getNotificationTitle(): String {
        return "Time to go for a run 🏃‍️"
    }

    override fun getNotificationMessage(): String {
        return "You are ready to go for a run?"
    }
}