package com.example.cocinaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivitySignUpBinding
import com.example.cocinaapp.usecases.activities
import com.example.cocinaapp.ui.viewmodel.LogInViewModel
import com.example.cocinaapp.ui.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel
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

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        signUpViewModel.signupState.observe(this, Observer {
            if(it.equals("Created")){
                casosUsoAtivities.callHome()
                finish()
            }else{
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.btnSignup.setOnClickListener {
            val name = binding.txtName.text.toString()
            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPass.text.toString()

            signUpViewModel.signUp(name, email, pass)
        }

        binding.btnLogin.setOnClickListener {
            casosUsoAtivities.callLogIn()
            finish()
        }
    }
}