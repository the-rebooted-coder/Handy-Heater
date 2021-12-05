package com.onesilicondiode.handyheater

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

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
        haptics()
    }
    fun stopHeating(view: View) {
        Toast.makeText(this, "Heating Stop", Toast.LENGTH_SHORT).show()
        freeze.visibility = View.GONE
        haptics()
        heat.visibility = View.VISIBLE

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