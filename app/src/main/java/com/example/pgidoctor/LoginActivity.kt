package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val goToRegister: TextView = findViewById(R.id.go_to_register)

        val auth =FirebaseAuth.getInstance()

        goToRegister.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
        }

        val emailText: TextInputLayout = findViewById(R.id.email_text)
        val passwordText: TextInputLayout = findViewById(R.id.password_text)

        val loginButton: Button = findViewById(R.id.login_button)

        val loginProgress: ProgressBar = findViewById(R.id.login_progress)

        loginButton.setOnClickListener {
            val email = emailText.editText?.text.toString()
            val password = passwordText.editText?.text.toString()

            emailText.error = null
            passwordText.error = null

            if (TextUtils.isEmpty(email)) {
                emailText.error = "Email is required"
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailText.error = "Please enter a valid email address"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                passwordText.error = "Password is required"
                return@setOnClickListener
            }

            loginProgress.visibility = View.VISIBLE


            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if(task.isSuccessful) {
                        if(auth.currentUser?.email == "admin@gmail.com") {
                            startActivity(Intent(this, AdminActivity::class.java))
                            finish()
                        }
                        else {
                            val fireStore = FirebaseFirestore.getInstance()
                            var type = ""
                            fireStore.collection("Users").document(auth.currentUser.uid).get()
                                .addOnSuccessListener {
                                    type = it.getString("type").toString()
                                    if (type == "Doctor") {
                                        startActivity(
                                            Intent(
                                                this,
                                                DoctorPortalActivity::class.java
                                            )
                                        )
                                        finish()
                                    } else {
                                        startActivity(
                                            Intent(
                                                this,
                                                CompounderPortalActivity::class.java
                                            )
                                        )
                                        finish()
                                    }
                                }
                        }
                    }
                    else {
                        Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show()
                        Log.d("Error", task.exception.toString())
                    }
                }
        }
    }
}