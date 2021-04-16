package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        var collectdiagnosisdata: Button = itemView.findViewById(R.id.collectdiagnosisdata)
        var profileImage: CircleImageView = itemView.findViewById(R.id.imageoftask)

    }
    override fun onBindViewHolder(holder: AssignedPatientsAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        holder.nameText.text = "Name: " + details.name
        holder.date.text = "Collection Date: " + details.date
        holder.hospital.text = "Hospital: " + details.hospitalText
        holder.unit.text = "Unit: " + details.unitText


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