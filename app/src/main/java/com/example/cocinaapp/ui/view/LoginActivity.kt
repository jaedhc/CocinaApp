package com.example.cocinaapp.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivityLoginBinding
import com.example.cocinaapp.usecases.activities
import com.example.cocinaapp.ui.viewmodel.LogInViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var logInViewModel: LogInViewModel
    private val casosUsoAtivities by lazy { activities(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        logInViewModel = ViewModelProvider(this).get(LogInViewModel::class.java)

        logInViewModel.loginState.observe(this, Observer {
            if(it.equals("Logged")){
                casosUsoAtivities.callHome()
                finish()
            } else {
                //Manejar errores en la UI
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            }
        })

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPass.text.toString()

            logInViewModel.logIn(email, pass)
        }

        binding.btnSignup.setOnClickListener {
            casosUsoAtivities.callSignUp()
            finish()
        }

    }
}