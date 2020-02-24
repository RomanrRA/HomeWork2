package com.example.homework2.data.provider

import androidx.lifecycle.LiveData
import com.example.homework2.data.entity.Note
import com.example.homework2.data.entity.User
import com.example.homework2.data.model.NoteResult

interface RemouteDataProvider {
    fun subscribeToAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
    fun getCurrentUser(): LiveData<User?>
    fun deleteNote(noteId: String): LiveData<NoteResult>
}