package com.mksolution.newsshortadmin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mksolution.newsshortadmin.databinding.ActivityAddPollBinding
import java.util.*

class AddPollActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddPollBinding

    private lateinit var dialog: Dialog
    private var imageUri : Uri?=null
    private var launchGelaryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.imageView.setImageURI(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddPollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(true)

        binding.imageView.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGelaryActivity.launch(intent)
        }
        binding.btnUp.setOnClickListener {
            validateData()
        }
    }
    private fun validateData() {

         if(binding.pollTitle.text.toString().isEmpty()){
            binding.pollTitle.error = "Enter poll Title "
        }
        else if(binding.pollQuestion.text.toString().isEmpty()){
            binding.pollQuestion.error = "Enter Poll Question"
        }
        else{
            if (imageUri != null){
                saveImage(imageUri)
                dialog.show()
            }
            else {
                saveData2()
                dialog.show()
            }
        }


    }
    private fun saveData2() {
        val dbRef = FirebaseDatabase.getInstance().reference.child("Poll")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["pollUrl"] = ""
        postMap["pollId"] = postId.toString()
        postMap["pollTitle"] = binding.pollTitle.text.toString()
        postMap["pollQuestion"] = binding.pollQuestion.text.toString()
        postMap["timestamp"] = System.currentTimeMillis()

        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "Poll Added", Toast.LENGTH_SHORT).show()
                binding.pollTitle.text?.clear()
                binding.pollQuestion.text?.clear()
                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }


    private fun saveImage(uri: Uri?) {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("PollImage/$fileName")
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
        val dbRef = FirebaseDatabase.getInstance().reference.child("Poll")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["pollUrl"] = imageUrl
        postMap["pollId"] = postId.toString()
        postMap["pollTitle"] = binding.pollTitle.text.toString()
        postMap["pollQuestion"] = binding.pollQuestion.text.toString()
        postMap["timestamp"] = System.currentTimeMillis()
        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "poll Added", Toast.LENGTH_SHORT).show()
                binding.pollTitle.text?.clear()
                binding.pollQuestion.text?.clear()
                imageUri = null
                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }
}