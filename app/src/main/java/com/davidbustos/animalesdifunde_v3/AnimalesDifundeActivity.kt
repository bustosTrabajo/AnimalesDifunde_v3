package com.davidbustos.animalesdifunde_v3

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.davidbustos.animalesdifundekotlin.SubirNuevoAnimalActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_animales_difunde.*
import kotlinx.android.synthetic.main.activity_registro.*

class AnimalesDifundeActivity : AppCompatActivity() {

    lateinit var toogle: ActionBarDrawerToggle
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView


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

                R.id.item1 ->

                    Toast.makeText(applicationContext,"Perfil de Usuario", Toast.LENGTH_LONG).show()

                R.id.item2 -> Toast.makeText(applicationContext,"Todos mis Animales",Toast.LENGTH_LONG).show()

                R.id.item3 -> Toast.makeText(applicationContext, "Mensajería de Usuario",Toast.LENGTH_LONG).show()

            }
            true
        }

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