package com.example.cocinaapp.data.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class RecipiesRepo {

    private val firestore = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    fun getRecipies(): LiveData<List<RecipiesModel>> {
        val recipies = MutableLiveData<List<RecipiesModel>>()

        firestore.collection("Recipies").addSnapshotListener { snapshot, exception ->

            val recipiesList = mutableListOf<RecipiesModel>()

            snapshot?.documents?.forEach {
                val name = it.getString("name")
                val photo = it.getString("photo")
                val likes = it.get("likes").toString().toInt()
                val time = it.get("time").toString().toInt()

                val recipie = RecipiesModel(it.id, name!!, photo!!, likes, time)

                recipiesList.add(recipie)
            }

            recipies.value = recipiesList
        }
        return recipies
    }

    fun getSharedOrOwned(): LiveData<List<RecipiesModel>> {
        auth = Firebase.auth
        val recipies = MutableLiveData<List<RecipiesModel>>()

        firestore.collection("Recipies").get().addOnCompleteListener {
            if(it.isSuccessful) {
                it.result.documents.forEach { doc ->

                    CoroutineScope(Dispatchers.IO).launch {

                        val sharedList = async { getShared() }.await()
                        val recipieList = it.result.documents.filter {
                            sharedList.contains(it.get("recipieId").toString())
                        }.map { doc2 ->
                            val name = doc2.getString("name")
                            val photo = doc2.getString("photo")
                            val likes = doc2.get("likes").toString().toInt()
                            val time = doc2.get("time").toString().toInt()

                            RecipiesModel(doc2.id, name!!, photo!!, likes, time)
                        }

                        withContext(Dispatchers.Main) {
                            recipies.value = recipieList
                        }
                    }
                }
            }
        }
        return recipies
    }

    suspend fun getShared(): List<String>{
        auth = Firebase.auth?: return emptyList()

        return withContext(Dispatchers.IO){
            try {
                val doc = firestore.collection("Shared")
                    .whereEqualTo("user_id", auth.uid)
                    .get()
                    .await()
                doc.map { it.get("recipie_id").toString() }
            }catch (e: Exception){
                emptyList()
            }
        }
    }

    /*
    suspend fun getShared(): List<String> {
        auth = Firebase.auth
        return withContext(Dispatchers.IO) {
            val list:MutableList<String> = mutableListOf()
            try {
                firestore.collection("User_Recipies")
                    .document(auth.uid.toString())
                    .collection("recipies")
                    .get()
                    .await().forEach{
                        if(it.getBoolean("shared") == true){
                            list.add(it.id)
                        }
                    }
                list
            } catch (e: Exception) {
                emptyList()
            }
        }
    }*/

    fun getRecipie(id: String): LiveData<RecipiesModel> {
        val recipieLiveData = MutableLiveData<RecipiesModel>()

        firestore.collection("Recipies").whereEqualTo("recipieId", id)
            .get().addOnSuccessListener {
                it.documents.forEach {
                    val name = it.getString("name")
                    val photo = it.getString("photo")
                    val likes = it.get("likes").toString().toInt()
                    val time = it.get("time").toString().toInt()

                    val ing = it.get("ingredients") as? List<*>
                    val steps = it.get("steps") as? List<*>

                    val usr = it.get("created_by") as Map<*, *>

                    CoroutineScope(Dispatchers.IO).launch {
                        val liked = async { isLiked(id) }.await()
                        val shared = async { isShared(id) }.await()
                        val recipie = RecipiesModel(
                            it.id,
                            name!!,
                            photo!!,
                            likes,
                            time,
                            ing,
                            steps,
                            usr,
                            liked,
                            shared
                        )
                        withContext(Dispatchers.Main){
                            recipieLiveData.value = recipie
                        }
                    }
                }
            }
        return recipieLiveData
    }

    suspend fun isLiked(id: String): Boolean{
        auth = Firebase.auth
        var found = false
        return withContext(Dispatchers.IO){
            try {
                val doc = firestore.collection("Liked")
                    .whereEqualTo("user_id", auth.uid)
                    .get()
                    .await()
                doc.documents.forEach{
                    if(it.get("recipie_id") == id){
                        found = true
                    }
                }
                found
            } catch (e:Exception){
                false
            }
        }
    }

    /*suspend fun isLiked(id: String): Boolean {
        auth = Firebase.auth
        var found = false
        return withContext(Dispatchers.IO) {
            try {
                val doc = firestore.collection("User_Recipies")
                    .document(auth.uid.toString())
                    .collection("recipies")
                    .document(id).get()
                    .await()
                doc.getBoolean("liked") ?: false
            } catch (e: Exception) {
                false
            }
        }
    }*/

    /*suspend fun isShared(id: String): Boolean {
        auth = Firebase.auth
        return withContext(Dispatchers.IO) {
            try {
                val doc = firestore.collection("User_Recipies")
                    .document(auth.uid.toString())
                    .collection("recipies")
                    .document(id).get()
                    .await()
                doc.getBoolean("shared") ?: false
            } catch (e: Exception) {
                false
            }
        }
    }*/

    suspend fun isShared(id:String): Boolean{
        auth = Firebase.auth?: return false
        var found = false
        return withContext(Dispatchers.IO){
            try {
                val doc = firestore.collection("Shared")
                    .whereEqualTo("user_id", auth.uid)
                    .get()
                    .await()
                doc.documents.forEach{
                    if(it.get("recipie_id") == id){
                        found = true
                    }
                }
                found
            } catch (e: Exception){
                false
            }
        }

    }

}
