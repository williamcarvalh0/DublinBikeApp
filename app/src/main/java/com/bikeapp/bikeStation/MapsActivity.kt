package com.bikeapp.bikeStation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bikeapp.bikeStation.model.BikeStation
import com.bikeapp.bikeStation.model.Position
import com.bikeapp.bikeStation.model.Stations

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException
import kotlin.collections.listOf as listOf1

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var listOfBikeStations: List<BikeStation>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        retrieveFavourites()
    }

    override fun onResume() {
        super.onResume()
        Log.i(getString(R.string.MAPLOGGING), "onResume")
        retrieveFavourites()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getBikeStationJsonData()
        setMarkerListener()

    }
    fun getBikeStationJsonData() {

        Log.i(getString(R.string.MAPLOGGING), "Loading JSON data")
        val url: String = "https://api.jcdecaux.com/vls/v1/stations?contract=dublin&apiKey=11eb674a4b9d0b0a5ddef706808e9f512a795334"
        Log.i(getString(R.string.MAPLOGGING), url)
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                //   TODO("Not yet implemented")
                Log.i(getString(R.string.MAPLOGGING), "JSON HTTP CALL FAILED")
            }

            override fun onResponse(call: Call, response: Response) {
                // TODO("Not yet implemented")
                Log.i(getString(R.string.MAPLOGGING), "JSON HTTP CALL SUCCEEDED")

                val body = response?.body?.string()
                //  println("json loading" + body)
                Log.i(getString(R.string.MAPLOGGING), body)
                var jsonBody = "{\"stations\": " + body + "}"

                val gson = GsonBuilder().create()
                listOfBikeStations = gson.fromJson(jsonBody, Stations::class.java).stations

                renderListOfBikeStationMarkers()
            }
        })
    }

    fun renderListOfBikeStationMarkers() {
        runOnUiThread {
            listOfBikeStations.forEach {
                val position = LatLng(it.position.lat, it.position.lng)
                var marker1 = mMap.addMarker(
                    MarkerOptions().position(position).title("Marker in ${it.address}")
                )
                marker1.setTag(it.number)
                Log.i(getString(R.string.MAPLOGGING), it.address)
            }
            val centreLocation = LatLng(53.349562, -6.278198)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centreLocation, 16.0f))
        }
    }

    fun setMarkerListener() {
        mMap.setOnMarkerClickListener { marker ->
            Log.i(getString(R.string.MAPLOGGING), getString(R.string.MAKERCLICKED))
            Log.i(
                getString(R.string.MAPLOGGING),
                "Marker id (tag) is " + marker.getTag().toString()
            )
            if (marker.isInfoWindowShown) {
                marker.hideInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            val intent = Intent(this, StationActivity::class.java)
            intent.putExtra("lat", marker.position.latitude.toString())
            intent.putExtra("id", marker.getTag().toString())
            intent.putExtra("lng", marker.position.longitude.toString())
            startActivity(intent)
            true
        }
    }

    fun addMarkers() {
        val smithfield = LatLng(53.349562, -6.278198)
        var marker1 =
            mMap.addMarker(MarkerOptions().position(smithfield).title("Marker in smithfield"))
        marker1.setTag(42)

        val Parnell = LatLng(53.353462, -6.265305)
        var marker2 = mMap.addMarker(MarkerOptions().position(Parnell).title("Marker in Parnell"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Parnell))
        marker2.setTag(30)

        val Clonmel = LatLng(53.336021, -6.26298)
        var marker3 = mMap.addMarker(MarkerOptions().position(Clonmel).title("Marker in Clonmel"))

       listOfBikeStations = listOf1(
           BikeStation(
               48,
               "Excise Walk",
               Position(53.347777, -6.244239)
           )
       )
        listOfBikeStations.forEach {
            val position = LatLng(it.position.lat, it.position.lng)
            var marker1 =
                mMap.addMarker(MarkerOptions().position(position).title("Marker in ${it.address}"))
            marker1.setTag(it.number)
            Log.i(getString(R.string.MAPLOGGING), it.address)
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Clonmel, 16.0f))
    }

    fun retrieveFavourites() {
        Log.i(getString(R.string.MAPLOGGING), "Marker Preferences are loaded")
        var prefs = getSharedPreferences("com.example.myapplication.googlemap.dublinbike", Context.MODE_PRIVATE)
        var markers = prefs.getStringSet("stationmarkers", setOf())?.toMutableSet()
        markers?.forEach{m ->  Log.i(getString(R.string.MAPLOGGING), "Favourite Marker: ${m}")
        }
    }
}