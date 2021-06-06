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

class SubirAnimalActivity : AppCompatActivity() , AdapterView.OnItemClickListener{

    //Iniciar Base de Datos
    private val fStoreDB= FirebaseFirestore.getInstance()
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    private var listView: ListView?=null

    private var arrayAdapter: ArrayAdapter<String>?=null

    private var tipoAnimal:String=""


    private var latitud:String=""
    private var longitud:String=""


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

        btnSiguiente.setOnClickListener(){
            segundoPaso(etNombreAnimalRegistro.text.toString(), tipoAnimal, etRazaAnimalRegistro.text.toString())
        }
        btnAnterior.setOnClickListener(){
            var intent= Intent(this,InicioActivity::class.java)
            startActivity(intent)
        }



    }
    override fun onBackPressed(){

    }

    private fun segundoPaso(nombreAnimal:String, tipoAnimal:String, razaAnimal:String){

        val intent=Intent(this,UbicacionActivity::class.java)

        intent.putExtra("nombreAnimal",nombreAnimal)
        intent.putExtra("tipoAnimal",tipoAnimal)
        intent.putExtra("razaAnimal",razaAnimal)

        startActivity(intent)

    }
    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        tipoAnimal= parent?.getItemAtPosition(position) as String
        Toast.makeText(this,"Tipo de Animal: "+tipoAnimal,Toast.LENGTH_LONG).show()
    }


}