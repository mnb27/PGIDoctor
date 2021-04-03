package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val auth = FirebaseAuth.getInstance()




            val doctorPortal: CardView = findViewById(R.id.doctorPortal)
            val compounderPortal: CardView = findViewById(R.id.compounderPortal)
            val register: CardView = findViewById(R.id.register)
            val adminPortal: CardView = findViewById(R.id.adminPortal)



            register.setOnClickListener {
                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

            adminPortal.setOnClickListener {
                val intent = Intent(this, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }

            doctorPortal.setOnClickListener {
                val intent = Intent(this, DoctorPortalActivity::class.java)
                startActivity(intent)
                finish()
            }
            compounderPortal.setOnClickListener {
                val intent = Intent(this, CompounderPortalActivity::class.java)
                startActivity(intent)
                finish()
            }




    }
}