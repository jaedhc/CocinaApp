package com.example.cocinaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.data.model.RecipiesRepo
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel(){

    val recipiesRepo = RecipiesRepo()
    var userName: MutableLiveData<String> = MutableLiveData()
    var userPhoto: MutableLiveData<String> = MutableLiveData()
    fun getUserRecipies(id:String): LiveData<List<RecipiesModel>>{
        return recipiesRepo.getSharedOrOwned(id)
    }

    fun getUserData(id:String){
        val db = Firebase.firestore
        var usrname:String = ""
        var photo:String = ""

        viewModelScope.launch(Dispatchers.IO) {
            try{
                db.collection("Users")
                    .document(id)
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