package com.mksolution.newsshortadmin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.database.FirebaseDatabase

class ChartAdapter(var context: Context,var list: List<Chart>) :RecyclerView.Adapter<ChartAdapter.ChartHolder>() {

    inner class ChartHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tag = itemView.findViewById<TextView>(R.id.chartTag)
        val titlee = itemView.findViewById<TextView>(R.id.chartTitle)
        val img = itemView.findViewById<ShapeableImageView>(R.id.imChart)
        val delete = itemView.findViewById<ImageButton>(R.id.imageButton)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.chart_item,parent,false)
        return ChartHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ChartHolder, position: Int) {
        val data = list[position]
        holder.tag.text = data.chartTag
        holder.titlee.text = data.chartTitle
        if (data.chartUrl.isNotEmpty()){
            Glide.with(context).load(data.chartUrl).into(holder.img)
        }
        holder.delete.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference.child("Chart")
            // Assuming 'newsItem' holds the current news item's data

            dbRef.child(data.chartId).removeValue().addOnSuccessListener {
                Toast.makeText(context, "Chart deleted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to delete news: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}