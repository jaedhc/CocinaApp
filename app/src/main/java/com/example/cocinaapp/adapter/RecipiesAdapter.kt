package com.example.cocinaapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.data.model.RecipiesModel

class RecipiesAdapter: RecyclerView.Adapter<RecipiesHolder>() {

    private var listOfRecipies = listOf<RecipiesModel>()
    private var listener: OnRecipieClickListener? = null
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipiesHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recipie_item, parent, false)
        return RecipiesHolder(view)
    }

    override fun getItemCount(): Int = listOfRecipies.size

    override fun onBindViewHolder(holder: RecipiesHolder, position: Int) {
        val recipies = listOfRecipies[position]

        holder.itemView.setOnClickListener {
            listener?.onRecipieSelected(position, listOfRecipies)
        }

        val shared = recipies.shared

        val name = recipies.name
        val photo = recipies.photo
        val likes = recipies.likes
        val time = recipies.time

        context = holder.itemView.context

        holder.recipie.text = name
        holder.likes.text = likes.toString()
        holder.mins.text = time.toString()

        Glide.with(holder.itemView.context)
            .load(photo)
            .into(holder.img)

        if(shared){
            holder.layout.visibility = View.VISIBLE
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRecipieList(list: List<RecipiesModel>){
        this.listOfRecipies = list
        notifyDataSetChanged()
    }

    fun setOnRecipieClickListener(listener: OnRecipieClickListener){
        this.listener = listener
    }

    interface OnRecipieClickListener{
        fun onRecipieSelected(position: Int, recipies: List<RecipiesModel>)

    }
}

class RecipiesHolder(itemView: View): ViewHolder(itemView){
    val img = itemView.findViewById<ImageView>(R.id.img_food)
    val recipie = itemView.findViewById<TextView>(R.id.txt_recipie_name)
    val likes = itemView.findViewById<TextView>(R.id.txt_likes)
    val mins = itemView.findViewById<TextView>(R.id.txt_mins)
    val layout = itemView.findViewById<LinearLayout>(R.id.shared_layout)
}