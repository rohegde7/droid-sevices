package com.example.pservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Gravity
import android.widget.Toast

class MyBroadcast: BroadcastReceiver() {

    val TAG = "MyBoardcast"

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e(TAG, "onReceive() on Thread name: " + Thread.currentThread().name)
    }
}