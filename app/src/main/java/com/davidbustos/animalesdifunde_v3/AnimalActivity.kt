package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_animal.*
import kotlinx.android.synthetic.main.activity_animal.btnVolver
import kotlinx.android.synthetic.main.activity_perfil_usuario.*

class AnimalActivity : AppCompatActivity() {
    private var nombreAnimal:String=""
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)


        //Recogemos datos de Animal
        intent.getStringExtra("nombreAnimal")?.let{nombreAnimal=it}

        tv_nombreAnimalPerfil.setText(nombreAnimal)

        btnVolver.setOnClickListener(){
            var intent= Intent(this,AnimalesDifundeActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed(){

    }
}