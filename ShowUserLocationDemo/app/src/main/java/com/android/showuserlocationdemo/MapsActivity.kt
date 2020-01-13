package com.android.showuserlocationdemo

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
import java.lang.Exception
import java.util.*

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

        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location?) {
                mMap.clear()
                val myLocation = LatLng(location!!.latitude, location.longitude)
                mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
                mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))

                val geocoder = Geocoder(applicationContext, Locale.getDefault())
                try {
                    val addressesList = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    var address = ""

                    if(addressesList[0].getAddressLine(0) != null && addressesList[0].getAddressLine(0).isNotEmpty()) {
                        address = addressesList[0].getAddressLine(0)
                    } else {
                        if (addressesList != null && addressesList.size > 0) {
                            if (addressesList[0].thoroughfare != null)
                                address += "${addressesList[0].thoroughfare}, "
                            if (addressesList[0].locality != null)
                                address += "${addressesList[0].locality} - "
                            if (addressesList[0].adminArea != null)
                                address += "${addressesList[0].adminArea}, "
                            if (addressesList[0].countryName != null)
                                address += "${addressesList[0].countryName}."
                        }
                    }

                    if (address != "") Toast.makeText(applicationContext, address, Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {

            }

            override fun onProviderEnabled(provider: String?) {

            }

            override fun onProviderDisabled(provider: String?) {

            }
        }

        if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, locationListener)
            val lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            mMap.clear()
            val myLocation = LatLng(lastKnownLocation!!.latitude, lastKnownLocation.longitude)
            mMap.addMarker(MarkerOptions().position(myLocation).title("My Location"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation))
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0f, locationListener)
        }

    }

}
