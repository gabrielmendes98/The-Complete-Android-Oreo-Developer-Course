package com.android.bluetoothapp

import android.app.DownloadManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val REQUEST_ENABLE_BT = 1
    private val bluetoothDevices = ArrayList<String>()
    private val addresses = ArrayList<String>()
    private  lateinit var arrayAdapter: ArrayAdapter<String>

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent!!.action
            Log.i("Action", action)

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action){
                statusTextView.text = "Finished"
                searchButton.isEnabled = true
            }
            if (BluetoothDevice.ACTION_FOUND == action) {
                val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                val name = device!!.name
                val address = device.address
                val rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE)

                if(!addresses.contains(address)){
                    addresses.add(address)
                    var deviceString = ""
                    if(name.isNotEmpty()) {
                        deviceString = "$address - RSSI: $rssi dBm"
                    } else {
                        deviceString = "$name - RSSI: $rssi dBm"
                    }

                    bluetoothDevices.add(deviceString)
                    arrayAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(bluetoothAdapter == null) {
            Toast.makeText(this, "This device does not support bluetooth", Toast.LENGTH_SHORT).show()
            return
        }
        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }

        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, bluetoothDevices)
        listView.adapter = arrayAdapter


        val intentFilter = IntentFilter()
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED)
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
        registerReceiver(broadcastReceiver, intentFilter)

        searchButton.setOnClickListener {
            statusTextView.text = "Searching..."
            searchButton.isEnabled = false
            bluetoothDevices.clear()
            addresses.clear()
            bluetoothAdapter.startDiscovery()
        }
    }
}
