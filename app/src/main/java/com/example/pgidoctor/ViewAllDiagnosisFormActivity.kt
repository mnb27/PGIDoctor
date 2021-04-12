package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class ViewAllDiagnosisFormActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewAllDiagnosisFormAdapter: ViewAllDiagnosisFormAdapter
    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_diagnosis_form)

        previousDetails = intent.extras?.get("previousDetails") as PatientDetails
        val collectButton: Button = findViewById(R.id.collectButton)
        collectButton.setOnClickListener {
            val intent = Intent(this,PatientDiagnosisFormActivity::class.java)
            intent.putExtra("previousDetails",previousDetails)
            startActivity(intent)
        }
        val text: TextView = findViewById(R.id.textView4)
        text.text = "Diagnosis Forms For " + previousDetails?.name

        val id = previousDetails?.id
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        var list: MutableList<PatientDiagnosisDetails> = mutableListOf()


        recyclerView = findViewById(R.id.recyclerView)

        viewAllDiagnosisFormAdapter = ViewAllDiagnosisFormAdapter(this,list)


        recyclerView.adapter = viewAllDiagnosisFormAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        if (id != null) {
            fetchToDoList(list,id)
        }

    }

    private fun fetchToDoList(list: MutableList<PatientDiagnosisDetails>,id: String) {
        doAsync {

            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("PatientDetails").document(id).collection("DiagnosisForm").get()
                .addOnSuccessListener { documents->
                    for(document in documents){
                        val det = document.toObject(PatientDiagnosisDetails::class.java)
                            list.add(det)
                    }
                    (recyclerView.adapter as ViewAllDiagnosisFormAdapter).notifyDataSetChanged()
                }


            uiThread {
                    viewAllDiagnosisFormAdapter.setList(list)
            }
        }
    }
}