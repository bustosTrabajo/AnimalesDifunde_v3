package com.davidbustos.animalesdifunde_v3

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
//Maps
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_ubicacion.*


class UbicacionActivity : AppCompatActivity() , OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener{

    //Google Maps
    private lateinit var map: GoogleMap
    private lateinit var marker: Marker
    //Datos de Animal
    private lateinit var nombreAnimal:String
    private lateinit var tipoAnimal:String
    private lateinit var razaAnimal:String
    //Latitud - Longitud
    private var latitud:String=""
    private var longitud:String=""


    companion object{
        const val REQUEST_CODE_LOCATION=0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ubicacion)

        recogerDatos()

        btnSiguiente2.setOnClickListener(){
            tercerPaso(nombreAnimal, tipoAnimal, razaAnimal, latitud, longitud)
        }
        btnAnterior2.setOnClickListener(){
            var intent= Intent(this,SubirAnimalActivity::class.java)
            startActivity(intent)
        }


        createMapFragment()

    }
    private fun recogerDatos(){
        //Recogemos datos de Animal
        intent.getStringExtra("nombreAnimal")?.let{nombreAnimal=it}
        intent.getStringExtra("tipoAnimal")?.let{tipoAnimal=it}
        intent.getStringExtra("razaAnimal")?.let{razaAnimal=it}
        Log.i("i","Datos del Animal -> "+" "+nombreAnimal+" "+tipoAnimal+" "+razaAnimal)

    }

    override fun onBackPressed(){

    }
    private fun createMapFragment(){
        //val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment

        val mapFragment:SupportMapFragment=supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
    override fun onMapReady(googleMap: GoogleMap?) {
        map= googleMap!!
        //Add marker in Sydney and move the camera
        var m1: LatLng = LatLng(50.0,50.0)
        marker=map.addMarker(MarkerOptions().position(m1).draggable(true).title("Marcador del Mapa"))
        //map.setOnMarkerClickListener(this)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLng(m1))

    }

    private fun tercerPaso(nombreAnimal:String,tipoAnimal:String,razaAnimal:String,latitud:String,longitud:String){
        val intent=Intent(this,FotoActivity::class.java)

        intent.putExtra("nombreAnimal",nombreAnimal)
        intent.putExtra("tipoAnimal",tipoAnimal)
        intent.putExtra("razaAnimal",razaAnimal)
        intent.putExtra("latitud",latitud)
        intent.putExtra("longitud",longitud)

        startActivity(intent)

    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Bot??n pulsado", Toast.LENGTH_LONG).show()
        return false
    }

    override fun onMyLocationClick(miPosicion: Location) {
        Toast.makeText(this, "Est??s en ${miPosicion.latitude}, ${miPosicion.longitude}" , Toast.LENGTH_LONG).show()
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {

    }

    override fun onMarkerDragEnd(p0: Marker?) {
        latitud= marker.position.latitude.toString()
        longitud= marker.position.longitude.toString()
        Toast.makeText(this,"Lat: "+latitud+"Long: "+longitud,Toast.LENGTH_LONG).show()
    }

    // ??Est?? aceptado el permiso de ubicaci??n de usuario ? PARTE 1
    private fun isLocationPermisssionGranted() =
        ContextCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    // PARTE 2
    private fun enabledLocation(){
        if(!::map.isInitialized) return
        if(isLocationPermisssionGranted()){
            //Permisos de ubicaci??n correctos - SI
            map.isMyLocationEnabled = true

        }else{
            //Permisos de ubicaci??n de usuario a??n sin aceptar. Volver a pedirle al usuario que los acepte
            requestLocationPermission()

        }
    }
    //M??todo para pedir permisos - PARTE 3
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
                Toast.makeText(this,"Para activar la localizaci??n ve a ajustes y acepta los permisos",Toast.LENGTH_LONG).show()

            }
            else -> {}
        }
    }
    //Soluci??n de error de Background y Permisos
    override fun onResumeFragments(){
        super.onResumeFragments()

        if(!::map.isInitialized) return
        if(!isLocationPermisssionGranted()){
            map.isMyLocationEnabled=false
            Toast.makeText(this,"Para activar la localizaci??n debes activar los permisos",Toast.LENGTH_LONG).show()

        }
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        TODO("Not yet implemented")
    }
}