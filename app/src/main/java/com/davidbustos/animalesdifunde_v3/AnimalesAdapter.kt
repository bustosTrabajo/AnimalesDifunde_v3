package com.davidbustos.animalesdifunde_v3

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carta_animal.view.*

class AnimalesAdapter(val animalClick: (Animal)->Unit): RecyclerView.Adapter<AnimalesAdapter.AnimalesViewHolder>() {

    var animales:List<Animal> =emptyList()

    fun setData(list:List<Animal>){
        animales=list
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalesViewHolder {
        return AnimalesViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.carta_animal,
                parent, false
            )

        )
    }
    override fun onBindViewHolder(holder: AnimalesViewHolder, position: Int) {

        holder.itemView.tv_nombreAnimal.text =animales[position].nombre


        holder.itemView.setOnClickListener(){
            animalClick(animales[position])
        }
    }
    override fun getItemCount(): Int {
        return animales.size
    }
    class AnimalesViewHolder(itemView: android.view.View):RecyclerView.ViewHolder(itemView)

}