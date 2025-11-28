package com.example.notepadandro

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepadandro.databinding.ActivityMainBinding
import com.example.notepadandro.ui.NoteAdapter
import com.example.notepadandro.viewmodel.NoteViewModel
import com.example.notepadandro.viewmodel.NoteViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((application as NoteApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val adapter = NoteAdapter { note ->
            val intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
            intent.putExtra(NoteDetailActivity.EXTRA_NOTE_ID, note.id)
            startActivity(intent)
        }

        binding.contentMain.recyclerView.adapter = adapter
        binding.contentMain.recyclerView.layoutManager = LinearLayoutManager(this)

        noteViewModel.allNotes.observe(this) {
            notes -> notes?.let { adapter.submitList(it) }
        }

        binding.fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NoteDetailActivity::class.java)
            startActivity(intent)
        }
    }
}