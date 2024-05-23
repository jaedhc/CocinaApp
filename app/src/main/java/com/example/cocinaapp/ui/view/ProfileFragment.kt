package com.example.cocinaapp.ui.view

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.adapter.RecipiesAdapter
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.ui.viewmodel.HomeViewModel
import com.example.cocinaapp.ui.viewmodel.ProfileViewModel
import com.example.cocinaapp.ui.viewmodel.RecipiesModelView
import com.example.cocinaapp.ui.viewmodel.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import java.util.jar.Attributes.Name

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var profilesModelView: ProfileViewModel
    private lateinit var recipiesAdapter: RecipiesAdapter
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()

        this.view?.let { init(it) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        init(view)

        return view
    }

    private fun init(view: View){
        val preferences = this.activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val name = preferences?.getString("name", "user")
        val photo = preferences?.getString("photo", "user")

        auth = Firebase.auth

        val img = view.findViewById<ImageView>(R.id.usr_img)
        val nameTxt = view.findViewById<TextView>(R.id.txt_name)
        Glide.with(this)
            .load(photo)
            .circleCrop()
            .into(img)

        nameTxt.text = name

        val recycler = view.findViewById<RecyclerView>(R.id.usr_recipies_rcy)

        profilesModelView = ViewModelProvider(this).get(ProfileViewModel::class.java)

        recipiesAdapter = RecipiesAdapter()

        profilesModelView.getUserRecipies(auth.uid.toString()).observe(viewLifecycleOwner, Observer {
            recipiesAdapter.setRecipieList(it)
            recycler.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = recipiesAdapter
            }

        })

        recipiesAdapter.setOnRecipieClickListener(object : RecipiesAdapter.OnRecipieClickListener{
            override fun onRecipieSelected(position: Int, recipies: List<RecipiesModel>) {
                val intent = Intent(activity, RecipieActivity::class.java)
                intent.putExtra("id", recipies[position].id)
                activity!!.startActivity(intent)
            }

        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}