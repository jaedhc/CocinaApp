package com.example.cocinaapp.ui.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Visibility
import com.example.cocinaapp.R
import com.example.cocinaapp.adapter.RecipiesAdapter
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.ui.viewmodel.RecipiesModelView
import com.example.cocinaapp.usecases.activities
import com.facebook.shimmer.ShimmerFrameLayout

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
        val cat = arguments?.getString("category", null)
        val txtFilter = view.findViewById<TextView>(R.id.txt_filter)

        val shimmerFrameLayout = view.findViewById<ShimmerFrameLayout>(R.id.shimmer_view_container)

        val searchView = view.findViewById<SearchView>(R.id.search_view)
        val searchEditText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        val closeIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)

        // Añadir una receta
        val add = view.findViewById<CardView>(R.id.btnAdd)

        recycler.visibility = View.GONE
        add.visibility = View.GONE
        shimmerFrameLayout.startShimmer()


        // Cambiar el color del texto y del hint
        searchEditText.setHintTextColor(Color.BLACK)
        searchEditText.setTextColor(Color.BLACK)
        closeIcon.setColorFilter(Color.BLACK)

        // Cambiar el color del icono de búsqueda
        searchView.post {
            val searchIcon = searchView.findViewById<ImageView>(androidx.appcompat.R.id.search_mag_icon)
            searchIcon.setColorFilter(Color.BLACK) // Cambiar el color del icono a negro
        }

        // Observando el modelo de datos
        recipiesModelView = ViewModelProvider(this).get(RecipiesModelView::class.java)
        recipiesAdapter = RecipiesAdapter()

        // Filtrar recetas por categoría o por búsqueda
        if (cat == null) {
            txtFilter.text = "Filtered by: All"
            recipiesModelView.getRecipies().observe(viewLifecycleOwner, Observer { recipes ->
                // Filtrar las recetas según el texto ingresado en el SearchView
                val filteredRecipes = filterRecipesBySearchQuery(recipes, searchView.query.toString())

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                add.visibility = View.VISIBLE

                recipiesAdapter.setRecipieList(filteredRecipes)
                recycler.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = recipiesAdapter
                }
            })
        } else {
            txtFilter.text = "Filtered by: $cat"
            recipiesModelView.getRecipiesByCat(cat).observe(viewLifecycleOwner, Observer { recipes ->
                // Filtrar las recetas según el texto ingresado en el SearchView
                val filteredRecipes = filterRecipesBySearchQuery(recipes, searchView.query.toString())

                shimmerFrameLayout.stopShimmer()
                shimmerFrameLayout.visibility = View.GONE
                recycler.visibility = View.VISIBLE
                add.visibility = View.VISIBLE

                recipiesAdapter.setRecipieList(filteredRecipes)
                recycler.apply {
                    layoutManager = LinearLayoutManager(this.context)
                    adapter = recipiesAdapter
                }
            })
        }

        // Implementar búsqueda en tiempo real
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // No es necesario hacer nada aquí ya que estamos filtrando en tiempo real
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Filtrar las recetas cuando el texto cambia
                recipiesModelView.getRecipies().observe(viewLifecycleOwner, Observer { recipes ->
                    val filteredRecipes = filterRecipesBySearchQuery(recipes, newText ?: "")
                    recipiesAdapter.setRecipieList(filteredRecipes)
                })
                return true
            }
        })


        add.setOnClickListener {
            casosUsoAtivities.callAdd()
        }

        // Configurar el listener de clics en recetas
        recipiesAdapter.setOnRecipieClickListener(object : RecipiesAdapter.OnRecipieClickListener {
            override fun onRecipieSelected(position: Int, recipies: List<RecipiesModel>) {
                val intent = Intent(activity, RecipieActivity::class.java)
                intent.putExtra("id", recipies[position].id)
                activity!!.startActivity(intent)
            }
        })

        return view
    }

    // Función para filtrar recetas según el texto de búsqueda
    private fun filterRecipesBySearchQuery(recipes: List<RecipiesModel>, query: String): List<RecipiesModel> {
        return recipes.filter { it.name.contains(query, ignoreCase = true) }
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