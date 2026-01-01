package com.example.practica3

import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random

enum class Priority(val displayName: String, val color: Int) {
    HIGH("Alta", Color.parseColor("#FFCDD2")),      // Rojo pastel
    MEDIUM("Media", Color.parseColor("#FFF9C4")),   // Amarillo pastel
    LOW("Baja", Color.parseColor("#C8E6C9"))        // Verde pastel
}

data class Note(
    val id: Int,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
    val priority: Priority = Priority.MEDIUM
) {
    val color: Int
        get() = priority.color
    
    fun getFormattedDate(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
