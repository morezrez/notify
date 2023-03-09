package mamali.qa.notify

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mamali.qa.notify.databinding.FragmentNotesBinding

class NotesFragment : Fragment(), NoteClickDeleteInterface {


    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentNotesBinding.inflate(inflater, container, false)
        val recyclerAdapter = NoteListAdapter(this, this)

        binding.notesRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
//get arguments
        val bundle = arguments
        val args = bundle?.let { NotesFragmentArgs.fromBundle(it) }
        val parent = args?.parent
        val parentId = args?.parentId
        noteViewModel.getNotes(parentId)

        noteViewModel.listLiveData.observe(requireActivity(), Observer { notes ->
            notes?.let { recyclerAdapter.submitList(it) }
        })

        noteViewModel.updateToolbar(
            binding.toolbarTitleTxt,
            binding.icBackToolbar,
            binding.toolbarOptionBlubIcon,
            args?.parent.toString()
        )

        binding.btnFloatingAdd.setOnClickListener {
            noteViewModel.addFloatingButtonOnClick(
                binding.btnFloatingAddFile,
                binding.btnFloatingAddNote
            )
        }

        binding.btnFloatingAddNote.setOnClickListener {
            val action =
                parentId?.let { it1 ->
                    NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(
                        it1, null,null,parent)
                }
            action?.let { it1 -> findNavController().navigate(it1) }
        }

        binding.btnFloatingAddFile.setOnClickListener {
            NewFileCustomDialog(parent.toString(), parentId).show(
                requireActivity().supportFragmentManager,
                "MyCustomFragment"
            )

        }

        binding.icBackToolbar.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.toolbarOptionBlubIcon.setOnClickListener {
            val popup = PopupMenu(context, binding.toolbarOptionBlubIcon)
            popup.inflate(R.menu.delete_update_menu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.delete -> {
                        noteViewModel.deleteNote(parentId)
                        activity?.onBackPressed()
                    }
                    R.id.update -> {
                        UpdateFileCustomDialog(parentId,binding.toolbarTitleTxt).show(requireActivity().supportFragmentManager, "myUpadteDialog")
                    }
                }
                true
            })
            popup.show()
        }

        return binding.root
    }

    override fun onDeleteIconClick(id: Int) {
        noteViewModel.deleteNote(id)
    }


}