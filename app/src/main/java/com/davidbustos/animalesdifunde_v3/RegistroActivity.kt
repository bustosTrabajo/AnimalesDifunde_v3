package com.davidbustos.animalesdifunde_v3

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_registro.*
import java.util.HashMap
import kotlin.properties.Delegates

class RegistroActivity : AppCompatActivity() {

    private val REQUEST_GALLERY= 1001

    //Autentificación con Firebase
    private val auth= Firebase.auth
    var autentificacion:Boolean=false

    //Iniciar Base de Datos
    private val db= FirebaseFirestore.getInstance()

    //Manejo de Foto de Perfil
    private var fotoPerfil:Boolean=false
    lateinit private var FileUri: Uri
    lateinit private var FileUriString:String
    lateinit private var url:Task<Uri>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        btnImagenPerfil.setOnClickListener(){
            mostrarGaleria()
        }

        btnRegistrarNuevoUsuario.setOnClickListener(){
            if(fotoPerfil){
                //Condición de políticas de privacidad de AnimalesDifunde
                if(checkPoliticas.isChecked){
                    registroAutentificacion()

                }else{
                    Toast.makeText(this,"Debe aceptar políticas de privacidad de AnimalesDifunde",Toast.LENGTH_LONG).show()
                }
            }else{
                //Ventana de Diálogo
                //Continuar sin foto de Perfil ¿Deseas continuar sin foto de perfil?
                //1.Sí
                val builder=AlertDialog.Builder(this)
                builder.setTitle("Datos de Registro")
                builder.setMessage("¿Deseas continuar sin foto/imagen de perfil?")
                builder.setPositiveButton(android.R.string.ok){
                    dialog,_ ->
                    //Condición de políticas de privacidad de AnimalesDifunde
                    if(checkPoliticas.isChecked){
                        registroAutentificacion()

                    }else{
                        Toast.makeText(this,"Debe aceptar políticas de privacidad de AnimalesDifunde",Toast.LENGTH_LONG).show()
                    }
                }
                //2.No
                builder.setNegativeButton(android.R.string.cancel){
                    dialog,_ ->
                    mostrarGaleria()
                }
                builder.show()
            }
        }
    }
    private fun mostrarGaleria(){
        val intentGaleria=Intent(Intent.ACTION_PICK)
        intentGaleria.type="image/*"
        startActivityForResult(intentGaleria,REQUEST_GALLERY)
    }

    private fun subirImagenBD(){
        if(FileUri!=null){
            var pd= ProgressDialog(this)
            pd.setTitle("Uploading")
            pd.show()

            var imageRef=FirebaseStorage.getInstance().reference.child("imagenes/usuarios/"+etCorreoElectronicoRegistro.text.toString()+FileUri.lastPathSegment)
            url=imageRef.downloadUrl
            imageRef.putFile(FileUri)
                .addOnSuccessListener { p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,"File Uploaded",Toast.LENGTH_LONG).show()

                }
                .addOnFailureListener{p0->
                    pd.dismiss()
                    Toast.makeText(applicationContext,p0.message,Toast.LENGTH_LONG).show()

                }
                .addOnProgressListener{p0 ->
                    var progress=(100.0 * p0.bytesTransferred)/p0.totalByteCount
                    pd.setMessage("Uploaded ${progress.toInt()}")

                }

        }

    }
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_GALLERY){
            imagenPerfil.setImageURI(data?.data)
            fotoPerfil=true
            FileUri= data?.data!!

        }

    }
    private fun registroUsuarioBD(){
        //Recoger valores de nuestros RadioGroup
        val soy:String=valoresRadioGroup()

        db.collection("usuarios").document(etCorreoElectronicoRegistro.text.toString())
            .set(
                hashMapOf("nombre" to etNombreRegistro.text.toString(),
                    "apellido" to etApellidoRegistro.text.toString(),
                    "correo" to etCorreoElectronicoRegistro.text.toString(),
                    "telefono" to etTelefonoRegistro.text.toString(),
                    "direccion" to etDireccionRegistro.text.toString(),
                    "soy" to soy

                )
            )
    }
    private fun valoresRadioGroup():String{
        var valorRadioGroup:String=""

        if(rbHombre.isChecked){
            valorRadioGroup="Hombre"
        }
        if(rbMujer.isChecked){
            valorRadioGroup="Mujer"
        }
        if(rbOtro.isChecked){
            valorRadioGroup="Otro"
        }
        return valorRadioGroup
    }

    private fun registroAutentificacion(){
        if(etCorreoElectronicoRegistro.text.isNotEmpty() && etContrasenaRegistro.text.isNotEmpty()) {
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(etCorreoElectronicoRegistro.text.toString(),
                    etContrasenaRegistro.text.toString())
                .addOnCompleteListener{
                    if(it.isSuccessful){
                        autentificacion=true
                        //subirImagenBD()
                        guardarDatos()
                        registroUsuarioBD()
                        Toast.makeText(this,"Registro completado con éxito",Toast.LENGTH_LONG).show()
                        var intent= Intent(this,AnimalesDifundeActivity::class.java)
                        startActivity(intent)

                    }else{
                        showAlert()
                    }
                }
        }

    }

    //Guardar datos del usuario
    private fun guardarDatos() {
        val email=etCorreoElectronicoRegistro.text.toString()
        //val urlString=url.toString()
        val sharedPreferences: SharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor =sharedPreferences.edit()

        editor.putString("usuario",email)
        editor.apply()

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