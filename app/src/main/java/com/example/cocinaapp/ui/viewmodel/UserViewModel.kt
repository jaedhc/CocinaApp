package com.example.cocinaapp.ui.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class UserViewModel: ViewModel() {

    private val storage = FirebaseStorage.getInstance()
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

    fun uploadImg(uri:Uri){
        auth = Firebase.auth

        val ref = storage.reference.child("Users/${auth.uid}.png")

        var map: Map<String, Any> = mapOf("photoURL" to "")

        viewModelScope.launch(Dispatchers.IO){
            ref.putFile(uri).addOnCompleteListener{
                if(it.isSuccessful){
                    viewModelScope.launch(Dispatchers.IO){
                        ref.downloadUrl.addOnSuccessListener { uriD ->
                            map = mapOf<String, Any>(
                                "photoURL" to uriD
                            )

                            val mapDos = mapOf<String, Any>(
                                "created_by.user_photo" to uriD
                            )

                            firestore.collection("Users")
                                .document(auth.uid.toString())
                                .update(map).addOnFailureListener(){
                                    Log.d("Photo Error", it.message.toString())
                                }

                            firestore
                                .collection("Recipies")
                                .whereEqualTo("created_by.user_id", auth.uid)
                                .get().addOnSuccessListener {
                                    it.forEach{ doc ->
                                        doc.reference.update(mapDos)
                                    }
                                }


                        }.await()

                        withContext(Dispatchers.Main){
                            userPhoto.value = map.get("photoURL").toString()
                        }

                    }
                } else {
                    Log.d("FORTNITE", it.toString())
                }
            }
        }
    }

}