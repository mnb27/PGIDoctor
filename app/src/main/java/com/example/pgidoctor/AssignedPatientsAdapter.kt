package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class AssignedPatientsAdapter(var context: Context, var detailsList: MutableList<PatientDetails>):
    RecyclerView.Adapter<AssignedPatientsAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.nameOfTask)
        var date: TextView = itemView.findViewById(R.id.date)
        var hospital: TextView = itemView.findViewById(R.id.hospital)
        var unit: TextView = itemView.findViewById(R.id.unit)
        var viewMore: Button = itemView.findViewById(R.id.viewMore)
        var whatsapp: Button = itemView.findViewById(R.id.whatsapp)
        var collectdiagnosisdata: Button = itemView.findViewById(R.id.collectdiagnosisdata)
        var starred: Button = itemView.findViewById(R.id.starred)
        var important: Button = itemView.findViewById(R.id.important)
        var nearby: Button = itemView.findViewById(R.id.nearby)
        var severe: Button = itemView.findViewById(R.id.severe)
        var profileImage: CircleImageView = itemView.findViewById(R.id.imageoftask)

    }
    override fun onBindViewHolder(holder: AssignedPatientsAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        holder.nameText.text = details.name

        var datee = details.date
        holder.date.text = "Taken On: " + datee.substring(6,8) + " / " + datee.substring(4,6) + " / " + datee.substring(0,4)
        holder.hospital.text = "Hospital: " + details.hospitalText
        holder.unit.text = "Unit: " + details.unitText


        var number = "+91"+details.mobile

        holder.whatsapp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }

        var clickStarred = true
        var clickImportant = true
        var clickNearby = true
        var clickSevere = true

        var isstarred = details.isstarred
        if(isstarred.toString() == "yes") {
            holder.starred.setBackgroundResource(R.drawable.star)
            clickStarred = false
        }else {
            holder.starred.setBackgroundResource(R.drawable.ic_star)
            clickStarred = true
        }

        var isnearby = details.isnearby
        if(isnearby.toString() == "yes") {
            holder.nearby.setBackgroundResource(R.drawable.nearby)
            clickNearby = false
        }else {
            holder.nearby.setBackgroundResource(R.drawable.ic_loaction)
            clickNearby = true
        }

        var issevere = details.issevere
        if(issevere.toString() == "yes") {
            holder.severe.setBackgroundResource(R.drawable.alarm)
            clickSevere = false
        }else {
            holder.severe.setBackgroundResource(R.drawable.ic_danger)
            clickSevere = true
        }

        var isimportant = details.isimportant
        if(isimportant.toString() == "yes") {
            holder.important.setBackgroundResource(R.drawable.information)
            clickImportant = false
        }else {
            holder.important.setBackgroundResource(R.drawable.ic_info)
            clickImportant = true
        }


        holder.viewMore.text = "View More"

        val url = details.profileImageUrl
        Log.d("dekha","$url")
        if(url.isNotEmpty()) {
            Picasso.with(context).load(url).into(holder.profileImage)
        }
        else {
            val urll = "https://cdn.pixabay.com/photo/2018/01/14/23/12/nature-3082832__340.jpg"
            Picasso.with(context).load(urll).into(holder.profileImage);
        }

        holder.viewMore.setOnClickListener {
            val intent = Intent(context,ViewMoreActivity::class.java)
            intent.putExtra("previousDetails",details)
            context.startActivity(intent)
        }

        holder.collectdiagnosisdata.setOnClickListener {
            val intent = Intent(context,ViewAllTestsActivity::class.java)
            intent.putExtra("previousDetails",details)
            context.startActivity(intent)
        }

        val id = details.id


        val firestore = FirebaseFirestore.getInstance()
        holder.starred.setOnClickListener(View.OnClickListener {
            clickStarred = if (clickStarred) {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "yes";
                Docref.update("isstarred" , mark)
                        .addOnSuccessListener {
                            holder.starred.setBackgroundResource(R.drawable.star)
                        }
                false
            } else {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "no";
                Docref.update("isstarred" , mark)
                        .addOnSuccessListener {
                            holder.starred.setBackgroundResource(R.drawable.ic_star)
                        }
                true
            }
        })

        holder.important.setOnClickListener(View.OnClickListener {
            clickImportant = if (clickImportant) {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "yes";
                Docref.update("isimportant" , mark)
                        .addOnSuccessListener {
                            holder.important.setBackgroundResource(R.drawable.information)
                        }
                false
            } else {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "no";
                Docref.update("isimportant" , mark)
                        .addOnSuccessListener {
                            holder.important.setBackgroundResource(R.drawable.ic_info)
                        }
                true
            }
        })

        holder.nearby.setOnClickListener(View.OnClickListener {
            clickNearby = if (clickNearby) {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "yes";
                Docref.update("isnearby" , mark)
                        .addOnSuccessListener {
                            holder.nearby.setBackgroundResource(R.drawable.nearby)
                        }
                false
            } else {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "no";
                Docref.update("isnearby" , mark)
                        .addOnSuccessListener {
                            holder.nearby.setBackgroundResource(R.drawable.ic_loaction)
                        }
                true
            }
        })

        holder.severe.setOnClickListener(View.OnClickListener {
            clickSevere = if (clickSevere) {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "yes";
                Docref.update("issevere" , mark)
                        .addOnSuccessListener {
                            holder.severe.setBackgroundResource(R.drawable.alarm)
                        }
                false
            } else {
                var Docref = firestore.collection("PatientDetails").document(id);
                var mark = "no";
                Docref.update("issevere" , mark)
                        .addOnSuccessListener {
                            holder.severe.setBackgroundResource(R.drawable.ic_danger)
                        }
                true
            }
        })

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignedPatientsAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_assigned_patients_adapter,parent,false)
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