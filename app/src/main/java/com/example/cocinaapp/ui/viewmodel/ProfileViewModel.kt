package com.example.cocinaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.data.model.RecipiesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel : ViewModel(){

    val recipiesRepo = RecipiesRepo()

    fun getUserRecipies(): LiveData<List<RecipiesModel>>{
        return recipiesRepo.getSharedOrOwned()
    }

}