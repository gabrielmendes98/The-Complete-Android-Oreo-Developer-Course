package com.android.mynotes

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import java.text.FieldPosition

// SHARED PREFERENCES INITIALIZATION
class MainActivity : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var gson: Gson

    companion object {
        lateinit var arrayAdapter: ArrayAdapter<String>
        val noteTextsArray = ArrayList<String>()
        val noteNamesArray = ArrayList<String>()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId) {
            R.id.add_note -> {
                addNewNote()
                return true
            }
            else -> return false
        }
    }

    private fun addNewNote() {
        val intent = Intent(this, TextActivity :: class.java)
        intent.putExtra("whatToDo", "addNoteName")
        startActivity(intent)
    }

    private fun loadMyNotes(){
        val type = object : TypeToken<ArrayList<String>>() {}.type

        val jsonNames = sharedPreferences.getString("noteNames", null)
        if (jsonNames != null) {
            val aux: ArrayList<String> = gson.fromJson(jsonNames, type)
            noteNamesArray.addAll(aux)
            arrayAdapter.notifyDataSetChanged()
        }

        val jsonTexts = sharedPreferences.getString("noteTexts", null)
        if(jsonTexts != null) {
            val aux: ArrayList<String> = gson.fromJson(jsonTexts, type)
            noteTextsArray.addAll(aux)
        }
    }

    private fun openDeleteDialog(position: Int) {
        AlertDialog.Builder(this@MainActivity)
            .setTitle("Do you want to delete this note?")
            .setPositiveButton("Yes") { _, _ ->  deleteNote(position) }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    private fun deleteNote(position: Int) {
        noteNamesArray.removeAt(position)
        noteTextsArray.removeAt(position)
        arrayAdapter.notifyDataSetChanged()

        val editor = sharedPreferences.edit()
        var json: String

        json = gson.toJson(noteNamesArray)
        editor.putString("noteNames", json).apply()
        json = gson.toJson(noteTextsArray)
        editor.putString("noteTexts", json).apply()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = this.getSharedPreferences("com.android.mynotes", Context.MODE_PRIVATE)
        gson = Gson()
        arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, noteNamesArray)
        listView.adapter = arrayAdapter
        loadMyNotes()

        listView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, TextActivity :: class.java)
            intent.putExtra("whatToDo", "editNote")
            intent.putExtra("position", position)
            startActivity(intent)
        }

        listView.setOnItemLongClickListener { parent, view, position, id ->
            openDeleteDialog(position)
            true
        }

    }
}
