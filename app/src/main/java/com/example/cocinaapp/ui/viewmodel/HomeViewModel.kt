package com.example.cocinaapp.ui.viewmodel

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

class HomeViewModel: ViewModel(){

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    var userName: MutableLiveData<String> = MutableLiveData()
    var userPhoto: MutableLiveData<String> = MutableLiveData()

    fun getUserData(){
        auth = Firebase.auth
        val db = Firebase.firestore
        var usrname:String = ""
        var photo:String = ""

        viewModelScope.launch(Dispatchers.IO) {
            try{
                db.collection("Users")
                    .document(auth.currentUser!!.uid)
                    .get().addOnCompleteListener {
                        if(it.isSuccessful){
                            usrname = it.result.get("name").toString()
                            photo = it.result.get("photoURL").toString()
                        }
                    }.await()

                withContext(Dispatchers.Main){
                    userName.value = usrname
                    userPhoto.value = photo
                }

            }catch(e:Exception){
                withContext(Dispatchers.Main){
                    userName.value = "Error"
                    userPhoto.value = e.message.toString()
                }
            }
        }
    }

}