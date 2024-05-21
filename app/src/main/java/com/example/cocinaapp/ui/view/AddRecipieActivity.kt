package com.example.cocinaapp.ui.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.cocinaapp.databinding.ActivityAddRecipieBinding

class AddRecipieActivity : AppCompatActivity() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var binding: ActivityAddRecipieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var imgUri: Uri

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
            it?.let {
                imgUri = it
                binding.imgRecipie.setImageURI(imgUri)
            }
        }

        binding.imgRecipie.setOnClickListener {
            getContent.launch("image/*")
        }

    }
}