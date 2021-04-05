package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_search_by_name.*

class SearchByNameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_by_name)

        val name: TextInputLayout = findViewById(R.id.name)
        val search: Button = findViewById(R.id.searchButton)

        searchButton.setOnClickListener {
            val nameText = name.editText?.text.toString()
            name.error = ""
            if(TextUtils.isEmpty(nameText)){
                name.error = "Name is required"
                return@setOnClickListener
            }

            val intent = Intent(this,ViewByNameActivity::class.java)
            intent.putExtra("name",name.editText?.text.toString())
            startActivity(intent)
        }
    }
}