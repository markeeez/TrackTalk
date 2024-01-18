package com.example.tracktalk.Screens.Feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tracktalk.Pit.PitData
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.ui.theme.F1Grey
import com.google.firebase.auth.FirebaseAuth
import java.util.UUID

// Utilizando ExperimentalMaterial3Api para funciones experimentales de Material 3
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDataScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    // Estado para almacenar el mensaje del Pit
    var mensaje: String by remember { mutableStateOf("") }

    // Obtener la instancia de FirebaseAuth y el nombre de usuario actual
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val currentUsername = currentUser?.email?.substringBefore('@') ?: "Nombre de Usuario"

    // Contexto actual
    val context = LocalContext.current

    // Estado para almacenar la URL de la foto de perfil
    var imageUrl by remember { mutableStateOf<String?>(null) }

    // Composable principal que representa la pantalla de creación de nuevo Pit
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(F1Grey)
    ) {
        // App Bar con título y botón de cierre
        TopAppBar(
            title = { Text(text = "Nuevo Pit", color = Color.White) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "close_button",
                        tint = Color.White
                    )
                }
            },
            actions = {
                // Botón para publicar el Pit
                Button(
                    onClick = {
                        // Obtener la URL de la foto de perfil del usuario actual
                        sharedViewModel.mostrarFoto(
                            context = context,
                            username = currentUsername,
                            onPhotoLoaded = { photoUrl ->
                                imageUrl = photoUrl

                                // Crear el objeto PitData con la URL de la foto de perfil
                                val pitData = imageUrl?.let {
                                    PitData(
                                        idPit = UUID.randomUUID().toString(),
                                        mensaje = mensaje,
                                        usuario = currentUsername,
                                        foto = it
                                    )
                                }

                                // Guardar el objeto PitData y la foto de perfil en Firebase
                                if (pitData != null) {
                                    sharedViewModel.saveData(pitData = pitData, context = context)
                                }

                                // Volver a la pantalla anterior
                                navController.popBackStack()
                            }
                        )
                    }
                ) {
                    Text(text = "Pit")
                }
            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = F1Grey)
        )

        // Contenido principal de la pantalla
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Campo de texto para el mensaje del Pit
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                value = mensaje,
                onValueChange = { mensaje = it },
                label = { Text(text = "¿Qué está pasando?", color = Color.White) },
                textStyle = TextStyle.Default.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                maxLines = 5,
            )
        }
    }
}


