package com.davidbustos.animalesdifunde_v3

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_foto.*

class FotoActivity : AppCompatActivity() {

    //Datos de Animal
    private var nombreAnimal:String=""
    private var tipoAnimal:String=""
    private var razaAnimal:String=""
    private var latitud:String=""
    private var longitud:String=""

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    private val REQUEST_CAMERA=1002

    //Uri-> identificador de un recurso de nuestro móvil. En este caso una foto
    var foto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_foto)

        recogerDatos()

        btnSiguiente3.setOnClickListener(){
            //Comprobar si foto ha sido ya subida con éxito
            registrarAnimal(nombreAnimal, tipoAnimal, razaAnimal,latitud,longitud)

        }

        btnAnterior3.setOnClickListener(){
            //Volver al paso anterior
            val intent=Intent(this,UbicacionActivity::class.java)

            intent.putExtra("nombreAnimal",nombreAnimal)
            intent.putExtra("tipoAnimal",tipoAnimal)
            intent.putExtra("razaAnimal",razaAnimal)
            intent.putExtra("latitud",latitud)
            intent.putExtra("longitud",longitud)



            startActivity(intent)
        }


        btnFotoAnimal.setOnClickListener(){
            checkPermissions()
        }

    }
    private fun recogerDatos(){
        //Recogemos datos de Animal
        intent.getStringExtra("nombreAnimal")?.let{nombreAnimal=it}
        intent.getStringExtra("tipoAnimal")?.let{tipoAnimal=it}
        intent.getStringExtra("razaAnimal")?.let{razaAnimal=it}
        intent.getStringExtra("latitud")?.let{latitud=it}
        intent.getStringExtra("longitud")?.let{longitud=it}
        Log.i("DATOS ANIMAL","Datos del Animal -> "+" "+nombreAnimal+" "+tipoAnimal+" "+razaAnimal+" "+latitud+" "+longitud)
    }
    private fun registrarAnimal(nombreAnimal:String,tipoAnimal:String,razaAnimal:String,latitud:String,longitud:String){
     val animal=hashMapOf("nombre" to nombreAnimal, "raza" to razaAnimal, "tipo" to tipoAnimal, "usuario" to usuario, "latitud" to latitud, "longitud" to longitud)

     fStoreDB.collection("animales")
         .document(nombreAnimal)
         .set(animal as Map<String, Any>)
         .addOnSuccessListener{ documentReference ->
             Log.e("TAG", "Success")
             //Volver al paso anterior
             val intent=Intent(this,AnimalActivity::class.java)
             intent.putExtra("nombreAnimal",nombreAnimal)
             intent.putExtra("tipoAnimal",tipoAnimal)
             intent.putExtra("razaAnimal",razaAnimal)
             intent.putExtra("latitud",latitud)
             intent.putExtra("longitud",longitud)
             startActivity(intent)
         }
         .addOnFailureListener { e ->
             Log.e("TAG","Error adding document",e)
         }
    }
    override fun onBackPressed(){

    }

    /*
   Guardar forma pedir permisos. Volver a utilizarla en todos mis Proyectos
    */
    private fun checkPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            //Permiso no aceptado por el momento. Solicitar Permisos para que sean aceptados
            requestCameraPermission()
        }else{
            //Abrir cámara
            openCamera()
        }
    }

    private fun openCamera(){
        //ContentValues - Manejador de contenidos - Crea un espacio en memoria que rellenará con los bits de la foto
        val value= ContentValues()
        value.put(MediaStore.Images.Media.TITLE, "Nueva imagen")
        foto=contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,value)
        val camaraIntent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        camaraIntent.putExtra(MediaStore.EXTRA_OUTPUT,foto)
        startActivityForResult(camaraIntent,REQUEST_CAMERA)


    }

    //Subir imagen de galería o Subir imagen de cámara
    override fun onActivityResult(requestCode:Int, resultCode:Int, data:Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode == RESULT_OK && requestCode == REQUEST_CAMERA){
            ivFotoAnimal.setImageURI(foto)
        }

    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado los permisos con anterioridad
            Toast.makeText(this,"Permisos rechazados por primera vez", Toast.LENGTH_SHORT).show()
        }else{
            //Pide permisos y establece Request Code. Al ser respondidos se lanza método "onRequestPermissionResult"

            //Request Code -> Código de verificación de permisos: 777. Cada vez que solicite permisos confirmará mediante la búsqueda del correspondiente requestCode
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.CAMERA),777)

        }
    }

    //grantResults --> Contiene un array con todos los permisos solicitados y su información contenido. En éste caso, solo habrá la información de un permiso: el de camara.
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==777){ //nuestros permisos
            //grantResults[0]  -> Selecciono el primer permiso guardado en mi Array
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                openCamera()
            }else{
                //El permiso no ha sido aceptado
                Toast.makeText(this,"Permisos rechazados por primera vez", Toast.LENGTH_LONG).show()
            }

        }
    }

}