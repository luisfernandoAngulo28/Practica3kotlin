package com.example.practica3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class NoteAdapter(
    private val notes: MutableList<Note>,
    private val onDeleteClick: (Int) -> Unit,
    private val onEditClick: (Int) -> Unit
) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    private var lastPosition = -1

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvNoteTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvNoteDescription)
        val tvTimestamp: TextView = itemView.findViewById(R.id.tvNoteTimestamp)
        val btnDelete: MaterialButton = itemView.findViewById(R.id.btnDelete)
        
        // Obtener referencia al CardView para cambiar color de fondo
        private val cardView = itemView as com.google.android.material.card.MaterialCardView
        
        fun setCardBackgroundColor(color: Int) {
            cardView.setCardBackgroundColor(color)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.tvTitle.text = note.title
        holder.tvDescription.text = note.description
        holder.tvTimestamp.text = note.getFormattedDate()
        
        // Aplicar color pastel al fondo de la tarjeta
        holder.setCardBackgroundColor(note.color)
        
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }
        
        // Long press para editar
        holder.itemView.setOnLongClickListener {
            onEditClick(position)
            true
        }
        
        // Aplicar animación de entrada
        if (position > lastPosition) {
            val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.slide_in_right)
            holder.itemView.startAnimation(animation)
            lastPosition = position
        }
    }

    override fun getItemCount(): Int = notes.size

    fun addNote(note: Note) {
        notes.add(note)
        notifyItemInserted(notes.size - 1)
    }

    fun removeNote(position: Int) {
        notes.removeAt(position)
        notifyItemRemoved(position)
    }
}
