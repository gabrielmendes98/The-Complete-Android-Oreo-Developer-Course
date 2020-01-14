package com.android.processingjsondemo

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {

    class DownloadTask : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg urls: String?): String? {
            var result = ""

            try {

                val url = URL(urls[0])
                val httpURLConnection = url.openConnection() as HttpURLConnection
                val inputStream = httpURLConnection.inputStream
                val inputStreamReader = InputStreamReader(inputStream)
                var data = inputStreamReader.read()

                while (data != -1) {
                    val current = data.toChar()
                    result += current
                    data = inputStreamReader.read()
                }

                return result

            } catch(e: Exception) {
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try {
                val jsonObject = JSONObject(result)
                val weatherInfo = jsonObject.getString("weather")
                val jsonArray = JSONArray(weatherInfo)

                for(i in 0 until jsonArray.length()) {
                    val jsonPart = jsonArray.getJSONObject(i)
                    Log.i("main", jsonPart.getString("main"))
                    Log.i("description", jsonPart.getString("description"))
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val link = "https://samples.openweathermap.org/data/2.5/weather?q=London,uk&appid=b6907d289e10d714a6e88b30761fae22"
        val downloadTask = DownloadTask()
        val htmlContent = downloadTask.execute(link).get()
        Log.i("Content:", htmlContent)

    }
}
