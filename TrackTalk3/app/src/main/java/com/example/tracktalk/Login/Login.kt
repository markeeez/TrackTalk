@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.tracktalk.Login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.tracktalk.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.tracktalk.ui.theme.AppScreens
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.GoogleAuthProvider
import kotlin.Exception


@Composable
fun Login(
    navController: NavHostController
) {
    // Obtener el contexto local para utilizar en algunas funciones de Jetpack Compose
    val context = LocalContext.current

    // Inicializar el ViewModel asociado con la pantalla de inicio de sesión
    val viewModel: LoginScreenViewModel = viewModel()

    // Variables de estado para el nombre de usuario y su validez
    var username by remember { mutableStateOf("") }
    var isValidUsername by remember { mutableStateOf(false) }

    // Variables de estado para la contraseña y su validez
    var password by remember { mutableStateOf("") }
    var isValidPassword by remember { mutableStateOf(false) }

    // Variable de estado para controlar la visibilidad de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }

    // Clave de API para la autenticación con Google
    val token = "478523323266-p7ac7lnrubbiama331stulks7l9c0o8q.apps.googleusercontent.com"

    // Utilizar el launcher para manejar el resultado de la actividad de inicio de sesión de Google
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
        try {
            // Obtener la cuenta de Google y la credencial para autenticación
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            // Iniciar sesión con la credencial de Google y navegar a la pantalla principal
            viewModel.singInWithGoogleCredencial(credential) {
                navController.navigate(AppScreens.MainScreen.ruta)
            }
        } catch (e: Exception) {
            Log.d("Tracktalk", "GoogleSign Falló")
        }
    }

    // Diseño de la pantalla de inicio de sesión utilizando Jetpack Compose
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxSize()
            .background(Color(0xFFC0C0C0))
    ) {
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .padding(top = 50.dp)
                .fillMaxWidth()
        ) {
            // Tarjeta que contiene los elementos de la interfaz de usuario
            Card(
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.Black),
                shape = RoundedCornerShape(10.dp),
            ) {
                Column(
                    Modifier
                        .padding(16.dp)
                        .background(Color(0xFFC0C0C0))
                ) {
                    // Composable personalizado para la imagen en la pantalla de inicio de sesión
                    RowImage()

                    // Composable para el campo de entrada del nombre de usuario
                    RowEmail(
                        username = username,
                        usernameChange = {
                            username = it
                            isValidUsername = Patterns.EMAIL_ADDRESS.matcher(username).matches()
                        },
                        isValidUsername
                    )

                    // Composable para el campo de entrada de la contraseña
                    RowPassword(
                        contrasena = password,
                        passwordChange = {
                            password = it
                            isValidPassword = password.length >= 6
                        },
                        passwordVisible = passwordVisible,
                        passwordVisibleChange = {
                            passwordVisible = !passwordVisible
                        },
                        isValidPassword = isValidPassword
                    )

                    // Composable para el botón de inicio de sesión
                    RowButtonLogin(
                        context = context,
                        isValidUsername = isValidUsername,
                        isValidPassword = isValidPassword,
                        username = username,
                        password = password,
                        navController = navController,
                    )

                    // Composable para el botón de inicio de sesión con Google
                    RowLoginWithGoogle(
                        token = token,
                        context = context,
                        launcher = launcher
                    )

                    // Composable para el botón de crear cuenta
                    RowCreateAccount(
                        email = username,
                        password = password,
                        navController = navController
                    )
                }
            }
        }
    }
}
@Composable
fun RowCreateAccount(
    email: String,
    password: String,
    viewModel: LoginScreenViewModel = viewModel(),
    navController: NavHostController
) {
    // Fila que contiene el texto para crear una cuenta (con enlace clickable)
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Texto "Crea una cuenta" con un enlace clickable
        Text(
            text = "Crea una cuenta",
            modifier = Modifier.clickable {
                if (email.isNotEmpty() && password.isNotEmpty()) {
                    // Verificar que email y password no estén vacíos
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(AppScreens.MainScreen.ruta)
                    }
                } else {
                    // Mostrar un mensaje al usuario indicando que debe ingresar email y password
                    Log.w("RowCreateAccount", "Ingrese email y password")
                }
            }
        )
    }
}


