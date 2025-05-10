package com.GetGrinnected.myapplication

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

// Constants for notification
const val notificationID = 121
const val channelID = "channel1"
const val titleExtra = "titleExtra"
const val messageExtra = "messageExtra"



class NotificationReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "your_channel_id")
            .setContentTitle("test")
            .setContentText("This notification was scheduled!")
            .setSmallIcon(R.drawable.getgrinnected_logo)
            .build()

        notificationManager.notify(1, notification)
    }
}