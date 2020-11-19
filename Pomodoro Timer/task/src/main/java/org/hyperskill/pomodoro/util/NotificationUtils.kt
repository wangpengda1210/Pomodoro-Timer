package org.hyperskill.pomodoro.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import org.hyperskill.pomodoro.MainActivity
import org.hyperskill.pomodoro.R

const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(applicationContext: Context) {
    
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(applicationContext,
            NOTIFICATION_ID, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT)

    val builder = NotificationCompat.Builder(applicationContext,
            applicationContext.getString(R.string.channel_id))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("You need a rest!!!")
            .setContentText("It's time to stop")
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
    
    
    notify(NOTIFICATION_ID, builder.build())

}