package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle


import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

import android.app.DatePickerDialog
import android.view.View
import android.widget.*
import java.util.*
import android.widget.RadioGroup
class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val mPickTimeBtn = findViewById<Button>(R.id.pickDateBtn)
        val dob: TextInputLayout = findViewById(R.id.six)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        mPickTimeBtn.setOnClickListener {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
                dob.getEditText()?.setText("" + dayOfMonth + " / " + month + " / " + year)
            }, year, month, day)
            dpd.show()
        }
        //////end


        val name: TextInputLayout = findViewById(R.id.one)
        val email: TextInputLayout = findViewById(R.id.two)
        val hospitalName: TextInputLayout = findViewById(R.id.three)
        val unitName: TextInputLayout = findViewById(R.id.four)

        val mobile: TextInputLayout = findViewById(R.id.nine)

        val saveButton: Button =findViewById(R.id.saveButton)



        val auth = FirebaseAuth.getInstance()

        saveButton.setOnClickListener {
            var nameText = name.editText?.text.toString()
            var emailText = email.editText?.text.toString()
            var hospitalNameText = hospitalName.editText?.text.toString()
            var unitNameText = unitName.editText?.text.toString()
            var mobileText = mobile.editText?.text.toString()

            if (nameText.isEmpty()) {
                name.error = "Required Field"
                return@setOnClickListener
            }
            if (emailText.isEmpty()) {
                email.error = "Required Field"
                return@setOnClickListener
            }
            if (hospitalNameText.isEmpty()) {
                hospitalName.error = "Required Field"
                return@setOnClickListener
            }

            if (unitNameText.isEmpty()) {
                hospitalName.error = "Required Field"
                return@setOnClickListener
            }

            if (mobileText.isEmpty()) {
                mobile.error = "Required Field"
                return@setOnClickListener
            }

            // radio button
            val doctorRadio: RadioButton = findViewById(R.id.doctorRadio)
            var category:String
            if(doctorRadio.isChecked) category="Doctor"
            else category="Compounder"

            var male: RadioButton = findViewById(R.id.male)
            var gender:String
            if(male.isChecked) gender="male"
            else gender="female"

            var dob_text = dob.editText?.text.toString()
            var status = "pending"

            if (dob_text.isEmpty()) {
                dob.setError("Required Field")
                return@setOnClickListener
            }

            val newUser = PendingList(nameText,emailText,hospitalNameText,unitNameText,category,gender,dob_text,mobileText,status)
            val firestore = FirebaseFirestore.getInstance().collection("PendingList")

            firestore.document(mobileText).set(newUser)
                .addOnCompleteListener { task->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Successfully Applied",Toast.LENGTH_LONG).show()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                    }
                }


        }


    }

}