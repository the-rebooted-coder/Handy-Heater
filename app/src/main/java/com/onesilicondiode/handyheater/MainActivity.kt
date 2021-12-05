package com.onesilicondiode.handyheater

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.onesilicondiode.handyheater.LocationService
import kotlinx.android.synthetic.main.activity_main.*

@SuppressLint("StaticFieldLeak")
private var cpuTemp: TextView? = null
private var sensorManager: SensorManager? = null
private var tempSensor: Sensor? = null
private var isTemperatureSensorAvailable: Boolean? = null


class MainActivity : AppCompatActivity(),SensorEventListener {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        freeze.visibility = View.GONE
        uselessFlame.visibility = View.GONE
        setTemps()
    }

    private fun setTemps() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        if (sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            tempSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)
            isTemperatureSensorAvailable = true
        } else {
            cpuTemp!!.text = getString(R.string.sensor_not_avail)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun StartHeating(view: View) {
        Toast.makeText(this, "Heating Begins", Toast.LENGTH_SHORT).show()
        freeze.visibility = View.VISIBLE
        heat.visibility = View.GONE
        acquireWakeLock()
        startGPS()
        haptics()
        uselessFlame.visibility = View.VISIBLE
        uselessFlame.playAnimation()
        if(uselessSnow.isAnimating){
            uselessSnow.visibility = View.GONE
        }
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
        removeWakeLock()
        heat.visibility = View.VISIBLE
        uselessFlame.visibility = View.GONE
        uselessSnow.visibility = View.VISIBLE
        uselessSnow.playAnimation()
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

    @SuppressLint("SetTextI18n")
    override fun onSensorChanged(sensorEvent: SensorEvent) {
        cpuTemp!!.text = sensorEvent.values[0].toString() + "°" + "C"
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
    override fun onResume() {
        super.onResume()
        if (isTemperatureSensorAvailable == true) {
            sensorManager!!.registerListener(this, tempSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        if (isTemperatureSensorAvailable == true) {
            sensorManager!!.unregisterListener(this)
        }
    }
}