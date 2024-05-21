package com.example.cocinaapp.ui.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cocinaapp.usecases.activities
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class LogInViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    var loginState: MutableLiveData<String> = MutableLiveData()

    fun logIn(email:String, pass:String){
        auth = Firebase.auth
        val db = Firebase.firestore

        viewModelScope.launch(Dispatchers.IO) {
            try{
                val result = withContext(Dispatchers.IO){
                    auth.signInWithEmailAndPassword(email, pass).await()
                }

                db.collection("Users").document()

                if(result.user != null){
                    withContext(Dispatchers.Main){
                        loginState.value = "Logged"
                    }
                }
            }catch (e:Exception){
                withContext(Dispatchers.Main){
                    loginState.value = e.message
                }
            }
        }
    }
}