package com.example.pictureinpicturedemo

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            enterPictureInPictureMode()
        }
    }

    override fun onPictureInPictureModeChanged(
        isInPictureInPictureMode: Boolean,
        newConfig: Configuration?
    ) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)

        if(isInPictureInPictureMode){
            button.visibility = View.INVISIBLE
            supportActionBar?.hide()
            textView.text = "We are in picture in picture mode"
        } else {
            button.visibility = View.VISIBLE
            supportActionBar?.show()
            textView.text = "Hello World!"
        }
    }

}
