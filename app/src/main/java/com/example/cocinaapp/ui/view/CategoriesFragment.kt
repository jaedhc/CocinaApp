package com.example.cocinaapp.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cocinaapp.R
import com.example.cocinaapp.ui.viewmodel.HomeViewModel
import com.example.cocinaapp.usecases.activities
import java.lang.ClassCastException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CategoriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CategoriesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var updated: Boolean = false

    private val casosUsoAtivities by lazy { this.activity?.let { activities(it) } }
    private lateinit var homeViewModel: HomeViewModel
    private var listener: OnFragmentInteractionListener? = null

    interface OnFragmentInteractionListener {
        fun onFragmentAInteraction(value:String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(context is OnFragmentInteractionListener){
            listener = context
        } else {
            throw ClassCastException("$context must implement OnFragmentInteracionListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onResume() {
        super.onResume()
        updated = true

        this.view?.let { updateUserInfo(it, updated) }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        initButtons(view)

        val img = view.findViewById<ImageView>(R.id.cat_usr_img)

        updated = false
        img.setOnClickListener {
            casosUsoAtivities!!.callUser()
            updated = true
        }

        updateUserInfo(view, updated)

        return view
    }

    fun initButtons(view: View){
        val btnMexican = view.findViewById<CardView>(R.id.btn_mexican)
        val btnPastas = view.findViewById<CardView>(R.id.btn_Pastas)
        val btnAppetizers = view.findViewById<CardView>(R.id.btn_Appetizers)
        val btnSide = view.findViewById<CardView>(R.id.btn_Side_Dishes)
        val btnDesserts = view.findViewById<CardView>(R.id.btn_Desserts)
        val btnBeverages = view.findViewById<CardView>(R.id.btn_Beverages)
        val btnFast = view.findViewById<CardView>(R.id.btn_fastfood)

        btnMexican.setOnClickListener {
            listener?.onFragmentAInteraction("Mexican")
        }

        btnPastas.setOnClickListener {
            listener?.onFragmentAInteraction("Pastas")
        }
        btnAppetizers.setOnClickListener {
            listener?.onFragmentAInteraction("Appetizers")
        }
        btnSide.setOnClickListener {
            listener?.onFragmentAInteraction("Side Dishes")
        }
        btnDesserts.setOnClickListener {
            listener?.onFragmentAInteraction("Desserts")
        }
        btnBeverages.setOnClickListener {
            listener?.onFragmentAInteraction("Beverages")
        }
        btnFast.setOnClickListener {
            listener?.onFragmentAInteraction("Fast Food")
        }
    }

    fun updateUserInfo(view: View, updated: Boolean){
        val preferences = this.activity?.getSharedPreferences("User", Context.MODE_PRIVATE)
        val img = view.findViewById<ImageView>(R.id.cat_usr_img)
        val nameTxt = view.findViewById<TextView>(R.id.cat_txt_wel)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val editor = preferences?.edit()

        homeViewModel.userName.observe(this.requireActivity(), Observer { name ->
            homeViewModel.userPhoto.observe(this.requireActivity(), Observer { photo ->
                if(name.equals("Error")){
                    Toast.makeText(this.requireActivity(), photo, Toast.LENGTH_LONG).show()
                } else {
                    editor!!.putString("name", name)
                    editor.putString("photo", photo)
                    editor.apply()

                    nameTxt?.text = "Hi $name"

                    Glide.with(this)
                        .load(photo)
                        .circleCrop()
                        .into(img)
                }
            })
        })

        if(updated || preferences!!.getString("name", "").equals("")){
            homeViewModel.getUserData()
        } else {
            val name = preferences.getString("name", "user")
            val photo = preferences.getString("photo", "")
            nameTxt?.text = "Welcome $name"

            Glide.with(this)
                .load(photo)
                .circleCrop()
                .into(img)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CategoriesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CategoriesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}