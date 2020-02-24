package com.example.homework2.ui.note

import android.os.Handler
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_note.*
import org.jetbrains.anko.alert
import org.koin.android.viewmodel.ext.android.viewModel
import com.example.homework2.R
import com.example.homework2.common.getColorInt
import com.example.homework2.data.entity.Note
import com.example.homework2.ui.base.BaseActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteActivity : BaseActivity<NoteViewState.Data, NoteViewState>() {

    companion object {
        private val EXTRA_NOTE = NoteActivity::class.java.name + "extra.NOTE"
        private const val DATE_TIME_FORMAT = "dd.MM.yy HH:mm"

        fun start(context: Context, noteId: String? = null) {
            val intent = Intent(context, NoteActivity::class.java)
            intent.putExtra(EXTRA_NOTE, noteId)
            context.startActivity(intent)
        }
    }

    private val handler = Handler()
    override val layoutRes = R.layout.activity_note
    override val model: NoteViewModel by viewModel()
    private var note: Note? = null
    var color = Note.Color.WHITE

    val textChahgeListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            saveNote
        }
    }


    val saveNote = Runnable {
        if (et_title.text == null || et_title.text!!.length < 3) return@Runnable

        note = note?.copy(
            title = et_title.text.toString(),
            text = et_body.text.toString(),
            lastChanged = Date(),
            color = color
        ) ?: Note(
            UUID.randomUUID().toString(),
            et_title.text.toString(),
            et_body.text.toString(),
            color
        )

        note?.let { model.save(it) }
    }

    override fun renderData(data: NoteViewState.Data) {
        if (data.isDeleted) finish()
        this.note = data.note
        initView()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val noteId = intent.getStringExtra(EXTRA_NOTE)

        noteId?.let {
            model.loadNote(it)
        }?.let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        submit_note_text_button.setOnClickListener {
            saveNote
            onBackPressed()
        }
    }

    override fun onStop() {
        saveNote.run()
        handler.removeCallbacks(saveNote)
        super.onStop()
    }

    fun initView() {
        note?.let { note ->
            removeEditListener()
            if (et_title.text.toString() != note.title) et_title.setText(note.title)
            if (et_body.text.toString() != note.text) et_body.setText(note.text)
            color = note.color
            toolbar.setBackgroundColor(note.color.getColorInt(this))
            supportActionBar?.title =
                SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(note.lastChanged)
        } ?: let {
            supportActionBar?.title = getString(R.string.new_note_title)
        }

        setEditListener()

        colorPicker.onColorClickListener = {
            toolbar.setBackgroundColor(color.getColorInt(this))
            color = it
            saveNote
        }
    }

    private fun removeEditListener() {
        et_title.removeTextChangedListener(textChahgeListener)
        et_body.removeTextChangedListener(textChahgeListener)
    }

    private fun setEditListener() {
        et_title.addTextChangedListener(textChahgeListener)
        et_body.addTextChangedListener(textChahgeListener)
    }

//    fun saveNote() {
//        if (et_title.text == null || et_title.text!!.length < 3) return
//
//        note = note?.copy(
//            title = et_title.text.toString(),
//            text = et_body.text.toString(),
//            lastChanged = Date(),
//            color = color
//        ) ?: Note(
//            UUID.randomUUID().toString(),
//            et_title.text.toString(),
//            et_body.text.toString(),
//            color
//        )
//
//        note?.let {
//            model.save(it)
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?) =
        menuInflater.inflate(R.menu.note, menu).let { true }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> onBackPressed().let { true }
        R.id.palette -> togglePalette().let { true }
        R.id.delete -> deleteNote().let { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun togglePalette() {
        if (colorPicker.isOpen) {
            colorPicker.close()
        } else {
            colorPicker.open()
        }
    }

    private fun deleteNote() {
        alert {
            messageResource = R.string.note_delete_message
            negativeButton(R.string.note_delete_cancel) { dialog -> dialog.dismiss() }
            positiveButton(R.string.note_delete_ok) { model.deleteNote() }
        }.show()
    }

}
