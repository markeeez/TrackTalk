package com.example.tracktalk.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tracktalk.Login.Login
import com.example.tracktalk.PantallaPrincipal
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.Screens.SplashScreen.SplashScreen
import com.example.tracktalk.ui.theme.AppScreens

@Composable
fun AppNavigation(sharedViewModel: SharedViewModel) {
    // Inicializar el controlador de navegación
    val navController = rememberNavController()

    // Definir la estructura de navegación con el NavHost
    NavHost(navController = navController, startDestination = AppScreens.SplashScreen.ruta) {

        // Pantalla de presentación (Splash Screen)
        composable(AppScreens.SplashScreen.ruta) {
            // Llamar al composable SplashScreen y pasar el controlador de navegación
            SplashScreen(navController)
        }

        // Pantalla de inicio de sesión
        composable(AppScreens.Login.ruta) {
            // Llamar al composable Login y pasar el controlador de navegación
            Login(navController)
        }

        // Pantalla principal de la aplicación
        composable(AppScreens.MainScreen.ruta) {
            // Llamar al composable PantallaPrincipal y pasar el SharedViewModel
            PantallaPrincipal(sharedViewModel)
        }
    }
}

