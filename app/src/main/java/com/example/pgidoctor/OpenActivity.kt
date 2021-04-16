package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class OpenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open)

        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser != null){
            if(auth.currentUser?.email == "admin@gmail.com") {
                startActivity(Intent(this, AdminActivity::class.java))
                finish()
            }
            else{
                val fireStore = FirebaseFirestore.getInstance()
                var type = ""
                fireStore.collection("Users").document(auth.currentUser.uid).get()
                    .addOnSuccessListener {
                        type = it.getString("type").toString()
                        if(type == "Doctor"){
                            startActivity(Intent(this, DoctorPortalActivity::class.java))
                            finish()
                        }
                        else if(type == "Patient"){
                            startActivity(Intent(this,PatientPortalActivity::class.java))
                            finish()
                        }
                        else{
                            startActivity(Intent(this, CompounderPortalActivity::class.java))
                            finish()
                        }
                    }
            }

        }
        else {

            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
    }
}