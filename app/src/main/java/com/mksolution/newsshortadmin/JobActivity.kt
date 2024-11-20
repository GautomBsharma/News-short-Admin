package com.mksolution.newsshortadmin


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mksolution.newsshortadmin.databinding.ActivityJobBinding

class JobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityJobBinding
    private lateinit var adapter: JobAdapter

    private lateinit var list: ArrayList<Job>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewJob.layoutManager = LinearLayoutManager(this)
        list = arrayListOf()
        adapter = JobAdapter(this,list)
        binding.recyclerViewJob.adapter = adapter

        binding.button4.setOnClickListener {
            startActivity(Intent(this,AddJobActivity::class.java))
        }
        getData()

    }
    private fun getData() {
        val reff = FirebaseDatabase.getInstance().reference.child("Job")
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()  // Clear the list before adding new items
                    for (snap in snapshot.children) {
                        val datt = snap.getValue(Job::class.java)
                        datt?.let { list.add(it) }
                    }
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}