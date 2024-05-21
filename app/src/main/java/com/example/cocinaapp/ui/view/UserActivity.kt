package com.example.cocinaapp.ui.view

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivityUserBinding
import com.example.cocinaapp.usecases.activities
import com.example.cocinaapp.ui.viewmodel.LogInViewModel
import com.example.cocinaapp.ui.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class UserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserBinding
    private lateinit var userViewModel: UserViewModel
    private val casosUsoAtivities by lazy { activities(this) }
    private lateinit var auth: FirebaseAuth

    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = ViewModelProvider(this).get(UserViewModel::class.java)

        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)

        val name = sharedPreferences.getString("name", "user")
        val photo = sharedPreferences.getString("photo", "user")

        Glide.with(this)
            .load(photo)
            .circleCrop()
            .into(binding.usrImg)

        binding.txtName.text = name

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()){
            it?.let {
                userViewModel.uploadImg(it)
            }
        }

        binding.btnLogout.setOnClickListener {
            logOut()
        }

        binding.usrImg.setOnClickListener {
            getContent.launch("image/*")

            userViewModel.userPhoto.observe(this, Observer {
                sharedPreferences.edit().putString("photo", it).apply()
                Glide.with(this)
                    .load(it)
                    .circleCrop()
                    .into(binding.usrImg)
            })

        }
    }

    private fun logOut(){
        auth = Firebase.auth
        val sharedPreferences = applicationContext.getSharedPreferences("User", Context.MODE_PRIVATE)
        val edit = sharedPreferences.edit()
        edit.clear().apply()

        auth.signOut()
        casosUsoAtivities.callMain()
        finish()

    }
}