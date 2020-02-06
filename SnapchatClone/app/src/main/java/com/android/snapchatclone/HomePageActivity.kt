package com.android.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.SimpleAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePageActivity : AppCompatActivity() {

    private lateinit var mySnaps: ArrayList<HashMap<String,String>>
    private lateinit var arrayAdapter: SimpleAdapter
    private lateinit var snap: HashMap<String,String>
    private lateinit var imageUrls: ArrayList<String>
    private lateinit var snapKeys: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)

        snap = HashMap()
        imageUrls = ArrayList()
        mySnaps = ArrayList()
        snapKeys = ArrayList()
        arrayAdapter = SimpleAdapter(this, mySnaps, R.layout.two_line_list_item, arrayOf("message", "email"), intArrayOf(R.id.line_message, R.id.line_email))
        receivedSnapListView.adapter = arrayAdapter

        showFriendsSnaps()

        receivedSnapListView.setOnItemClickListener { parent, view, position, id ->
            val intent = Intent(this, SnapPhotoActivity::class.java)
            intent.putExtra("snapKey", snapKeys[position])
            intent.putExtra("imageUrl", imageUrls[position])
            startActivity(intent)
        }

    }

    private fun showFriendsSnaps() {
        val myUid = FirebaseAuth.getInstance().uid!!
        FirebaseDatabase.getInstance().reference.child("users").child(myUid).child("snaps").addChildEventListener(object : ChildEventListener{

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                // get the key of the snaps
                snapKeys.add(p0.key.toString())
                // get the message
                snap["message"] = p0.child("message").value as String
                // get the email of the sender and add to listView
                getFriendEmail(p0.child("from").value as String)

                imageUrls.add(p0.child("imageURL").value as String)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_new_snap -> {
                startActivity(Intent(this, NewSnapActivity::class.java))
            }
            R.id.menu_logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.menu_add_friends -> {
                startActivity(Intent(this, AddFriendsActivity::class.java))
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun getFriendEmail(friendUid: String) {
        FirebaseDatabase.getInstance().reference.child("users").child(friendUid).child("email").addListenerForSingleValueEvent(object :
            ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                snap["email"] = p0.getValue(String::class.java)!!
                mySnaps.add(snap)
                arrayAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
