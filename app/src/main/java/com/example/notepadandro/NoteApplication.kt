package com.example.notepadandro

import android.app.Application
import com.example.notepadandro.data.AppDatabase
import com.example.notepadandro.data.NoteRepository

class NoteApplication : Application() {
    private val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { NoteRepository(database.noteDao()) }
}