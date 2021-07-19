package com.example.noteapp.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes_table")
data class Note(
    @PrimaryKey
    var id: Int,
    @ColumnInfo
    var name: String,
    @ColumnInfo
    var content: String,
    @ColumnInfo
    var fav: Boolean
)