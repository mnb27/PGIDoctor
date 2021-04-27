package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_view_more_detail.*

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
        val doctorRemarks: TextInputLayout = findViewById(R.id.eight)
        val medicines: TextInputLayout = findViewById(R.id.nine)
//        val psmapet: TextInputLayout = findViewById(R.id.ten)

        val showMRI: Button = findViewById(R.id.showMRI)
        val showBonescan: Button = findViewById(R.id.showBonescan)
        val showPSMA: Button = findViewById(R.id.showPSMA)
        var loadImage: ImageView = findViewById(R.id.loadImage)

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

        var clickMRI = true
        showMRI.setOnClickListener(View.OnClickListener {
            val url = previousDetails?.mri.toString()
            if(url.isNotEmpty()) {
                Picasso.with(this).load(url).into(loadImage)
            }
            else {
                Toast.makeText(this, "MRI Unavailable", Toast.LENGTH_LONG).show()
            }
            clickMRI = if (clickMRI) {
                loadImage.visibility = View.VISIBLE
                false
            } else {
                loadImage.visibility = View.GONE
                true
            }
        })

        var clickBonescan = true
        showBonescan.setOnClickListener(View.OnClickListener {
            val url = previousDetails?.bonescan.toString()
            if(url.isNotEmpty()) {
                Picasso.with(this).load(url).into(loadImage)
            }
            else {
                Toast.makeText(this, "BoneScan Unavailable", Toast.LENGTH_LONG).show()
            }
            clickBonescan = if (clickBonescan) {
                loadImage.visibility = View.VISIBLE
                false
            } else {
                loadImage.visibility = View.GONE
                true
            }
        })

        var clickPSMA = true
        showPSMA.setOnClickListener(View.OnClickListener {
            val url = previousDetails?.psmapet.toString()
            if(url.isNotEmpty()) {
                Picasso.with(this).load(url).into(loadImage)
            }
            else {
                Toast.makeText(this, "PSMA PET Unavailable", Toast.LENGTH_LONG).show()
            }
            clickPSMA = if (clickPSMA) {
                loadImage.visibility = View.VISIBLE
                false
            } else {
                loadImage.visibility = View.GONE
                true
            }
        })




    }
}
