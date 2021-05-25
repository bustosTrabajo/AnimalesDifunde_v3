package com.davidbustos.animalesdifunde_v3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_subir_animal.*

class SubirAnimalActivity : AppCompatActivity() {

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_animal)

        btnRegistroNuevoAnimal.setOnClickListener(){
            registrarAnimal(etNombreAnimalRegistro.text.toString(), etTipoAnimalRegistro.text.toString(), etRazaAnimalRegistro.text.toString())
        }
    }

    private fun registrarAnimal(nombreAnimal:String,tipoAnimal:String,razaAnimal:String){
        val animal=hashMapOf("nombre" to nombreAnimal, "raza" to razaAnimal, "tipo" to tipoAnimal, "usuario" to usuario)

        fStoreDB.collection("animales")
            .document(nombreAnimal)
            .set(animal as Map<String, Any>)
            .addOnSuccessListener{ documentReference ->
                Log.e("TAG", "Success")
            }
            .addOnFailureListener { e ->
                Log.e("TAG","Error adding document",e)
            }
    }
}