package com.example.cocinaapp.ui.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.cocinaapp.R
import com.example.cocinaapp.data.model.RecipiesModel
import com.example.cocinaapp.databinding.ActivityAddRecipieBinding
import com.example.cocinaapp.ui.viewmodel.AddReipieViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.UUID

class AddRecipieActivity : AppCompatActivity() {

    private lateinit var getContent: ActivityResultLauncher<String>
    private lateinit var binding: ActivityAddRecipieBinding

    private lateinit var containerIngLayout: LinearLayout
    private lateinit var containerStpLayout: LinearLayout

    private lateinit var addViewModel: AddReipieViewModel

    private val ingredienstList = mutableListOf<TextInputLayout>()
    private val stepstList = mutableListOf<TextInputLayout>()

    private lateinit var auth: FirebaseAuth

    var ingredientsCount: Int = 1
    var stepsCount: Int = 1
    var imgUri: Uri? = null

    var userNmae: String? = null
    var userPhoto: String? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addViewModel = ViewModelProvider(this).get(AddReipieViewModel::class.java)

        val sharedPreferences = this.getSharedPreferences("User", Context.MODE_PRIVATE)

        userNmae = sharedPreferences.getString("name", null)
        userPhoto = sharedPreferences.getString("photo", null)

        containerIngLayout = findViewById(R.id.ingredientsLayout)
        containerStpLayout = findViewById(R.id.stepsLayout)

        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
            it?.let {
                imgUri = it
                binding.imgRecipie.setImageURI(imgUri)
            }
        }

        binding.imgRecipie.setOnClickListener {
            getContent.launch("image/*")
        }

        binding.btnAddIngredients.setOnClickListener {
            addEditText()
        }

        binding.btnAddStpe.setOnClickListener {
            addEditTextSteps()
        }

        binding.btnDeleteIngredient.setOnClickListener {
            deleteIngredient()
        }

        binding.btnDeleteStep.setOnClickListener {
            deleteStep()
        }

        binding.btnUpload.setOnClickListener {
            if (isValid()) {
                val map = getRecipie()
                addViewModel.setRecipie(map)
                finish()
            } else {
                Toast.makeText(this, "Por favor llene todos los datos", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getRecipie(): HashMap<String, Any> {
        val name = binding.edtName.text.toString()
        val time = binding.edtTime.text.toString().toInt()

        val steps = getSteps().toList()
        val ingredients =getIngredients().toList()

        val recipieId = UUID.randomUUID().toString()
        var imgUrl = runBlocking {
            addViewModel.uploadImg(recipieId, imgUri!!)
        }

        auth = Firebase.auth

        val map = hashMapOf<String, Any>(
            "category" to "Comida Tradicional",
            "created_by" to hashMapOf<String, String>(
                "user_id" to auth.uid.toString(),
                "user_name" to userNmae!!,
                "user_photo" to userPhoto!!
            ),
            "ingredients" to ingredients,
            "likes" to 0,
            "steps" to steps,
            "name" to name!!,
            "recipieId" to recipieId,
            "time" to time,
            "photo" to imgUrl
        )

        return map

    }

    private fun isValid(): Boolean {
        if(userNmae == null || userPhoto == null){
            Toast.makeText(this, "Error en la sesión", Toast.LENGTH_LONG).show()
            return false
        }

        if(imgUri == null){
            return false
        }

        if (binding.edtName.text.toString().equals("")) {
            return false
        }

        try {
            binding.edtTime.text.toString().toInt()
        } catch (e: Exception) {
            return false
        }

        return !(getSteps().isEmpty() || getIngredients().isEmpty())
    }

    private fun deleteIngredient() {
        if (ingredienstList.isNotEmpty()) {
            val lastEditText = ingredienstList.removeLast()
            containerIngLayout.removeView(lastEditText)
            ingredientsCount -= 1
        }
    }

    private fun deleteStep() {
        if (stepstList.isNotEmpty()) {
            val lastEditText = stepstList.removeLast()
            containerStpLayout.removeView(lastEditText)
            stepsCount -= 1
        }
    }

    private fun addEditText() {
        val context: Context = ContextThemeWrapper(this, R.style.TextLayout)
        val heightInPixels =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
                .toInt()
        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightInPixels
            ).apply {
                setMargins(0, 20, 0, 0)
            }
        }

        val contextEdt: Context = ContextThemeWrapper(this, R.style.TextBos)

        val textInputEditText = TextInputEditText(contextEdt).apply {
            hint = "Add ingredient $ingredientsCount"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(10, 0, 0, 10)
            }
        }

        textInputLayout.addView(textInputEditText)

        containerIngLayout.addView(textInputLayout)

        ingredienstList.add(textInputLayout)

        ingredientsCount += 1
    }

    private fun addEditTextSteps() {
        val context: Context = ContextThemeWrapper(this, R.style.TextLayout)
        val heightInPixels =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60f, resources.displayMetrics)
                .toInt()
        val textInputLayout = TextInputLayout(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightInPixels
            ).apply {
                setMargins(0, 20, 0, 0)
            }
        }

        val contextEdt: Context = ContextThemeWrapper(this, R.style.TextBos)

        val textInputEditText = TextInputEditText(contextEdt).apply {
            hint = "Add step $stepsCount"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                setMargins(10, 0, 0, 10)
            }
        }

        textInputLayout.addView(textInputEditText)

        containerStpLayout.addView(textInputLayout)

        stepstList.add(textInputLayout)

        stepsCount += 1
    }

    private fun getIngredients(): List<String> {
        val values = mutableListOf<String>()

        for (textInputLayout in ingredienstList) {
            val editText = textInputLayout.editText
            val text = editText?.text.toString()
            if (text.equals("")) {
                return emptyList()
            }
            values.add(text)
        }

        return values
    }

    private fun getSteps(): List<String> {
        val values = mutableListOf<String>()

        for (textInputLayout in stepstList) {
            val editText = textInputLayout.editText
            val text = editText?.text.toString()
            if (text.equals("")) {
                return emptyList()
            }
            values.add(text)
        }

        return values
    }

}