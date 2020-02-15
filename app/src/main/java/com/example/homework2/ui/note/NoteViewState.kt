package com.example.homework2.ui.note

import com.example.homework2.data.entity.Note
import com.example.homework2.ui.base.BaseViewState

class NoteViewState(note : Note? = null, error : Throwable? = null) : BaseViewState<Note?>(note, error)