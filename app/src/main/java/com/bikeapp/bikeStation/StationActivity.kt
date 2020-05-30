package com.bikeapp.bikeStation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_station.*

class StationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_station)

        var intent = intent;
        var stationLatitude = intent.getStringExtra("lat")
        var stationLongitude = intent.getStringExtra("lng")
        var stationId = intent.getStringExtra("id")

        twPosition.text = stationId
        twLat.text = stationLatitude
        twLong.text = stationLongitude

        btSavePref.setOnClickListener {
            // create shared preferences file
            val pref = getPreferences(Context.MODE_PRIVATE)
            val editor = pref.edit()

            //save lat, long and position
            editor.putString("lat", twLat.text.toString())
            editor.putString("long", twLong.text.toString())
            editor.putString("position", twPosition.text.toString())
            editor.commit()

            val toast = Toast.makeText(applicationContext, "Station Saved", Toast.LENGTH_LONG)
            toast.setGravity(Gravity.TOP, 0,140)
            toast.show()
        }

//
    }


}