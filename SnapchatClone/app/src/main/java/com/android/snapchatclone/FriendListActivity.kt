package com.android.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_friend_list.*

class FriendListActivity : AppCompatActivity() {

    private lateinit var friendsEmails: ArrayList<String>
    private lateinit var friendsUids: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var selectedFriends: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        selectedFriends = ArrayList()
        friendsUids = ArrayList()
        friendsEmails = ArrayList()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, friendsEmails)
        friendsListView.adapter = arrayAdapter

        val currUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("users").child(currUser!!.uid).child("friends")
        ref.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                friendsEmails.clear()
                friendsUids.clear()
                for(ds: DataSnapshot in p0.children){
                    val friendUid = ds.key.toString()
                    friendsUids.add(friendUid)
                    getFriendEmail(friendUid)
                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })

        friendsListView.setOnItemClickListener { parent, view, position, id ->
            if(!selectedFriends.contains(friendsUids[position])) {
                selectedFriends.add(friendsUids[position])
                view.setBackgroundColor(resources.getColor(android.R.color.holo_green_light))
            } else {
                selectedFriends.remove(friendsUids[position])
                view.setBackgroundColor(resources.getColor(R.color.white))
            }
        }

        sendSnapButton.setOnClickListener {
            sendSnap()
        }

    }

    private fun sendSnap() {
        val imageUrl = intent.extras!!.getString("imageUrl", "")
        val message = intent.extras!!.getString("message", "")
        val myUid = FirebaseAuth.getInstance().uid!!
        val snapMap: Map<String, String> = mapOf("from" to myUid, "imageURL" to imageUrl, "message" to message)

        selectedFriends.forEach { friendUid ->
            FirebaseDatabase.getInstance().reference.child("users").child(friendUid).child("snaps").push().setValue(snapMap)
        }

        Toast.makeText(applicationContext, "Snap sent successfully!", Toast.LENGTH_SHORT).show()
        finish()
        val intent = Intent(this, HomePageActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        startActivity(intent)

    }

    private fun getFriendEmail(friendUid: String) {
        FirebaseDatabase.getInstance().reference.child("users").child(friendUid).child("email").addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val email = p0.getValue(String::class.java)!!
                friendsEmails.add(email)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }


}
