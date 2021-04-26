package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class ViewAllTestsActivity : AppCompatActivity() {
    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_all_tests)
        previousDetails = intent.extras?.get("previousDetails") as PatientDetails
        val Urine: Button = findViewById(R.id.urine_test)
        val Blood: Button = findViewById(R.id.blood_test)
        val Thyroid: Button = findViewById(R.id.thyroid_test)
        val Cholestrol: Button = findViewById(R.id.cholestrol_test)

        val goback: ImageView = findViewById(R.id.backB)

        goback.setOnClickListener {
            val intent = Intent(this, OpenActivity::class.java)
            startActivity(intent)
        }

        Urine.setOnClickListener {
            val intent = Intent(this,AddNewReportByDoctor::class.java)
            intent.putExtra("test_type","urine")
            intent.putExtra("previousDetails",previousDetails)
            startActivity(intent)
        }

        Blood.setOnClickListener {
            val intent = Intent(this,AddNewReportByDoctor::class.java)
            intent.putExtra("test_type","blood")
            intent.putExtra("previousDetails",previousDetails)
            startActivity(intent)
        }

        Thyroid.setOnClickListener {
            val intent = Intent(this,AddNewReportByDoctor::class.java)
            intent.putExtra("test_type","thyroid")
            intent.putExtra("previousDetails",previousDetails)
            startActivity(intent)
        }

        Cholestrol.setOnClickListener {
            val intent = Intent(this,AddNewReportByDoctor::class.java)
            intent.putExtra("test_type","cholestrol")
            intent.putExtra("previousDetails",previousDetails)
            startActivity(intent)
        }

    }
}