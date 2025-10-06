package com.example.excercise5

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.excercise5.databinding.ActivityUploadBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import java.util.UUID


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    lateinit var ActivityResultLauncher : ActivityResultLauncher<Intent>
    lateinit var PermissionLaauncher : ActivityResultLauncher<String>
    lateinit var storage : FirebaseStorage
    lateinit var auth : FirebaseAuth
    lateinit var firestore : FirebaseFirestore
    var selectedPicture : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
       storage = Firebase.storage
        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    fun upload(view: View){
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"
        val reference = storage.reference
        val imageReference = reference.child("images").child(imageName)

        if(selectedPicture != null){
            imageReference.putFile(selectedPicture!!).addOnSuccessListener {

            }.addOnFailureListener {
                Toast.makeText(this,it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }

    }

    fun selectImage(view: View){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Permission Needed", Snackbar.LENGTH_INDEFINITE).setAction("Give Permission"){
                    PermissionLaauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)

                }.show()
            }else{
                PermissionLaauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }else{
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            ActivityResultLauncher.launch(intent)

        }
    }
    private fun registerLauncher(){
        ActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == RESULT_OK){
                val intentFromResult = result.data
                if(intentFromResult != null){
                    selectedPicture = intentFromResult.data
                    selectedPicture?.let{
                        binding.imageView.setImageURI(it)
                    }
                }
            }
        }
        PermissionLaauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            result ->
            if(result){
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                ActivityResultLauncher.launch(intent)
            }else{
                Toast.makeText(this@UploadActivity,"permissiomn needed", Toast.LENGTH_LONG)
            }
        }
    }
}