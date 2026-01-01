package com.example.practica3

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
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
    private lateinit var toolbar: Toolbar
    private val notesList = mutableListOf<Note>()
    private val filteredNotesList = mutableListOf<Note>()
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
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        
        recyclerView = findViewById(R.id.recyclerViewNotes)
        fabAddNote = findViewById(R.id.fabAddNote)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        
        // Agregar notas de ejemplo
        loadSampleNotes()
        
        // Inicializar lista filtrada con todas las notas
        filteredNotesList.addAll(notesList)
        
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
            filteredNotesList,
            onDeleteClick = { position -> deleteNote(position) },
            onEditClick = { position -> editNote(position) },
            onShareClick = { position -> shareNote(position) }
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
                val note = filteredNotesList[position]
                
                // Mostrar confirmación antes de eliminar
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setTitle("Eliminar nota")
                    .setMessage("¿Estás seguro de eliminar '${note.title}'?")
                    .setPositiveButton("Eliminar") { _, _ ->
                        notesList.remove(note)
                        filteredNotesList.removeAt(position)
                        noteAdapter.notifyItemRemoved(position)
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
        notesList.add(Note(noteIdCounter++, "Comprar víveres", "Comprar leche, huevos, pan, frutas y verduras para la semana", priority = Priority.HIGH))
        notesList.add(Note(noteIdCounter++, "Reunión del proyecto", "Reunión de equipo el viernes a las 3:00 PM para revisar avances", priority = Priority.MEDIUM))
        notesList.add(Note(noteIdCounter++, "Leer libro recomendado", "Terminar de leer 'Clean Code' antes de fin de mes", priority = Priority.LOW))
    }
    
    private fun addNewNote() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note_with_priority, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val etDescription = dialogView.findViewById<EditText>(R.id.etNoteDescription)
        val rgPriority = dialogView.findViewById<RadioGroup>(R.id.rgPriority)
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_new_note)
            .setView(dialogView)
            .setPositiveButton(R.string.button_add) { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()
                
                if (title.isEmpty()) {
                    Toast.makeText(this, R.string.message_title_empty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (description.isEmpty()) {
                    Toast.makeText(this, R.string.message_description_empty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                val priority = when (rgPriority.checkedRadioButtonId) {
                    R.id.rbHigh -> Priority.HIGH
                    R.id.rbMedium -> Priority.MEDIUM
                    R.id.rbLow -> Priority.LOW
                    else -> Priority.MEDIUM
                }
                
                val newNote = Note(
                    id = noteIdCounter++,
                    title = title,
                    description = description,
                    priority = priority
                )
                notesList.add(newNote)
                filteredNotesList.add(newNote)
                noteAdapter.notifyItemInserted(filteredNotesList.size - 1)
                recyclerView.smoothScrollToPosition(filteredNotesList.size - 1)
                Toast.makeText(this, R.string.message_note_added, Toast.LENGTH_SHORT).show()
                updateEmptyState()
            }
            .setNegativeButton(R.string.button_cancel, null)
            .create()
        
        // Aplicar animación al diálogo
        dialog.window?.attributes?.windowAnimations = R.style.DialogAnimation
        dialog.show()
    }
    
    private fun editNote(position: Int) {
        val note = filteredNotesList[position]
        val originalPosition = notesList.indexOf(note)
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_note_with_priority, null)
        val etTitle = dialogView.findViewById<EditText>(R.id.etNoteTitle)
        val etDescription = dialogView.findViewById<EditText>(R.id.etNoteDescription)
        val rgPriority = dialogView.findViewById<RadioGroup>(R.id.rgPriority)
        
        // Pre-llenar los campos con los datos actuales
        etTitle.setText(note.title)
        etDescription.setText(note.description)
        
        // Seleccionar la prioridad actual
        when (note.priority) {
            Priority.HIGH -> rgPriority.check(R.id.rbHigh)
            Priority.MEDIUM -> rgPriority.check(R.id.rbMedium)
            Priority.LOW -> rgPriority.check(R.id.rbLow)
        }
        
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_edit_note)
            .setView(dialogView)
            .setPositiveButton(R.string.button_save) { _, _ ->
                val title = etTitle.text.toString().trim()
                val description = etDescription.text.toString().trim()
                
                if (title.isEmpty()) {
                    Toast.makeText(this, R.string.message_title_empty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                if (description.isEmpty()) {
                    Toast.makeText(this, R.string.message_description_empty, Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }
                
                val priority = when (rgPriority.checkedRadioButtonId) {
                    R.id.rbHigh -> Priority.HIGH
                    R.id.rbMedium -> Priority.MEDIUM
                    R.id.rbLow -> Priority.LOW
                    else -> Priority.MEDIUM
                }
                
                // Actualizar la nota manteniendo el ID y timestamp
                val updatedNote = Note(
                    id = note.id,
                    title = title,
                    description = description,
                    timestamp = note.timestamp,
                    priority = priority
                )
                notesList[originalPosition] = updatedNote
                filteredNotesList[position] = updatedNote
                noteAdapter.notifyItemChanged(position)
                Toast.makeText(this, R.string.message_note_updated, Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton(R.string.button_cancel, null)
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
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        
        searchView.queryHint = getString(R.string.search_hint)
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }
            
            override fun onQueryTextChange(newText: String?): Boolean {
                filterNotes(newText ?: "")
                return true
            }
        })
        
        return true
    }
    
    private fun filterNotes(query: String) {
        filteredNotesList.clear()
        
        if (query.isEmpty()) {
            filteredNotesList.addAll(notesList)
        } else {
            val lowerCaseQuery = query.lowercase()
            notesList.forEach { note ->
                if (note.title.lowercase().contains(lowerCaseQuery) ||
                    note.description.lowercase().contains(lowerCaseQuery)) {
                    filteredNotesList.add(note)
                }
            }
        }
        
        noteAdapter.notifyDataSetChanged()
        
        // Mostrar mensaje si no hay resultados
        if (filteredNotesList.isEmpty() && query.isNotEmpty()) {
            Toast.makeText(this, R.string.message_no_results, Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun deleteNote(position: Int) {
        val note = filteredNotesList[position]
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_title_delete_note)
            .setMessage(getString(R.string.message_delete_confirm, note.title))
            .setPositiveButton(R.string.button_delete) { _, _ ->
                notesList.remove(note)
                filteredNotesList.removeAt(position)
                noteAdapter.notifyItemRemoved(position)
                Toast.makeText(this, R.string.message_note_deleted, Toast.LENGTH_SHORT).show()
                updateEmptyState()
            }
            .setNegativeButton(R.string.button_cancel, null)
            .show()
    }
    
    private fun shareNote(position: Int) {
        val note = filteredNotesList[position]
        val shareText = """
            📝 ${note.title}
            
            ${note.description}
            
            Prioridad: ${note.priority.displayName}
            Fecha: ${note.timestamp}
        """.trimIndent()
        
        val shareIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareText)
            type = "text/plain"
        }
        
        startActivity(Intent.createChooser(shareIntent, getString(R.string.share_chooser_title)))
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sort_by_date -> {
                sortNotesByDate()
                true
            }
            R.id.sort_by_title -> {
                sortNotesByTitle()
                true
            }
            R.id.sort_by_priority -> {
                sortNotesByPriority()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun sortNotesByDate() {
        filteredNotesList.sortByDescending { it.timestamp }
        noteAdapter.notifyDataSetChanged()
        Toast.makeText(this, R.string.sort_by_date_message, Toast.LENGTH_SHORT).show()
    }

    private fun sortNotesByTitle() {
        filteredNotesList.sortBy { it.title.lowercase() }
        noteAdapter.notifyDataSetChanged()
        Toast.makeText(this, R.string.sort_by_title_message, Toast.LENGTH_SHORT).show()
    }

    private fun sortNotesByPriority() {
        filteredNotesList.sortBy { 
            when (it.priority) {
                Priority.HIGH -> 0
                Priority.MEDIUM -> 1
                Priority.LOW -> 2
            }
        }
        noteAdapter.notifyDataSetChanged()
        Toast.makeText(this, R.string.sort_by_priority_message, Toast.LENGTH_SHORT).show()
    }
}