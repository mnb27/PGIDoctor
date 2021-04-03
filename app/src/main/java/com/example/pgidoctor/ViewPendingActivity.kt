package com.example.pgidoctor

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ViewPendingActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var ashadetailsAdapter: ViewPendingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_pending_activity)


        val fireStore = FirebaseFirestore.getInstance()
        val auth = FirebaseAuth.getInstance()

        var list: MutableList<PendingList> = mutableListOf()


        recyclerView = findViewById(R.id.recyclerView)
        ashadetailsAdapter = ViewPendingAdapter(this,list)


        recyclerView.adapter = ashadetailsAdapter

        recyclerView.layoutManager = LinearLayoutManager(this)

        fireStore.collection("PendingList").get()
            .addOnSuccessListener { documents->
                for(document in documents) {
                    list.add(document.toObject(PendingList::class.java))
                }
                (recyclerView.adapter as ViewPendingAdapter).notifyDataSetChanged()
                if(list.isEmpty()) {
                    Toast.makeText(this,"No pending requests",Toast.LENGTH_LONG).show()
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }
    }


}