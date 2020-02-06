package com.android.snapchatclone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Display
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_add_friends.*
import java.util.*
import kotlin.collections.ArrayList

class AddFriendsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var usersEmail: ArrayList<String>
    private lateinit var usersUid: ArrayList<String>
    private lateinit var arrayAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_friends)

        auth = FirebaseAuth.getInstance()

        usersEmail = ArrayList()
        usersUid = ArrayList()
        arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, usersEmail)
        addFriendsListView.adapter = arrayAdapter

        addFriendsSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    if(query.isNotEmpty()){
                        searchUsers(query)
                    }
                }

                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    if(newText.isNotEmpty()){
                        searchUsers(newText)
                    }
                }

                return false
            }

        })

        addFriendsListView.setOnItemClickListener { parent, view, position, id ->
            showAlertDialog(position)
        }


    }

    private fun showAlertDialog(position: Int) {
        AlertDialog.Builder(this)
            .setIcon(R.drawable.ic_person_add)
            .setTitle("Add friend")
            .setMessage("Do you want to add ${usersEmail[position]} to your friend list?")
            .setPositiveButton("Yes") { _, _ -> addFriend(usersUid[position]) }
            .setNegativeButton("No") { _, _ -> }
            .show()
    }

    private fun addFriend(userUid: String) {
        val currUserUid = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference.child("users").child(currUserUid).child("friends").child(userUid).setValue("true")
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Friend added.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    private fun searchUsers(query: String) {
        val currUser = FirebaseAuth.getInstance().currentUser
        val ref = FirebaseDatabase.getInstance().reference.child("users")
        ref.addValueEventListener(object : ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                usersEmail.clear()
                usersUid.clear()
                for(ds: DataSnapshot in p0.children){
                    val modelUser = ds.getValue(ModelUser.javaClass)

                    if(modelUser!!.uid != currUser!!.uid){
                        if(modelUser.email.toLowerCase().contains(query.toLowerCase())){
                            usersEmail.add(modelUser.email)
                            usersUid.add(modelUser.uid)
                        }
                    }

                    arrayAdapter.notifyDataSetChanged()

                }
            }

            override fun onCancelled(p0: DatabaseError) {}
        })
    }
}
