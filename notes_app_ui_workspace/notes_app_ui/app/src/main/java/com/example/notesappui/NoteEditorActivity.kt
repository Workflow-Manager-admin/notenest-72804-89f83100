package com.example.notesappui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.notesappui.databinding.ActivityNoteEditorBinding
import java.util.*

// PUBLIC_INTERFACE
class NoteEditorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteEditorBinding
    private lateinit var viewModel: NotesViewModel
    private var editingNote: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteEditorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = viewModels<NotesViewModel> { NotesViewModelFactory(application) }.value

        val noteId = intent.getLongExtra("NOTE_ID", -1)
        if (noteId != -1L) {
            editingNote = viewModel.getNoteById(noteId)
            editingNote?.let { note ->
                binding.titleInput.setText(note.title)
                binding.contentInput.setText(note.content)
                when (note.category) {
                    "Personal" -> binding.chipPersonal.isChecked = true
                    "Work" -> binding.chipWork.isChecked = true
                    "Ideas" -> binding.chipIdeas.isChecked = true
                }
            }
        }

        binding.saveBtn.setOnClickListener {
            val title = binding.titleInput.text?.toString()?.trim() ?: ""
            val content = binding.contentInput.text?.toString()?.trim() ?: ""
            val category = when {
                binding.chipPersonal.isChecked -> "Personal"
                binding.chipWork.isChecked -> "Work"
                binding.chipIdeas.isChecked -> "Ideas"
                else -> "Personal"
            }
            if (title.isNotBlank() || content.isNotBlank()) {
                val note = editingNote?.copy(
                    title = title,
                    content = content,
                    category = category
                ) ?: Note(
                    id = editingNote?.id ?: System.currentTimeMillis(),
                    title = title,
                    content = content,
                    category = category
                )
                viewModel.addOrUpdateNote(note)
            }
            finish()
        }

        binding.topBar.setNavigationOnClickListener { finish() }
    }
}
