package com.example.workingwithlists

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        val friends = arrayOf("Joe", "Mary", "Gabriel", "Gabriella", "Jureg", "Sarve")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, friends)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, friends[position], Toast.LENGTH_SHORT).show()
        }
    }
}
