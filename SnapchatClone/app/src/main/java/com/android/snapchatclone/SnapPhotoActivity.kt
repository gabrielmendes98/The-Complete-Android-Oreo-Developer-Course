package com.android.snapchatclone

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_snap_photo.*

class SnapPhotoActivity : AppCompatActivity() {

    private lateinit var snapKey: String
    private lateinit var imageUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_snap_photo)

        snapKey = intent.extras!!.getString("snapKey")!!
        imageUrl = intent.extras!!.getString("imageUrl")!!

        val circularProgressDrawable = CircularProgressDrawable(this)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        Glide
            .with(this)
            .load(imageUrl)
            .placeholder(circularProgressDrawable)
            .centerCrop()
            .dontAnimate()
            .into(receivedSnapPhotoImageView)

        closeSnapImageView.setOnClickListener {
            closeAndDeleteSnap()
        }
    }

    private fun closeAndDeleteSnap() {

        val myUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().reference.child("users").child(myUid!!).child("snaps").child(snapKey)
        ref.removeValue()

        FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl).delete()

        finish()
        val intent = Intent(this, HomePageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        closeAndDeleteSnap()
    }
}
