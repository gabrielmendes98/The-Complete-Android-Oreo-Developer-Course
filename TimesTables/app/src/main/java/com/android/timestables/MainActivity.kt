package com.android.timestables

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var arrayAdapter: ArrayAdapter<Int>

    private fun createTimesTables(progress: Int){
        arrayAdapter.clear()
        for(i in 1..100){
            arrayAdapter.add(progress*i)
        }
        arrayAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        arrayAdapter = ArrayAdapter(this,android.R.layout.simple_expandable_list_item_1, ArrayList<Int>())

        timesTablesSeekBar.max = 20
        timesTablesSeekBar.progress = 1
        listView.adapter = arrayAdapter
        createTimesTables(1)

        timesTablesSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if(progress < 1){
                    timesTablesSeekBar.progress = 1
                } else {
                    createTimesTables(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })
    }
}
