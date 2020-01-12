package com.android.sounddemo

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.Switch
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var audioManager: AudioManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mediaPlayer = MediaPlayer.create(this, R.raw.walking)
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        volumeSeekBar.max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        volumeSeekBar.progress = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        progressSeekBar.max = mediaPlayer.duration

        playSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                mediaPlayer.start()
                pauseButton.visibility = View.VISIBLE
            } else {
                mediaPlayer.seekTo(0)
                mediaPlayer.pause()
                pauseButton.visibility = View.INVISIBLE
                pauseButton.text = "pause"
            }
        }

        pauseButton.setOnClickListener {
            if (pauseButton.text == "pause") {
                pauseButton.text = "resume"
                mediaPlayer.pause()
            }
            else{
                pauseButton.text = "pause"
                mediaPlayer.start()
            }
        }

        volumeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        progressSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(fromUser)
                    mediaPlayer.seekTo(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                mediaPlayer.pause()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (pauseButton.text == "pause")
                    mediaPlayer.start()
            }

        })

        Timer().scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                progressSeekBar.progress = mediaPlayer.currentPosition
            }
        },0,300)

    }
}
