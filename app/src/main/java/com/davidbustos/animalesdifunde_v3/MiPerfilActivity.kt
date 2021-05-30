package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_mi_perfil_activities.*


class MiPerfilActivity : AppCompatActivity() {

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Coleccion de Usuarios
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("usuarios")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mi_perfil_activities)

        datosMiPerfil()

        btnGuardar.setOnClickListener() {
            collectionReference.document(usuario.toString()).set(
                hashMapOf(
                    "nombre" to etNombre.text.toString(),
                    "apellido" to etApellido.text.toString(),
                    "direccion" to etCorreo.text.toString(),
                    "correo" to etCorreo.text.toString(),
                    "soy" to etGenero.text.toString(),
                    "telefono" to etTelefono.text.toString()
                )
            )
        }
        btnLimpiar.setOnClickListener() {
            etNombre.setText("")
            etApellido.setText("")
            etCorreo.setText("")
            etGenero.setText("")
            etTelefono.setText("")

        }

        btnVolver.setOnClickListener(){
            var intent= Intent(this,InicioActivity::class.java)
            startActivity(intent)
        }

    }

    private fun datosMiPerfil() {
        collectionReference.document(usuario.toString()).get().addOnSuccessListener {
            etNombre.setText(it.get("nombre") as String?)
            etApellido.setText(it.get("apellido") as String?)
            etCorreo.setText(it.get("correo") as String?)
            etDireccion.setText(it.get("direccion") as String?)
            etGenero.setText(it.get("soy") as String?)
            etTelefono.setText(it.get("telefono") as String?)

        }
    }
    override fun onBackPressed(){

    }
}