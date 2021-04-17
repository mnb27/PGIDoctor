package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.view_patients.*

class PatientPortalActivity : AppCompatActivity() {
    var patientDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_portal)
        val Chat: CardView = findViewById(R.id.chat)
        val ViewReport: CardView = findViewById(R.id.view_records)
        val auth = FirebaseAuth.getInstance()
        val logout: ImageView = findViewById(R.id.logout)
        val editProfile: Button = findViewById(R.id.editProfileB)


        editProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        if(auth.currentUser == null){
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        val fireStore = FirebaseFirestore.getInstance()
        val user_email = auth.currentUser?.email

        var name: String = ""
        var fathername: String = ""
        var date: String = ""
        var crno: String = ""
        var mobile: String = ""
        var age: String = ""
        var gender: String = ""
        var profileImageUrl: String = ""
        var hospitalText: String=""
        var unitText: String = ""
        var id: String = ""
        var mob = ""

        fireStore.collection("Users").whereEqualTo("email",user_email).get()
            .addOnCompleteListener(){
                if(it.isSuccessful){
                    for(document in it.result!!){
                        mob = document.data.getValue("mobile").toString()
                        }
//                    Log.d("amvfd",mob)
                    fireStore.collection("PatientDetails").whereEqualTo("mobile",mob).get()
                        .addOnSuccessListener { documents->
                            for(document in documents) {
                                name = document.data.getValue("name").toString()
                                fathername = document.data.getValue("fathername").toString()
                                date = document.data.getValue("date").toString()
                                crno = document.data.getValue("crno").toString()
                                mobile = document.data.getValue("name").toString()
                                age = document.data.getValue("age").toString()
                                gender = document.data.getValue("gender").toString()
                                profileImageUrl = document.data.getValue("profileImageUrl").toString()
                                hospitalText = document.data.getValue("hospitalText").toString()
                                unitText = document.data.getValue("unitText").toString()
                                id = document.data.getValue("id").toString()

                            }
                            patientDetails = PatientDetails(name,fathername,date,crno,mobile,age,gender,profileImageUrl,hospitalText,unitText,
                            id)
                        }
                        .addOnFailureListener{
                            Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }

        ViewReport.setOnClickListener {
            val intent = Intent(this, ViewAllTestsActivity::class.java)
            intent.putExtra("previousDetails",patientDetails)
            startActivity(intent)
        }
        logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}