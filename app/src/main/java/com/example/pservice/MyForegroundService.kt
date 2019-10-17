package com.example.pservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class MyForegroundService : Service() {

    private val TAG = "MyForegroundService"
    private val CHANNEL_ID = "MyForegroundServiceChannel"
    private val CHANNEL_NAME = "MyForegroundServiceChannelName"
    private val NOTIFICATION_ID = 200

    val mMyBinder: Binder = MyForegroundServiceBinder()

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate() service - Thread ID: " + Thread.currentThread().id)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForegroundServiceWithNotification()

        Log.e(TAG, "onStartCommand - Thread ID: " + Thread.currentThread().id)
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        Log.e(TAG, "onBind")

        return mMyBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.e(TAG, "onUnbind")

        return super.onUnbind(intent)
    }

    override fun onRebind(intent: Intent?) {
        Log.e(TAG, "onRebind")

        super.onRebind(intent)
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy  - Thread ID: " + Thread.currentThread().id)
        super.onDestroy()
    }

    inner class MyForegroundServiceBinder: Binder() {
        fun getMyService(): MyForegroundService {
            return this@MyForegroundService
        }
    }

    fun startForegroundServiceWithNotification() {
        val notificationIntent = Intent(this, MainActivity::class.java)

        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText("Content Text")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Button1", null)
            .addAction(R.drawable.ic_launcher_background, "Button2", null)
            .addAction(R.drawable.ic_launcher_foreground, "Button3", null)
            .addAction(R.drawable.ic_launcher_foreground, "Action Button4", null)
            .build()

        val notificationManager =
            application.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
//        notificationManager.notify(NOTIFICATION_ID, notification)

        startForeground(1, notification)
    }
}