package com.davidbustos.animalesdifunde_v3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_subir_animal.*

class UbicacionActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, AdapterView.OnItemClickListener{

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    private var listView: ListView?=null

    private var arrayAdapter: ArrayAdapter<String>?=null

    //Google Maps
    private lateinit var map: GoogleMap
    private lateinit var marker: Marker


    //Datos de Animal
    private var nombreAnimal:String=""
    private var tipoAnimal:String=""
    private var razaAnimal:String=""
    private var latitud:String=""
    private var longitud:String=""


    companion object{
        const val REQUEST_CODE_LOCATION=0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subir_animal)

        arrayAdapter= ArrayAdapter(applicationContext,
            android.R.layout.simple_list_item_single_choice,
            resources.getStringArray(R.array.tiposAnimales))

        listView=findViewById(R.id.my_single_list_view)
        listView?.adapter=arrayAdapter
        listView?.choiceMode =ListView.CHOICE_MODE_SINGLE
        listView?.onItemClickListener=this

        btnRegistroNuevoAnimal.setOnClickListener(){
            registrarAnimal(etNombreAnimalRegistro.text.toString(), tipoAnimal, etRazaAnimalRegistro.text.toString())
        }
        btnVolverInicio.setOnClickListener(){
            var intent= Intent(this,InicioActivity::class.java)
            startActivity(intent)
        }


        createMapFragment()


    }
    override fun onBackPressed(){

    }
    private fun createMapFragment(){
        val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    override fun onMapReady(googleMap: GoogleMap?) {
        map= googleMap!!
        //Add marker in Sydney and move the camera
        var m1: LatLng = LatLng(50.0,50.0)
        marker=map.addMarker(MarkerOptions().position(m1).draggable(true).title("marker 1"))

        //map.setOnMarkerClickListener(this)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLng(m1))


    }
    private fun createMarker(){
        val coordinates = LatLng(28.043893,-16.539329)
        val marker :MarkerOptions? =MarkerOptions().position(coordinates).title("Mi playa favorita!")
        map.addMarker(marker)
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(coordinates, 18f),
            4000, null
        )
    }

    private fun registrarAnimal(nombreAnimal:String,tipoAnimal:String,razaAnimal:String){
        val animal=hashMapOf("nombre" to nombreAnimal, "raza" to razaAnimal, "tipo" to tipoAnimal, "usuario" to usuario, "latitud" to latitud, "longitud" to longitud)

        fStoreDB.collection("animales")
            .document(nombreAnimal)
            .set(animal as Map<String, Any>)
            .addOnSuccessListener{ documentReference ->
                Log.e("TAG", "Success")
            }
            .addOnFailureListener { e ->
                Log.e("TAG","Error adding document",e)
            }
    }

    private fun tercerPaso(nombreAnimal:String,tipoAnimal:String,razaAnimal:String,latitud:String,longitud:String){

    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tipoAnimal= parent?.getItemAtPosition(position) as String
        Toast.makeText(this,"Tipo de Animal: "+tipoAnimal,Toast.LENGTH_LONG).show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Botón pulsado", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMyLocationClick(miPosicion: Location) {
        Toast.makeText(this, "Estás en ${miPosicion.latitude}, ${miPosicion.longitude}" , Toast.LENGTH_LONG).show()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        latitud= marker.position.latitude.toString()
        longitud= marker.position.longitude.toString()
        Toast.makeText(this,"Lat: "+marker.position.latitude+"Long: "+marker.position.longitude,Toast.LENGTH_LONG).show()
    }

    // ¿Está aceptado el permiso de ubicación de usuario ? PARTE 1
    private fun isLocationPermisssionGranted() =
        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    // PARTE 2
    private fun enabledLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermisssionGranted()){
            //Permisos de ubicación correctos - SI
            map.isMyLocationEnabled = true

        }else{
            //Permisos de ubicación de usuario aún sin aceptar. Volver a pedirle al usuario que los acepte
            requestLocationPermission()

        }
    }
    //Método para pedir permisos - PARTE 3
    private fun requestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"Ve a ajustes y acepta los permisos",Toast.LENGTH_LONG).show()
        }else{
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_LOCATION)
        }
    }
    // Captura de respuesta de permisos de usuario -PARTE 4
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION ->if(grantResults.isNotEmpty() && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                map.isMyLocationEnabled = true
            }else{
                Toast.makeText(this,"Para activar la localización ve a ajustes y acepta los permisos",Toast.LENGTH_LONG).show()

            }
            else -> {}
        }
    }
    //Solución de error de Background y Permisos
    override fun onResumeFragments(){
        super.onResumeFragments()

        if(!::map.isInitialized) return
        if(!isLocationPermisssionGranted()){
            map.isMyLocationEnabled=false
            Toast.makeText(this,"Para activar la localización debes activar los permisos",Toast.LENGTH_LONG).show()

        }
    }
}