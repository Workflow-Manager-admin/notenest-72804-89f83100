package com.example.notesappui

import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

// PUBLIC_INTERFACE
class NotesRepository(app: Application) {
    private val prefs = app.getSharedPreferences("notes_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getNotes(): List<Note> {
        val raw = prefs.getString("notes", "[]") ?: "[]"
        val type = object : TypeToken<List<Note>>() {}.type
        return gson.fromJson(raw, type)
    }

    fun saveNotes(notes: List<Note>) {
        prefs.edit().putString("notes", gson.toJson(notes)).apply()
    }

    fun addOrUpdateNote(note: Note) {
        val notes = getNotes().toMutableList()
        val index = notes.indexOfFirst { it.id == note.id }
        if (index >= 0) notes[index] = note else notes.add(note)
        saveNotes(notes)
    }

    fun deleteNote(note: Note) {
        val notes = getNotes().filter { it.id != note.id }
        saveNotes(notes)
    }
}
