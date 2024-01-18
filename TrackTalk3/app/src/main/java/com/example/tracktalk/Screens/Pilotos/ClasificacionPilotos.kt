package com.example.tracktalk.Screens.Pilotos

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
import androidx.compose.runtime.collectAsState
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracktalk.ui.theme.F1Grey


// Vista previa y composición de la clasificación de pilotos en la Fórmula 1
@Preview
@Composable
fun ClasPil() {
    // Se obtiene la instancia del ViewModel
    val viewModel = viewModel<MainViewModel>()

    // Se obtienen los pilotos desde el flujo en el ViewModel
    val pilotos by viewModel.pilotos.collectAsState()

    // Comparador personalizado para ordenar pilotos por puntos
    val ComparadorPuntos = Comparator<Piloto> { right, left ->
        left.puntos.compareTo(right.puntos)
    }

    // Estado para gestionar el comparador actual
    var comparar by remember { mutableStateOf(ComparadorPuntos) }

    // Lista de pilotos desde el flujo
    val listaPilotos = pilotos

    // Se ordena la lista de pilotos según el comparador actual
    val clasPiloto = listaPilotos.sortedWith(comparar)

    // Diseño principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(F1Grey),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Espaciador superior
        Spacer(modifier = Modifier.height(16.dp))

        // Barra de título
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(F1Grey),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Clasificación F1",
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier
                    .background(F1Grey)
            )
        }

        // Lista de pilotos en un LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(clasPiloto) { piloto ->
                // Tarjeta representando a un piloto
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(8.dp)
                        .background(F1Grey)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(piloto.colorEquipo),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Foto del piloto
                        Image(
                            painterResource(id = piloto.foto),
                            contentDescription = "Piloto",
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .padding(8.dp)
                        )

                        // Nombre del piloto
                        Text(
                            text = "${piloto.nombre} ${piloto.apellido}",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                        )

                        // Puntos del piloto
                        Text(
                            text = "Puntos: ${piloto.puntos}",
                            color = Color.White,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }

                // Espaciador entre tarjetas
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


