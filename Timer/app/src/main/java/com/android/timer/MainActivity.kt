package com.android.timer

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var countDownTimer: CountDownTimer
    private var counterActive = false

    private fun setTime(minutes: Int, seconds: Int) {
        if (minutes < 10 && seconds < 10)
            timeTextView.text = "0$minutes:0$seconds"
        else if (minutes < 10 && seconds >= 10)
            timeTextView.text = "0$minutes:$seconds"
        else if (minutes >= 10 && seconds < 10)
            timeTextView.text = "$minutes:0$seconds"
        else
            timeTextView.text = "$minutes:$seconds"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val mediaPlayer = MediaPlayer.create(this, R.raw.alarm_sound)
        timeSeekBar.max = 720

        timeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val minutes = progress / 60
                val seconds = progress % 60

                setTime(minutes, seconds)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })

        startPauseButton.setOnClickListener {

            timeSeekBar.isEnabled = false

            val stringConverter = timeTextView.text.toString()
            val minutes = "${stringConverter[0]}${stringConverter[1]}".toInt()
            val seconds = "${stringConverter[3]}${stringConverter[4]}".toInt()
            val timeToPass = (minutes * 60000) + (seconds * 1000)

            if (!counterActive){
                countDownTimer = object : CountDownTimer(timeToPass.toLong() + 100, 1000) {

                    override fun onTick(millisUntilFinished: Long) {
                        val min = millisUntilFinished.toInt() / 60000
                        val sec = (millisUntilFinished.toInt()/1000) % 60
                        Log.i("Left", sec.toString())
                        setTime(min, sec)
                    }

                    override fun onFinish() {
                        mediaPlayer.start()
                        timeSeekBar.isEnabled = true
                        startPauseButton.text = "start"
                        counterActive = false
                    }

                }.start()
                counterActive = true
                startPauseButton.text = "stop"
            } else {
                if(mediaPlayer.isPlaying) {
                    mediaPlayer.pause()
                    mediaPlayer.seekTo(0)
                }
                timeSeekBar.isEnabled = true
                startPauseButton.text = "start"
                counterActive = false
                countDownTimer.cancel()
            }
        }

    }
}