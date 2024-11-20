package com.mksolution.newsshortadmin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mksolution.newsshortadmin.databinding.ActivityChartBinding

class ChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChartBinding
    private lateinit var adapter: ChartAdapter
    private lateinit var list: ArrayList<Chart>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycleChart.layoutManager = LinearLayoutManager(this)
        list = arrayListOf()
        adapter = ChartAdapter(this,list)
        binding.recycleChart.adapter = adapter

        getData()

        binding.button.setOnClickListener {
            startActivity(Intent(this,AddFactActivity::class.java))
        }

    }

    private fun getData() {
        val reff = FirebaseDatabase.getInstance().reference.child("Chart")
        reff.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()  // Clear the list before adding new items
                    for (snap in snapshot.children) {
                        val datt = snap.getValue(Chart::class.java)
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