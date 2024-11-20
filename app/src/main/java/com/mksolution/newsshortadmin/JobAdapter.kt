package com.mksolution.newsshortadmin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class JobAdapter (var context: Context, var jobList: ArrayList<Job>): RecyclerView.Adapter<JobAdapter.JobHolder>(){

    inner class JobHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.jobImg)
        val jobtitle = itemView.findViewById<TextView>(R.id.jobTitle)
        val provider = itemView.findViewById<TextView>(R.id.tvProvider)
        val deleteJob = itemView.findViewById<ImageButton>(R.id.delateJob)
        val about = itemView.findViewById<TextView>(R.id.jobDes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.job_item,parent,false)
        return JobHolder(view)
    }

    override fun getItemCount(): Int {
        return jobList.size
    }

    override fun onBindViewHolder(holder: JobHolder, position: Int) {
        val data = jobList[position]
        holder.jobtitle.text = data.jobTitle
        holder.provider.text = data.jobProvider
        holder.about.text = data.jobAbout
        if(data.jobUrl.isNotEmpty()){
            Glide.with(context).load(data.jobUrl).into(holder.image)
        }
        else{
            holder.image.setImageResource(R.drawable.img_4)
        }

        holder.deleteJob.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference.child("Job")
            // Assuming 'newsItem' holds the current news item's data

            dbRef.child(data.jobId).removeValue().addOnSuccessListener {
                Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to delete news: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun updateData(newData: ArrayList<Job>) {
        jobList = newData
        notifyDataSetChanged()
    }
}