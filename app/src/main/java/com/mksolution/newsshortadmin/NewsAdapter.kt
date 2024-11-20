package com.mksolution.newsshortadmin

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase

class NewsAdapter(private var context: Context,var list: ArrayList<News>):RecyclerView.Adapter<NewsAdapter.NewsHolder>() {

    inner class NewsHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image = itemView.findViewById<ImageView>(R.id.newsImg)
        val title = itemView.findViewById<TextView>(R.id.newsTitle)
        val news = itemView.findViewById<TextView>(R.id.tvNews)
        val edit = itemView.findViewById<ImageView>(R.id.edit)
        val delete = itemView.findViewById<ImageView>(R.id.delete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.news_item,parent,false)
        return NewsHolder(view)
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NewsHolder, position: Int) {
        val  data = list[position]
        holder.title.text = data.newsTitle
        holder.news.text = data.news
        if (data.newsUrl.isNotEmpty()){
            Glide.with(context).load(data.newsUrl).into(holder.image)
        }
        holder.delete.setOnClickListener {
            val dbRef = FirebaseDatabase.getInstance().reference.child("News")
              // Assuming 'newsItem' holds the current news item's data

            dbRef.child(data.newsId).removeValue().addOnSuccessListener {
                Toast.makeText(context, "News deleted successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener { e ->
                Toast.makeText(context, "Failed to delete news: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

        holder.edit.setOnClickListener {
            val  intent = Intent(context,EditNewsActivity::class.java)
            intent.putExtra("ID",data.newsId)
            context.startActivity(intent)
        }


    }
}