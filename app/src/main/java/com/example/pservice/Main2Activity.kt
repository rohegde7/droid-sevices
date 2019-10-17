package com.example.pservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main2.*

class Main2Activity : AppCompatActivity(R.layout.activity_main2) {

    private val foregroundIntent = Intent(this, MyForegroundService::class.java)
    private val mForegroundServiceIntent = Intent(this, MyForegroundService::class.java)
    private val myServiceIntent = Intent(this, MyService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        startMyService()
        initListeners()
    }

    fun startMyService() {
        startService(foregroundIntent)
    }

    fun initListeners() {

        btn_start_foreground.setOnClickListener {
            foregroundIntent.setAction("START_FOREGROUND")
            startService(mForegroundServiceIntent)
        }

        btn_stop_foreground.setOnClickListener {
            foregroundIntent.setAction("STOP_FOREGROUND")
            startService(foregroundIntent)
        }
    }
}
