package com.mksolution.newsshortadmin

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import com.mksolution.newsshortadmin.databinding.ActivityAddPollBinding
import com.mksolution.newsshortadmin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: NewsAdapter
    private lateinit var list: ArrayList<News>
    companion object {
        const val NOTIFICATION_PERMISSION_CODE = 1001
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
                // Permission already granted, you can send notifications
            } else {
                // Permission not granted, request it
                requestNotificationPermission()
            }
        } else {
            // For versions below Android 13, no need to request permission
            // Send notifications directly
        }


        binding.button3.setOnClickListener {
            startActivity(Intent(this,AddNewsActivity::class.java))
        }
        binding.button2.setOnClickListener {
            startActivity(Intent(this,ChartActivity::class.java))
        }
        binding.Job.setOnClickListener {
            startActivity(Intent(this,JobActivity::class.java))
        }
        binding.recycleNews.layoutManager = LinearLayoutManager(this)
        list = arrayListOf()
        adapter = NewsAdapter(this,list)
        binding.recycleNews.adapter = adapter

        FirebaseMessaging.getInstance().subscribeToTopic("test")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
                else {

                }
            }
        getData()


    }
    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                NOTIFICATION_PERMISSION_CODE
            )
        }
    }


    private fun getData() {
        val reff = FirebaseDatabase.getInstance().reference.child("News")
        reff.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    list.clear()  // Clear the list before adding new items
                    for (snap in snapshot.children) {
                        val datt = snap.getValue(News::class.java)
                        datt?.let { list.add(it) }
                    }
                    list.reverse()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == NOTIFICATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Permission granted, you can now send notifications
            } else {
                requestNotificationPermission()
                // Permission denied, notify the user or disable notification features
            }
        }
    }

}