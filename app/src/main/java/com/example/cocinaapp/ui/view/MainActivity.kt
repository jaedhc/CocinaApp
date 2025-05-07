package com.example.cocinaapp.ui.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivityMainBinding
import com.example.cocinaapp.usecases.activities
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth:FirebaseAuth
    private val casosUsoAtivities by lazy { activities(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        auth = Firebase.auth

        if(auth.currentUser != null){
            casosUsoAtivities.callHome()
            finish()
        }

        binding.btnLogin.setOnClickListener {
            val i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

        binding.btnSignin.setOnClickListener {
            val i = Intent(this, SignUpActivity::class.java)
            startActivity(i)
        }
    }
}