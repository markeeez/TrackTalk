package com.example.tracktalk.Pit

import java.util.UUID

// Definición de la clase de datos PitData utilizando una data class
data class PitData(
    // Identificador único generado aleatoriamente utilizando UUID
    var idPit: String = UUID.randomUUID().toString(),

    // Mensaje asociado al PitData
    var mensaje: String = "",

    // Nombre de usuario asociado al PitData
    var usuario: String = "",

    // URL de la foto de perfil al PitData
    var foto: String = ""
)

