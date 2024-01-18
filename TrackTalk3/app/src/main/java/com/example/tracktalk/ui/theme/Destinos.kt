package com.example.tracktalk.ui.theme

import com.example.tracktalk.R

sealed class Destinos(
    val icon: Int,
    val title: String,
    val ruta: String
) {
    // Clase sellada que define los destinos de navegación en la aplicación

    object Pantalla1 : Destinos(R.drawable.perfil, "Perfil", "Pantalla1")
    object Pantalla3 : Destinos(R.drawable.feed, "Feed", "Pantalla3")
    object Pantalla4 : Destinos(R.drawable.claspiloto, "Clasificación Pilotos", "Pantalla4")
    object Pantalla5 : Destinos(R.drawable.clasteam, "Clasificación Equipos", "Pantalla5")
    object Pantalla7 : Destinos(R.drawable.pilotos, "Pilotos", "Pantalla7")
}



