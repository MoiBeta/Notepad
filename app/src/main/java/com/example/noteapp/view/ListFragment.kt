package com.example.noteapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.noteapp.*
import com.example.noteapp.databinding.FragmentlistBinding
import com.example.noteapp.model.NotesDatabase
import com.example.noteapp.viewModel.NoteViewModel
import com.example.noteapp.viewModel.NoteViewModelFactory

class ListFragment: Fragment(), NoteAdapter.OnItemClickListener {
    private var _binding: FragmentlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : NoteAdapter
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentlistBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = NotesDatabase.getInstance(application).notesDao
        val viewModelFactory = NoteViewModelFactory(dataSource, application)
        noteViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(NoteViewModel::class.java)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var noteList = noteViewModel.notesList.value ?: mutableListOf()
        adapter = NoteAdapter(noteList, this)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.button.setOnClickListener {
            noteViewModel.newNote()
            findNavController().navigate(R.id.action_listFragment_to_editNoteFragment)
        }
        noteViewModel.notesList.observe(viewLifecycleOwner, Observer{
            noteList = it
            adapter.notifyDataSetChanged()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        noteViewModel.setWorkingNote(position)
        findNavController().navigate(R.id.action_listFragment_to_viewNoteFragment)
    }

    override fun onFavClick(position: Int) {
        noteViewModel.notesList.value!![position].fav = !noteViewModel.notesList.value!![position].fav
        adapter.notifyItemChanged(position)
    }

    override fun onDelClick(position: Int) {
        noteViewModel.deleteNote(position)
        adapter.notifyDataSetChanged()
    }
}