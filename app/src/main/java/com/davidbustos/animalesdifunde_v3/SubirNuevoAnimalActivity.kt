package com.davidbustos.animalesdifundekotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.davidbustos.animalesdifunde_v3.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_subir_nuevo_animal.*
import kotlin.properties.Delegates

class SubirNuevoAnimalActivity : AppCompatActivity() {

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()

    //Usuario
    val usuario_animal: String? =FirebaseAuth.getInstance().currentUser?.email

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_nuevo_animal)

        var btnRegistroNuevoAnimal: Button =findViewById<Button>(R.id.btnRegistroNuevoAnimal)

        btnRegistroNuevoAnimal.setOnClickListener(){
            registrarAnimal(etNombreAnimalRegistro.text.toString(), etTipoAnimalRegistro.text.toString(), etRazaAnimalRegistro.text.toString())
        }
    }
    private fun registrarAnimal(nombreAnimal:String,tipoAnimal:String,razaAnimal:String){
        val animal=hashMapOf("nombre" to nombreAnimal, "raza" to razaAnimal, "tipo" to tipoAnimal)

        fStoreDB.collection("animales")
            .document("Animal 7acbb")
            .set(animal as Map<String, Any>)
            .addOnSuccessListener{ documentReference ->
                Log.e("TAG", "Success")
            }
            .addOnFailureListener { e ->
                Log.e("TAG","Error adding document",e)
            }

    }
}
