package com.example.cocinaapp.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.data.model.RecipiesRepo
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID
import kotlin.random.Random

class RecipieViewModel: ViewModel() {

    val recipiesRepo = RecipiesRepo()
    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    fun getRecipie(id:String):LiveData<RecipiesModel>{
        return recipiesRepo.getRecipie(id)
    }

    fun updateLike(id:String, liked:Boolean){
        auth = Firebase.auth
        viewModelScope.launch {
            val doc = firestore.collection("Recipies").document(id)
            if(liked){
                doc.update("likes", FieldValue.increment(1))
                firestore
                    .collection("Liked")
                    .document(UUID.randomUUID().toString())
                    .set(hashMapOf(
                        "recipie_id" to id,
                        "user_id" to auth.uid
                    ))
            } else {
                doc.update("likes", FieldValue.increment(-1))
                val docDel = firestore.collection("Liked")
                    .whereEqualTo("user_id", auth.uid)
                    .whereEqualTo("recipie_id", id)
                    .get().addOnSuccessListener {
                        for(doc in it.documents){
                            doc.reference.delete().addOnSuccessListener {

                            }
                        }
                    }
                    /*.get().addOnCompleteListener{
                        if(it.isSuccessful){
                            it.result.documents.forEach {
                                if (it.getString("recipie_id") == id){
                                      firestore.collection("Liked").document(it.id).delete()
                                }
                            }
                        }
                    }.await() */
            }

            /*var doc = firestore.collection("User_Recipies")
                .document(auth.uid.toString())
                .collection("recipies")
                .document(id)
            doc.get().addOnSuccessListener {
                if(it.exists()){
                    Log.d("FORT", "EXISTE")
                    val likedMap = hashMapOf<String, Any>(
                        "liked" to liked
                    )
                    viewModelScope.launch{
                        firestore.collection("User_Recipies")
                            .document(auth.uid.toString())
                            .collection("recipies")
                            .document(id)
                            .update(likedMap)
                            .await()
                    }
                //doc.update(likedMap)
                } else {
                    Log.d("FORT", "NO EXISTE")
                    val likedMap = hashMapOf<String, Any>(
                        "liked" to liked,
                        "owned" to false,
                        "shared" to false
                    )
                    firestore.collection("User_Recipies")
                        .document(auth.uid.toString())
                        .collection("recipies")
                        .document(id)
                        .set(likedMap)
                        .addOnCompleteListener{
                            if(it.isSuccessful){
                                Log.d("FORT", "g")
                            } else {
                                Log.d("FORT", it.exception.toString())
                            }
                        }
                }
            }.await()

            doc = firestore.collection("Recipies").document(id)
            if(liked){
                doc.update("likes", FieldValue.increment(1))
            } else {
                doc.update("likes", FieldValue.increment(-1))
            }*/

        }
    }

    fun updateShare(id:String, shared:Boolean){
        auth = Firebase.auth
        viewModelScope.launch {
            if(shared){
                firestore
                    .collection("Shared")
                    .document(UUID.randomUUID().toString())
                    .set(hashMapOf(
                        "recipie_id" to id,
                        "user_id" to auth.uid
                        ))
            } else {
                val doc = firestore.collection("Shared")
                    .whereEqualTo("user_id", auth.uid)
                    .get()
                    .await()

                doc.documents.forEach {
                    if (it.getString("recipie_id") == id){
                        firestore.collection("Shared").document(it.id).delete().await()
                    }
                }
            }
        }
    }
    /*
    fun updateShare(id:String, shared:Boolean){
        auth = Firebase.auth
        viewModelScope.launch {
            val doc = firestore.collection("User_Recipies")
                .document(auth.uid.toString())
                .collection("recipies")
                .document(id)

            doc.get().addOnSuccessListener {
                if (it.exists()) {
                    val likedMap = hashMapOf<String, Any>(
                        "shared" to shared
                    )
                    doc.update(likedMap)
                } else {
                    Log.d("FORT", "not exist")
                    val map = mapOf<String, Any>(
                        "liked" to false,
                        "shared" to shared,
                        "owned" to false,
                    )

                    firestore.collection("User_Recipies")
                        .document(auth.uid.toString())
                        .collection("recipies")
                        .document(id)
                        .set(map)
                }
            }
        }
    } */
}