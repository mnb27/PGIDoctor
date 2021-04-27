package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

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
        val urinelatest: Button = findViewById(R.id.urinelatest)
        val bloodlatest: Button = findViewById(R.id.bloodlatest)
        val thyroidlatest: Button = findViewById(R.id.thyroidlatest)
        val cholestrollatest: Button = findViewById(R.id.cholestrollatest)
        var list: MutableList<PatientDiagnosisDetails> = mutableListOf()
        val goback: ImageView = findViewById(R.id.backB)
        val firestore = FirebaseFirestore.getInstance()
        previousDetails?.id?.let { firestore.collection("PatientDetails").document(it).collection("DiagnosisForm").get()
                .addOnSuccessListener {
                    for(document in it){
                        list.add(document.toObject(PatientDiagnosisDetails::class.java))
                    }
                    list.sortByDescending { det->det.date }
                }



        }

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

        urinelatest.setOnClickListener {
            var c=0
            val intent = Intent(this,ViewMoreDetailActivity::class.java)
            for(item in list){
                if(item.type == "urine"){
                    intent.putExtra("previousDetails",item)
                    c=1
                    startActivity(intent)
                    break

                }
            }
            if(c==0){
                Toast.makeText(this,"No Report Available",Toast.LENGTH_LONG).show()
            }
        }
        bloodlatest.setOnClickListener {
            var c=0
            val intent = Intent(this,ViewMoreDetailActivity::class.java)
            for(item in list){
                if(item.type == "blood"){
                    intent.putExtra("previousDetails",item)
                    c=1
                    startActivity(intent)
                    break

                }
            }
            if(c==0){
                Toast.makeText(this,"No Report Available",Toast.LENGTH_LONG).show()
            }
        }
        thyroidlatest.setOnClickListener {
            var c=0
            val intent = Intent(this,ViewMoreDetailActivity::class.java)
            for(item in list){
                if(item.type == "thyroid"){
                    intent.putExtra("previousDetails",item)
                    c=1
                    startActivity(intent)
                    break

                }
            }
            if(c==0){
                Toast.makeText(this,"No Report Available",Toast.LENGTH_LONG).show()
            }
        }
        cholestrollatest.setOnClickListener {
            var c=0
            val intent = Intent(this,ViewMoreDetailActivity::class.java)
            for(item in list){
                if(item.type == "cholestrol"){
                    intent.putExtra("previousDetails",item)
                    c=1
                    startActivity(intent)
                    break

                }
            }
            if(c==0){
                Toast.makeText(this,"No Report Available",Toast.LENGTH_LONG).show()
            }
        }

    }
}