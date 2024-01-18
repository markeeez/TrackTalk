package com.example.tracktalk.Screens.SplashScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.tracktalk.R
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.tracktalk.ui.theme.AppScreens
import com.example.tracktalk.ui.theme.F1Grey
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(navController: NavHostController) {
    // Efecto lanzado para simular una pausa de 1000 milisegundos (1 segundo)
    LaunchedEffect(key1 = true) {
        delay(1000)

        // Verificar si hay un usuario actualmente autenticado en Firebase
        if (FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
            // Si no hay usuario autenticado, navegar a la pantalla de inicio de sesión
            navController.navigate(AppScreens.Login.ruta) {
                popUpTo(AppScreens.Login.ruta) {
                    inclusive = true
                }
            }
        } else {
            // Si hay un usuario autenticado, navegar a la pantalla principal
            navController.navigate(AppScreens.MainScreen.ruta) {
                popUpTo(AppScreens.SplashScreen.ruta) {
                    inclusive = true
                }
                popUpTo(AppScreens.Login.ruta) {
                    inclusive = true
                }
            }
        }
    }

    // Llamada a la función que define la interfaz de usuario de la pantalla de carga
    Splash()
}

@Composable
fun Splash() {
    Column(
        modifier = Modifier.fillMaxSize().background(F1Grey),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Imagen del logotipo
        Image(
            painter = painterResource(id = R.drawable.logotrack),
            contentDescription = "Logo",
        )
        // Texto de bienvenida
        Text(text = "Bienvenido", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    // Llamada de vista previa para la pantalla de carga
    Splash()
}

