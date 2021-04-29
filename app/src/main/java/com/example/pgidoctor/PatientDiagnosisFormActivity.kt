package com.example.pgidoctor

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_patient_diagnosis_form.*
import java.util.*


class PatientDiagnosisFormActivity : AppCompatActivity() {

    val mriFile : Int = 0
    val bonescanFile : Int = 1
    val psmapetFile : Int = 2

    lateinit var uri : Uri
    var mriLink = ""
    var bonescanLink = ""
    var psmapetLink = ""
    lateinit var mStorage : StorageReference

    var previousDetails: PatientDetails? = null
    var ttesttype: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_diagnosis_form)

        ttesttype = intent.extras?.get("test_type").toString()
        previousDetails = intent.extras?.get("previousDetails") as PatientDetails

        val uploadMRI = findViewById<View>(R.id.uploadMRI) as Button
        val uploadBonescan = findViewById<View>(R.id.uploadBonescan) as Button
        val uploadPSMAPET = findViewById<View>(R.id.uploadPSMA) as Button
        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

        uploadMRI.setOnClickListener(View.OnClickListener {
            view: View? -> val intent = Intent()
            intent.setType ("*/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select File"), mriFile)
        })

        uploadBonescan.setOnClickListener(View.OnClickListener {
            view: View? -> val intent = Intent()
            intent.setType ("*/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select File"), bonescanFile)
        })

        uploadPSMAPET.setOnClickListener(View.OnClickListener {
            view: View? -> val intent = Intent()
            intent.setType ("*/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Select File"), psmapetFile)
        })

        val name: TextInputLayout = findViewById(R.id.one)
        val hospital: TextInputLayout = findViewById(R.id.onee)
        val unit: TextInputLayout = findViewById(R.id.oneee)

        name.editText?.setText(previousDetails?.name)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)

        val id = previousDetails?.id

        val mPickTimeBtn = findViewById<Button>(R.id.pickDateBtn) // date

        val date: TextInputLayout = findViewById(R.id.date)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        var dateChoose = ""

        mPickTimeBtn.setOnClickListener {
            var dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                date.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                var daynumber = ""
                var monthnumber = (monthOfYear+1).toString()
                var correctMonth = monthOfYear + 1
                if (correctMonth < 10) {
                    monthnumber = "0$correctMonth"
                }
                if (dayOfMonth < 10) {
                    daynumber = "0$dayOfMonth"
                    dateChoose = year.toString() + monthnumber + daynumber
                }
                else {dateChoose = year.toString() + monthnumber + dayOfMonth.toString()}
            }, year, month, day)
            dpd.show()
        }


        val firestore = FirebaseFirestore.getInstance()


                    val weight: TextInputLayout = findViewById(R.id.two)
                    val height: TextInputLayout = findViewById(R.id.three)
                    val smoking: TextInputLayout = findViewById(R.id.four)
                    val alcohole: TextInputLayout = findViewById(R.id.five)
                    val comorbidities: TextInputLayout = findViewById(R.id.six)
                    val familyho: TextInputLayout = findViewById(R.id.seven)
                    val remarks: TextInputLayout = findViewById(R.id.eight)
                    val medicines: TextInputLayout = findViewById(R.id.nine)

                    val saveButton: Button = findViewById(R.id.saveButton)

                    val auth = FirebaseAuth.getInstance()

                    saveButton.setOnClickListener {

                        var nameText = previousDetails?.name.toString()
                        var hospitalText = previousDetails?.hospitalText.toString()
                        var unitText = previousDetails?.unitText.toString()
                        val dateText = dateChoose
                        var weightText = weight.editText?.text.toString()
                        var heightText = height.editText?.text.toString()
                        var smokingText = smoking.editText?.text.toString()
                        var alcoholeText = alcohole.editText?.text.toString()
                        var comorbiditiesText = comorbidities.editText?.text.toString()
                        var familyhoText = familyho.editText?.text.toString()
                        var bonescanText = bonescanLink
                        var mriText = mriLink
                        var psmapetText = psmapetLink
                        var remarksText = remarks.editText?.text.toString()
                        var medicinesText = medicines.editText?.text.toString()

                        val patientDetails = PatientDiagnosisDetails(
                            nameText,
                            hospitalText,
                            unitText,
                            dateText,
                            weightText,
                            heightText,
                            smokingText,
                            alcoholeText,
                            comorbiditiesText,
                            familyhoText,
                            bonescanText,
                            mriText,
                            psmapetText,
                            ttesttype!!,
                            remarksText,
                            medicinesText
                        )
                        val firestore = FirebaseFirestore.getInstance().collection("PatientDetails")


                        if (id != null) {
                            firestore.document(id).collection("DiagnosisForm").document().set(patientDetails)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(this, "Successfully Saved", Toast.LENGTH_LONG).show()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == mriFile) {
                uri = data!!.data!!
                uploadMRI()
            }else if (requestCode == bonescanFile) {
                uri = data!!.data!!
                uploadBonescan()
            }else if (requestCode == psmapetFile) {
                uri = data!!.data!!
                uploadPSMAPET()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun uploadMRI() {
        if (uri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Uploads/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                    Toast.makeText(this, "Uploaded MRI :)", Toast.LENGTH_LONG).show()
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("TAG", "File Location: $it")
                        mriLink = it.toString()
                        Log.d("TAG", "File url: $mriLink")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(this, "Failed To Upload :)", Toast.LENGTH_LONG).show()
                }
    }

    private fun uploadBonescan() {
        if (uri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Uploads/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                    Toast.makeText(this, "Uploaded BoneScan", Toast.LENGTH_LONG).show()
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("TAG", "File Location: $it")
                        bonescanLink = it.toString()
                        Log.d("TAG", "File url: $bonescanLink")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(this, "Failed To Upload :(", Toast.LENGTH_LONG).show()
                }
    }

    private fun uploadPSMAPET() {
        if (uri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/Uploads/$filename")
        ref.putFile(uri!!)
                .addOnSuccessListener {
                    Log.d("collect data activity", "Successfully uploaded image: ${it.metadata?.path}")
                    Toast.makeText(this, "Uploaded PSMA PET", Toast.LENGTH_LONG).show()
                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("TAG", "File Location: $it")
                        psmapetLink = it.toString()
                        Log.d("TAG", "File url: $psmapetLink")
                    }
                }
                .addOnFailureListener {
                    Log.d("TAG", "Failed to upload image to storage: ${it.message}")
                    Toast.makeText(this, "Failed To Upload :(", Toast.LENGTH_LONG).show()
                }
    }

}