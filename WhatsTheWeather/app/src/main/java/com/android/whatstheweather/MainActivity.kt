package com.android.whatstheweather

import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
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

        button.setOnClickListener {
            val link = "https://openweathermap.org/data/2.5/weather?q=${editText.text}&appid=b6907d289e10d714a6e88b30761fae22"
            val downloadTask = DownloadTask()
            downloadTask.execute(link)

            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(editText.windowToken,0)

        }
    }

    inner class DownloadTask : AsyncTask<String, Void, String>() {
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
                runOnUiThread { Toast.makeText(applicationContext, "Could not find weather information for this place!", Toast.LENGTH_SHORT).show() }
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
                var message = ""

                for(i in 0 until jsonArray.length()) {
                    val jsonPart = jsonArray.getJSONObject(i)
                    val main = jsonPart.getString("main")
                    val description = jsonPart.getString("description")

                    if (main != "" && description != ""){
                        message += "Weather: $main - $description\r\n"
                    }
                }

                val mainInfo = jsonObject.getJSONObject("main")
                val temp = mainInfo.getString("temp")
                val tempMin = mainInfo.getString("temp_min")
                val tempMax = mainInfo.getString("temp_max")
                val humidity = mainInfo.getString("humidity")

                message += "Temperature: $temp\r\n"
                message += "Temperature min: $tempMin\r\n"
                message += "Temperature max: $tempMax\r\n"
                message += "Humidity: $humidity%\r\n"

                val cloudsInfo = jsonObject.getJSONObject("clouds")
                val clouds = cloudsInfo.getString("all")
                message += "Clouds: $clouds%\r\n"

                if (message != ""){
                    infoTextView.text = message
                } else {
                    runOnUiThread { Toast.makeText(applicationContext, "Could not find weather information for this place!", Toast.LENGTH_SHORT).show() }
                }

            } catch (e: Exception) {
                runOnUiThread { Toast.makeText(applicationContext, "Could not find weather information for this place!", Toast.LENGTH_SHORT).show() }
                e.printStackTrace()
            }

        }
    }

}
