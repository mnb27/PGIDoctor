package com.example.pgidoctor

import android.R.attr.button
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.contains
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_search_by_name.*
import java.util.*


class SearchActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var assignedPatientsAdapter: AssignedPatientsAdapter
    var list: MutableList<PatientDetails> = mutableListOf()
    var list1: MutableList<PatientDetails> = mutableListOf()
    lateinit var search: Button
    lateinit var nameT: TextInputLayout
    lateinit var datePicker: Button
    lateinit var  spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val auth = FirebaseAuth.getInstance()

        search = findViewById(R.id.searchButton)
        val searchmenu: ImageButton = findViewById(R.id.sortButton)
        nameT = findViewById(R.id.name)
        nameT.hint = "Please Select from PopUp to Search"
        nameT.isEnabled = false


        searchmenu.setOnClickListener{
            showPopup(searchmenu)
        }

        datePicker = findViewById(R.id.pickDate)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        nameT.isEnabled = false
        var date = ""
        datePicker.setOnClickListener {
            var dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in TextView
//                nameT.editText?.setText("" + dayOfMonth + " / " + (monthOfYear.toInt()+1).toString() + " / " + year)
                var daynumber = ""
                var monthnumber = (monthOfYear+1).toString()
                var correctMonth = monthOfYear + 1
                if (correctMonth < 10) {
                    monthnumber = "0$correctMonth"
                }
                if (dayOfMonth < 10) {
                    daynumber = "0$dayOfMonth"
                    date = daynumber + "-" + monthnumber + "-" + year.toString()
                    nameT.editText?.setText(date)
                }
                else {date = dayOfMonth.toString() + "-" + monthnumber + "-" + year.toString()
                    nameT.editText?.setText(date)}
            }, year, month, day)

            dpd.show()
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
        spinner = findViewById(R.id.spinner)
        val fireStore = FirebaseFirestore.getInstance()
        auth.currentUser?.uid?.let {
            fireStore.collection("Users").document(it).get()
                .addOnSuccessListener { it ->
                    if(it.exists()){
                        hospital = it.getString("hospital").toString()
                        unit = it.getString("unit").toString()

                        fireStore.collection("PatientDetails").whereEqualTo("hospitalText",hospital).whereEqualTo("unitText",unit).get()
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
                }
        }
    }


    private fun showPopup(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.search_menu)

        popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->

            when (item!!.itemId) {
                R.id.header1 -> {
                    spinner.adapter = null

                    nameT.hint = "Enter Name to Search"
                    nameT.editText?.setText("")
                    nameT.isEnabled = true
                    search.setOnClickListener {
                        list1.clear()
                        var nameentered = nameT.editText?.text.toString().toLowerCase()
                        for(item in list){
                            if(nameentered.length>=2 && item.name.toLowerCase().contains(nameentered)){
                                list1.add(item)
                            }else if(item.name.toLowerCase().startsWith(nameentered)){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatients::class.java)
                    //startActivity(intent)
                }
                R.id.header2 -> {
                    spinner.adapter = null
                    nameT.hint = "Enter Date to Search"
                    datePicker.visibility = View.VISIBLE
//                    nameT.isEnabled = true
                    search.setOnClickListener {
                        list1.clear()
                        var nameentered = nameT.editText?.text.toString()
                        Log.d("Daatee",nameentered)
                        var datee = nameentered.substring(6,10) + nameentered.substring(3,5) + nameentered.substring(0,2)
                        Log.d("Datee",datee)
                        for(item in list){
                            if(item.date == datee){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                    }
                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header3 -> {
                    nameT.hint = "Enter test to Search"
                    nameT.isEnabled = true
                    var diagnosis = resources.getStringArray(R.array.Diagnosis)
                    var nameentered = ""
                    val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,diagnosis)
                    spinner.adapter = adapter
                    spinner.onItemSelectedListener = object :
                        AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View, position: Int, id: Long
                        ) {

                            nameT.editText?.setText(diagnosis[position])
                            nameentered = diagnosis[position]


                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // write code to perform some action
                        }
                    }
                    search.setOnClickListener {
                        list1.clear()
                        var nameentered = nameT.editText?.text.toString()
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("PatientDetails").get()
                            .addOnSuccessListener {
                                for(document in it){
                                    var patient = document.toObject(PatientDetails::class.java)
                                    firestore.collection("PatientDetails").document(patient.id).collection("DiagnosisForm")
                                        .whereEqualTo("type",nameentered).get()
                                        .addOnSuccessListener {
                                            if(!it.isEmpty){
                                                list1.add(patient)
                                            }
                                            (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                                        }
                                }
                                (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()
                            }
                    }
                }
                R.id.header4 -> {
                    spinner.adapter = null

                        list1.clear()
                        for(item in list){
                            if(item.isimportant == "yes"){
                                list1.add(item)
                            }
                        }
                        (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()

                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header5 -> {
                    spinner.adapter = null

                    list1.clear()
                    for(item in list){
                        if(item.isstarred == "yes"){
                            list1.add(item)
                        }
                    }
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()

                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header6 -> {
                    spinner.adapter = null

                    list1.clear()
                    for(item in list){
                        if(item.issevere == "yes"){
                            list1.add(item)
                        }
                    }
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()

                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
                R.id.header7 -> {
                    spinner.adapter = null

                    list1.clear()
                    for(item in list){
                        if(item.isnearby == "yes"){
                            list1.add(item)
                        }
                    }
                    (recyclerView.adapter as AssignedPatientsAdapter).notifyDataSetChanged()

                    //val intent = Intent(this,AssignedPatientsByDate::class.java)
                    //startActivity(intent)
                }
            }

            true
        })

        popup.show()
    }

}