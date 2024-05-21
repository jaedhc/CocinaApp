package com.example.cocinaapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.cocinaapp.R

class ListAdapter: RecyclerView.Adapter<ListHolder>() {

    private var listOfElements = listOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListHolder(view)
    }

    override fun getItemCount(): Int = listOfElements.size

    override fun onBindViewHolder(holder: ListHolder, position: Int) {
        val elements = listOfElements[position]

        holder.element.text = elements.toString()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setElementsList(list: List<*>?){
        this.listOfElements = list as List<String>
        notifyDataSetChanged()
    }
}

class ListHolder(itemView: View): ViewHolder(itemView) {

    val element = itemView.findViewById<TextView>(R.id.txt_elemento)

}
