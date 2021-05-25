package com.davidbustos.animalesdifunde_v3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.davidbustos.animalesdifundekotlin.AnimalesAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_animales_difunde.*

class AnimalesDifundeActivity : AppCompatActivity() {

    //Navigation Drawer
    lateinit var toogle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    //Todos los Animales

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("animales")

    var animalesAdapter: AnimalesAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales_difunde)
        cargarDatos()
        drawerLayout=findViewById(R.id.drawerLayout)
        navigationView=findViewById(R.id.nav_view)
        toogle= ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                //Inflar diferentes fragments o activities relacionados con el item de menú
                R.id.item1 -> miPerfil()
                R.id.item2 -> misAnimales()
                R.id.item3 -> misMensajes()

            }
            true
        }

        setUpRecyclerView()

    }
    fun setUpRecyclerView(){
        val query: Query =collectionReference;
        val firestoreRecyclerOptions: FirestoreRecyclerOptions<Animal> = FirestoreRecyclerOptions.Builder<Animal>()
            .setQuery(query,Animal::class.java)
            .build();

        animalesAdapter = AnimalesAdapter(firestoreRecyclerOptions);

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = animalesAdapter

    }

    override fun onStart(){
        super.onStart()
        animalesAdapter!!.startListening()


    }
    override fun onDestroy(){
        super.onDestroy()
        animalesAdapter!!.stopListening()


    }

    private fun miPerfil() {
        var intent= Intent(this, MiPerfilActivity::class.java)
        startActivity(intent)
    }
    private fun misAnimales(){
        var intent= Intent(this, MisAnimalesActivity::class.java)
        startActivity(intent)

    }
    private fun misMensajes(){
        var intent= Intent(this, MisMensajesActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        if(toogle.onOptionsItemSelected(item)){
            true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun cargarDatos(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        var usuario=sharedPreferences.getString("usuario","no usuario")
        Toast.makeText(applicationContext,"Nombre de Usuario: {$usuario}", Toast.LENGTH_LONG).show()

    }
    private fun recogerImagen(){


    }
}