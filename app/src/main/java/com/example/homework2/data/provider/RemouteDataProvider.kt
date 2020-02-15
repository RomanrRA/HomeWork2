package com.example.homework2.data.provider

import androidx.lifecycle.LiveData
import com.example.homework2.data.entity.Note
import com.example.homework2.data.model.NoteResult

interface RemouteDataProvider {
    fun subscripeAllNotes(): LiveData<NoteResult>
    fun getNoteById(id: String): LiveData<NoteResult>
    fun saveNote(note: Note): LiveData<NoteResult>
}