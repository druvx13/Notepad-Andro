package com.example.notepadandro

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.notepadandro.data.Note
import com.example.notepadandro.databinding.ActivityNoteDetailBinding
import com.example.notepadandro.viewmodel.NoteViewModel
import com.example.notepadandro.viewmodel.NoteViewModelFactory

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private var currentNote: Note? = null
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val noteId = intent.getIntExtra(EXTRA_NOTE_ID, -1)

        if (noteId != -1) {
            noteViewModel.getNoteById(noteId).observe(this, Observer { note ->
                note?.let {
                    currentNote = it
                    binding.editTextTitle.setText(it.title)
                    binding.editTextContent.setText(it.content)
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_note_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_save -> {
                saveNote()
                true
            }
            R.id.action_delete -> {
                deleteNote()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun saveNote() {
        val title = binding.editTextTitle.text.toString().trim()
        val content = binding.editTextContent.text.toString().trim()

        if (title.isEmpty() && content.isEmpty()) {
            Toast.makeText(this, R.string.cannot_save_empty_note, Toast.LENGTH_SHORT).show()
            return
        }

        val noteToSave = currentNote
        if (noteToSave != null) {
            noteToSave.title = title
            noteToSave.content = content
            noteViewModel.update(noteToSave)
        } else {
            noteViewModel.insert(Note(title = title, content = content))
        }
        finish()
    }

    private fun deleteNote() {
        currentNote?.let {
            noteViewModel.delete(it)
            finish()
        }
    }

    companion object {
        const val EXTRA_NOTE_ID = "com.example.notepadandro.NOTE_ID"
    }
}