package com.example.cocinaapp.ui.view

import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivityHomeBinding
import com.example.cocinaapp.ui.viewmodel.HomeViewModel
import com.example.cocinaapp.ui.viewmodel.UserViewModel

class HomeActivity : AppCompatActivity(), CategoriesFragment.OnFragmentInteractionListener {

    private lateinit var binding: ActivityHomeBinding

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(CategoriesFragment())

        val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions.entries.all { it.value }){

            } else {
                finishAffinity()
            }
        }

        init()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(arrayOf(
                android.Manifest.permission.READ_MEDIA_IMAGES,
                android.Manifest.permission.READ_MEDIA_VIDEO
            ))
        } else {
            requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE))
        }

    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    private fun init(){
        val sharedPreferences = getSharedPreferences("User", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.userName.observe(this, Observer { name ->
            homeViewModel.userPhoto.observe(this, Observer { photo ->
                if(name.equals("Error")){
                    Toast.makeText(this, photo, Toast.LENGTH_LONG).show()
                } else {
                    editor.putString("name", name)
                    editor.putString("photo", photo)
                    editor.apply()
                }
            })
        })

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(CategoriesFragment())
                R.id.recipies -> replaceFragment(RecipiesFragment())
                R.id.profile -> replaceFragment(ProfileFragment())

                else -> { }
            }

            true
        }

        homeViewModel.getUserData()
    }

    override fun onFragmentAInteraction(value: String) {
        val recipiesFragment = RecipiesFragment().apply {
            arguments = Bundle().apply {
                putString("category", value)
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, recipiesFragment)
            .addToBackStack(null)
            .commit()
    }
}