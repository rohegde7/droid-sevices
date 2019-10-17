package com.example.pservice

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyIntentService : IntentService("MyIntentService") {

    private val TAG = "MyIntentService"
    private val CHANNEL_ID = "MyIntentServiceChannelID"
    private val CHANNEL_NAME = "MyIntentServiceChannelName"

    override fun onCreate() {
        Log.e(TAG, "onCreate - Thread name: " + Thread.currentThread().name)
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand - Thread name: " + Thread.currentThread().name)

        // TODO:
        //      1. Start foreground service and observe behaviour
        startForegroundServiceWithNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG, "onBind - Thread name: " + Thread.currentThread().name)
        return super.onBind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind - Thread name: " + Thread.currentThread().name)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy - Thread name: " + Thread.currentThread().name)
        super.onDestroy()
    }

    override fun onHandleIntent(intent: Intent?) {
        Log.e(TAG, "onHandleIntent - Thread name: " + Thread.currentThread().name)

        // TODO :
        //      1. Run the Log.e() inside while(true)
        //      2. Create another thread and run Log.e() inside while(true)
    }

    fun startForegroundServiceWithNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Content Text")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)

        }
//        notificationManager.notify(NOTIFICATION_ID, notification)

        startForeground(1, notification)
    }
}