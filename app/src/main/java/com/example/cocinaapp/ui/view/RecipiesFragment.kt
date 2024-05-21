package com.example.cocinaapp.ui.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cocinaapp.R
import com.example.cocinaapp.adapter.RecipiesAdapter
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.ui.viewmodel.RecipiesModelView
import com.example.cocinaapp.usecases.activities

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
/**
 * A simple [Fragment] subclass.
 * Use the [RecipiesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipiesFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var recipiesModelView: RecipiesModelView
    private lateinit var recipiesAdapter: RecipiesAdapter
    private val casosUsoAtivities by lazy { activities(this.requireActivity()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipies, container, false)

        val recycler = view.findViewById<RecyclerView>(R.id.recipies_recycler)

        recipiesModelView = ViewModelProvider(this).get(RecipiesModelView::class.java)

        recipiesAdapter = RecipiesAdapter()

        recipiesModelView.getRecipies().observe(viewLifecycleOwner, Observer {
            recipiesAdapter.setRecipieList(it)

            recycler.apply {
                layoutManager = LinearLayoutManager(this.context)
                adapter = recipiesAdapter
            }

        })
        val add = view.findViewById<CardView>(R.id.btnAdd)

        add.setOnClickListener {
            casosUsoAtivities.callAdd()
        }

        recipiesAdapter.setOnRecipieClickListener(object : RecipiesAdapter.OnRecipieClickListener{
            override fun onRecipieSelected(position: Int, recipies: List<RecipiesModel>) {
                val intent = Intent(activity, RecipieActivity::class.java)
                intent.putExtra("id", recipies[position].id)
                activity!!.startActivity(intent)
            }

        })

        return view

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RecipiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RecipiesFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}