package com.example.cocinaapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.data.model.RecipiesRepo

class RecipiesModelView: ViewModel() {

    val recipiesRepo = RecipiesRepo()

    fun getRecipies(): LiveData<List<RecipiesModel>>{
        return recipiesRepo.getRecipies()
    }

    fun getRecipiesByCat(cat: String): LiveData<List<RecipiesModel>>{
        return recipiesRepo.getRecipiesByCat(cat)
    }
}