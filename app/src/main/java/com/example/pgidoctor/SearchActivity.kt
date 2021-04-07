package com.example.pgidoctor

import android.R.attr.button
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search_by_name.*


class SearchActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var assignedPatientsAdapter: AssignedPatientsAdapter
    var list: MutableList<PatientDetails> = mutableListOf()
    var list1: MutableList<PatientDetails> = mutableListOf()
    lateinit var search: Button
    lateinit var nameT: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val auth = FirebaseAuth.getInstance()

        search = findViewById(R.id.searchButton)
        val searchmenu: ImageButton = findViewById(R.id.sortButton)
        nameT = findViewById(R.id.name)

        searchmenu.setOnClickListener{
            showPopup(searchmenu)
        }

        /*searchButton.setOnClickListener {
            val nameText = name.editText?.text.toString()
            name.error = ""
            if(TextUtils.isEmpty(nameText)){
                name.error = "Name is required"
                return@setOnClickListener
            }

            //val intent = Intent(this,ViewByNameActivity::class.java)
            //intent.putExtra("name",name.editText?.text.toString())
            //startActivity(intent)
        }*/



        recyclerView = findViewById(R.id.recyclerView)
        assignedPatientsAdapter = AssignedPatientsAdapter(this,list1)


        recyclerView.adapter = assignedPatientsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        var unit = ""
        var hospital = ""

        val fireStore = FirebaseFirestore.getInstance()
        fireStore.collection("PatientDetails").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val det = document.toObject(PatientDetails::class.java)
                    list.add(det)
                }
                (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                if (list.isEmpty()) {
                    Toast.makeText(
                        applicationContext,
                        "No Such Patient Exists",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
    }


    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.search_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    nameT.hint = "Enter Name to Search"
                    search.setOnClickListener {
                        list1.clear()
                        var nameentered = nameT.editText?.text.toString()
                        for(item in list){
                            if(item.name.contains(nameentered)){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatients::class.java)
                    //startActivity(intent)
                }
                R.id.header2 -> {
                    nameT.hint = "Enter Date to Search"
                    search.setOnClickListener {
                        list1.clear()
                        var nameentered = nameT.editText?.text.toString()
                        for(item in list){
                            if(item.date == nameentered){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header3 -> {
                    Toast.makeText(this, "By diag", Toast.LENGTH_SHORT).show()
                }
            }

            true
        })

        popup.show()
    }

}