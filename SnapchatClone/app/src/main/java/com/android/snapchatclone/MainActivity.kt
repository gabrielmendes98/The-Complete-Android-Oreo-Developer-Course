package com.android.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        signupButton.setOnClickListener {
            startActivity(Intent(this, CreateAccountActivity::class.java))
        }

        auth = FirebaseAuth.getInstance()
        if(auth.currentUser != null) {
            finish()
            startActivity(Intent(this, HomePageActivity::class.java))
        }

        loginButton.setOnClickListener {
            performLogin()
        }

    }

    private fun performLogin(){
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "You must fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    finish()
                    startActivity(Intent(this, HomePageActivity::class.java))
                } else {
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener {
                Toast.makeText(baseContext, it.message,
                    Toast.LENGTH_SHORT).show()
            }
    }
}
