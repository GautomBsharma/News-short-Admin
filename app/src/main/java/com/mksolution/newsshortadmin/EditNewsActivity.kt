package com.mksolution.newsshortadmin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mksolution.newsshortadmin.databinding.ActivityEditNewsBinding
import java.util.*

class EditNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditNewsBinding
    private var selectedItem :Any = ""
    private var imageUrl :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("ID").toString()

        val databaseReference = FirebaseDatabase.getInstance().getReference("News").child(id)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val blog = snapshot.getValue(News::class.java)
                if (blog != null) {
                    binding.newsTitle.setText(blog.newsTitle)
                }
                if (blog != null) {
                    binding.news.setText(blog.news)
                }

                if (blog != null) {
                    if (blog.newsUrl.isNotEmpty()){


                        Glide.with(this@EditNewsActivity).load(blog.newsUrl).into(binding.imageView)
                    }
                }
                if (blog != null) {
                    selectedItem = blog.newsTag
                }


            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })


    }
}