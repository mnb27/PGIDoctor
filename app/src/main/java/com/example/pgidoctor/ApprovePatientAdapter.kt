package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText

import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference

class ApprovePatientAdapter(var context: Context, var detailsList: MutableList<PatientDetails>):
    RecyclerView.Adapter<ApprovePatientAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var email: TextView = itemView.findViewById(R.id.email)
        var hospital: TextView = itemView.findViewById(R.id.hospital)
        var unit: TextView = itemView.findViewById(R.id.unit)
        var approve: Button = itemView.findViewById(R.id.approve)


    }
    override fun onBindViewHolder(holder: ApprovePatientAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        holder.name.text = "Name: " + details.name
        holder.email.text = "Email: " + details.email
        holder.hospital.text = "Hospital: " + details.hospitalText
        holder.unit.text = "Unit: " + details.unitText


        holder.approve.setOnClickListener {
            var Docref = FirebaseFirestore.getInstance().collection("PendingList").document(details.mobile);
            var ans = "approved";
            Docref.update("status" , ans);

            // hide card on approval
            holder.name.visibility = View.GONE
            holder.email.visibility = View.GONE
            holder.hospital.visibility = View.GONE
            holder.unit.visibility = View.GONE

            holder.approve.visibility = View.GONE

            val auth = FirebaseAuth.getInstance()
            var email = details.email.toString()
            var password = "Test12345"
            var name = details.name
            var mobile = details.mobile
            var hospital = details.hospitalText
            var unit = details.unitText
            val id = details.id

            var uid = auth.currentUser.uid
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = User(
                            auth.currentUser?.uid!!,
                            name,
                            mobile,
                            email,
                            "Patient",
                            hospital,
                            unit
                        )
                        val firestore = FirebaseFirestore.getInstance()
                        firestore.collection("Users").document(auth.currentUser.uid).set(user)
                            .addOnSuccessListener {
                                firestore.collection("PendingPatientDetails").document(id).delete()
                                    .addOnSuccessListener {
                                        auth.signOut()
                                    }
                            }






                    } else {
                        Toast.makeText(context, "Unable to add member", Toast.LENGTH_LONG).show()
                    }
                }



            Toast.makeText(context, "Approved.", Toast.LENGTH_LONG).show()
        }




    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApprovePatientAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.approve_patient_item,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    fun setList(list: MutableList<PatientDetails>){
        detailsList = list
        notifyDataSetChanged()
    }
}