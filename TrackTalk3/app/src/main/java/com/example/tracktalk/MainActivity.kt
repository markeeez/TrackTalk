@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tracktalk

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ScaffoldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.tracktalk.ui.theme.TrackTalkTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.material.Scaffold
import androidx.compose.ui.res.painterResource
import com.example.tracktalk.ui.theme.Destinos
import com.example.tracktalk.ui.theme.Destinos.*
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.PermIdentity
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.tracktalk.Navigation.AppNavigation
import com.example.tracktalk.Navigation.NavigationHost
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.ui.theme.F1Grey


// MainActivity: Actividad principal que inicializa la aplicación
class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrackTalkTheme {
                // Configuración de la interfaz de usuario
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Navegación principal de la aplicación
                    AppNavigation(sharedViewModel)
                }
            }
        }
    }
}

// PantallaPrincipal: Composable que representa la pantalla principal de la aplicación
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PantallaPrincipal(sharedViewModel: SharedViewModel) {
    val navController = rememberNavController()
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    val navigationItems = listOf(
        Pantalla1,
        Pantalla3,
        Pantalla4,
        Pantalla5,
        Pantalla7
    )
    // Estructura de Scaffold que contiene la barra superior, el contenido y el cajón de navegación
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { TopBar(scope, scaffoldState, navController) },
        drawerContent = { Drawer(scope, scaffoldState, navController, menuItems = navigationItems) }
    ) {
        // Contenido principal de la aplicación
        NavigationHost(navController, sharedViewModel)
    }
}

// TopBar: Barra superior de la aplicación que muestra el título y el icono de menú
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController
) {
    CenterAlignedTopAppBar(
        title = { Text(text = "TrackTalk", color = Color.White) },
        navigationIcon = {
            // Icono de menú que abre el cajón de navegación al hacer clic
            IconButton(onClick = {
                scope.launch {
                    scaffoldState.drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Icono de menú",
                    tint = Color.White
                )
            }
        },
        actions = {
            // Ícono de perfil que navega a la pantalla de perfil al hacer clic
            IconButton(onClick = {
                navController.navigate(Destinos.Pantalla1.ruta)
            }) {
                Icon(
                    imageVector = Icons.Filled.PermIdentity,
                    contentDescription = "Perfil",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = F1Grey)
    )
}

// Drawer: Cajón de navegación lateral que muestra las opciones de navegación
@Composable
fun Drawer(
    scope: CoroutineScope,
    scaffoldState: ScaffoldState,
    navController: NavController,
    menuItems: List<Destinos>
) {
    Column(modifier = Modifier
        .background(F1Grey)
        .fillMaxSize()) {
        // Imagen de encabezado del cajón de navegación
        Image(
            painterResource(id = R.drawable.f1),
            contentDescription = "Menú opciones",
            modifier = Modifier
                .height(160.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(15.dp))
        // Identificación de la ruta actual
        val currentRoute = currentRoute(navController)
        // Creación de elementos del cajón de navegación
        menuItems.forEach { item ->
            DrawerItem(
                item = item,
                selected = currentRoute == item.ruta
            ) {
                // Navegación a la pantalla correspondiente al hacer clic y cierre del cajón
                navController.navigate(item.ruta) {
                    launchSingleTop = true
                }
                scope.launch {
                    scaffoldState.drawerState.close()
                }
            }
        }
    }
}

// DrawerItem: Elemento individual del cajón de navegación
@Composable
fun DrawerItem(
    item: Destinos,
    selected: Boolean,
    onItemClick: (Destinos) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(6.dp)
            .clip(RoundedCornerShape(12))
            .background(
                if (selected) MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.25f
                )
                else Color.Transparent
            )
            .padding(8.dp)
            .clickable { onItemClick(item) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono del elemento
        Image(
            painterResource(id = item.icon),
            contentDescription = item.title
        )
        Spacer(modifier = Modifier.width(12.dp))
        // Título del elemento
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White
        )
    }
}

// currentRoute: Función que obtiene la ruta actual de la navegación
@Composable
fun currentRoute(navController: NavController): String? {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    return navBackStackEntry?.destination?.route
}

