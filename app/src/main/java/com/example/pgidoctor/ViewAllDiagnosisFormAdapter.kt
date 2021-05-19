package com.example.pgidoctor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class ViewAllDiagnosisFormAdapter(var context: Context, var detailsList: MutableList<PatientDiagnosisDetails>,val id: String):
    RecyclerView.Adapter<ViewAllDiagnosisFormAdapter.DetailsViewHolder>() {

    class DetailsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var nameText: TextView = itemView.findViewById(R.id.nameOfTask)
        var date: TextView = itemView.findViewById(R.id.date)
        var viewMore: Button = itemView.findViewById(R.id.viewMore)
        var editButton: ImageView = itemView.findViewById(R.id.editButton)
        var text: Button = itemView.findViewById(R.id.textonly)
    }
    override fun onBindViewHolder(holder: ViewAllDiagnosisFormAdapter.DetailsViewHolder, position: Int) {
        var details = detailsList[position]
        var pos = position + 1
        holder.nameText.text = "Report # $pos"

        var datee = details.date
        if (datee != null) {
            holder.date.text = "Taken On: " + datee.substring(6,8) + " / " + datee.substring(4,6) + " / " + datee.substring(0,4)
        }

        if(details.doctorRemarks == ""){
            holder.text.visibility = View.VISIBLE
        }
        
        holder.viewMore.text = "View Details"

        holder.viewMore.setOnClickListener {
            val intent = Intent(context,ViewMoreDetailActivity::class.java)
            intent.putExtra("previousDetails",details)

            context.startActivity(intent)
        }
        holder.editButton.setOnClickListener{
            val intent = Intent(context,UpdateDiagnosisFormActivity::class.java)
            intent.putExtra("previousDetails",details)
            intent.putExtra("patientid",id)
            context.startActivity(intent)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewAllDiagnosisFormAdapter.DetailsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.view_diagnosis_form,parent,false)
        return DetailsViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return detailsList.size
    }

    fun setList(list: MutableList<PatientDiagnosisDetails>){
        detailsList = list
        notifyDataSetChanged()
    }
}