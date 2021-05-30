package com.davidbustos.animalesdifundekotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.davidbustos.animalesdifunde_v3.Animal
import com.davidbustos.animalesdifunde_v3.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.internal.ContextUtils.getActivity
import kotlinx.android.synthetic.main.carta_animal.view.*

class AnimalesAdapter(options: FirestoreRecyclerOptions<Animal>

                      ) : FirestoreRecyclerAdapter<Animal, AnimalesAdapter.AnimalesAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalesAdapterVH {

        return AnimalesAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.carta_animal,parent,false))

    }
    override fun onBindViewHolder(holder: AnimalesAdapterVH, position: Int, model: Animal) {

        holder.tv_nombreAnimal.text=model.nombre
        holder.tv_tipoAnimal.text=model.tipo
        holder.tv_razaAnimal.text=model.raza
        holder.tv_usuarioAnimal.text=model.usuario

    }

    inner class AnimalesAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_nombreAnimal:TextView
        var tv_tipoAnimal:TextView
        var tv_razaAnimal:TextView
        var tv_usuarioAnimal:TextView

        init{
            tv_nombreAnimal=itemView.tv_nombreAnimal
            tv_tipoAnimal=itemView.tv_tipoAnimal
            tv_razaAnimal=itemView.tv_razaAnimal
            tv_usuarioAnimal=itemView.tv_usuarioAnimal


        }
    }

}

