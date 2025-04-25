package com.example.myapplication

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

// BroadcastReceiver for handling notifications
class Notification : BroadcastReceiver() {

    // Method called when the broadcast is received
    override fun onReceive(context: Context, intent: Intent) {

        // Build the notification using NotificationCompat.Builder
        val notification = NotificationCompat.Builder(context, channelID)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(intent.getStringExtra(titleExtra)) // Set title from intent
            .setContentText(intent.getStringExtra(messageExtra)) // Set content text from intent
            .build()

        // Get the NotificationManager service
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Show the notification using the manager
        manager.notify(notificationID, notification)
    }
}