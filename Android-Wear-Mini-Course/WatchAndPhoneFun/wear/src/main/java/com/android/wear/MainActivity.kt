package com.android.wear

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.PendingResult
import com.google.android.gms.wearable.DataApi
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable

class MainActivity : WearableActivity(), DataApi.DataListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val friend = "Gabriel"

        val putDataMapRequest = PutDataMapRequest.create("/data")
        putDataMapRequest.dataMap.putString("friend", friend)

        val putDataRequest = putDataMapRequest.asPutDataRequest()
        val pendingResult = Wearable.DataApi.putDataItem(googleApiClient, putDataRequest)

        // Enables Always-on
        setAmbientEnabled()
    }

    override fun onDataChanged(p0: DataEventBuffer?) {

    }

    override fun onConnected(p0: Bundle?) {

    }

    override fun onConnectionSuspended(p0: Int) {

    }

    override fun onConnectionFailed(p0: ConnectionResult) {

    }
}
