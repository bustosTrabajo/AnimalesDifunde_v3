package com.davidbustos.animalesdifunde_v3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_subir_animal.*

class SubirAnimalActivity : AppCompatActivity() , AdapterView.OnItemClickListener{

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    private var listView: ListView?=null

    private var arrayAdapter: ArrayAdapter<String>?=null

    private var tipoAnimal:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_animal)

        arrayAdapter= ArrayAdapter(applicationContext,
            android.R.layout.simple_list_item_single_choice,
            resources.getStringArray(R.array.tiposAnimales))

        listView=findViewById(R.id.my_single_list_view)
        listView?.adapter=arrayAdapter
        listView?.choiceMode =ListView.CHOICE_MODE_SINGLE
        listView?.onItemClickListener=this

        btnRegistroNuevoAnimal.setOnClickListener(){
            registrarAnimal(etNombreAnimalRegistro.text.toString(), tipoAnimal, etRazaAnimalRegistro.text.toString())
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

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tipoAnimal= parent?.getItemAtPosition(position) as String
        Toast.makeText(this,"Tipo de Animal: "+tipoAnimal,Toast.LENGTH_LONG).show()
    }
}