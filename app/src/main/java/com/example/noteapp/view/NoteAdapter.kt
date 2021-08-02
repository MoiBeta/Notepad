package com.example.noteapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.model.Note
import com.example.noteapp.R
import com.example.noteapp.databinding.NoteitemBinding
import com.example.noteapp.viewModel.NoteViewModel

class NoteAdapter(val noteViewModel: NoteViewModel): ListAdapter<Note,NoteAdapter.NoteViewHolder>(NoteDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote: Note = getItem(position)
        holder.bind(currentNote, noteViewModel)
    }

    class NoteViewHolder private constructor(val itemBinding: NoteitemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(note: Note, noteViewModel: NoteViewModel) {
            itemBinding.note = note
            itemBinding.viewModel = noteViewModel
            itemBinding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): NoteViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = NoteitemBinding.inflate(layoutInflater, parent, false)

                return NoteViewHolder(binding)
            }
        }
    }

    class NoteDiffCallback : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
            return oldItem == newItem
        }
    }
}