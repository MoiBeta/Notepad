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
import com.example.noteapp.model.Note
import com.example.noteapp.model.NotesDao
import com.example.noteapp.model.NotesDatabase
import com.example.noteapp.viewModel.NoteViewModel
import com.example.noteapp.viewModel.NoteViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFragment: Fragment(){
    private var _binding: FragmentlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : NoteAdapter
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var dataSource: NotesDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentlistBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        dataSource = NotesDatabase.getInstance(application).notesDao
        val viewModelFactory = NoteViewModelFactory(dataSource, application)
        noteViewModel = ViewModelProvider(this.requireActivity(), viewModelFactory).get(NoteViewModel::class.java)
        binding.noteViewModel = noteViewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = NoteAdapter(noteViewModel)

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        dataSource.getNotes().observe(viewLifecycleOwner, Observer{
            adapter.submitList(it)
            noteViewModel.setNoteList(it as MutableList<Note>)
        })

        noteViewModel.notesList.observe(viewLifecycleOwner,{
            adapter.notifyDataSetChanged()
        })

        noteViewModel.navigateToFragment.observe(viewLifecycleOwner,{
            if(it==2){
                findNavController().navigate(R.id.action_listFragment_to_editNoteFragment)
            } else if(it==1){
                findNavController().navigate(R.id.action_listFragment_to_viewNoteFragment)
            }
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}