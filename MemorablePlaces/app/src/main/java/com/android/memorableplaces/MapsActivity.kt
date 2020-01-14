package com.android.memorableplaces

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val position = intent.getIntExtra("itemNumber", -1)
        if (position == 0) {
            locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationListener = object : LocationListener {
                override fun onLocationChanged(location: Location?) {
                    centerMapOnLocation(location, "My Location")
                }

                override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                override fun onProviderEnabled(provider: String?) {}
                override fun onProviderDisabled(provider: String?) {}
            }

            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
            } else {
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                mMap.clear()
                val myLocation = LatLng(lastKnownLocation!!.latitude, lastKnownLocation.longitude)
                mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
            }

            mMap.setOnMapLongClickListener {
                addLocation(it)
            }

        }
        else {
            val location = Location("")
            location.latitude = MainActivity.locations[position-1].latitude
            location.longitude = MainActivity.locations[position-1].longitude
            centerMapOnLocation(location, MainActivity.addresses[position-1])
        }
    }

    private fun addLocation(location: LatLng) {

        val geocoder = Geocoder(applicationContext, Locale.getDefault())
        val addressesList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
        var address = ""

        if(addressesList[0].getAddressLine(0) != null && addressesList[0].getAddressLine(0).isNotEmpty()) {
            address = addressesList[0].getAddressLine(0)
        }
        if (address == "") {
            val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss", Locale.getDefault())
            address += sdf.format(Date())
        }

        MainActivity.addresses.add(address)
        MainActivity.locations.add(location)
        MainActivity.arrayAdapter.add(address)
        MainActivity.arrayAdapter.notifyDataSetChanged()
        saveInfo(address)

        Toast.makeText(applicationContext, "Added", Toast.LENGTH_SHORT).show()
        mMap.addMarker(MarkerOptions().position(location).title(address))
    }

    private fun centerMapOnLocation(location: Location?, title: String) {
        val locationToPlace = LatLng(location!!.latitude, location.longitude)
        mMap.clear()
        mMap.addMarker(MarkerOptions().position(locationToPlace).title(title))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locationToPlace, 15f))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                mMap.clear()
                val myLocation = LatLng(lastKnownLocation!!.latitude, lastKnownLocation.longitude)
                mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f))
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
            }
        }
    }

    private fun saveInfo(address: String) {
        val sharedPreferences = this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        var json: String

        json = gson.toJson(MainActivity.addresses)
        editor.putString("addresses", json).apply()

        json = gson.toJson(MainActivity.locations)
        editor.putString("locations", json).apply()
    }

}
