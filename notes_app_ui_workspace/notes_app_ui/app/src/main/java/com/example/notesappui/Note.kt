package com.example.notesappui

// PUBLIC_INTERFACE
data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val category: String,
    val pinned: Boolean = false
)
