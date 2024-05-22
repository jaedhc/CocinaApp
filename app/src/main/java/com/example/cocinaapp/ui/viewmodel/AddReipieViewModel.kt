package com.example.cocinaapp.ui.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddReipieViewModel: ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()
    suspend fun uploadImg(id: String, uri:Uri): String{
        return try {
            val ref = storage.reference.child("Recipies/$id.png")
            ref.putFile(uri).await()
            ref.downloadUrl.await().toString()
        } catch (e: Exception){
            "https://firebasestorage.googleapis.com/v0/b/cocinapomodoro.appspot.com/o/Recipies%2Fdef_recipie.jpg?alt=media&token=f112f80f-5f4f-475c-bde1-21fbea3baddc"

        }
    }

    fun setRecipie(map: HashMap<String, Any>){
        val id = map.get("recipieId").toString()
        viewModelScope.launch(Dispatchers.IO){
            firestore.collection("Recipies").document(id).set(map)
        }
    }

}