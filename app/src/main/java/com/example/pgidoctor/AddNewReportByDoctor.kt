package com.example.pgidoctor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class AddNewReportByDoctor : AppCompatActivity() {
    var Test_type: String? = null
    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_report_by_doctor)
        Test_type = intent.extras?.get("test_type").toString()

        val AddNewReport: Button = findViewById(R.id.add_new_report)
        val ViewAllReport: Button = findViewById(R.id.view_all_reports)
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

