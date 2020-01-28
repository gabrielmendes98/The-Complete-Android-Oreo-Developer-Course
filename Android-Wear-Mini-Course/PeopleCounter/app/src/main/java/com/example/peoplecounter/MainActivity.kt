package com.example.peoplecounter

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : WearableActivity() {

    private var counter = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()

        addButton.setOnClickListener {
            counter++
            countTextView.text = counter.toString()
        }

        resetButton.setOnClickListener {
            counter = 0
            countTextView.text = counter.toString()
        }
    }
}
