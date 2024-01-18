package com.example.tracktalk.Screens.Pilotos

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tracktalk.R
import com.example.tracktalk.ui.theme.AlfaRomeoColor
import com.example.tracktalk.ui.theme.AlphaTauriColor
import com.example.tracktalk.ui.theme.AlpineColor
import com.example.tracktalk.ui.theme.AstonMartinColor
import com.example.tracktalk.ui.theme.FerrariColor
import com.example.tracktalk.ui.theme.HaasColor
import com.example.tracktalk.ui.theme.McLarenColor
import com.example.tracktalk.ui.theme.MercedesColor
import com.example.tracktalk.ui.theme.RedBullColor
import com.example.tracktalk.ui.theme.WilliamsColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

// ViewModel para la gestión de datos en la aplicación
class MainViewModel : ViewModel() {
    // Flujo de texto de búsqueda
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    // Estado de búsqueda para controlar si se está realizando una búsqueda
    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    // Lista original de pilotos
    private val _pilotos = MutableStateFlow(allPilotos)
    val pilotos = searchText
        .combine(_pilotos) { text, pilotos ->
            if (text.isBlank()) {
                pilotos
            } else {
                pilotos.filter {
                    it.busquedaEncontrada(text)
                }
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _pilotos.value
        )

    // Función para manejar cambios en el texto de búsqueda
    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }
}

// Modelo de datos para representar a un piloto
data class Piloto(
    val nombre: String,
    val apellido: String,
    val foto: Int,
    val colorEquipo: Color,
    val equipo: String,
    val victorias: Int,
    val podios: Int,
    val fotoEquipo: Int,
    val puntos: Int
) {
    // Función para determinar si el piloto coincide con la búsqueda
    fun busquedaEncontrada(query: String): Boolean {
        val matchingCombinations = listOf(
            "$nombre$apellido",
            "$nombre $apellido",
            "${nombre.first()} ${apellido.first()}",
            "$apellido"
        )
        return matchingCombinations.any {
            it.contains(query, ignoreCase = true)
        }
    }
}

