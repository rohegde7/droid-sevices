package com.example.pservice

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.pservice.Util.JAI_HIND
import com.example.pservice.Util.PLAY
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val TAG = "MAIN ACTIVITY"

    var mServiceConnection: ServiceConnection? = null
    var mIntentServiceConnection: MyIntentService? = null

    var mMyService: MyService? = null
    var mMyIntentService: MyIntentService? = null

    var isServiceBound = false
    var isIntentServiceBound = false

    lateinit var mServiceIntent: Intent
    lateinit var mForegroundServiceIntent: Intent
    lateinit var mIntentServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e(TAG, "onCreate --> Thread name: " + Thread.currentThread().name)

        super.onCreate(savedInstanceState)

        initServices()

        checkPermissions()
    }
    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.RECEIVE_SMS
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permission was denied")
                    .setMessage("Permission needed")
                    .setPositiveButton("Ok", { _, _ ->
                        requestPermission()
                    })
                    .show()

            } else {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.FOREGROUND_SERVICE), 7
        )
    }

    private fun initServices() {
        mServiceIntent = Intent(this, MyService::class.java)
        mForegroundServiceIntent = Intent(this, MyForegroundService::class.java)
        mIntentServiceIntent = Intent(this, MyIntentService::class.java)
    }

    public fun onClick(view: View) {
        val resourceID = view.id

        when (resourceID) {

            R.id.start_activity -> startMain2Activity()
            R.id.btn_start_service -> startService()
            R.id.btn_stop_service -> stopService()
            R.id.btn_start_foreground_service -> startForegroundService()
            R.id.btn_stop_foreground_service -> stopForegroundService()
            R.id.btn_bind_service -> bindService()
            R.id.btn_unbind_service -> unbindService()
            R.id.btn_start_intent_service -> startIntentService()
            R.id.btn_play_true_broadcast -> callTrueBroadcast()
            R.id.btn_play_false_broadcast -> callFalseBroadcast()
            R.id.btn_do_something_in_service -> doSomethingWithService()

            else -> showSnackBar("Nothingggg....")
        }
    }

    private fun startService() {
        startService(mServiceIntent)
    }

    private fun stopService() {
        stopService(mServiceIntent)
    }

    private fun startForegroundService() {
        startService(mForegroundServiceIntent)
    }

    private fun stopForegroundService() {
        stopService(mForegroundServiceIntent)
    }

    private fun startIntentService() {
        startService(mIntentServiceIntent)
    }

    private fun callTrueBroadcast() {
        val broadcastIntent = Intent(JAI_HIND)
        broadcastIntent.putExtra(PLAY, true)
        sendBroadcast(broadcastIntent)
    }

    private fun callFalseBroadcast() {
        val broadcastIntent = Intent(JAI_HIND)
        broadcastIntent.putExtra(PLAY, false)
        sendBroadcast(broadcastIntent)
    }

    private fun startMain2Activity() {
        startActivity(Intent(this, Main2Activity::class.java))
    }

    private fun bindService() {
        initServiceConnection()
        bindService(mServiceIntent, mServiceConnection!!, Context.BIND_AUTO_CREATE)
    }

    private fun initServiceConnection() {
        if (mServiceConnection == null)
            mServiceConnection = object : ServiceConnection {

                override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
                    val myServiceBinder = iBinder as MyService.MyServiceBinder
                    mMyService = myServiceBinder.getMyService()

                    isServiceBound = true

                    showSnackBar("Service Binded")
                }

                override fun onServiceDisconnected(componentName: ComponentName) {
                    isServiceBound = false

                    showSnackBar("Service Unbinded")
                }
            }
    }

    private fun unbindService() {
        if (isServiceBound) {
            unbindService(mServiceConnection!!)
            showSnackBar("Service to be unbinded")
        }
    }

    private fun showSnackBar(msg: String) {
        Snackbar.make(nested_scroll_view_root, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun doSomethingWithService() {
        mMyService?.doSomething()
    }

    override fun onDestroy() {
        super.onDestroy()

        Log.e(TAG, "onDestroy")
    }
}
