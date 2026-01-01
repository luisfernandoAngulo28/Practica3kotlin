package com.example.practica3

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var noteAdapter: NoteAdapter
    private lateinit var fabAddNote: FloatingActionButton
    private lateinit var emptyStateLayout: View
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
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        
        // Agregar notas de ejemplo
        loadSampleNotes()
        
        // Configurar RecyclerView
        setupRecyclerView()
        
        // Actualizar estado vacío
        updateEmptyState()
        
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
        noteAdapter = NoteAdapter(
            notesList,
            onDeleteClick = { position -> deleteNote(position) },
            onEditClick = { position -> editNote(position) }
        )
        
        recyclerView.apply {
            adapter = noteAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
            // Configurar animaciones para agregar/eliminar items
            itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator().apply {
                addDuration = 300
                removeDuration = 300
            }
        }
        
        // Configurar Swipe to Delete
        setupSwipeToDelete()
    }
    
    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(
            0, // No permitir arrastrar (drag)
            ItemTouchHelper.LEFT // Permitir swipe a la izquierda
        ) {
            private val deleteIcon = android.graphics.drawable.ColorDrawable(Color.parseColor("#FF5252"))
            private val paint = Paint().apply {
                color = Color.WHITE
                textSize = 48f
                textAlign = Paint.Align.CENTER
            }
            
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false
            
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val note = notesList[position]
                
                // Mostrar confirmación antes de eliminar
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Eliminar nota")
                    .setMessage("¿Estás seguro de eliminar '${note.title}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        noteAdapter.removeNote(position)
                        Toast.makeText(this@MainActivity, "Nota eliminada", Toast.LENGTH_SHORT).show()
                        updateEmptyState()
                    }
                    .setNegativeButton("Cancelar") { _, _ ->
                        // Restaurar el item si cancela
                        noteAdapter.notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        // Restaurar el item si cierra el diálogo
                        noteAdapter.notifyItemChanged(position)
                    }
                    .show()
            }
            
            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                val itemView = viewHolder.itemView
                
                // Dibujar fondo rojo detrás del item
                if (dX < 0) { // Swipe a la izquierda
                    val background = RectF(
                        itemView.right.toFloat() + dX,
                        itemView.top.toFloat(),
                        itemView.right.toFloat(),
                        itemView.bottom.toFloat()
                    )
                    c.drawRect(background, Paint().apply { color = Color.parseColor("#FF5252") })
                    
                    // Dibujar texto "Eliminar"
                    val textX = itemView.right.toFloat() - 100
                    val textY = itemView.top.toFloat() + (itemView.height / 2f) + 15
                    c.drawText("❌ Eliminar", textX, textY, paint)
                }
                
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
        
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    
    private fun loadSampleNotes() {
        // Comentar para mostrar Empty State al iniciar
        // Descomentar para tener notas de ejemplo
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
                updateEmptyState()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        
        // Aplicar animación al diálogo
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }
    
    private fun editNote(position: Int) {
        val note = notesList[position]
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val etDescription = dialogView.findViewById<EditText>(R.id.etNoteDescription)
        
        // Pre-llenar los campos con los datos actuales
        etTitle.setText(note.title)
        etDescription.setText(note.description)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("Editar Nota")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
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
                
                // Actualizar la nota manteniendo el ID, timestamp y color
                val updatedNote = Note(
                    id = note.id,
                    title = title,
                    description = description,
                    timestamp = note.timestamp,
                    color = note.color
                )
                notesList[position] = updatedNote
                noteAdapter.notifyItemChanged(position)
                Toast.makeText(this, "Nota actualizada correctamente", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .create()
        
        // Aplicar animación al diálogo
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }
    
    private fun updateEmptyState() {
        if (notesList.isEmpty()) {
            emptyStateLayout.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyStateLayout.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
    }
    
    private fun deleteNote(position: Int) {
        val note = notesList[position]
        MaterialAlertDialogBuilder(this)
            .setTitle("Eliminar nota")
            .setMessage("¿Estás seguro de eliminar '${note.title}'?")
            .setPositiveButton("Eliminar") { _, _ ->
                noteAdapter.removeNote(position)
                Toast.makeText(this, "Nota eliminada", Toast.LENGTH_SHORT).show()
                updateEmptyState()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}