package com.example.notesappui

import android.app.Application
import androidx.lifecycle.*

// PUBLIC_INTERFACE
class NotesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = NotesRepository(application)
    private val _allNotes = MutableLiveData<List<Note>>(repo.getNotes())
    private val _query = MutableLiveData<String>("")
    private val _category = MutableLiveData<String?>(null)

    val filteredNotes: LiveData<List<Note>> = MediatorLiveData<List<Note>>().apply {
        addSource(_allNotes) { filterData() }
        addSource(_query) { filterData() }
        addSource(_category) { filterData() }
        fun filterData() {
            val notes = _allNotes.value ?: emptyList()
            val q = _query.value?.trim()?.lowercase().orEmpty()
            val cat = _category.value
            value = notes.filter {
                (cat == null || it.category == cat) &&
                (q.isBlank() || it.title.lowercase().contains(q) || it.content.lowercase().contains(q))
            }
        }
    }

    fun refresh() {
        _allNotes.value = repo.getNotes()
    }

    // PUBLIC_INTERFACE
    fun addOrUpdateNote(note: Note) {
        repo.addOrUpdateNote(note)
        refresh()
    }

    // PUBLIC_INTERFACE
    fun deleteNote(note: Note) {
        repo.deleteNote(note)
        refresh()
    }

    // PUBLIC_INTERFACE
    fun setQuery(query: String) {
        _query.value = query
    }

    // PUBLIC_INTERFACE
    fun setCategory(category: String?) {
        _category.value = category
    }

    // PUBLIC_INTERFACE
    fun getNoteById(id: Long): Note? {
        return repo.getNotes().find { it.id == id }
    }
}

// Boilerplate for ViewModelProvider.Factory
class NotesViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NotesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(app) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
