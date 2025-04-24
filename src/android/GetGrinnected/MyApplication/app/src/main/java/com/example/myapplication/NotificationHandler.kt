package com.example.myapplication

import android.app.NotificationManager
import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.core.app.NotificationCompat
import kotlin.random.Random

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"

    // SIMPLE NOTIFICATION
    fun showSimpleNotification(event: Event) {
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(event.event_name)
            .setContentText(event.event_time)
            .setSmallIcon(R.drawable.gg_logo_2)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(false)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }
}