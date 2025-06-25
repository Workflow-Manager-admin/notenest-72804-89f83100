package com.example.notesappui

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.notesappui.databinding.ItemNoteBinding

// PUBLIC_INTERFACE
class NotesAdapter(
    private val listener: OnItemClickListener
) : ListAdapter<Note, NotesAdapter.NoteViewHolder>(NoteDiffCallback()) {

    interface OnItemClickListener {
        fun onNoteClick(note: Note)
        fun onNoteDelete(note: Note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NoteViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(note: Note) {
            binding.title.text = note.title
            binding.content.text = note.content
            binding.category.text = note.category
            // Bold the title if note is pinned (for extensibility)
            binding.title.setTypeface(null, if (note.pinned) Typeface.BOLD else Typeface.NORMAL)
            binding.container.setOnClickListener { listener.onNoteClick(note) }

            binding.deleteIcon.setOnClickListener { listener.onNoteDelete(note) }

            val colorRes = when (note.category) {
                "Personal" -> R.color.category_personal
                "Work" -> R.color.category_work
                "Ideas" -> R.color.category_ideas
                else -> R.color.primary
            }
            binding.category.setTextColor(ContextCompat.getColor(binding.root.context, colorRes))
        }
    }
}

private class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem.id == newItem.id
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = oldItem == newItem
}
