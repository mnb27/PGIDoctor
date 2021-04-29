package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class AssignedDoctorsAdapter(var context: Context, var detailsList: MutableList<User>):
    RecyclerView.Adapter<AssignedDoctorsAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.nameOfTask)
        var hospital: TextView = itemView.findViewById(R.id.hospital)
        var unit: TextView = itemView.findViewById(R.id.unit)
        var whatsapp: Button = itemView.findViewById(R.id.whatsapp)
    }
    override fun onBindViewHolder(holder: AssignedDoctorsAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        holder.nameText.text = "Name: " + details.name
        holder.hospital.text = details.email
        holder.unit.text = "Hospital: " + details.hospital + " , Unit: " + details.unit

        var number = "+91"+details.mobile

        holder.whatsapp.setOnClickListener {
            val url = "https://api.whatsapp.com/send?phone=$number"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignedDoctorsAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_assigned_doctors_adapter,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    fun setList(list: MutableList<User>){
        detailsList = list
        notifyDataSetChanged()
    }
}