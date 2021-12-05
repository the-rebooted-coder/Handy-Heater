package com.onesilicondiode.handyheater

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //do nothing, only proceed
        val toReturn = Intent(this, MainActivity::class.java)
        startActivity(toReturn)
        finish()
    }
}