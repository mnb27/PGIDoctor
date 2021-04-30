package com.example.pgidoctor

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.change_password_layout.view.*
import kotlinx.android.synthetic.main.forgot_password.view.*

class PatientRegistartionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_registartion)


        val auth =FirebaseAuth.getInstance()


        val emailText: TextInputLayout = findViewById(R.id.email_text)
        val passwordText: TextInputLayout = findViewById(R.id.password_text)
        val hospitalText: TextInputLayout = findViewById(R.id.hospital)
        val nameText: TextInputLayout = findViewById(R.id.name)
        val unitText: TextInputLayout = findViewById(R.id.unit)
        val mobileText: TextInputLayout = findViewById(R.id.mobile)

        val loginButton: Button = findViewById(R.id.login_button)

        val loginProgress: ProgressBar = findViewById(R.id.login_progress)


        loginButton.setOnClickListener {
            val email = emailText.editText?.text.toString()
            val password = passwordText.editText?.text.toString()
            val name = nameText.editText?.text.toString()
            val hospital = hospitalText.editText?.text.toString()
            val unit = unitText.editText?.text.toString()
            val mobile = mobileText.editText?.text.toString()

            emailText.error = null
            passwordText.error = null
            nameText.error = null
            unitText.error = null
            hospitalText.error = null
            mobileText.error = null

            if (TextUtils.isEmpty(email)) {
                emailText.error = "Email is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(name)) {
                nameText.error = "Name is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(hospital)) {
                hospitalText.error = "Hospital is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(unit)) {
                unitText.error = "Unit is required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(mobile)) {
                mobileText.error = "Mobile is required"
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


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if(task.isSuccessful) {

                        val user = User(
                            auth.currentUser?.uid!!,
                                name,
                                mobile,
                                email,
                                "Patient",
                                hospital,
                                unit
                        )
                        val firestore = FirebaseFirestore.getInstance().collection("Users")
                        firestore.document(auth.currentUser?.uid!!).set(user)
                            .addOnSuccessListener {
                                val intent = Intent(this, PatientPortalActivity::class.java)
                                startActivity(intent)
                                finish()
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