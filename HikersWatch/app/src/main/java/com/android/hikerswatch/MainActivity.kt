package com.android.hikerswatch

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                try {
                    val latitude = location!!.latitude.toString()
                    val longitude = location.longitude.toString()
                    val altitude = location.altitude.toString()
                    val accuracy = location.accuracy

                    val geocoder = Geocoder(applicationContext, Locale.getDefault())
                    val addressesList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    var address = ""
                    if (addressesList != null && addressesList.size > 0) {
                        if (addressesList[0].thoroughfare != null)
                            address += "${addressesList[0].thoroughfare}, "
                        if (addressesList[0].locality != null)
                            address += "${addressesList[0].locality} - "
                        if (addressesList[0].adminArea != null)
                            address += "${addressesList[0].adminArea}, "
                        if (addressesList[0].countryName != null)
                            address += addressesList[0].countryName
                    }

                    infoTextView.text = "Latitude: $latitude\nLongitude: $longitude\nAltitude: $altitude\nAddress: $address\nAccuracy: $accuracy"
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()
                    if(ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
                }


            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }

        Toast.makeText(applicationContext, "Loading", Toast.LENGTH_SHORT).show()

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5f, locationListener)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 5f, locationListener)
        }
    }
}
