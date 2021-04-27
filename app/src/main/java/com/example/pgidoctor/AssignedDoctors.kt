package com.example.pgidoctor

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search_by_name.*


class AssignedDoctors : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var assignedDoctorsAdapter: AssignedDoctorsAdapter
    var list: MutableList<User> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_doctors)
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()
        val goback: ImageView = findViewById(R.id.backB)

        goback.setOnClickListener {
            val intent = Intent(this, PatientPortalActivity::class.java)
            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerView)
        assignedDoctorsAdapter = AssignedDoctorsAdapter(this,list)


        recyclerView.adapter = assignedDoctorsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        var unit = ""
        var hospital = ""

        auth.currentUser?.uid?.let {
            fireStore.collection("Users").document(it).get()
                .addOnSuccessListener { it ->
                    if(it.exists()){
                        hospital = it.getString("hospital").toString()
                        unit = it.getString("unit").toString()

                        fireStore.collection("Users").whereEqualTo("type","Doctor").whereEqualTo("hospital",hospital).whereEqualTo("unit",unit).get()
                            .addOnSuccessListener { documents->
                                for(document in documents) {
                                    list.add(document.toObject(User::class.java))
                                }
                                list.sortBy { det->det.name }
                                (recyclerView.adapter as AssignedDoctorsAdapter).notifyDataSetChanged()
                                if(list.isEmpty()) {
                                    Toast.makeText(this,"No Doctor records",Toast.LENGTH_LONG).show()
                                    val intent = Intent(this, OpenActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                            .addOnFailureListener{
                                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
                            }

                    }
                }
        }


    }
}