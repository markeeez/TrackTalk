package com.example.tracktalk.Navigation
import androidx.compose.runtime.Composable
import com.example.tracktalk.ui.theme.Destinos.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pilotos.ui.theme.Pilotos
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.Screens.Equipos.ClasificacionEquipos
import com.example.tracktalk.Screens.Pilotos.ClasPil
import com.example.tracktalk.Screens.Feed.AddDataScreen
import com.example.tracktalk.Screens.Feed.Feed
import com.example.tracktalk.Screens.Perfil.Perfil
import com.example.tracktalk.ui.theme.AppScreens


@Composable
fun NavigationHost(navController: NavHostController, sharedViewModel: SharedViewModel) {
    // Definir la estructura de navegación con el NavHost
    NavHost(navController = navController, startDestination = Pantalla3.ruta) {

        // Pantalla 1: Perfil
        composable(Pantalla1.ruta) {
            // Llamar al composable Perfil y pasar el SharedViewModel
            Perfil(sharedViewModel)
        }

        // Pantalla 3: Feed
        composable(Pantalla3.ruta) {
            // Llamar al composable Feed y pasar el controlador de navegación y el SharedViewModel
            Feed(navController, sharedViewModel)
        }

        // Pantalla 4: Clasificación Pilotos
        composable(Pantalla4.ruta) {
            // Llamar al composable ClasPil
            ClasPil()
        }

        // Pantalla 5: Clasificación Equipos
        composable(Pantalla5.ruta) {
            // Llamar al composable ClasificacionEquipos
            ClasificacionEquipos()
        }

        // Pantalla 7: Pilotos
        composable(Pantalla7.ruta) {
            // Llamar al composable Pilotos
            Pilotos()
        }

        // Pantalla de agregar datos
        composable(AppScreens.AddDataScreen.ruta) {
            // Llamar al composable AddDataScreen y pasar el controlador de navegación y el SharedViewModel
            AddDataScreen(navController, sharedViewModel)
        }
    }
}

