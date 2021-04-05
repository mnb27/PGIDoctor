package com.example.pgidoctor

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class PatientDiagnosisFormActivity : AppCompatActivity() {

    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_diagnosis_form)

        previousDetails = intent.extras?.get("previousDetails") as PatientDetails
        val name: TextInputLayout = findViewById(R.id.one)
        val hospital: TextInputLayout = findViewById(R.id.onee)
        val unit: TextInputLayout = findViewById(R.id.oneee)

        name.editText?.setText(previousDetails?.name)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)


        val firestore = FirebaseFirestore.getInstance()
        var c = 0

        firestore.collection("PatientDiagnosisDetails").whereEqualTo("name",previousDetails?.name)
            .whereEqualTo("unitText", previousDetails?.unitText)
            .get()
            .addOnSuccessListener { documents->
                for(document in documents) {
                    c=1
                }
                if(c==1) {
                    Toast.makeText(this,"Data Already taken",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, OpenActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val weight: TextInputLayout = findViewById(R.id.two)
                    val height: TextInputLayout = findViewById(R.id.three)
                    val smoking: TextInputLayout = findViewById(R.id.four)
                    val alcohole: TextInputLayout = findViewById(R.id.five)
                    val comorbidities: TextInputLayout = findViewById(R.id.six)
                    val familyho: TextInputLayout = findViewById(R.id.seven)
                    val bonescan: TextInputLayout = findViewById(R.id.eight)
                    val mri: TextInputLayout = findViewById(R.id.nine)
                    val psmapet: TextInputLayout = findViewById(R.id.ten)

                    val saveButton: Button = findViewById(R.id.saveButton)

                    val auth = FirebaseAuth.getInstance()

                    saveButton.setOnClickListener {

                        var nameText = previousDetails?.name.toString()
                        var hospitalText = previousDetails?.hospitalText.toString()
                        var unitText = previousDetails?.unitText.toString()
                        var weightText = weight.editText?.text.toString()
                        var heightText = height.editText?.text.toString()
                        var smokingText = smoking.editText?.text.toString()
                        var alcoholeText = alcohole.editText?.text.toString()
                        var comorbiditiesText = comorbidities.editText?.text.toString()
                        var familyhoText = familyho.editText?.text.toString()
                        var bonescanText = bonescan.editText?.text.toString()
                        var mriText = mri.editText?.text.toString()
                        var psmapetText = psmapet.editText?.text.toString()

                        val patientDetails = PatientDiagnosisDetails(
                            nameText,
                            hospitalText,
                            unitText,
                            weightText,
                            heightText,
                            smokingText,
                            alcoholeText,
                            comorbiditiesText,
                            familyhoText,
                            bonescanText,
                            mriText,
                            psmapetText
                        )
                        val firestore = FirebaseFirestore.getInstance().collection("PatientDiagnosisDetails")

                        firestore.document().set(patientDetails)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, OpenActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                } else {
                                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                                }
                            }


                    }

                }

            }
            .addOnFailureListener {
                val intent = Intent(this, OpenActivity::class.java)

                startActivity(intent)
                Log.e("No", "Error")
            }

    }

}