package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore

class UpdateDiagnosisFormActivity : AppCompatActivity() {

    var previousDetails: PatientDiagnosisDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_diagnosis_form)

        previousDetails = intent.extras?.get("previousDetails") as PatientDiagnosisDetails
        var formid = previousDetails?.id
        var patientid = intent.extras?.get("patientid") as String
        val name: TextInputLayout = findViewById(R.id.one)
        val hospital: TextInputLayout = findViewById(R.id.onee)
        val unit: TextInputLayout = findViewById(R.id.oneee)

        val date: TextInputLayout = findViewById(R.id.date)
        val updateButton: Button = findViewById(R.id.updateButton)

        val weight: TextInputLayout = findViewById(R.id.two)
        val height: TextInputLayout = findViewById(R.id.three)
        val smoking: TextInputLayout = findViewById(R.id.four)
        val alcohole: TextInputLayout = findViewById(R.id.five)
        val comorbidities: TextInputLayout = findViewById(R.id.six)
        val familyho: TextInputLayout = findViewById(R.id.seven)
        val doctorRemarks: TextInputLayout = findViewById(R.id.eight)
        val medicines: TextInputLayout = findViewById(R.id.nine)
//        val psmapet: TextInputLayout = findViewById(R.id.ten)

        name.editText?.setText(previousDetails?.name)
        var datee = previousDetails?.date
        if (datee != null) {
            date.editText?.setText(datee.substring(6,8) + " / " + datee.substring(4,6) + " / " + datee.substring(0,4))
        }
        weight.editText?.setText(previousDetails?.weight)
        height.editText?.setText(previousDetails?.height)
        smoking.editText?.setText(previousDetails?.smoking)
        alcohole.editText?.setText(previousDetails?.alcohole)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)
        comorbidities.editText?.setText(previousDetails?.comorbidities)
        familyho.editText?.setText(previousDetails?.familyho)
        medicines.editText?.setText(previousDetails?.medicines)
        doctorRemarks.editText?.setText(previousDetails?.doctorRemarks)
//        psmapet.editText?.setText(previousDetails?.psmapet)


        updateButton.setOnClickListener {
            var nameText = name.editText?.text.toString()
            var hospitalText = hospital.editText?.text.toString()
            var unitText = unit.editText?.text.toString()
            val dateText = date.editText?.text.toString()
            var weightText = weight.editText?.text.toString()
            var heightText = height.editText?.text.toString()
            var smokingText = smoking.editText?.text.toString()
            var alcoholeText = alcohole.editText?.text.toString()
            var comorbiditiesText = comorbidities.editText?.text.toString()
            var familyhoText = familyho.editText?.text.toString()

            var remarksText = doctorRemarks.editText?.text.toString()
            var medicinesText = medicines.editText?.text.toString()

            val patientDetails = PatientDiagnosisDetails(
                    nameText,
                    hospitalText,
                    unitText,
                    previousDetails?.date.toString(),
                    weightText,
                    heightText,
                    smokingText,
                    alcoholeText,
                    comorbiditiesText,
                    familyhoText,
                    previousDetails?.bonescan.toString(),
                    previousDetails?.mri.toString(),
                    previousDetails?.psmapet.toString(),
                    previousDetails?.type.toString(),
                    remarksText,
                    medicinesText,
                    formid.toString()
            )

            val firestore = FirebaseFirestore.getInstance().collection("PatientDetails")


                firestore.document(patientid).collection("DiagnosisForm").document(formid.toString()).set(patientDetails)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Successfully Updated", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, AssignedPatients::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
                            }
                        }

        }



    }
}