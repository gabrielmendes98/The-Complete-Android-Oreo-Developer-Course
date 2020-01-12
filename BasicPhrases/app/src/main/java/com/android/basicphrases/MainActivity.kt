package com.android.basicphrases

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    fun playPhrase(view: View){
        val clicked = view as Button
        val mediaPlayer = MediaPlayer.create(this, resources.getIdentifier(clicked.tag.toString(), "raw", packageName))
        mediaPlayer.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
