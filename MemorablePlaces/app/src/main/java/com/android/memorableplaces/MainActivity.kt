package com.android.memorableplaces

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var arrayAdapter: ArrayAdapter<String>
        var locations = ArrayList<LatLng>()
        var addresses = ArrayList<String>()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        retriveInfo()
        val initialList = ArrayList<String>()
        initialList.add("Add new location...")

        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, initialList)

        listView.adapter = arrayAdapter
        arrayAdapter.addAll(addresses)
        
        listView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, MapsActivity :: class.java)
            intent.putExtra("itemNumber", position)
            startActivity(intent)
        }

    }

    private fun retriveInfo() {
        val sharedPreferences = this.getSharedPreferences(packageName, Context.MODE_PRIVATE)
//        sharedPreferences.edit().clear().apply()
        val gson = Gson()
        val jsonAddresses = sharedPreferences.getString("addresses", null)
        if (jsonAddresses != null) {
            val type = object : TypeToken<ArrayList<String>>() {}.type
            addresses = gson.fromJson(jsonAddresses, type)
        }

        val jsonLocations = sharedPreferences.getString("locations", null)
        if (jsonLocations != null) {
            val type = object : TypeToken<ArrayList<LatLng>>() {}.type
            locations = gson.fromJson(jsonLocations, type)
        }
    }

}
