package com.mksolution.newsshortadmin

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mksolution.newsshortadmin.databinding.ActivityAddJobBinding
import java.util.*

class AddJobActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddJobBinding
    private lateinit var dialog: Dialog
    private var imageUri : Uri?=null
    private var selectedItem :Any = ""
    private var launchGelaryActivity = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.resultCode == Activity.RESULT_OK){
            imageUri = it.data!!.data
            binding.imageJob.setImageURI(imageUri)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddJobBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dialog = Dialog(this)
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(true)
        val sura_ty = resources.getStringArray(R.array.degree)
        val arrayAdapter = ArrayAdapter(this,R.layout.drop_doun_item,sura_ty)
        binding.autoCompleteTextViewJob.setAdapter(arrayAdapter)
        binding.autoCompleteTextViewJob.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            selectedItem = adapterView.getItemAtPosition(i)

        }
        binding.imageJob.setOnClickListener {
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGelaryActivity.launch(intent)
        }
        binding.btnUp.setOnClickListener {
            validateData()

        }
    }

    private fun validateData() {


        if (selectedItem.toString().isEmpty()){
            Toast.makeText(this, "Select Degree", Toast.LENGTH_SHORT).show()

        }
        else if(binding.jobTitle.text.toString().isEmpty()){
            binding.jobTitle.error = "Enter Job Title "
        }
        else if(binding.jobDedline.text.toString().isEmpty()){
            binding.jobDedline.error = "Enter Job Deadline "
        }
        else if(binding.jobSloat.text.toString().isEmpty()){
            binding.jobSloat.error = "Enter Job Slot "
        }
        else if(binding.jobLocation.text.toString().isEmpty()){
            binding.jobLocation.error = "Enter Job location "
        }

        else if(binding.jobExperiance.text.toString().isEmpty()){
            binding.jobExperiance.error = "Enter Job Experiance "
        }
        else if(binding.jobProvider.text.toString().isEmpty()){
            binding.jobProvider.error = "Enter Job Provider "
        }
        else if(binding.jobAbout.text.toString().isEmpty()){
            binding.jobAbout.error = "Enter about ((type, time ,facility)) "
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
        val dbRef = FirebaseDatabase.getInstance().reference.child("Job")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["jobUrl"] = ""
        postMap["jobId"] = postId.toString()
        postMap["jobDeadline"] = binding.jobDedline.text.toString()
        postMap["jobSloat"] = binding.jobSloat.text.toString()
        postMap["jobProvider"] = binding.jobProvider.text.toString()
        postMap["jobProviderLink"] = binding.jobProviderLink.text.toString()
        postMap["jobLocation"] = binding.jobLocation.text.toString()
        postMap["jobExperiance"] = binding.jobExperiance.text.toString()
        postMap["jobTitle"] = binding.jobTitle.text.toString().lowercase()
        postMap["jobAbout"] = binding.jobAbout.text.toString()
        postMap["jobSalary"] = binding.jobSelary.text.toString()
        postMap["jobTime"] = System.currentTimeMillis()


        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "Job Added", Toast.LENGTH_SHORT).show()
                binding.jobSloat.text?.clear()
                binding.jobAbout.text?.clear()
                binding.jobTitle.text?.clear()
                binding.jobExperiance.text?.clear()
                binding.jobProvider.text?.clear()
                binding.jobDedline.text?.clear()
                binding.jobLocation.text?.clear()
                binding.jobProviderLink.text?.clear()
                binding.jobSelary.text?.clear()

                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }


    private fun saveImage(uri: Uri?) {
        val fileName = UUID.randomUUID().toString()+".jpg"
        val storageRef = FirebaseStorage.getInstance().reference.child("JobImage/$fileName")
        if (uri != null) {
            storageRef.putFile(uri).addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image->
                    storeJob(image.toString())

                }
            }
                .addOnFailureListener{
                    Toast.makeText(this, "Upload Storage Fail", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
        }
    }

    private fun storeJob(imageUrl: String) {
        val dbRef = FirebaseDatabase.getInstance().reference.child("Job")
        val postId= dbRef.push().key
        val postMap = HashMap<String, Any>()
        postMap["jobUrl"] = imageUrl
        postMap["jobId"] = postId.toString()
        postMap["jobDeadline"] = binding.jobDedline.text.toString()
        postMap["jobSloat"] = binding.jobSloat.text.toString()
        postMap["jobProvider"] = binding.jobProvider.text.toString()
        postMap["jobProviderLink"] = binding.jobProviderLink.text.toString()
        postMap["jobLocation"] = binding.jobLocation.text.toString()
        postMap["jobExperiance"] = binding.jobExperiance.text.toString()
        postMap["jobTitle"] = binding.jobTitle.text.toString()
        postMap["jobAbout"] = binding.jobAbout.text.toString()
        postMap["jobSalary"] = binding.jobSelary.text.toString()
        postMap["jobTime"] = System.currentTimeMillis()

        if (postId != null) {
            dbRef.child(postId).setValue(postMap).addOnSuccessListener {
                Toast.makeText(this, "Job Added", Toast.LENGTH_SHORT).show()


                binding.jobSloat.text?.clear()
                binding.jobAbout.text?.clear()
                binding.jobTitle.text?.clear()
                binding.jobExperiance.text?.clear()
                binding.jobProvider.text?.clear()
                binding.jobDedline.text?.clear()
                binding.jobLocation.text?.clear()
                binding.jobProviderLink.text?.clear()
                binding.jobSelary.text?.clear()
                imageUri = null
                dialog.dismiss()

            }.addOnFailureListener {
                Toast.makeText(this, "Upload  Fail", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }


}