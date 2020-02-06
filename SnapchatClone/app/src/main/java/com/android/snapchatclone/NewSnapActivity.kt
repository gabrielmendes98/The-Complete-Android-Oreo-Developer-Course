package com.android.snapchatclone

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_new_snap.*
import java.io.ByteArrayOutputStream
import java.util.*

class NewSnapActivity : AppCompatActivity() {

    private val imageName = "${UUID.randomUUID()}.jpeg"
    private var selectedImage: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_snap)

        if(checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        selectImageButton.setOnClickListener {
            getPhoto()
        }

        newSnapNextButton.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            newSnapNextButton.text = "Uploading..."
            newSnapNextButton.isClickable = false
            uploadFile()
        }
    }

    private fun uploadFile() {
        val ref = FirebaseStorage.getInstance().reference.child("images").child(imageName)

        if(selectedImage != null) {
            ref.putFile(selectedImage!!)
                .addOnSuccessListener {taskSnapshot ->
                    val handler = Handler()
                    handler.postDelayed(object : Runnable{
                        override fun run() {
                            progressBar.progress = 0
                        }

                    }, 500)

                    ref.downloadUrl.addOnSuccessListener {uri ->
                        val imageUrl = uri.toString()
                        val intent = Intent(applicationContext, FriendListActivity::class.java)
                        intent.putExtra("imageUrl", imageUrl)
                        intent.putExtra("message", messageEditText.text.toString())
                        startActivity(intent)
                    }

                }
                .addOnFailureListener {e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
                .addOnProgressListener {taskSnapshot ->
                    val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                    progressBar.progress = progress.toInt()
                }
        } else {
            Toast.makeText(this, "Please select a image.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getPhoto() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            try {
                selectedImage = data?.data
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, selectedImage)
                newSnapImageView.setImageBitmap(bitmap)
                selectImageTextView.visibility = View.INVISIBLE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }
}
