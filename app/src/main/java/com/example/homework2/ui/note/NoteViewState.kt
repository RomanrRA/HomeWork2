package com.example.homework2.ui.note

import com.example.homework2.data.entity.Note
import com.example.homework2.ui.base.BaseViewState

class NoteViewState(data: Data = Data(), error: Throwable? = null) :
    BaseViewState<NoteViewState.Data>(data, error) {
    data class Data(val isDeleted: Boolean = false, val note: Note? = null)
}