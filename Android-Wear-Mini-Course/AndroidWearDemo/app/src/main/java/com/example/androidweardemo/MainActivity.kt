package com.example.androidweardemo

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        button.setOnClickListener {
            if(resources.configuration.isScreenRound)
                Toast.makeText(this, "Round", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Square", Toast.LENGTH_SHORT).show()
        }
    }
}
