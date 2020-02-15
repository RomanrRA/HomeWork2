package com.example.homework2.data.provider

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homework2.data.entity.Note
import com.example.homework2.data.model.NoteResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QueryDocumentSnapshot

class FirestoreProvaider : RemouteDataProvider {


    companion object {
        private const val NOTES_COLLECTION = "notes"
    }

    private val store : FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    private val noteReference = store.collection(NOTES_COLLECTION)

    override fun subscripeAllNotes(): LiveData<NoteResult> {

        val result = MutableLiveData<NoteResult>()
        noteReference.addSnapshotListener { snapshot, e ->
            e?.let {
                result.value = NoteResult.Error(e)
            } ?: let {
                snapshot?.let { snapshot ->
                    val notes = mutableListOf<Note>()
                    for (doc: QueryDocumentSnapshot in snapshot) {
                        notes.add(doc.toObject(Note::class.java))
                    }
                    result.value = NoteResult.Success(notes)
                }
            }
        }
        return result
    }

    override fun getNoteById(id: String): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        noteReference.document(id).get().addOnSuccessListener { snapshot ->
            result.value = NoteResult.Success(snapshot.toObject(Note::class.java))
        }.addOnFailureListener {
            result.value = NoteResult.Error(it)
        }

        return result
    }

    override fun saveNote(note: Note): LiveData<NoteResult> {
        val result = MutableLiveData<NoteResult>()
        noteReference.document(note.id).set(note).addOnSuccessListener {
            result.value = NoteResult.Success(note)
        }.addOnFailureListener {
            result.value = NoteResult.Error(it)
        }

        return result
    }


}