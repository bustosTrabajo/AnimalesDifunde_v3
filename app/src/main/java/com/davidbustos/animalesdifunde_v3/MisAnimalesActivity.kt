package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_animales_difunde.*
import kotlinx.android.synthetic.main.activity_mis_animales.*
import kotlinx.android.synthetic.main.activity_mis_animales.btnVolver

class MisAnimalesActivity : AppCompatActivity() {

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Base de Datos de Firestore
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_animales)

        btnSubirNuevoAnimal.setOnClickListener(){
            subirAnimal()
        }
        btnVolver.setOnClickListener(){
            var intent= Intent(this,AnimalesDifundeActivity::class.java)
            startActivity(intent)
        }
        misAnimales()

    }
    private fun subirAnimal(){
        intent = Intent(this, SubirAnimalActivity::class.java)
        startActivity(intent)

    }

    fun misAnimales(){
        recyclerMisAnimales?.layoutManager=LinearLayoutManager(this)

        recyclerMisAnimales?.adapter=
            AnimalesAdapter{ animal->
                animalSelected(animal)
            }
        //Recogemos lista de animales de la BD de Mis Animales
        db.collection("animales")
            .whereEqualTo("usuario", usuario)
            .get()
            .addOnSuccessListener{ animales ->
                val listaAnimales=animales.toObjects(Animal::class.java)

                (recyclerMisAnimales?.adapter as AnimalesAdapter).setData(listaAnimales)

            }

        //Actualización del Recycler de Mis Animales
        db.collection("animales")
            .whereEqualTo("usuario", usuario)
            .addSnapshotListener{ animales, error ->
                if(error == null){
                    animales?.let{
                        val listaAnimales=it.toObjects(Animal::class.java)

                        (recyclerMisAnimales?.adapter as AnimalesAdapter).setData(listaAnimales)
                    }
                }
            }
    }

    private fun animalSelected(animal: Animal) {
        val intent=Intent(this,AnimalActivity::class.java)
        intent.putExtra("nombreAnimal",animal.nombre)
        startActivity(intent)

    }

    override fun onBackPressed(){

    }
}