//Lista de pilotos
private val allPilotos = listOf(
    Piloto(
        nombre = "Max",
        apellido = "Verstappen",
        foto = R.drawable.verstappen,
        colorEquipo = RedBullColor,
        equipo = "Red Bull",
        victorias = 54,
        podios = 98,
        fotoEquipo = R.drawable.redbull,
        puntos = 575
    ),
    Piloto(
        nombre = "Sergio",
        apellido = "Pérez",
        foto = R.drawable.checo,
        colorEquipo = RedBullColor,
        equipo = "Red Bull",
        victorias = 6,
        podios = 34,
        fotoEquipo = R.drawable.redbull,
        puntos = 285
    ),
    Piloto(
        nombre = "Fernando",
        apellido = "Alonso",
        foto = R.drawable.alonso,
        colorEquipo = AstonMartinColor,
        equipo = "Aston Martin",
        victorias = 32,
        podios = 106,
        fotoEquipo = R.drawable.astonmartin,
        puntos = 206
    ),
    Piloto(
        nombre = "Lance",
        apellido = "Stroll",
        foto = R.drawable.stroll,
        colorEquipo = AstonMartinColor,
        equipo = "Aston Martin",
        victorias = 0,
        podios = 3,
        fotoEquipo = R.drawable.astonmartin,
        puntos = 74
    ),
    Piloto(
        nombre = "Lewis",
        apellido = "Hamilton",
        foto = R.drawable.hamilton,
        colorEquipo = MercedesColor,
        equipo = "Mercedes",
        victorias = 103,
        podios = 197,
        fotoEquipo = R.drawable.mercedes,
        puntos = 234
    ),
    Piloto(
        nombre = "George",
        apellido = "Russell",
        foto = R.drawable.rusell,
        colorEquipo = MercedesColor,
        equipo = "Mercedes",
        victorias = 1,
        podios = 11,
        fotoEquipo = R.drawable.mercedes,
        puntos = 175
    ),
    Piloto(
        nombre = "Charles",
        apellido = "Leclerc",
        foto = R.drawable.leclerc,
        colorEquipo = FerrariColor,
        equipo = "Ferrari",
        victorias = 5,
        podios = 30,
        fotoEquipo = R.drawable.ferrari,
        puntos = 206
    ),
    Piloto(
        nombre = "Carlos",
        apellido = "Sainz",
        foto = R.drawable.sainz,
        colorEquipo = FerrariColor,
        equipo = "Ferrari",
        victorias = 2,
        podios = 18,
        fotoEquipo = R.drawable.ferrari,
        puntos = 200
    ),
    Piloto(
        nombre = "Lando",
        apellido = "Norris",
        foto = R.drawable.norris,
        colorEquipo = McLarenColor,
        equipo = "McLaren",
        victorias = 0,
        podios = 13,
        fotoEquipo = R.drawable.mclaren,
        puntos = 205
    ),
    Piloto(
        nombre = "Oscar",
        apellido = "Piastri",
        foto = R.drawable.piastri,
        colorEquipo = McLarenColor,
        equipo = "McLaren",
        victorias = 0,
        podios = 2,
        fotoEquipo = R.drawable.mclaren,
        puntos = 97
    ),
    Piloto(
        nombre = "Esteban",
        apellido = "Ocon",
        foto = R.drawable.ocon,
        colorEquipo = AlpineColor,
        equipo = "Alpine BWT",
        victorias = 1,
        podios = 3,
        fotoEquipo = R.drawable.alpine,
        puntos = 58
    ),
    Piloto(
        nombre = "Pierre",
        apellido = "Gasly",
        foto = R.drawable.gasly,
        colorEquipo = AlpineColor,
        equipo = "Alpine BWT",
        victorias = 1,
        podios = 4,
        fotoEquipo = R.drawable.alpine,
        puntos = 62
    ),
    Piloto(
        nombre = "Yuki",
        apellido = "Tsunoda",
        foto = R.drawable.tsunoda,
        colorEquipo = AlphaTauriColor,
        equipo = "Alpha Tauri",
        victorias = 0,
        podios = 0,
        fotoEquipo = R.drawable.alphatauri,
        puntos = 17
    ),
    Piloto(
        nombre = "Daniel",
        apellido = "Ricciardo",
        foto = R.drawable.ricciardo,
        colorEquipo = AlphaTauriColor,
        equipo = "Alpha Tauri",
        victorias = 8,
        podios = 32,
        fotoEquipo = R.drawable.alphatauri,
        puntos = 6
    ),
    Piloto(
        nombre = "Valtteri",
        apellido = "Bottas",
        foto = R.drawable.bottas,
        colorEquipo = AlfaRomeoColor,
        equipo = "Alfa Romeo",
        victorias = 10,
        podios = 67,
        fotoEquipo = R.drawable.alfaromeo,
        puntos = 10
    ),
    Piloto(
        nombre = "Guanyu",
        apellido = "Zhou",
        foto = R.drawable.zhou,
        colorEquipo = AlfaRomeoColor,
        equipo = "Alfa Romeo",
        victorias = 0,
        podios = 0,
        fotoEquipo = R.drawable.alfaromeo,
        puntos = 6
    ),
    Piloto(
        nombre = "Kevin",
        apellido = "Magnussen",
        foto = R.drawable.magnusen,
        colorEquipo = HaasColor,
        equipo = "Haas",
        victorias = 0,
        podios = 1,
        fotoEquipo = R.drawable.haas,
        puntos = 3
    ),
    Piloto(
        nombre = "Nico",
        apellido = "Hülkenberg",
        foto = R.drawable.hulkenberg,
        colorEquipo = HaasColor,
        equipo = "Haas",
        victorias = 0,
        podios = 0,
        fotoEquipo = R.drawable.haas,
        puntos = 9
    ),
    Piloto(
        nombre = "Alex",
        apellido = "Albon",
        foto = R.drawable.albon,
        colorEquipo = WilliamsColor,
        equipo = "Williams",
        victorias = 0,
        podios = 2,
        fotoEquipo = R.drawable.williams,
        puntos = 27
    ),
    Piloto(
        nombre = "Logan",
        apellido = "Sargeant",
        foto = R.drawable.sargeant,
        colorEquipo = WilliamsColor,
        equipo = "Williams",
        victorias = 0,
        podios = 0,
        fotoEquipo = R.drawable.williams,
        puntos = 1
    ),




    )

