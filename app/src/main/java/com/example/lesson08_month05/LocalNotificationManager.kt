package com.example.lesson08_month05

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import javax.inject.Inject

class LocalNotificationManager @Inject constructor (private val application: Application){
    private val builderNotifications =NotificationCompat.Builder(application,Constants.CHANNEL_ID)
        .setSmallIcon(R.drawable.google1)
        .setContentTitle("My google maps")
        .setContentText("hallo")
        .setStyle(NotificationCompat.BigTextStyle())
        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    init {
        createChannel()
    }
    fun createNotification(){
       with(NotificationManagerCompat.from(application)){
           notify(Constants.NOTIFICATION_ID,builderNotifications.build())
        }
    }
    private fun createChannel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val channel =NotificationChannel(Constants.CHANNEL_ID,"Test channel",NotificationManager.IMPORTANCE_DEFAULT)
            val notificationManager:NotificationManager =application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
          notificationManager.createNotificationChannel(channel)
        }
    }
}