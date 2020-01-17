package com.android.alertdialogdemo

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val NAME = "com.android.alertdialogdemo"
    private val MODE = Context.MODE_PRIVATE
    private lateinit var sharedPreferences: SharedPreferences
    private var language: String? = ""

    private fun getLanguage() {
        language = sharedPreferences.getString("language", "")
        if (language != null)
            languageTextView.text = language
    }

    private fun setLanguage(preferredLanguage: String) {
        sharedPreferences.edit().putString("language", preferredLanguage).apply()
        language = preferredLanguage
        languageTextView.text = preferredLanguage
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this@MainActivity)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .setTitle("What is your preferred language?")
            .setMessage("Choose your language")
            .setPositiveButton("English") { _, _ -> setLanguage("English") }
            .setNegativeButton("Portuguese") { _, _ -> setLanguage("Portuguese") }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        super.onOptionsItemSelected(item)

        when(item.itemId) {
            R.id.english -> setLanguage("English")
            R.id.portuguese -> setLanguage("Portuguese")
            else -> return false
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        sharedPreferences = this.getSharedPreferences(NAME, MODE)

        getLanguage()
        if(language == "" || language == null) {
            showAlertDialog()
        }

    }
}
