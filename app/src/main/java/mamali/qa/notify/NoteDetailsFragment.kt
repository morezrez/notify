package mamali.qa.notify

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.databinding.FragmentNoteDetailsBinding
import mamali.qa.notify.databinding.FragmentNotesBinding
import mamali.qa.notify.viewModel.NoteDetailViewModel
import mamali.qa.notify.viewModel.NoteDetailViewModelFactory


class NoteDetailsFragment : Fragment() {
    lateinit var binding: FragmentNoteDetailsBinding

    private val noteDetailViewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModelFactory((requireActivity().application as NotesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val title = binding.edtNoteTitle.text.toString()
                val desc = binding.edtNoteDetail.text.toString()
                val kind = "Note"
                val bundle = arguments
                val args = bundle?.let { NotesFragmentArgs.fromBundle(it) }
                val parent = args?.parent
                val parentId = args?.parentId
                parent?.let {
                    parentId?.let { it1 ->
                        noteDetailViewModel.getInput(
                            title, desc, kind, it,
                            it1
                        )
                    }
                }
                super.remove()
                requireActivity().onBackPressed()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this,  // LifecycleOwner
            callback
        )
    }

}