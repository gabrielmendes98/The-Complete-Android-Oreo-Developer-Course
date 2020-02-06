package com.android.snapchatclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_account.*
import kotlinx.android.synthetic.main.activity_main.*

class CreateAccountActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        createAccountButton.setOnClickListener {
            performSignup()
        }
    }

    private fun performSignup() {
        val email = emailEditText2.text.toString()
        val password = passwordEditText2.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "You must fill all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if(password != confirmPassword) {
            Toast.makeText(this, "Your passwords must match", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    FirebaseDatabase.getInstance().reference.child("users").child(task.result?.user!!.uid).child("email").setValue(email)
                    FirebaseDatabase.getInstance().reference.child("users").child(task.result?.user!!.uid).child("uid").setValue(auth.currentUser!!.uid)
                    Toast.makeText(baseContext, "Success! We logged in automatically for you!", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(Intent(this, HomePageActivity::class.java))
                } else {
                    // If sign in fails, display a message to the user.
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
