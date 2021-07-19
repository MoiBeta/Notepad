package com.example.noteapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.model.Note
import com.example.noteapp.R
import com.example.noteapp.databinding.NoteitemBinding

class NoteAdapter(
    private val noteList: MutableList<Note>,
    private var mListener: OnItemClickListener
): RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {
    inner class NoteViewHolder(private val itemBinding: NoteitemBinding) : RecyclerView.ViewHolder(itemBinding.root){
        fun bind(note: Note){
            itemBinding.name.text = note.name
            if(note.fav){
                itemBinding.fav.setImageResource(R.drawable.ic_star)
            } else {
                itemBinding.fav.setImageResource(R.drawable.ic_star_border)
            }
            val position = adapterPosition
            itemBinding.item.setOnClickListener {
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position)
                }
            }
            itemBinding.fav.setOnClickListener{
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onFavClick(position)
                }
            }
            itemBinding.del.setOnClickListener {
                if(position != RecyclerView.NO_POSITION) {
                    mListener.onDelClick(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemBinding = NoteitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val currentNote: Note = noteList[position]
        currentNote.id = noteList[position].id
        holder.bind(currentNote)
    }

    override fun getItemCount(): Int = noteList.size

    interface OnItemClickListener {
        fun onItemClick(position: Int)
        fun onFavClick(position: Int)
        fun onDelClick(position: Int)
    }
}