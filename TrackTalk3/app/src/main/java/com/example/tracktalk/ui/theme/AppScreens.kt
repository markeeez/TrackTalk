package com.example.tracktalk.ui.theme

sealed class AppScreens(val ruta: String) {
        // Clase sellada que define las diferentes pantallas de la aplicación con sus rutas asociadas

        object SplashScreen : AppScreens("SplashScreen")
        object MainScreen : AppScreens("MainScreen")
        object Login : AppScreens("Login")
        object AddDataScreen : AppScreens("add_data_screen")
}

