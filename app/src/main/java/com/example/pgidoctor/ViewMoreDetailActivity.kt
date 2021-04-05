package com.example.pgidoctor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.textfield.TextInputLayout

class ViewMoreDetailActivity : AppCompatActivity() {

    var previousDetails: PatientDiagnosisDetails? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_more_detail)
        previousDetails = intent.extras?.get("previousDetails") as PatientDiagnosisDetails
        val name: TextInputLayout = findViewById(R.id.one)
        val hospital: TextInputLayout = findViewById(R.id.onee)
        val unit: TextInputLayout = findViewById(R.id.oneee)

        val date: TextInputLayout = findViewById(R.id.date)


        val weight: TextInputLayout = findViewById(R.id.two)
        val height: TextInputLayout = findViewById(R.id.three)
        val smoking: TextInputLayout = findViewById(R.id.four)
        val alcohole: TextInputLayout = findViewById(R.id.five)
        val comorbidities: TextInputLayout = findViewById(R.id.six)
        val familyho: TextInputLayout = findViewById(R.id.seven)
        val bonescan: TextInputLayout = findViewById(R.id.eight)
        val mri: TextInputLayout = findViewById(R.id.nine)
        val psmapet: TextInputLayout = findViewById(R.id.ten)

        name.editText?.setText(previousDetails?.name)
        date.editText?.setText(previousDetails?.date)
        weight.editText?.setText(previousDetails?.weight)
        height.editText?.setText(previousDetails?.height)
        date.editText?.setText(previousDetails?.date)
        smoking.editText?.setText(previousDetails?.smoking)
        alcohole.editText?.setText(previousDetails?.alcohole)
        hospital.editText?.setText(previousDetails?.hospitalText)
        unit.editText?.setText(previousDetails?.unitText)
        comorbidities.editText?.setText(previousDetails?.comorbidities)
        familyho.editText?.setText(previousDetails?.familyho)
        bonescan.editText?.setText(previousDetails?.bonescan)
        mri.editText?.setText(previousDetails?.mri)
        psmapet.editText?.setText(previousDetails?.psmapet)

    }
}