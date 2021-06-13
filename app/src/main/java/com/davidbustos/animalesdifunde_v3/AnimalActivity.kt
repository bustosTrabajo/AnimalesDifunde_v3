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
import com.davidbustos.animalesdifunde_v3.adapters.ViewPagerAdapter
import com.davidbustos.animalesdifunde_v3.fragments.DatosFragment

import com.davidbustos.animalesdifunde_v3.fragments.FotosAnimalFragment
import com.davidbustos.animalesdifunde_v3.fragments.UbicacionFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_animal.*


//      btnVolver
//import kotlinx.android.synthetic.main.activity_animal.btnVolver


//Maps
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_animales_difunde.*
import kotlinx.android.synthetic.main.activity_perfil_usuario.*
import kotlinx.android.synthetic.main.fragment_datos.*

class AnimalActivity : AppCompatActivity(){
    //implementar OnMapReadyCallBack

    //Animal
    private lateinit var nombreAnimal:String
    private lateinit var tipoAnimal:String
    private lateinit var razaAnimal:String

    //Navigation Drawer
    lateinit var toogle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Coleccion de Animales
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("animales")

    //Datos para mapa
    private var latitud:String="0.0"
    private var longitud:String="0.0"

    companion object{
        const val REQUEST_CODE_LOCATION=0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animal)

        recogerDatos()
        cargarDatos()

        toogle= ActionBarDrawerToggle(this,drawerLayoutAnimal,R.string.open, R.string.close)
        drawerLayoutAnimal.addDrawerListener(toogle)
        toogle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_viewAnimal.setNavigationItemSelectedListener {
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


        setUpTabs()

    }
    override fun onBackPressed(){

    }
    private fun setUpTabs(){
        val adapter= ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(DatosFragment().newInstanceDatos(nombreAnimal,tipoAnimal,razaAnimal),"DATOS")
        adapter.addFragment(FotosAnimalFragment(),"GALERIA")
        adapter.addFragment(UbicacionFragment().newInstanceUbicacion(latitud,longitud),"UBICACIÓN")
        viewPager.adapter=adapter
        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)!!.setIcon(R.drawable.ic_animal_perfil)
        tabs.getTabAt(1)!!.setIcon(R.drawable.ic_camara)
        tabs.getTabAt(2)!!.setIcon(R.drawable.ic_mapa)

    }
    private fun recogerDatos(){
        //Recogemos datos del Animal - 2ªFORMA
        val bundle = intent.extras
        nombreAnimal = bundle?.getString("nombreAnimal").toString()
        tipoAnimal = bundle?.getString("tipoAnimal").toString()
        razaAnimal = bundle?.getString("razaAnimal").toString()
        latitud = bundle?.getString("latitud").toString()
        longitud = bundle?.getString("longitud").toString()

    }
    private fun cargarDatos(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        var usuario=sharedPreferences.getString("usuario","no usuario")
        Toast.makeText(applicationContext,"Nombre de Usuario: {$usuario}", Toast.LENGTH_LONG).show()
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

}