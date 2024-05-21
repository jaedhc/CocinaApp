package com.example.cocinaapp.usecases

import android.app.Activity
import android.content.Intent
import com.example.cocinaapp.ui.view.AddRecipieActivity
import com.example.cocinaapp.ui.view.RecipieActivity
import com.example.cocinaapp.ui.view.HomeActivity
import com.example.cocinaapp.ui.view.LoginActivity
import com.example.cocinaapp.ui.view.MainActivity
import com.example.cocinaapp.ui.view.SignUpActivity
import com.example.cocinaapp.ui.view.UserActivity

class activities(val activity: Activity) {

    fun callLogIn(){
        val i = Intent(activity, LoginActivity::class.java)
        activity.startActivity(i)
    }

    fun callHome(){
        val i = Intent(activity, HomeActivity::class.java)
        activity.startActivity(i)
    }

    fun callSignUp(){
        val i = Intent(activity, SignUpActivity::class.java)
        activity.startActivity(i)
    }

    fun callMain(){
        val i = Intent(activity, MainActivity::class.java)
        activity.startActivity(i)
    }

    fun callUser(){
        val i = Intent(activity, UserActivity::class.java)
        activity.startActivity(i)
    }

    fun callRecipie(){
        val i = Intent(activity, RecipieActivity::class.java)
        activity.startActivity(i)
    }

    fun callAdd(){
        val i = Intent(activity, AddRecipieActivity::class.java)
        activity.startActivity(i)
    }
}