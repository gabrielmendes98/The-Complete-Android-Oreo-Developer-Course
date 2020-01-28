package com.android.watchandphonefun

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.wearable.*

class MainActivity : AppCompatActivity(), DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        googleApiClient = GoogleApiClient.Builder(this)
            .addApi(Wearable.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()
    }

    override fun onDataChanged(dataEventBuffer: DataEventBuffer?) {
        dataEventBuffer?.forEach {
            if (it.type == DataEvent.TYPE_CHANGED) {
                val item = it.dataItem

                if(item.uri.path?.compareTo("/data") == 0){
                    val dataMap = DataMapItem.fromDataItem(item).dataMap
                    val friend = dataMap.getString("friend")

                    // Use Friend
                }
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
        Wearable.DataApi.addListener(googleApiClient, this)
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}
