package com.example.homework2.data

import com.example.homework2.data.entity.Note
import com.example.homework2.data.provider.RemouteDataProvider

class NotesRepository(val remoteProvider: RemouteDataProvider) {
    fun getNotes() = remoteProvider.subscribeToAllNotes()
    fun saveNote(note: Note) = remoteProvider.saveNote(note)
    fun getNoteById(id: String) = remoteProvider.getNoteById(id)
    fun getCurrentUser() = remoteProvider.getCurrentUser()
    fun deleteNote(id: String) = remoteProvider.deleteNote(id)
}