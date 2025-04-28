package com.example.myapplication

import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.random.Random

//import com.ayush.assignment.databinding.ActivityMainBinding

class NotificationHandler(private val context: Context) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    private val notificationChannelID = "notification_channel_id"
    //private lateinit var binding: ActivityMainBinding
    // SIMPLE NOTIFICATION
    fun showSimpleNotification(event: Event){
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(event.event_name)
            .setContentText(event.event_time)
            .setSmallIcon(R.drawable.getgrinnected_logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(false)
            .build()  // finalizes the creation

        notificationManager.notify(Random.nextInt(), notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun showSimpleNotificationDelay(event: Event){
        val notification = NotificationCompat.Builder(context, notificationChannelID)
            .setContentTitle(event.event_name)
            .setContentText(event.event_time)
            .setSmallIcon(R.drawable.getgrinnected_logo)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(false)
            .build()  // finalizes the creation
        Handler(Looper.getMainLooper()).postDelayed({
            notificationManager.notify(Random.nextInt(), notification)
        }, getDifferenceInMillis(
            LocalDateTime.now().toString().toDate("yyyy-MM-dd'T'HH:mm:ss.SSS"),
            event.event_end_time.toDate("yyyy-MM-dd'T'HH:mm:ss.SSS")))

    }
    /**
     * a function that turns a string into a date
     *
     * @param dateFormat string in date format
     * @param timeZone grabs the current timezone from your phone
     */
    fun String.toDate(
        dateFormat: String = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        timeZone: TimeZone = TimeZone.getTimeZone("UTC"),
    ): Date {
        val parser = SimpleDateFormat(dateFormat, Locale.getDefault())
        parser.timeZone = timeZone
        return parser.parse(this)
    }

    /**
     * a function that gets the time between an event and current time in miliseconds
     *
     * @param date1 the current time
     * @param date2 the time the event starts
     */
    fun getDifferenceInMillis(date1: Date, date2: Date): Long {
        val fifteenMinutesInMillis = 26 * 60 * 1000L // 900000L
        return (date2.time - date1.time) - fifteenMinutesInMillis
    }

    //@SuppressLint("ScheduleExactAlarm")
    fun scheduleNotification(event: Event) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("title", event.event_name)
            putExtra("message", event.event_time)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val delayInMillis = 60 * 1000L // <- fix this (1 minute)
        val triggerAtMillis = System.currentTimeMillis() + delayInMillis
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                intent.data = Uri.parse("package:${context.packageName}")
                context.startActivity(intent)
                return
            }
        }
*/
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerAtMillis,
            pendingIntent
        )
    }
}