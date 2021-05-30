package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidbustos.animalesdifundekotlin.AnimalesAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_animales_difunde.*
import kotlinx.android.synthetic.main.activity_mis_animales.*

class MisAnimalesActivity : AppCompatActivity() {

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email
    //Mis Animales
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("animales")
    var misAnimalesAdapter: AnimalesAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_animales)

        btnSubirNuevoAnimal.setOnClickListener(){
            subirAnimal()
        }
        setUpRecyclerViewMisAnimales()

    }
    private fun subirAnimal(){
        intent = Intent(this, SubirAnimalActivity::class.java)
        startActivity(intent)

    }
    private fun setUpRecyclerViewMisAnimales(){
        val query=collectionReference.whereEqualTo("usuario", usuario)
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Animal> = FirestoreRecyclerOptions.Builder<Animal>()
            .setQuery(query,Animal::class.java)
            .build();

        misAnimalesAdapter = AnimalesAdapter(firestoreRecyclerOptions);

        recyclerMisAnimales.layoutManager = LinearLayoutManager(this)
        recyclerMisAnimales.adapter = misAnimalesAdapter

    }

    override fun onStart(){
        super.onStart()
        misAnimalesAdapter!!.startListening()


    }

    override fun onDestroy(){
        super.onDestroy()
        misAnimalesAdapter!!.stopListening()

    }
    override fun onBackPressed(){

    }
}