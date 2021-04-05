package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.util.*

class ViewByNameActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var viewByNameAdapter: AssignedPatientsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_by_name)
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        var name = intent.extras?.get("name") as String
        name = name.toLowerCase()

        var list: MutableList<PatientDetails> = mutableListOf()


        recyclerView = findViewById(R.id.recyclerView1)
        val text: TextView = findViewById(R.id.text)
        text.text = "Patient's Name Matching with $name"
        viewByNameAdapter = AssignedPatientsAdapter(this,list)


        recyclerView.adapter = viewByNameAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchToDoList(list,name)

    }

    private fun fetchToDoList(list: MutableList<PatientDetails>,name: String) {
        doAsync {

            val fireStore = FirebaseFirestore.getInstance()
            fireStore.collection("PatientDetails").get()
                .addOnSuccessListener { documents->
                    for(document in documents){
                        val det = document.toObject(PatientDetails::class.java)
                        val nameText = det.name.toLowerCase()
                        if(nameText.contains(name) || name.contains(nameText)){
                            list.add(det)
                        }
                    }
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                    if(list.isEmpty()){
                        Toast.makeText(applicationContext,"No Such Patient Exists",Toast.LENGTH_LONG).show()
                        startActivity(Intent(applicationContext,SearchByNameActivity::class.java))
                        finish()
                    }
                }


            uiThread {
                viewByNameAdapter.setList(list)
            }
        }
    }
}