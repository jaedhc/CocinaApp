package com.example.cocinaapp.ui.view

import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.adapter.ListAdapter
import com.example.cocinaapp.databinding.ActivityRecipieBinding
import com.example.cocinaapp.ui.viewmodel.RecipieViewModel

class RecipieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipieBinding

    private lateinit var recipieViewModel: RecipieViewModel
    private lateinit var listAdapter: ListAdapter
    private lateinit var listAdapter2: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recipieViewModel = ViewModelProvider(this).get(RecipieViewModel::class.java)

        var slLike = false
        var slShare = false


        listAdapter = ListAdapter()
        listAdapter2 = ListAdapter()

        val id = intent.getStringExtra("id")
        var init = 0
        if (id != null) {
            recipieViewModel.getRecipie(id).observe(this, Observer {
                binding.txtUser.text = it.user!!.get("user_name").toString()
                binding.txtDuration.text ="${it.time.toString()} mins"
                binding.txtTitle.text = it.name

                Glide.with(this)
                    .load(it.user.get("user_photo").toString())
                    .circleCrop()
                    .into(binding.imgUser)

                Glide.with(this)
                    .load(it.photo)
                    .into(binding.imgFood)

                listAdapter.setElementsList(it.ingredients)

                binding.rcIng.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = listAdapter
                }

                listAdapter2.setElementsList(it.steps)

                binding.rcSteps.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = listAdapter2
                }

                slLike = it.liked
                slShare = it.shared
                if(it.liked){
                    binding.btnLike.setCardBackgroundColor(resources.getColor(R.color.Priamry))
                    binding.imgLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                } else {
                    binding.btnLike.setCardBackgroundColor(resources.getColor(R.color.white))
                    binding.imgLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Priamry))
                }

                if(it.shared){
                    binding.btnShare.setCardBackgroundColor(resources.getColor(R.color.Priamry))
                    binding.imgShare.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                } else {
                    binding.btnShare.setCardBackgroundColor(resources.getColor(R.color.white))
                    binding.imgShare.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Priamry))
                }

            })

            binding.btnLike.setOnClickListener {
                if (slLike){
                    slLike = false
                    binding.btnLike.setCardBackgroundColor(resources.getColor(R.color.white))
                    binding.imgLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Priamry))
                } else {
                    slLike = true
                    binding.btnLike.setCardBackgroundColor(resources.getColor(R.color.Priamry))
                    binding.imgLike.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                }

                recipieViewModel.updateLike(id, slLike)
            }

            binding.btnShare.setOnClickListener {
                if (slShare){
                    slShare = false
                    binding.btnShare.setCardBackgroundColor(resources.getColor(R.color.white))
                    binding.imgShare.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.Priamry))
                } else {
                    slShare = true
                    binding.btnShare.setCardBackgroundColor(resources.getColor(R.color.Priamry))
                    binding.imgShare.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white))
                }

                recipieViewModel.updateShare(id, slShare)
            }

        }

    }
}