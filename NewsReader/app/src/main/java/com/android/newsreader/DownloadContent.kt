package com.android.newsreader

import android.os.AsyncTask
import android.util.Log
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class DownloadContent : AsyncTask<String, Void, String>() {
    private val maxNumberOfNews = 20

    override fun doInBackground(vararg urls: String?): String? {
        var result = ""

        try {

            var url = URL(urls[0])
            var httpURLConnection = url.openConnection() as HttpURLConnection
            var inputStream = httpURLConnection.inputStream
            var inputStreamReader = InputStreamReader(inputStream)
            var data = inputStreamReader.read()

            while (data != -1) {
                result += data.toChar()
                data = inputStreamReader.read()
            }

            val jsonArray = JSONArray(result)
            var numberOfIterations = maxNumberOfNews
            if(jsonArray.length() < maxNumberOfNews) {
                numberOfIterations = jsonArray.length()
            }

            val jsonToReturn = JSONArray()
            for (i in 0..numberOfIterations) {
                try {
                    result = ""
                    url = URL("https://hacker-news.firebaseio.com/v0/item/${jsonArray.getString(i)}.json")
                    httpURLConnection = url.openConnection() as HttpURLConnection
                    inputStream = httpURLConnection.inputStream
                    inputStreamReader = InputStreamReader(inputStream)
                    data = inputStreamReader.read()

                    while (data != -1) {
                        result += data.toChar()
                        data = inputStreamReader.read()
                    }

                    val jsonObject = JSONObject(result)
                    val storyTitle = jsonObject.getString("title")
                    val storyUrl = jsonObject.getString("url")
                    val jsonConverter = JSONObject()
                    jsonConverter.put("title", storyTitle)
                    jsonConverter.put("url", storyUrl)

                    jsonToReturn.put(jsonConverter)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return jsonToReturn.toString()
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}