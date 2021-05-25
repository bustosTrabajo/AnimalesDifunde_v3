package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mis_animales.*

class MisAnimalesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_animales)

        btnSubirNuevoAnimal.setOnClickListener(){
            subirAnimal()
        }

    }
    private fun subirAnimal(){
        Toast.makeText(this,"Subir nuevo Animal",Toast.LENGTH_LONG).show()
        Log.i("Subir Animal", "Subir Animal")
        intent = Intent(this, SubirAnimalActivity::class.java)
        startActivity(intent)
    }
}