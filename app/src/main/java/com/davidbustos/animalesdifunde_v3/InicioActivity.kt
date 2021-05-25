package com.davidbustos.animalesdifunde_v3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_inicio.*

class InicioActivity : AppCompatActivity() {

    private val auth= Firebase.auth
    lateinit var email: EditText
    lateinit var contrasena: EditText
    lateinit var url:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        btnEntrar.setOnClickListener(){
            login()
        }

        btnRegistro.setOnClickListener(){
            registro()
        }
        btnSalirAplicacion.setOnClickListener(){
            cerrarAplicacion()
        }


    }
    override fun onBackPressed(){

    }
    private fun cerrarAplicacion(){
        finish()
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
    private fun login(){
        if(etEmail.text.isNotEmpty() && etContrasena.text.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(etEmail.text.toString(),
                    etContrasena.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        var intent= Intent(this,AnimalesDifundeActivity::class.java)
                        guardarDatos()
                        startActivity(intent)
                    }else{
                        showAlert()
                    }
                }
        }
    }
    //Guardar datos del Usuario
    private fun guardarDatos(){
        val email=etEmail.text.toString()
        //val urlString=url.toString()
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =sharedPreferences.edit()

        editor.putString("usuario",email)
        editor.apply()


    }
    private fun registro(){
        var intent= Intent(this,RegistroActivity::class.java)
        startActivity(intent)

    }
    private fun showAlert(){
        val builder= AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}