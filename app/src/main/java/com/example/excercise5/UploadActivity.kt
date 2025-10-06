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


class UploadActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadBinding
    lateinit var ActivityResultLauncher : ActivityResultLauncher<Intent>
    lateinit var PermissionLaauncher : ActivityResultLauncher<String>
    var selectedPicture : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        registerLauncher()
    }

    fun upload(view: View){

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