package com.example.pilotos.ui.theme

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tracktalk.Screens.Pilotos.MainViewModel
import com.example.tracktalk.ui.theme.F1Grey

// Vista previa y composición de la pantalla de Pilotos en la Fórmula 1
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun Pilotos() {
    // Se obtiene la instancia del ViewModel
    val viewModel = viewModel<MainViewModel>()

    // Se obtienen el texto de búsqueda y la lista de pilotos desde el ViewModel
    val buscarTexto by viewModel.searchText.collectAsState()
    val pilotos by viewModel.pilotos.collectAsState()

    // Diseño principal de la pantalla
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(F1Grey)
    ) {
        // Campo de búsqueda de pilotos
        TextField(
            value = buscarTexto,
            onValueChange = viewModel::onSearchTextChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = "Buscar Piloto") }
        )

        // Espaciador
        Spacer(modifier = Modifier.height(16.dp))

        // Lista de pilotos en un LazyColumn
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(pilotos) { piloto ->
                // Tarjeta representando a un piloto
                Card(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier
                            .background(piloto.colorEquipo)
                            .fillMaxSize()
                    ) {
                        // Columna para la foto y detalles del equipo del piloto
                        Column(modifier = Modifier.width(100.dp)) {
                            // Foto del piloto
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Image(
                                    painterResource(id = piloto.foto),
                                    contentDescription = "Piloto",
                                    modifier = Modifier
                                        .padding(8.dp)
                                )
                            }

                            // Nombre del equipo
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Text(
                                    text = piloto.equipo,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(5.dp)
                                )
                            }

                            // Logo del equipo
                            Row(
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.width(100.dp)
                            ) {
                                Image(
                                    painterResource(id = piloto.fotoEquipo),
                                    contentDescription = "Equipo",
                                    modifier = Modifier
                                        .padding(8.dp)
                                        .size(40.dp)
                                )
                            }
                        }

                        // Columna para el nombre del piloto y estadísticas
                        Column() {
                            // Nombre del piloto
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "${piloto.nombre} ${piloto.apellido}",
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Victorias del piloto
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Victorias: ${piloto.victorias}",
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }

                            // Podios del piloto
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp)
                            ) {
                                Text(
                                    text = "Podios: ${piloto.podios}",
                                    color = Color.White,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                // Espaciador entre tarjetas
                Spacer(modifier = Modifier.padding(10.dp))
            }
        }
    }
}


