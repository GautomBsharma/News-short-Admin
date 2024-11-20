package com.mksolution.newsshortadmin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessaging
import androidx.activity.result.contract.ActivityResultContracts
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mksolution.newsshortadmin.Api.NotificationApi
import com.mksolution.newsshortadmin.Models.Notification
import com.mksolution.newsshortadmin.databinding.ActivityAddNewsBinding
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.util.*

class AddNewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewsBinding
    private lateinit var dialog: Dialog
    private var imageUri : Uri?=null
    private var selectedItem :Any = ""


    private var launchGelaryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.imageView.setImageURI(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(true)
        val sura_ty = resources.getStringArray(R.array.news_tag)
        val arrayAdapter = ArrayAdapter(this,R.layout.drop_doun_item,sura_ty)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)
        binding.autoCompleteTextView.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            selectedItem = adapterView.getItemAtPosition(i)

        }
        binding.imageView.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGelaryActivity.launch(intent)
        }
        binding.btnUp.setOnClickListener {
            validateData()

        }
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


    }


    private fun validateData() {
        

        if (selectedItem.toString().isEmpty()){
            Toast.makeText(this, "Select Tag", Toast.LENGTH_SHORT).show()

        }
        else if(binding.newsTitle.text.toString().isEmpty()){
            binding.newsTitle.error = "Enter News Title "
        }
        else if(binding.news.text.toString().isEmpty()){
            binding.news.error = "Enter News"
        }
        else{
            if (imageUri != null) {
                saveImage(imageUri)
                dialog.show()
            } else {
                saveData2()
                dialog.show()
            }
        }


    }

    private fun saveData2() {
        val dbRef = FirebaseDatabase.getInstance().reference.child("News")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["newsUrl"] = ""
        postMap["newsId"] = postId.toString()
        postMap["newsSource"] = binding.newsSource.text.toString()
        postMap["newsTag"] = selectedItem
        postMap["newsTitle"] = binding.newsTitle.text.toString()
        postMap["news"] = binding.news.text.toString()
        postMap["newsTime"] = System.currentTimeMillis()

        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "news Added", Toast.LENGTH_SHORT).show()
                if (binding.checkBoxNotification.isChecked){
                    sendNotification(binding.newsTitle.text.toString(),"")
                }

                binding.newsTitle.text?.clear()
                binding.news.text?.clear()
                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }


    private fun saveImage(uri: Uri?) {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("NewsImage/$fileName")
        if (uri != null) {
            storageRef.putFile(uri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    storeNews(image.toString())

                }
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Upload Storage Fail", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
        }
    }

    private fun storeNews(imageUrl: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("News")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["newsUrl"] = imageUrl
        postMap["newsId"] = postId.toString()
        postMap["newsTag"] = selectedItem
        postMap["newsTitle"] = binding.newsTitle.text.toString()
        postMap["news"] = binding.news.text.toString()
        postMap["newsTime"] = System.currentTimeMillis()

        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "news Added", Toast.LENGTH_SHORT).show()
                if (binding.checkBoxNotification.isChecked){
                    sendNotification(binding.newsTitle.text.toString(),imageUrl)
                }

                binding.newsTitle.text?.clear()
                binding.news.text?.clear()
                imageUri = null

                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    fun sendNotification (title: String,imageUrl: String){
        val notification = Notification(
           message =  NotificationData(
               "test",
               hashMapOf("title" to title,
                   "imageUrl" to imageUrl
               )
            )
        )

        NotificationApi.sendNotification().notification(notification).enqueue(
            object :Callback<Notification>{
                override fun onResponse(
                    p0: Call<Notification>,
                    p1: retrofit2.Response<Notification>
                ) {
                    Toast.makeText(this@AddNewsActivity, "Notification send", Toast.LENGTH_SHORT).show()

                }

                override fun onFailure(p0: Call<Notification>, p1: Throwable) {
                    Toast.makeText(this@AddNewsActivity, "Notification not sent: ${p1.message}", Toast.LENGTH_SHORT).show()

                }

            }
        )
    }

}