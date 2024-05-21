package com.example.cocinaapp.data.model

data class RecipiesModel(
    val id: String,
    val name: String,
    val photo: String,
    val likes: Int,
    val time: Int,
    val ingredients: List<*>? = null,
    val steps: List<*>? = null,
    val user: Map<*, *>? = null,
    val liked: Boolean = false,
    val shared: Boolean = false,
)