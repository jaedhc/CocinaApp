package com.example.cocinaapp.ui.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.adapter.RecipiesAdapter
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.databinding.ActivityViewUserBinding
import com.example.cocinaapp.ui.viewmodel.ProfileViewModel

class ViewProfileActivity : AppCompatActivity() {

    private lateinit var profilesModelView: ProfileViewModel
    private lateinit var recipiesAdapter: RecipiesAdapter
    private lateinit var binding: ActivityViewUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recycler = binding.usrRecipiesRcy

        val id = intent.getStringExtra("userId")

        profilesModelView = ViewModelProvider(this).get(ProfileViewModel::class.java)

        recipiesAdapter = RecipiesAdapter()

        profilesModelView.getUserRecipies(id!!).observe(this, Observer {
            recipiesAdapter.setRecipieList(it)
            recycler.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = recipiesAdapter
            }
        })

        profilesModelView.userName.observe(this, Observer{
            binding.txtName.text = it
        })

        profilesModelView.userPhoto.observe(this, Observer {
            Glide.with(this)
                .load(it)
                .circleCrop()
                .into(binding.usrImg)
        })

        profilesModelView.getUserData(id)
        val activity = this
        recipiesAdapter.setOnRecipieClickListener(object : RecipiesAdapter.OnRecipieClickListener{
            override fun onRecipieSelected(position: Int, recipies: List<RecipiesModel>) {
                val intent = Intent(activity, RecipieActivity::class.java)
                intent.putExtra("id", recipies[position].id)
                startActivity(intent)
            }

        })
    }
}