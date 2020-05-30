package com.bikeapp.bikeStation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       imStations.setOnClickListener {
           openMapsActivity()
        }

        imPreferences.setOnClickListener {
            openPreferencesActivity()
        }
    }

    private fun openMapsActivity() {
        val intent = Intent (this, MapsActivity::class.java)
        startActivity(intent)
    }

    private fun openPreferencesActivity() {
        val intent = Intent (this, PreferencesActivity::class.java)
        startActivity(intent)
    }
}
