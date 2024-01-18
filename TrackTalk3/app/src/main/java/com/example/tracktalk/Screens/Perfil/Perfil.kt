package com.example.tracktalk.Screens.Perfil

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.tracktalk.MainActivity
import com.example.tracktalk.Pit.SharedViewModel
import com.example.tracktalk.R
import com.example.tracktalk.ui.theme.F1Grey
import com.example.tracktalk.ui.theme.F1SilverDark
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


// Composable para la pantalla de perfil del usuario
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Perfil(sharedViewModel: SharedViewModel) {
    // Obtener instancia de FirebaseAuth y usuario actual
    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val context = LocalContext.current

    // Estado para manejar la URI de la imagen seleccionada
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Estado para manejar el nombre editado por el usuario
    var editedName by remember { mutableStateOf(currentUser?.displayName ?: "") }

    // Columna principal que contiene secciones de información del usuario
    Row(modifier = Modifier.background(F1Grey)) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(F1Grey)
        ) {
            item {
                // Sección del encabezado con la foto de perfil
                currentUser?.let {
                    val username = it.email?.substringBefore('@') ?: "Usuario no disponible"

                    // Encabezado con foto de perfil
                    ProfileHeader(
                        context = context,
                        onImageSelected = { uri ->
                            selectedImageUri = uri
                        },
                        sharedViewModel = sharedViewModel,
                        email = username
                    )
                }
            }
            item {
                // Información del usuario
                currentUser?.let {
                    val username = it.email?.substringBefore('@') ?: "Usuario no disponible"

                    // Sección de información del usuario
                    UserInfoSection(
                        username = username,
                        name = editedName,  // Mostrar el nombre editado
                        lastName = "",
                        email = it.email ?: "Correo no disponible"
                    )

                    // Campo de texto editable para el nombre
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        value = editedName,
                        onValueChange = { editedName = it },
                        label = { Text(text = "Nombre", color = Color.White) },
                        textStyle = TextStyle.Default.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                        maxLines = 5
                    )

                    // Botón para guardar el nombre editado
                    Button(
                        onClick = {
                            // Actualizar el nombre en Firebase
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(editedName)
                                .build()

                            it.updateProfile(profileUpdates)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(
                                            context,
                                            "Actualizado con éxito",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                    } else {
                                        Toast.makeText(
                                            context,
                                            "No se ha cambiado el nombre",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                    ) {
                        Text("Guardar")
                    }
                }
            }
            item {
                // Botón para cerrar sesión
                LogoutButton()
            }
        }
    }
}

// Composable para el botón de cierre de sesión
@Composable
fun LogoutButton() {
    val context: Context = LocalContext.current

    // Botón que cierra la sesión en Firebase y redirige a la pantalla de inicio
    Button(
        onClick = {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(context, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
    ) {
        Text(text = "Cerrar Sesión")
    }
}

// Composable para el encabezado del perfil que incluye la foto de perfil
@Composable
fun ProfileHeader(
    context: Context,
    onImageSelected: (Uri) -> Unit,
    sharedViewModel: SharedViewModel,
    email: String
) {
    // Estado para manejar la URI de la imagen seleccionada
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para iniciar la actividad de la galería y seleccionar una imagen
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                onImageSelected(it)
            }
        }

    // Columna principal del encabezado
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ...

        var imageUrl by remember { mutableStateOf<String?>(null) }
        val profileImageModifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .background(F1SilverDark)

        // Mostrar la imagen seleccionada si hay una, de lo contrario mostrar la imagen predeterminada
        val painter = if (selectedImageUri != null) {
            rememberAsyncImagePainter(selectedImageUri)
        } else {
            painterResource(id = R.drawable.pilotos)
        }

        // Obtener la URL de la foto de perfil del usuario
        sharedViewModel.mostrarFoto(
            context = context,
            username = email,
            onPhotoLoaded = { photoUrl ->
                imageUrl = photoUrl
            }
        )

        // Mostrar la imagen de perfil
        if (imageUrl != null) {
            val painter = rememberImagePainter(imageUrl)
            Image(
                painter = painter,
                contentDescription = "Foto Perfil",
                modifier = profileImageModifier
            )
        } else {
            // Puedes mostrar un placeholder o mensaje cuando no hay foto disponible
            Text(text = "No hay foto disponible")
        }

        // Espaciador antes de los botones de edición y guardado
        Spacer(modifier = Modifier.padding(8.dp))

        // Botón para editar la foto de perfil
        Button(
            onClick = {
                launcher.launch("image/*")
            }
        ) {
            Text(text = "Editar foto de perfil")
        }

        // Botón para guardar la nueva foto de perfil
        Button(
            onClick = {
                if (selectedImageUri != null) {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            sharedViewModel.subirPerfil(
                                perfilUsuario = PerfilUsuario(email, sharedViewModel.saveImage(selectedImageUri!!)),
                                context
                            )
                        } catch (e: Exception) {
                            // Manejar la excepción, por ejemplo, mostrar un mensaje de error
                            Log.e("GuardadoPerfil", "Error al guardar el perfil: ${e.message}")
                        }
                    }
                } else {
                    // Mostrar un mensaje al usuario indicando que debe seleccionar una foto
                    Log.w("GuardadoPerfil", "No se ha seleccionado ninguna foto")
                }
            }
        ) {
            Text(text = "Guardar")
        }
    }
}

// Composable para mostrar la sección de información del usuario
@Composable
fun UserInfoSection(
    username: String,
    name: String,
    lastName: String,
    email: String
) {
    // Columna que contiene elementos de información del usuario
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Elemento de información con icono para el nombre de usuario
        ProfileItem(icon = Icons.Default.PersonOutline, text = username, textColor = Color.White)

        // Elemento de información con icono para el nombre completo
        ProfileItem(icon = Icons.Default.Person, text = "$name $lastName", textColor = Color.White)

        // Elemento de información con icono para el correo electrónico
        ProfileItem(icon = Icons.Default.Email, text = email, textColor = Color.White)
    }
}

// Composable para mostrar elementos de información del usuario
@Composable
fun ProfileItem(icon: ImageVector, text: String, textColor: Color) {
    // Fila que contiene un icono y texto de información del usuario
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono que representa el tipo de información
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )

        // Espaciador y texto con la información del usuario
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyLarge, color = textColor)
    }
}





