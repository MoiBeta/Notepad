package com.example.noteapp.viewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.noteapp.model.NotesDao

class NoteViewModelFactory(private val notesDao: NotesDao, private val application: Application) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(notesDao, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}