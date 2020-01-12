package com.android.guessthecelebrity

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import org.jsoup.Jsoup
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {

    private val imageUrls = ArrayList<String>()
    private val names = ArrayList<String>()
    private var correctName = ""
    private var locationOfCorrectName = -1

    class GetWebContent : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String {
            val doc = Jsoup.connect(params[0]).get()
            return doc.outerHtml()
        }
    }

    class ImageDownloader: AsyncTask<String, Void, Bitmap>() {
        override fun doInBackground(vararg params: String?): Bitmap? {
            try {
                val url = URL(params[0])
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                return bitmap
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getWebContent = GetWebContent()
        var webContent = ""
        try {
            webContent = getWebContent.execute("http://www.posh24.se/kandisar").get()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        getImageUrls(webContent)
        getNames(webContent)

        loadImageView()
        loadButtons()

    }

    fun checkAnswer(view: View) {
        val button = view as Button
        if(button.tag.toString() == locationOfCorrectName.toString()){
            Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Wrong! It was $correctName", Toast.LENGTH_SHORT).show()
        }

        loadImageView()
        loadButtons()

    }

    private fun loadButtons() {
        val randomNames = ArrayList<String>()
        randomNames.add(correctName)
        while (randomNames.size < 4){
            val rndName = names.shuffled().first()
            if (!randomNames.contains(rndName)) {
                randomNames.add(rndName)
            }
        }

        randomNames.shuffle()
        locationOfCorrectName = randomNames.indexOf(correctName)
        button.text = randomNames[0]
        button2.text = randomNames[1]
        button3.text = randomNames[2]
        button4.text = randomNames[3]

    }

    private fun loadImageView() {
        val task = ImageDownloader()
        try {
            val randomNumber = (0..49).shuffled().first()
            correctName = names[randomNumber]
            val imageBitmap = task.execute(imageUrls[randomNumber]).get()
            imageView.setImageBitmap(imageBitmap)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getImageUrls(webContent: String) {
        val p = Pattern.compile("img src=\"(.*?)\" alt=")
        val m = p.matcher(webContent)

        if(webContent != ""){
            while (m.find()){
                imageUrls.add(m.group(1))
            }
        }
    }

    private fun getNames(webContent: String) {
        val p = Pattern.compile("\" alt=\"(.*?)\">")
        val m = p.matcher(webContent)

        if(webContent != ""){
            while (m.find()){
                names.add(m.group(1))
            }
        }
    }

}
