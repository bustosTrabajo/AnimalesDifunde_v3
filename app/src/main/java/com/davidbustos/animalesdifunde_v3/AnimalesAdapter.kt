package com.davidbustos.animalesdifundekotlin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.davidbustos.animalesdifunde_v3.Animal
import com.davidbustos.animalesdifunde_v3.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.carta_animal.view.*

class AnimalesAdapter(options: FirestoreRecyclerOptions<Animal>) :
    FirestoreRecyclerAdapter<Animal, AnimalesAdapter.AnimalesAdapterVH>(options) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalesAdapterVH {

        return AnimalesAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.carta_animal,parent,false))

    }
    override fun onBindViewHolder(holder: AnimalesAdapterVH, position: Int, model: Animal) {
        holder.tv_nombreAnimal.text=model.nombre
        holder.tv_tipoAnimal.text=model.tipo
        holder.tv_razaAnimal.text=model.raza
        holder.tv_usuarioAnimal.text=model.usuario_animal

    }
    class AnimalesAdapterVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_nombreAnimal:TextView=itemView.tv_nombreAnimal
        var tv_tipoAnimal:TextView=itemView.tv_tipoAnimal
        var tv_razaAnimal:TextView=itemView.tv_razaAnimal
        var tv_usuarioAnimal:TextView=itemView.tv_usuarioAnimal

    }
}