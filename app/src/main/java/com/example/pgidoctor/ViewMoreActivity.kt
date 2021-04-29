package com.example.pgidoctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout

class ViewMoreActivity : AppCompatActivity() {

    var previousDetails: PatientDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more)

        previousDetails = intent.extras?.get("previousDetails") as PatientDetails
        val name: TextInputLayout = findViewById(R.id.one)
        val fatherName: TextInputLayout = findViewById(R.id.two)
        val date: TextInputLayout = findViewById(R.id.three)
        val crNo: TextInputLayout = findViewById(R.id.four)
        val mobile: TextInputLayout = findViewById(R.id.five)
        val age: TextInputLayout = findViewById(R.id.six)
        val gender: TextInputLayout = findViewById(R.id.seven)
        val hospital: TextInputLayout = findViewById(R.id.eight)
        val unit: TextInputLayout = findViewById(R.id.nine)

        var datee = previousDetails?.date

        name.editText?.setText(previousDetails?.name)
        if (datee != null) {
            date.editText?.setText(datee.substring(6,8) + " / " + datee.substring(4,6) + " / " + datee.substring(0,4))
        }
        age.editText?.setText(previousDetails?.age)
        fatherName.editText?.setText(previousDetails?.fathername)
        crNo.editText?.setText(previousDetails?.crno)
        mobile.editText?.setText(previousDetails?.mobile)
        gender.editText?.setText(previousDetails?.gender)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)

        /*
        name.isEnabled = false
        date.isEnabled = false
        age.isEnabled = false
        fatherName.isEnabled = false
        crNo.isEnabled = false
        mobile.isEnabled = false
        gender.isEnabled = false
        hospital.isEnabled = false
        unit.isEnabled = false
         */
    }
}