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


@Suppress("SENSELESS_COMPARISON")
class NoteDetailsFragment : Fragment() {
    lateinit var binding: FragmentNoteDetailsBinding

    private val noteDetailViewModel: NoteDetailViewModel by viewModels {
        NoteDetailViewModelFactory((requireActivity().application as NotesApplication).repository)
    }
    lateinit var name: String
    lateinit var desc: String
    lateinit var title: String
    lateinit var desc2: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)


        val args = getArgs()
        name = args?.name.toString()
        desc = args?.desc.toString()
        if (name != null && desc != null && name != "null" && desc != "null") {
            binding.edtNoteTitle.setText(name)
            binding.edtNoteDetail.setText(desc)
        }


        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                title = binding.edtNoteTitle.text.toString()
                desc2 = binding.edtNoteDetail.text.toString()
                val kind = "Note"
                val args = getArgs()
                val parent = args?.parent
                val parentId = args?.parentId

                if (name != null && desc != null && parentId != null && name != "null" && desc != "null") {
                    noteDetailViewModel.updateNote(title, desc2, parentId)
                } else if (binding.edtNoteTitle.text.isNotEmpty() && binding.edtNoteDetail.text.isNotEmpty()) {
                    parent?.let {
                        parentId?.let { it1 ->
                            noteDetailViewModel.getInput(
                                title, desc2, kind, it,
                                it1
                            )
                        }
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

    fun getArgs(): NoteDetailsFragmentArgs? {
        val bundle = arguments
        val args = bundle?.let { NoteDetailsFragmentArgs.fromBundle(it) }

        return args
    }

}

