package com.example.tracktalk.Pit

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.tracktalk.Screens.Perfil.PerfilUsuario
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class SharedViewModel : ViewModel() {
    // Instancias de Firebase Storage y su referencia
    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    // GUARDAR DATOS EN FIRESTORE
    fun saveData(
        pitData: PitData,
        context: android.content.Context
    ) = CoroutineScope(Dispatchers.IO).launch {
        // Referencia a la colección "pits" en Firestore y documento con el ID único de PitData
        val fireStoreRef = Firebase.firestore
            .collection("pits")
            .document(pitData.idPit)

        try {
            // Guardar los datos en Firestore
            fireStoreRef.set(pitData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Subido con éxito.", Toast.LENGTH_SHORT).show()
                }
        } catch (e: Exception) {
            Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    // GUARDAR DATOS DEL PERFIL EN FIRESTORE
    fun subirPerfil(perfilUsuario: PerfilUsuario, context: android.content.Context) =
        CoroutineScope(Dispatchers.IO).launch {
            // Referencia a la colección "perfiles" en Firestore y documento con el nombre de usuario
            val fireStoreRef = Firebase.firestore
                .collection("perfiles")
                .document(perfilUsuario.usuario)

            try {
                // Guardar los datos del perfil en Firestore
                fireStoreRef.set(perfilUsuario)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Subido con éxito.", Toast.LENGTH_SHORT).show()
                    }
            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }

    // OBTENER DATOS DE PITS DESDE FIRESTORE
    fun retrieveData(
        context: android.content.Context,
        onDataLoaded: (List<PitData>) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        // Referencia a la colección "pits" en Firestore
        val fireStoreRef = Firebase.firestore.collection("pits")

        try {
            // Obtener datos de pits desde Firestore
            fireStoreRef.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val pitList = mutableListOf<PitData>()

                    for (document in task.result?.documents ?: emptyList()) {
                        val pitData = document.toObject<PitData>()
                        pitData?.let {
                            pitList.add(it)
                        }
                    }

                    if (pitList.isNotEmpty()) {
                        onDataLoaded(pitList)
                    } else {
                        Toast.makeText(context, "No se encontraron Pits.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Error al recuperar Pits: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Excepción: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // OBTENER URL DE LA FOTO DE PERFIL DESDE FIRESTORE
    fun mostrarFoto(
        context: android.content.Context,
        username: String,
        onPhotoLoaded: (String?) -> Unit
    ) = CoroutineScope(Dispatchers.IO).launch {
        // Referencia a la colección "perfiles" en Firestore
        val fireStoreRef = Firebase.firestore.collection("perfiles")

        try {
            // Obtener datos del perfil desde Firestore filtrando por nombre de usuario
            fireStoreRef.whereEqualTo("usuario", username)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (!task.result?.isEmpty!!) {
                            // Obtener el primer documento y convertirlo a un objeto PerfilUsuario
                            val document = task.result?.documents?.get(0)
                            val perfilUsuario = document?.toObject<PerfilUsuario>()
                            val photoUrl = perfilUsuario?.fotoPerfil
                            onPhotoLoaded(photoUrl)
                        } else {
                            Toast.makeText(context, "Perfil no encontrado.", Toast.LENGTH_SHORT).show()
                            onPhotoLoaded(null)
                        }
                    } else {
                        Toast.makeText(
                            context,
                            "Error al recuperar perfil: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                        onPhotoLoaded(null)
                    }
                }
        } catch (e: Exception) {
            Toast.makeText(context, "Excepción: ${e.message}", Toast.LENGTH_SHORT).show()
            onPhotoLoaded(null)
        }
    }

    // SUBIR IMAGEN A FIREBASE STORAGE Y OBTENER SU URL
    suspend fun saveImage(imageUri: Uri): String {
        // Crear una referencia única para la imagen en Firebase Storage
        val imageName = "images/${UUID.randomUUID()}"
        val imageRef: StorageReference = storageRef.child(imageName)

        // Subir la imagen a Firebase Storage
        val uploadTask = imageRef.putFile(imageUri)

        // Esperar a que la tarea de carga se complete
        uploadTask.await()

        // Obtener la URL de la imagen subida
        return imageRef.downloadUrl.await().toString()
    }
}



