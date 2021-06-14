package com.davidbustos.animalesdifunde_v3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_perfil_usuario.*
//Picasso
import com.squareup.picasso.Picasso

class PerfilUsuarioActivity : AppCompatActivity() {
    val REQ_IMAGEN  = 123
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Coleccion de Usuarios
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("usuarios")

    //ImageView de Perfil de Usuario
    //id => ivFotoUsuario
    lateinit var ivFotoUsuario : ImageView

    //Storage Reference
    lateinit var storageReference: StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_usuario)

        cargarFoto()

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
            var intent= Intent(this,AnimalesDifundeActivity::class.java)
            startActivity(intent)
        }

    }
    private fun cargarFoto(){
        ivFotoUsuario=findViewById(R.id.ivFotoUsuario)
        val storage= Firebase.storage
        storageReference=storage.reference
        var imageRef = storageReference.child(usuario + "/images/default.png")
        if (imageRef != null) {
            imageRef.downloadUrl.addOnSuccessListener {
                Picasso.get().load(it).resize(150, 150).centerCrop().into(ivFotoUsuario)
            }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==REQ_IMAGEN && resultCode==RESULT_OK){
            if(data!=null){
                val uri: Uri? =data.data // mnt/sdacard/images/image.jpg por ejemplo
                if(usuario!="-1"){
                    var imageRef=storageReference.child(usuario+"/images/default.png")
                    subirImagen(imageRef, uri)
                }

            }
        }
    }


    private fun subirImagen(imageRef: StorageReference, ruta: Uri?) {
        if(ruta!=null){
            imageRef.putFile(ruta).addOnCompleteListener(){
                if(it.isSuccessful){
                    Toast.makeText(this, "Imagen perfil subida", Toast.LENGTH_LONG).show()
                    cargarFoto()
                }
                else{
                    Toast.makeText(this, "Error: ${it.exception.toString()}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}