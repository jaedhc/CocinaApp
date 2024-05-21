package com.example.cocinaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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