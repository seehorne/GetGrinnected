package com.example.myapplication

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import kotlin.random.Random
//import com.ayush.assignment.databinding.ActivityMainBinding

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"
    //private lateinit var binding: ActivityMainBinding
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
/*
    @SuppressLint("ScheduleExactAlarm")

    private fun scheduleNotification() {
        // Create an intent for the Notification BroadcastReceiver
        val intent = Intent(applicationContext, Notification::class.java)

        // Extract title and message from user input
        val title = binding.title.text.toString()
        val message = binding.message.text.toString()

        // Add title and message as extras to the intent
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)

        // Create a PendingIntent for the broadcast
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Get the AlarmManager service
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Get the selected time and schedule the notification
        val time = getTime()
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )

        // Show an alert dialog with information
        // about the scheduled notification
        showAlert(time, title, message)
    }
    */
}