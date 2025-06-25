package com.example.notesappui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notesappui.databinding.ActivityMainBinding

// PUBLIC_INTERFACE
class MainActivity : AppCompatActivity(), NotesAdapter.OnItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NotesViewModel
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, NotesViewModelFactory(application))[NotesViewModel::class.java]
        adapter = NotesAdapter(this)

        binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.notesRecyclerView.adapter = adapter

        viewModel.filteredNotes.observe(this) { notes ->
            adapter.submitList(notes)
            binding.emptyView.visibility = if (notes.isEmpty()) android.view.View.VISIBLE else android.view.View.GONE
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this, NoteEditorActivity::class.java)
            startActivity(intent)
        }

        binding.searchInput.addTextChangedListener {
            viewModel.setQuery(it?.toString() ?: "")
        }

        binding.categoryChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedCat = when (checkedId) {
                R.id.chip_all -> null
                R.id.chip_personal -> "Personal"
                R.id.chip_work -> "Work"
                R.id.chip_ideas -> "Ideas"
                else -> null
            }
            viewModel.setCategory(selectedCat)
        }
    }

    // PUBLIC_INTERFACE
    override fun onNoteClick(note: Note) {
        val intent = Intent(this, NoteEditorActivity::class.java)
        intent.putExtra("NOTE_ID", note.id)
        startActivity(intent)
    }

    // PUBLIC_INTERFACE
    override fun onNoteDelete(note: Note) {
        viewModel.deleteNote(note)
    }
}
