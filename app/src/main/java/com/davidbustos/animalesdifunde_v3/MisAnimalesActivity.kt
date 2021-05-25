package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.davidbustos.animalesdifundekotlin.AnimalesAdapter
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
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
        intent = Intent(this, SubirAnimalActivity::class.java)
        startActivity(intent)
    }
}