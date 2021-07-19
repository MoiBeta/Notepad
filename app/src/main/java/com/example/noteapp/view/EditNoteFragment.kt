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
import com.example.noteapp.databinding.NoteeditfragmentBinding
import com.example.noteapp.model.Note
import com.example.noteapp.viewModel.NoteViewModel
import com.example.noteapp.viewModel.NoteViewModelFactory

class EditNoteFragment: Fragment() {
    private var binding: NoteeditfragmentBinding? = null
    private lateinit var currentNote: Note
    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val fragmentBinding = NoteeditfragmentBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        noteViewModel = ViewModelProvider(this.requireActivity()).get(NoteViewModel::class.java)
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        currentNote = noteViewModel.currentNote
        binding?.apply {
            title.setText(noteViewModel.currentNote.name)
            content.setText(noteViewModel.currentNote.content)
            button.setOnClickListener {
                noteViewModel.setNoteNameAndContent(binding?.title?.text.toString(), binding?.content?.text.toString())
                noteViewModel.saveNote()
                findNavController().navigate(R.id.action_editNoteFragment_to_listFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}