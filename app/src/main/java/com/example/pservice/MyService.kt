package com.example.pservice

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.pservice.Util.JAI_HIND
import com.example.pservice.Util.PLAY

class MyService : Service() {

    private val TAG: String = "MyService"
    private val CHANNEL_ID = "MyServiceChannelID"
    private val CHANNEL_NAME = "MyServiceChannelName"

    val mMyBinder: Binder = MyServiceBinder()

    lateinit var mMyBoardCast: BroadcastReceiver
    lateinit var mIntentFilterForBroadcast: IntentFilter

    override fun onCreate() {
        super.onCreate()
        Log.e(TAG, "onCreate() service - Thread name: " + Thread.currentThread().name)

        initBroadCastReceiver()
    }

    private fun initBroadCastReceiver() {
        mMyBoardCast = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val play: Boolean? = intent?.getBooleanExtra(PLAY, false)

                if(play == true)
                    Log.e(TAG, "play == true")
                else Log.e(TAG, "play == false")
            }
        }

        mIntentFilterForBroadcast = IntentFilter(JAI_HIND)

        registerReceiver(mMyBoardCast, mIntentFilterForBroadcast)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand() - Thread name: " + Thread.currentThread().name)

//        stopSelf()

         return START_STICKY /*|| START_STICKY_COMPATIBILITY --> default*/
        // return START_NOT_STICKY
        // return START_REDELIVER_INTENT

//        return super.onStartCommand(intent, flags, startId)
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
        Log.e(TAG, "onDestroy - Thread name: " + Thread.currentThread().name)

        super.onDestroy()

        unregisterReceiver(mMyBoardCast)
    }

    fun doSomething() {
        Log.e(TAG, "doing something")
    }

    inner class MyServiceBinder : Binder() {
        fun getMyService(): MyService {
            return this@MyService
        }
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