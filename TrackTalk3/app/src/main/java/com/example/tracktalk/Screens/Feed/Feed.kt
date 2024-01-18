package com.example.tracktalk.Screens.Feed

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.tracktalk.Pit.PitData
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.ui.theme.AppScreens
import com.example.tracktalk.ui.theme.*


// Composable para representar el Feed de la aplicación
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Feed(
    navController: NavController,
    sharedViewModel: SharedViewModel
) {
    // Contexto actual
    val context = LocalContext.current

    // Estado para almacenar todos los Pits recuperados de Firestore
    var allPits by remember { mutableStateOf<List<PitData>>(emptyList()) }

    // Recuperar todos los datos de Firestore al iniciar el composable
    sharedViewModel.retrieveData(
        context = context
    ) { pitList ->
        allPits = pitList
    }

    // Estructura principal de la pantalla utilizando el componente Scaffold
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            // Botón flotante para navegar a la pantalla de agregar nuevo Pit
            FloatingActionButton(
                onClick = { navController.navigate(AppScreens.AddDataScreen.ruta) },
                backgroundColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.White
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Icon")
            }
        },
        topBar = {
            // Barra superior (TopAppBar) similar a la de Twitter
            TopAppBar(
                title = { Text(text = "Feed", fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center) },
                backgroundColor = F1Grey,
                contentColor = Color.White,
                elevation = 4.dp,
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) {
        // Contenido principal de la pantalla utilizando LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .background(F1Grey),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(allPits) { pit ->
                // Tarjeta (Card) para cada Pit en el Feed
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                        .background(F1Grey)
                        .padding(16.dp)
                ) {
                    // Estados locales para gestionar los likes y su estado
                    var likes by remember { mutableStateOf(0) }
                    var isLiked by remember { mutableStateOf(false) }

                    // Estructura interna de la tarjeta que representa un Pit
                    Column(modifier = Modifier.background(F1Grey)) {
                        // Sección de foto de perfil y nombre de usuario
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            // Imagen de perfil redonda
                            Image(
                                painter = rememberImagePainter(data = pit.foto),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                            )

                            // Espaciador y texto con el nombre de usuario
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = pit.usuario, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        // Espaciador antes del mensaje del Pit
                        Spacer(modifier = Modifier.height(5.dp))

                        // Mensaje del Pit
                        Text(text = pit.mensaje, modifier = Modifier.padding(10.dp), color = Color.White)

                        // Sección de botón de "Me gusta" con contador y cambio de color
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.End,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Icono del botón "Me gusta" con cambio de color si está activo
                            IconButton(
                                onClick = {
                                    likes += if (isLiked) -1 else 1
                                    isLiked = !isLiked
                                }
                            ) {
                                Icon(
                                    imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                    contentDescription = "Me gusta",
                                    tint = if (isLiked) Color.Red else Color.White
                                )
                            }

                            // Contador de "Me gusta"
                            Text(text = likes.toString(), color = Color.White)
                        }
                    }
                }

                // Separador entre las tarjetas (Pits)
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    color = Color.White,
                    thickness = 1.dp
                )
            }
        }
    }
}




