package com.example.homework2.ui.main

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import com.example.homework2.data.NotesRepository
import com.example.homework2.data.entity.Note
import com.example.homework2.data.model.NoteResult
import com.example.homework2.ui.base.BaseViewModel

class MainViewModel(notesRepository: NotesRepository) :
    BaseViewModel<List<Note>?, MainViewState>() {

    private val notesObserver = object : Observer<NoteResult> {
        override fun onChanged(t: NoteResult?) {
            t ?: return

            when (t) {
                is NoteResult.Success<*> -> {
                    viewStateLiveData.value = MainViewState(notes = t.data as? List<Note>)
                }
                is NoteResult.Error -> {
                    viewStateLiveData.value = MainViewState(error = t.error)
                }
            }
        }
    }

    private val repositoryNotes = notesRepository.getNotes()

    init {
        viewStateLiveData.value = MainViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    @VisibleForTesting
    override public fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
        super.onCleared()
    }

}