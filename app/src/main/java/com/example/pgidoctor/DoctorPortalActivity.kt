package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase

class DoctorPortalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_portal)

        val auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        val logout: ImageView = findViewById(R.id.logout)
        val collectData: CardView = findViewById(R.id.collectData)
        val viewPatient: CardView = findViewById(R.id.viewPatients)

        logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        collectData.setOnClickListener {
            startActivity(Intent(this,CollectDataActivity::class.java))
        }

        viewPatient.setOnClickListener {
            startActivity(Intent(this,AssignedPatients::class.java))
        }


    }
}