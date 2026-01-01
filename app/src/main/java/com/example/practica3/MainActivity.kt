package com.example.practica3

import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var btnAddNote: Button
    private val notesList = mutableListOf<Note>()
    private var noteIdCounter = 1
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        // Inicializar vistas
        recyclerView = findViewById(R.id.recyclerViewNotes)
        btnAddNote = findViewById(R.id.btnAddNote)
        
        // Agregar notas de ejemplo
        loadSampleNotes()
        
        // Configurar RecyclerView
        setupRecyclerView()
        
        // Configurar botón de agregar
        btnAddNote.setOnClickListener {
            addNewNote()
        }
    }
    
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(notesList) { position ->
            deleteNote(position)
        }
        
        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }
    
    private fun loadSampleNotes() {
        notesList.add(Note(noteIdCounter++, "Comprar despensa", "Leche, pan, huevos y frutas"))
        notesList.add(Note(noteIdCounter++, "Estudiar Android", "Repasar RecyclerView y Adapters"))
        notesList.add(Note(noteIdCounter++, "Ejercicio", "Salir a correr 30 minutos"))
    }
    
    private fun addNewNote() {
        val newNote = Note(
            id = noteIdCounter++,
            title = "Nueva Nota #${noteIdCounter - 1}",
            description = "Esta es una descripción de ejemplo"
        )
        noteAdapter.addNote(newNote)
        
        // Scroll automático a la nueva nota
        recyclerView.smoothScrollToPosition(notesList.size - 1)
    }
    
    private fun deleteNote(position: Int) {
        noteAdapter.removeNote(position)
    }
}