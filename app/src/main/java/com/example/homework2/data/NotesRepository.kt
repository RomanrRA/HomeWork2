package com.example.homework2.data

import com.example.homework2.data.entity.Note
import com.example.homework2.data.provider.FirestoreProvaider
import com.example.homework2.data.provider.RemouteDataProvider

object NotesRepository {
    private val remoteProvaider: RemouteDataProvider = FirestoreProvaider()

    fun getNotes() = remoteProvaider.subscripeAllNotes()
    fun saveNote(note: Note) = remoteProvaider.saveNote(note)
    fun getNouteById(id: String) = remoteProvaider.getNoteById(id)
}