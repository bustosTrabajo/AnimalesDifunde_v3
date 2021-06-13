package com.davidbustos.animalesdifunde_v3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidbustos.animalesdifunde_v3.R
import android.Manifest
import android.location.Location
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

//Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

//Google Maps
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

//SupportMapFragment

import com.google.android.gms.maps.SupportMapFragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager

import kotlinx.android.synthetic.main.fragment_ubicacion.*


class UbicacionFragment() : Fragment(),OnMapReadyCallback{

    //implementar OnMapReadyCallback

    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Recoger tipo de Actividad
    private val ARGS_LATITUD= "latitud";
    private val latitud by lazy { arguments?.getString(ARGS_LATITUD) }

    //Recoger tipo de Actividad
    private val ARGS_LONGITUD = "longitud";
    private val longitud by lazy { arguments?.getString(ARGS_LONGITUD) }

    //Google Maps
    private lateinit var map: GoogleMap
    private lateinit var marker: Marker

    companion object{
        const val REQUEST_CODE_LOCATION=0
    }


    //Recoger Datos de Actividad creada. Ubicacion de Animal
    fun newInstanceUbicacion(latitud:String,longitud:String): Fragment{
        val args = Bundle()
        args.putString(ARGS_LATITUD, latitud)
        args.putString(ARGS_LONGITUD,longitud)

        val fragment = UbicacionFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ubicacion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val txtLatitud:TextView=view!!.findViewById(R.id.txtLatitudAnimal)
        txtLatitud.setText("Latitud ->"+latitud)
        Log.i("Valor Componente",""+latitud)

        val txtLongitud:TextView=view!!.findViewById(R.id.txtLongitudAnimal)
        txtLongitud.setText("Longitud ->"+longitud)
        Log.i("Valor Componente",""+longitud)

        createMapFragment()

    }
    private fun createMapFragment(){
        val mapFragment: SupportMapFragment =supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
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

}