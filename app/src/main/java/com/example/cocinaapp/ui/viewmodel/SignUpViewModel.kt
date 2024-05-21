package com.example.cocinaapp.ui.viewmodel

import android.provider.Settings.Global.getString
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignUpViewModel: ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    var signupState: MutableLiveData<String> = MutableLiveData()

    fun signUp(name: String, email:String, pass:String){
        auth = Firebase.auth
        val db = Firebase.firestore

        var state:String = ""

        viewModelScope.launch(Dispatchers.IO) {
            auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {task ->
                if(task.isSuccessful){
                    val user = hashMapOf(
                        "userId" to auth.currentUser!!.uid,
                        "name" to name,
                        "email" to email,
                        "status" to "default",
                        "photoURL" to "https://firebasestorage.googleapis.com/v0/b/cocinapomodoro.appspot.com/o/Users%2Fdefault_pic.jpg?alt=media&token=9d3e04ad-5cf4-4174-bfbd-baacbe4e22fd",
                        "role" to "user",
                        "created_at" to System.currentTimeMillis()
                    )
                    viewModelScope.launch(Dispatchers.IO){
                        db.collection("Users")
                            .document(auth.currentUser!!.uid).set(user)
                            .addOnCompleteListener { usr ->
                                if(usr.isSuccessful){
                                    state = "Created"
                                } else {
                                    state = usr.exception.toString()
                                }
                            }.await()

                        withContext(Dispatchers.Main){
                            signupState.value = state
                        }
                    }
                } else {
                    state = task.exception.toString()
                }
            }.await()
        }

    }

}