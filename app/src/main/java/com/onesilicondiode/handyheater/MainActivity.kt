package com.onesilicondiode.handyheater

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    fun StartHeating(view: View) {
        freeze.setTransitionVisibility(View.VISIBLE)


        heat.setTransitionVisibility(View.INVISIBLE)

    }
    fun stopHeating(view: View) {}
}