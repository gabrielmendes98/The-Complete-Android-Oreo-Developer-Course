package com.android.mynotes

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_text.*

class TextActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text)
        val whatToDo = intent.extras!!.getString("whatToDo")

        val sharedPreferences = this.getSharedPreferences("com.android.mynotes", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        var json: String

        if (whatToDo == "editNote") {
            val position = intent.extras!!.getInt("position")
            val note = MainActivity.noteTextsArray[position]
            if(note.isNotEmpty())
                editText.setText(MainActivity.noteTextsArray[position], TextView.BufferType.EDITABLE)
        }

        floatingActionButton.setOnClickListener {
            if (whatToDo == "addNoteName") {
                MainActivity.noteNamesArray.add(editText.text.toString())
                MainActivity.arrayAdapter.notifyDataSetChanged()
                MainActivity.noteTextsArray.add("")
                json = gson.toJson(MainActivity.noteNamesArray)
                editor.putString("noteNames", json).apply()
                json = gson.toJson(MainActivity.noteTextsArray)
                editor.putString("noteTexts", json).apply()
            }
            else if (whatToDo == "editNote") {
                val position = intent.extras!!.getInt("position")
                MainActivity.noteTextsArray[position] = editText.text.toString()
                json = gson.toJson(MainActivity.noteTextsArray)
                editor.putString("noteTexts", json).apply()
                Log.i("ARRAY added", MainActivity.noteTextsArray.toString())
            }

            finish()
        }
    }
}
