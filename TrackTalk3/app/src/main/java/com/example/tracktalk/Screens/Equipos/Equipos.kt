package com.example.tracktalk.Screens.Equipos
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tracktalk.R
import com.example.tracktalk.ui.theme.*
// Definición de la data class Equipos que representa información sobre un equipo de F1
data class Equipos(
    val nombreEquipo: String,
    val colorEquipo: Color,
    val puntosEquipo: Int,
    val fotoEquipo: Int
)

// Lista de todos los equipos con información predeterminada
private val allEquipos = listOf(
    Equipos(
        nombreEquipo = "Red Bull Racing",
        colorEquipo = RedBullColor,
        puntosEquipo = 860,
        fotoEquipo = R.drawable.redbull
    ),

    Equipos(
        nombreEquipo = "Hass",
        colorEquipo = HaasColor,
        puntosEquipo = 0,
        fotoEquipo = R.drawable.haas
    )
)

// Composable que muestra la clasificación de equipos de F1
@Composable
fun ClasificacionEquipos() {
    // Estado para mantener la lista de equipos ordenada por puntos
    var sortedEquipos by remember(allEquipos) { mutableStateOf(allEquipos.sortedByDescending { it.puntosEquipo }) }

    // Composable principal que contiene la clasificación de equipos
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(F1Grey),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Encabezado de la clasificación de equipos
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(F1Grey),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Clasificacion Equipos F1",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .background(F1Grey)
            )
        }

        // Lista de equipos en formato de tarjeta
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(sortedEquipos) { equipo ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                        .background(F1Grey)
                ) {
                    // Contenido de la tarjeta para cada equipo
                    Row(
                        modifier = Modifier.fillMaxSize().background(equipo.colorEquipo),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Imagen del equipo
                        Image(
                            painterResource(id = equipo.fotoEquipo),
                            contentDescription = "Equipo",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .padding(8.dp)
                        )

                        // Nombre del equipo
                        Text(
                            text = equipo.nombreEquipo,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        )

                        // Puntos del equipo
                        Text(
                            text = "Puntos: ${equipo.puntosEquipo}",
                            color = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }

                // Espaciador entre las tarjetas
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

