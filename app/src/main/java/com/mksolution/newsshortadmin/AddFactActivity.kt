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
import com.mksolution.newsshortadmin.databinding.ActivityAddFactBinding
import java.util.*

class AddFactActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddFactBinding
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
        binding = ActivityAddFactBinding.inflate(layoutInflater)
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
        if (imageUri.toString().isEmpty()){
            Toast.makeText(this, "Pick Image", Toast.LENGTH_SHORT).show()
        }
        else if(binding.chartTag.text.toString().isEmpty()){
            binding.chartTag.error = "Enter Chart Tag "
        }
        else if(binding.chartTitle.text.toString().isEmpty()){
            binding.chartTitle.error = "Enter Chart Title "
        }


        else{
            saveImage(imageUri)
            dialog.show()
        }


    }

    private fun saveImage(uri: Uri?) {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("chartImage/$fileName")
        if (uri != null) {
            storageRef.putFile(uri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    storeFacts(image.toString())

                }
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Upload Storage Fail", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
        }
    }
    private fun storeFacts(imageUrl: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("Chart")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["chartUrl"] = imageUrl
        postMap["chartId"] = postId.toString()
        postMap["chartTitle"] = binding.chartTitle.text.toString()
        postMap["chartTag"] = binding.chartTag.text.toString()
        postMap["timestamp"] = System.currentTimeMillis()


        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "fact Added", Toast.LENGTH_SHORT).show()
                binding.chartTag.text?.clear()
                binding.chartTitle.text?.clear()


                imageUri = null
                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }
}