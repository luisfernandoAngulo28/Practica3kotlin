package com.example.practica3

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var fabAddNote: FloatingActionButton
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
        fabAddNote = findViewById(R.id.fabAddNote)
        
        // Agregar notas de ejemplo
        loadSampleNotes()
        
        // Configurar RecyclerView
        setupRecyclerView()
        
        // Configurar botón de agregar
        fabAddNote.setOnClickListener {
            // Animación del FAB al presionarlo
            it.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_scale_down))
            it.postDelayed({
                it.startAnimation(android.view.animation.AnimationUtils.loadAnimation(this, R.anim.fab_scale_up))
                addNewNote()
            }, 200)
        }
    }
    
    private fun setupRecyclerView() {
        noteAdapter = NoteAdapter(notesList) { position ->
            deleteNote(position)
        }
        
        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            // Configurar animaciones para agregar/eliminar items
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator().apply {
                addDuration = 300
                removeDuration = 300
            }
        }
    }
    
    private fun loadSampleNotes() {
        notesList.add(Note(noteIdCounter++, "Comprar despensa", "Leche, pan, huevos y frutas"))
        notesList.add(Note(noteIdCounter++, "Estudiar Android", "Repasar RecyclerView y Adapters"))
        notesList.add(Note(noteIdCounter++, "Ejercicio", "Salir a correr 30 minutos"))
    }
    
    private fun addNewNote() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val etDescription = dialogView.findViewById<EditText>(R.id.etNoteDescription)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Nueva Nota")
            .setView(dialogView)
            .setPositiveButton("Agregar") { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()
                
                if (title.isEmpty()) {
                    Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (description.isEmpty()) {
                    Toast.makeText(this, "La descripción no puede estar vacía", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                val newNote = Note(
                    id = noteIdCounter++,
                    title = title,
                    description = description
                )
                noteAdapter.addNote(newNote)
                recyclerView.smoothScrollToPosition(notesList.size - 1)
                Toast.makeText(this, "Nota agregada correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        
        // Aplicar animación al diálogo
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }
    
    private fun deleteNote(position: Int) {
        val note = notesList[position]
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar nota")
            .setMessage("¿Estás seguro de eliminar '${note.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                noteAdapter.removeNote(position)
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}