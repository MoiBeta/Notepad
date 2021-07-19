package com.example.noteapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.noteapp.*
import com.example.noteapp.databinding.NoteviewfragmentBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.example.noteapp.viewModel.NoteViewModelFactory

class ViewNoteFragment: Fragment() {
    private var binding: NoteviewfragmentBinding? = null
    private lateinit var noteViewModel: NoteViewModel
    private lateinit var currentNote: Note

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = NoteviewfragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        noteViewModel = ViewModelProvider(this.requireActivity()).get(NoteViewModel::class.java)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentNote = noteViewModel.currentNote
        binding?.apply{
            title.text = noteViewModel.currentNote.name
            content.text = noteViewModel.currentNote.content
        }
        binding?.root?.setOnClickListener {
            findNavController().navigate(R.id.action_viewNoteFragment_to_editNoteFragment)
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}