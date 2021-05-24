package com.davidbustos.animalesdifunde_v3

import android.app.ProgressDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_animales_difunde.*
import kotlinx.android.synthetic.main.activity_registro.*

class AnimalesDifundeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_animales_difunde)

        cargarDatos()

    }
    private fun cargarDatos(){
        val sharedPreferences=getSharedPreferences("sharedPrefs",Context.MODE_PRIVATE)
        txtNombreUsuario.text=sharedPreferences.getString("usuario","")
    }
    private fun recogerImagen(){


    }
}