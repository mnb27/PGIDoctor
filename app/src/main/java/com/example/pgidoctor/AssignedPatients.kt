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


class AssignedPatients : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var assignedPatientsAdapter: AssignedPatientsAdapter
    var list: MutableList<PatientDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assigned_patients)
        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        val sort: ImageButton = findViewById(R.id.sortButton)

        sort.setOnClickListener{
            showPopup(sort)
        }

        recyclerView = findViewById(R.id.recyclerView)
        assignedPatientsAdapter = AssignedPatientsAdapter(this,list)


        recyclerView.adapter = assignedPatientsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        var unit = ""
        var hospital = ""

        auth.currentUser?.uid?.let {
            fireStore.collection("Users").document(it).get()
                .addOnSuccessListener { it ->
                    if(it.exists()){
                        hospital = it.getString("hospital").toString()
                        unit = it.getString("unit").toString()

                        fireStore.collection("PatientDetails").whereEqualTo("hospitalText",hospital).whereEqualTo("unitText",unit).get()
                            .addOnSuccessListener { documents->
                                for(document in documents) {
                                    list.add(document.toObject(PatientDetails::class.java))
                                }
                                list.sortBy { det->det.name }
                                (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                                if(list.isEmpty()) {
                                    Toast.makeText(this,"No patient records",Toast.LENGTH_LONG).show()
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


    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.header_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    list.sortBy{det -> det.name.toLowerCase()}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }
                R.id.header2 -> {
                    list.sortByDescending{det -> det.date}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }
                R.id.header3 -> {
                    list.sortByDescending{det -> det.isstarred}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }
                R.id.header4 -> {
                    list.sortByDescending{det -> det.isimportant}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }
                R.id.header5 -> {
                    list.sortByDescending{det -> det.isnearby}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }
                R.id.header6 -> {
                    list.sortByDescending{det -> det.issevere}
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                }

            }

            true
        })

        popup.show()
    }

}