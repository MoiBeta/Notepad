package com.example.noteapp.viewModel

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.*
import com.example.noteapp.model.Note
import com.example.noteapp.model.NotesDao
import kotlinx.coroutines.*
import kotlin.collections.mutableListOf as mutableListOf

class NoteViewModel(private val notesDao: NotesDao, application: Application): AndroidViewModel(application) {
    private var _notesList: MutableLiveData<MutableList<Note>> = MutableLiveData(mutableListOf())
    val notesList get() = _notesList

    private var _currentNote: Note
    val currentNote get() = _currentNote

    private var _navigateToFragment = MutableLiveData<Int>()
    val navigateToFragment get() = _navigateToFragment

    init{
        _navigateToFragment.value = 0
        _currentNote = Note(-1,"","", false)
        }

    fun setNoteList(notes: MutableList<Note>){ _notesList.value = notes }

    fun setWorkingNote(id: Int){
        _currentNote = _notesList.value!![id]
    }

    /* A ViewModelScope is defined for each ViewModel in your app. Any coroutine launched in this scope is
    automatically canceled if the ViewModel is cleared. Coroutines are useful here for when you have work
    that needs to be done only if the ViewModel is active. For example, if you are computing some data for
    a layout, you should scope the work to the ViewModel so that if the ViewModel is cleared, the work is
    canceled automatically to avoid consuming resources.*/

    fun saveNote(){
        if(_notesList.value!!.isEmpty()){
            _notesList.value!!.add(_currentNote)
            viewModelScope.launch {
                addNoteToDatabase(_currentNote)
            }
            _currentNote = Note(-1,"","", false)
        }else {
            try {
                _notesList.value!![_currentNote.id] = _currentNote
                viewModelScope.launch {
                    updateNoteOnDatabase(_currentNote)
                }
                _currentNote = Note(-1,"","", false)
            } catch (e: IndexOutOfBoundsException){
                _notesList.value!!.add(_currentNote)
                viewModelScope.launch {
                    addNoteToDatabase(_currentNote)
                }
                _currentNote = Note(-1,"","", false)
            }

        }
    }

    fun setNoteNameAndContent(name: String, content: String){
        _currentNote.name = name
        _currentNote.content = content
    }

    fun deleteNote(note: Note){
        viewModelScope.launch{
            removeNoteFromDatabase(_notesList.value!![note.id])
        }
    }

    fun changeFav(note: Note){
        _notesList.value!![note.id].fav = !_notesList.value!![note.id].fav
        viewModelScope.launch {
            updateNoteOnDatabase(_notesList.value!![note.id])
        }
    }

    private suspend fun addNoteToDatabase(note: Note) {
        withContext(Dispatchers.IO){
            try{
                notesDao.insert(note)
            } catch (e: SQLiteConstraintException){
                updateNoteOnDatabase(note)
            }
        }
    }

    private suspend fun updateNoteOnDatabase(note: Note){
        withContext(Dispatchers.IO){
                notesDao.update(note)
        }
    }

    private suspend fun removeNoteFromDatabase(note: Note){
        withContext(Dispatchers.IO){ notesDao.delete(note) }
    }

    fun newNote(){
        _currentNote = if(_notesList.value!!.isNotEmpty()){
            Note(_notesList.value!!.lastIndex +1, "","",false)
        } else {
            Note(0,"", "",false)
        }
        _navigateToFragment.value = 2
    }

    fun onNoteClicked(note: Note){
        setWorkingNote(note.id)
        _navigateToFragment.value = 1
    }

    fun onNoteEditRequest(){
        _navigateToFragment.value = 2
    }

    fun onNoteSaved(){
        _navigateToFragment.value = 0
    }
}