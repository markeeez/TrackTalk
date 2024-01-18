package com.example.tracktalk.Login

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlin.Exception

class LoginScreenViewModel : ViewModel() {
    // Instancia de FirebaseAuth para la autenticación
    private val auth: FirebaseAuth = Firebase.auth

    // LiveData para manejar el estado de carga
    private val _loading = MutableLiveData(false)

    // Función para realizar el inicio de sesión con credenciales de Google
    fun singInWithGoogleCredencial(
        credential: AuthCredential,
        home: () -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithCredential(credential)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Usuario autenticado con éxito, ejecutar acción de inicio
                        Log.d("TrackTalck", "Logueado Con Google")
                        home()
                    }
                }
                .addOnFailureListener {
                    // Manejar fallo en el inicio de sesión con Google
                    Log.d("TrackTalk", "Fallo al loguear con Google")
                }
        } catch (e: Exception) {
            // Manejar excepción al iniciar sesión con Google
            Log.d("TrackTakl", "Excepción al loguear Con Google: ${e.localizedMessage}")
        }
    }

    // Función para realizar el inicio de sesión con correo electrónico y contraseña
    fun signWithEmailAndPassword(
        context: Context,
        email: String,
        password: String,
        home: () -> Unit
    ) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Usuario autenticado con éxito, ejecutar acción de inicio
                        Log.d("TrackTalk", "signInWithEmailAndPassword logueado!!")
                        home()
                    }
                }
                .addOnFailureListener { ex ->
                    // Manejar fallo en el inicio de sesión con correo electrónico y contraseña
                    Log.d("TrackTalk", "signInWithEmailAndPassword error inicio")
                    mostrarErrores(context, "Email o contraseña incorrecto")
                }
        } catch (ex: Exception) {
            // Manejar excepción al iniciar sesión con correo electrónico y contraseña
            Log.d("TrackTalk", "signInWithEmailAndPassword: ${ex.message}")
        }
    }

    // Función para mostrar errores mediante un cuadro de diálogo
    private fun mostrarErrores(context: Context, message: String) {
        AlertDialog.Builder(context).setMessage(message).show()
    }

    // Función para crear un nuevo usuario con correo electrónico y contraseña
    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ) {
        // Verificar si ya se está realizando una operación de carga
        if (_loading.value == false) {
            _loading.value = true
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Usuario creado con éxito, ejecutar acción de inicio
                        home()
                    } else {
                        // Manejar fallo en la creación de usuario
                        Log.d("TrackTalk", "createUserWithEmailAndPassword: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }
}
