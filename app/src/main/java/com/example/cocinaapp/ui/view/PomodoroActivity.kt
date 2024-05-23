package com.example.cocinaapp.ui.view

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.cocinaapp.R
import com.example.cocinaapp.databinding.ActivityPomodoroBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PomodoroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPomodoroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPomodoroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val steps = intent.getStringArrayListExtra("steps")
        var flag = false
        var c = 1

        lifecycleScope.launch {
            steps!!.forEach {
                flag = false
                binding.txtStep.text = it.toString()
                binding.txtStepNum.text = c.toString()
                val totalMins = 25
                var i = 0
                while(i < totalMins * 60 && !flag){
                    delay(1000L)
                    val mins = totalMins - (i / 60) -1
                    var seconds = 59 - (i % 60)
                    binding.counter.text = "$mins:$seconds"
                    i++
                }
                flag = false
                rest(true)
                val restMins = 5
                var j = 0
                while (j < restMins * 60 && !flag){
                    delay(1000L)
                    val mins = restMins - (j / 60) -1
                    val seconds = 59 - (j % 60)
                    binding.counter.text = "$mins:$seconds"
                    j++
                }
                rest(false)
                c+=1
            }

            binding.txtStep.text = "Completado"
            binding.counter.text = ""
            binding.btnCancel.text = "SALIR"
            binding.btnSkip.visibility = View.GONE
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnSkip.setOnClickListener {
            flag = true
        }

    }

    private fun rest(bool:Boolean){

        val primary = ContextCompat.getColor(this, R.color.Priamry)
        if(bool){

            binding.txtStep.text = "Rest"
            binding.counter.setTextColor(primary)
            binding.parent.setBackgroundColor(Color.WHITE)
            binding.txtStep.setTextColor(primary)
        } else {
            binding.counter.setTextColor(Color.WHITE)
            binding.parent.setBackgroundColor(primary)
            binding.txtStep.setTextColor(Color.WHITE)
        }
    }
}
