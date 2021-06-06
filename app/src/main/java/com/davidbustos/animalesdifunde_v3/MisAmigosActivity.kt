package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_perfil_usuario.*

class MisAmigosActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_amigos)

        btnVolver.setOnClickListener(){
            var intent= Intent(this,AnimalesDifundeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed(){

    }
}