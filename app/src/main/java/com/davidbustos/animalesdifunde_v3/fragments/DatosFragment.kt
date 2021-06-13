
package com.davidbustos.animalesdifunde_v3.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.davidbustos.animalesdifunde_v3.R
import android.util.Log
import android.widget.EditText

//Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_perfil_usuario.*

import kotlinx.android.synthetic.main.fragment_datos.*

//ImageView
import android.widget.ImageView;
import com.google.firebase.database.annotations.Nullable


class DatosFragment: Fragment() {
    //Usuario
    val usuario: String? = FirebaseAuth.getInstance().currentUser?.email

    //Firebase
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val collectionReference: CollectionReference = db.collection("animales")


    //Recoger datos de Actividad
    private val ARGS_NAME = "name";
    private val nombre by lazy { arguments?.getString(ARGS_NAME) }

    //Recoger tipo de Actividad
    private val ARGS_TIPO = "tipo";
    private val tipo by lazy { arguments?.getString(ARGS_TIPO) }

    //Recoger tipo de Actividad
    private val ARGS_RAZA = "raza";
    private val raza by lazy { arguments?.getString(ARGS_RAZA) }


    //Recoger Datos de Actividad
    fun newInstanceDatos(name: String,tipo:String,raza:String): Fragment{
        val args = Bundle()
        args.putString(ARGS_NAME, name)
        args.putString(ARGS_TIPO,tipo)
        args.putString(ARGS_RAZA,raza)

        val fragment = DatosFragment()
        fragment.arguments = args
        return fragment
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        return inflater.inflate(R.layout.fragment_datos, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        etNombreAnimalPerfil.setEnabled(false)
        btnGuardarAnimal.setOnClickListener{
            collectionReference.document(etNombreAnimalPerfil.text.toString()).set(
                hashMapOf(
                    "raza"  to etRazaAnimalPerfil.text.toString(),
                    "tipo" to etTipoAnimalPerfil.text.toString()
                )
            )
        }
        btnLimpiarAnimal.setOnClickListener{
            etRazaAnimalPerfil.setText("")
            etTipoAnimalPerfil.setText("")

        }

        val etNombre:EditText=view!!.findViewById(R.id.etNombreAnimalPerfil)
        etNombre.setText(nombre)
        val etTipo:EditText=view!!.findViewById(R.id.etTipoAnimalPerfil)
        etTipo.setText(tipo)
        val etRaza:EditText=view!!.findViewById(R.id.etRazaAnimalPerfil)
        etRaza.setText(raza)
    }


}


