package com.example.practica3

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val color: Int = getRandomPastelColor()
) {
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
    
    companion object {
        private val PASTEL_COLORS = listOf(
            "#FFE5E5", // Rosa pastel
            "#E5F3FF", // Azul pastel
            "#E5FFE5", // Verde pastel
            "#FFF5E5", // Naranja pastel
            "#F5E5FF", // Púrpura pastel
            "#FFFFE5", // Amarillo pastel
            "#E5FFFF", // Cyan pastel
            "#FFE5F5", // Rosa claro
            "#F0E5FF", // Lavanda pastel
            "#E5FFF0"  // Menta pastel
        )
        
        fun getRandomPastelColor(): Int {
            return Color.parseColor(PASTEL_COLORS[Random.nextInt(PASTEL_COLORS.size)])
        }
    }
}
