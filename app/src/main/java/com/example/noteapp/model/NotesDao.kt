package com.example.noteapp.model

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.noteapp.model.Note

@Dao
interface NotesDao {
    @Insert
    fun insert(note: Note)
    @Delete
    fun delete(note: Note)
    @Update
    fun update(note: Note)
    @Query("SELECT * FROM notes_table")
    fun getNotes(): LiveData<List<Note>?>
    //Needs to be livedata so Room can maintain it updated
}