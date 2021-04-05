package com.example.pgidoctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.DocumentSnapshot

class AssignedPatients : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var assignedPatientsAdapter: AssignedPatientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_patients)
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        var list: MutableList<PatientDetails> = mutableListOf()


        recyclerView = findViewById(R.id.recyclerView)
        assignedPatientsAdapter = AssignedPatientsAdapter(this,list)


        recyclerView.adapter = assignedPatientsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        var unit = ""
        var hospital = ""

        auth.currentUser?.uid?.let {
            fireStore.collection("Users").document(it).get()
                .addOnSuccessListener { it ->
                    if(it.exists()){
                        hospital = it.getString("hospital").toString()
                        unit = it.getString("unit").toString()

                        fireStore.collection("PatientDetails").whereEqualTo("hospitalText",hospital).whereEqualTo("unitText",unit).get()
                            .addOnSuccessListener { documents->
                                for(document in documents) {
                                    list.add(document.toObject(PatientDetails::class.java))
                                }
                                list.sortBy { det->det.name }
                                (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                                if(list.isEmpty()) {
                                    Toast.makeText(this,"No pending requests",Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, MainActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                            }

                    }
                }
        }


    }
}