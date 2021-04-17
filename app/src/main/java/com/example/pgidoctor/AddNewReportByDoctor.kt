package com.example.pgidoctor

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_new_report_by_doctor.*

class AddNewReportByDoctor : AppCompatActivity() {
    var Test_type: String? = null
    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_report_by_doctor)
        Test_type = intent.extras?.get("test_type").toString()


        val auth = FirebaseAuth.getInstance()
        val fireStore = FirebaseFirestore.getInstance()


        val AddNewReport: Button = findViewById(R.id.add_new_report)
        val ViewAllReport: Button = findViewById(R.id.view_all_reports)

        var type = ""
        fireStore.collection("Users").document(auth.currentUser.uid).get()
                .addOnSuccessListener {
                    type = it.getString("type").toString()
                    if(type == "Patient") {
                        AddNewReport.visibility = View.GONE
                    }
                }


        previousDetails = intent.extras?.get("previousDetails") as PatientDetails
        AddNewReport.setOnClickListener {
            val i = Intent(this,PatientDiagnosisFormActivity::class.java)
            i.putExtra("test_type",Test_type)
            i.putExtra("previousDetails",previousDetails)
            startActivity(i)
        }
        ViewAllReport.setOnClickListener {
            val i = Intent(this,ViewAllDiagnosisFormActivity::class.java)
            i.putExtra("test_type",Test_type)
            i.putExtra("previousDetails",previousDetails)
            startActivity(i)
        }
    }
}

