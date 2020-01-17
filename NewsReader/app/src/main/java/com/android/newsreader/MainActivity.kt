package com.android.newsreader

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val downloadContent = DownloadContent()
        val link = "https://hacker-news.firebaseio.com/v0/topstories.json"
        val jsonArrayString = downloadContent.execute(link).get()
        val jsonArray = JSONArray(jsonArrayString)

        val titles = ArrayList<String>()
        val urls = ArrayList<String>()
        for(i in 0 until jsonArray.length()){
            val jsonObject = jsonArray.getJSONObject(i)
            titles.add(jsonObject.getString("title"))
            urls.add(jsonObject.getString("url"))
        }

        val arrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, titles)
        listView.adapter = arrayAdapter

        listView.setOnItemClickListener { parent, view, position, id ->
            try {
                val intent = Intent(this, WebViewActivity :: class.java)
                intent.putExtra("url", urls[position])
                startActivity(intent)
            } catch (e: Exception){
                android.widget.Toast.makeText(this, "ERROR", android.widget.Toast.LENGTH_SHORT).show()
            }

        }

    }
}


