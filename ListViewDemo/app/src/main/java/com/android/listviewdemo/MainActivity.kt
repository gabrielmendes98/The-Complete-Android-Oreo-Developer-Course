package com.android.listviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val todoList = ArrayList<String>()
        todoList.add("Android Course")
        todoList.add("example text 2")
        todoList.add("example text 3")

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, todoList)

        listView.adapter = arrayAdapter

        floatingActionButton.setOnClickListener {
            arrayAdapter.add("Added new activity")
        }

        listView.setOnItemClickListener { parent, view, position, id ->
            Toast.makeText(this, arrayAdapter.getItem(position), Toast.LENGTH_SHORT).show()
        }

    }
}
