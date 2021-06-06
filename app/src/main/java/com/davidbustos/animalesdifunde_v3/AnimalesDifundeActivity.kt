package com.davidbustos.animalesdifunde_v3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
//import com.davidbustos.animalesdifundekotlin.AnimalesAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_animales_difunde.*

class AnimalesDifundeActivity : AppCompatActivity(){

    //Navigation Drawer
    lateinit var toogle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    //Base de Datos de Firestore
    private var db = Firebase.firestore

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email



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
                R.id.item4 -> misAmigos()
                R.id.item5 -> cerrarSesion()

            }
            true
        }
        logoPerro.setOnClickListener(){
            Toast.makeText(this,"Cargar Recycler con animales de tipo Perro",Toast.LENGTH_LONG).show()
            buscarPorCategoria("Perro")
        }
        logoGato.setOnClickListener(){
            buscarPorCategoria("Gato")
        }
        logoReptil.setOnClickListener(){
            buscarPorCategoria("Reptil")
        }
        logoExotico.setOnClickListener(){
            buscarPorCategoria("Exótico")
        }
        logoPajaro.setOnClickListener(){
            buscarPorCategoria("Pájaro")
        }

        btnBusquedaPorNombre.setOnClickListener(){
            buscarPorNombre(et_busquedaPorNombre.text.toString())
        }

        todosLosAnimales()


    }
    private fun buscarPorNombre(nombreAnimal:String){

        db.collection("animales")
            .whereEqualTo("nombre", nombreAnimal)
            .addSnapshotListener{ animales, error ->
                if(error == null){
                    animales?.let{
                        val listaAnimales=it.toObjects(Animal::class.java)
                        (recycler?.adapter as AnimalesAdapter).setData(listaAnimales)
                    }
                }
            }
    }

    private fun buscarPorCategoria(categoria:String){
        db.collection("animales")
            .whereEqualTo("tipo", categoria)
            .addSnapshotListener{ animales, error ->
            if(error == null){
                animales?.let{
                    val listaAnimales=it.toObjects(Animal::class.java)
                    (recycler?.adapter as AnimalesAdapter).setData(listaAnimales)
                }
            }
        }
    }

    fun todosLosAnimales(){
        recycler?.layoutManager=LinearLayoutManager(this)

        recycler?.adapter=
            AnimalesAdapter{ animal->
                animalSelected(animal)
            }
        //Recogemos lista de animales de la BD
        db.collection("animales")
            .get()
            .addOnSuccessListener{ animales ->
                val listaAnimales=animales.toObjects(Animal::class.java)

                (recycler?.adapter as AnimalesAdapter).setData(listaAnimales)

            }

        //Actualización del Recycler
        db.collection("animales")
            .addSnapshotListener{ animales, error ->
                if(error == null){
                    animales?.let{
                        val listaAnimales=it.toObjects(Animal::class.java)

                        (recycler?.adapter as AnimalesAdapter).setData(listaAnimales)
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

    private fun miPerfil() {
        var intent= Intent(this, PerfilUsuarioActivity::class.java)
        intent.putExtra("usuario",usuario)
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
    private fun misAmigos(){
        var intent= Intent(this, MisAmigosActivity::class.java)
        startActivity(intent)
    }
    private fun cerrarSesion(){
        FirebaseAuth.getInstance().signOut()
        finish()
        var intent= Intent(this, InicioActivity::class.java)
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

}

