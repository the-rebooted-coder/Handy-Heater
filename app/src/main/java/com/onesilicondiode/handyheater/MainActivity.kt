package com.onesilicondiode.handyheater

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import android.os.PowerManager
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        freeze.visibility = View.GONE

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun StartHeating(view: View) {
        Toast.makeText(this, "Heating Begins", Toast.LENGTH_SHORT).show()
        freeze.visibility = View.VISIBLE
        heat.visibility = View.GONE
        acquireWakeLock()
        startGPS();
        haptics()
    }

    private fun startGPS() {
        if (!checkPermission()) {
            requestPermission()
        }
        else {
            ContextCompat.startForegroundService(this, Intent(this, LocationService::class.java))

        }
    }
    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION)
        val result1 = ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
    }

    private fun acquireWakeLock() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )
    }

    fun stopHeating(view: View) {
        Toast.makeText(this, "Heating Stop", Toast.LENGTH_SHORT).show()
        freeze.visibility = View.GONE
        haptics()
        removeWakeLock();
        heat.visibility = View.VISIBLE

    }

    private fun removeWakeLock() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }

    private fun haptics() {
        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(32, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            //deprecated in API 26
            vibrator.vibrate(28)
        }
    }
}