@Composable
fun RowLoginWithGoogle(
    token: String,
    context: Context,
    launcher: ActivityResultLauncher<Intent>
) {
    // Fila que contiene la opción de inicio de sesión con Google
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .clickable {
                // Lógica para iniciar sesión con Google al hacer clic en la fila
                val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(token)
                    .requestEmail()
                    .build()
                val googleSignInClient = GoogleSignIn.getClient(context, options)
                launcher.launch(googleSignInClient.signInIntent)
            },
        horizontalArrangement = Arrangement.Center
    ) {
        // Icono de Google
        Image(
            painter = painterResource(id = R.drawable.google),
            contentDescription = null,
            modifier = Modifier
                .size(20.dp),
        )
        // Texto indicando inicio de sesión con Google
        Text(
            text = " Login With Google",
            color = Color.Black
        )
    }
}


@Composable
fun RowButtonLogin(
    context: Context,
    isValidUsername: Boolean,
    isValidPassword: Boolean,
    username: String,
    password: String,
    viewModel: LoginScreenViewModel = viewModel(),
    navController: NavHostController
) {
    // Fila que contiene el botón de inicio de sesión
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Botón de inicio de sesión
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                // Lógica para iniciar sesión al hacer clic en el botón
                viewModel.signWithEmailAndPassword(context, username, password) {
                    navController.navigate(AppScreens.MainScreen.ruta)
                }
            },
            // Habilita el botón solo si el nombre de usuario y la contraseña son válidos
            enabled = isValidUsername && isValidPassword
        ) {
            // Texto del botón
            Text(text = "Iniciar Sesión", color = Color.Black)
        }
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowPassword(
    contrasena: String,
    passwordChange: (String) -> Unit,
    passwordVisible: Boolean,
    passwordVisibleChange: () -> Unit,
    isValidPassword: Boolean
) {
    // Fila que contiene un campo de texto para la contraseña
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Campo de texto con contorno para la contraseña
        OutlinedTextField(
            value = contrasena,
            onValueChange = passwordChange,
            maxLines = 1,
            singleLine = true,
            label = { Text(text = "Contraseña", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            // Icono para alternar la visibilidad de la contraseña
            trailingIcon = {
                val image = if (passwordVisible) {
                    Icons.Filled.VisibilityOff
                } else {
                    Icons.Filled.Visibility
                }
                // Botón para cambiar la visibilidad de la contraseña
                IconButton(
                    onClick = passwordVisibleChange
                ) {
                    Icon(
                        imageVector = image,
                        contentDescription = "Ver contraseña"
                    )
                }
            },
            // Transformación visual según la visibilidad de la contraseña
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            // Colores del campo de texto basados en la validez de la contraseña
            colors = if (isValidPassword) {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedBorderColor = Color.Green
                )
            } else {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Red,
                    focusedBorderColor = Color.Red
                )
            }
        )
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowEmail(
    username: String,
    usernameChange: (String) -> Unit,
    isValid: Boolean
) {
    // Fila que contiene un campo de texto para el email
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Campo de texto con contorno para el email
        OutlinedTextField(
            value = username,
            onValueChange = usernameChange,
            label = { Text(text = "Email", color = Color.Black) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            singleLine = true,
            // Colores del campo de texto basados en la validez del email
            colors = if (isValid) {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Green,
                    focusedBorderColor = Color.Green
                )
            } else {
                TextFieldDefaults.outlinedTextFieldColors(
                    focusedLabelColor = Color.Red,
                    focusedBorderColor = Color.Red
                )
            }
        )
    }
}


@Composable
fun RowImage() {
    // Fila que contiene la imagen del logo
    Row(
        Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        // Imagen del logo
        Image(
            modifier = Modifier.width(100.dp),
            painter = painterResource(id = R.drawable.logotrack),
            contentDescription = "Imagen login"
        )
    }
}


   



