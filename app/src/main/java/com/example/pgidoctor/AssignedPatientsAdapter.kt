package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AssignedPatientsAdapter(var context: Context, var detailsList: MutableList<PatientDetails>):
    RecyclerView.Adapter<AssignedPatientsAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.nameOfTask)
        var date: TextView = itemView.findViewById(R.id.date)
        var hospital: TextView = itemView.findViewById(R.id.hospital)
        var unit: TextView = itemView.findViewById(R.id.unit)
        var viewMore: TextView = itemView.findViewById(R.id.viewMore)

    }
    override fun onBindViewHolder(holder: AssignedPatientsAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        holder.nameText.text = "Name: " + details.name
        holder.date.text = "Date Of Collection: " + details.date
        holder.hospital.text = "Hospital: " + details.hospitalText
        holder.unit.text = "Unit: " + details.unitText


        holder.viewMore.text = "View More"

        holder.viewMore.setOnClickListener {
            val intent = Intent(context,ViewMoreActivity::class.java)
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