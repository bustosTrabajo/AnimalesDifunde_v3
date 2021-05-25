package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.davidbustos.animalesdifundekotlin.SubirNuevoAnimalActivity
import kotlinx.android.synthetic.main.activity_mis_animales.*

class MisAnimalesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_animales)


        btnSubirNuevoAnimal.setOnClickListener{
            var intent= Intent(this, SubirNuevoAnimalActivity::class.java)
            startActivity(intent)
        }
    }
}