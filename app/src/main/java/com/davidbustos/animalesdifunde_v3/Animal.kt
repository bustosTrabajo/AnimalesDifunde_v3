package com.davidbustos.animalesdifunde_v3
import java.util.*

data class Animal(
    var nombre:String="",
    var tipo:String="",
    var raza:String="",
    var usuario:String="",
    var latitud:String="",
    var longitud:String?=null
)