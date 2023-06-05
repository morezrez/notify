package mamali.qa.notify.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import mamali.qa.notify.*
import mamali.qa.notify.databinding.FragmentNotesBinding
import mamali.qa.notify.dialogs.NewFileCustomDialog
import mamali.qa.notify.dialogs.UpdateFileCustomDialog
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.utils.showPopUpDelete
import mamali.qa.notify.utils.showPopUpUpdateAndDelete

@AndroidEntryPoint
class NotesFragment : Fragment(), AdapterCommunicatorInterface {

    private val noteViewModel: NoteViewModel by viewModels()
    var popup: PopupWindow? = null
    private lateinit var binding: FragmentNotesBinding
    private lateinit var recyclerAdapter: NoteListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerAdapter = NoteListAdapter(this)
        binding.notesRecyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }
        //get arguments
        val bundle = arguments
        val args = bundle?.let { NotesFragmentArgs.fromBundle(it) }
        val parent = args?.parent.toString()
        val parentId = args?.parentId!!
        noteViewModel.getNotes(parentId)

        registerObserver()

        //making difference in toolbar for root and inside folders
        updateToolbar(parent)

        binding.btnFloatingAdd.setOnClickListener {
            btnFloatingAddClicked()
        }

        binding.btnFloatingAddNote.setOnClickListener {
            btnFloatingAddNoteClicked(parentId, parent)
        }

        binding.btnFloatingAddFile.setOnClickListener {
            btnFloatingAddFileClicked(parentId, parent)
        }

        binding.icBackToolbar.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.toolbarOptionBlubIcon.setOnClickListener {
            toolbarOptionBlubIconClicked(parentId)
        }
    }

    override fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView) {
        popup?.dismiss()
        popup = context?.showPopUpDelete {
            noteViewModel.deleteNote(id)
            popup?.dismiss()
        }
        popup?.isOutsideTouchable = true
        popup?.isFocusable
        popup?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.showAsDropDown(imgOptionBlub)
    }

    override fun fragmentTransferFile(parent: String, parentId: Int) {
        val action = NotesFragmentDirections.actionNotesFragmentSelf(parent, parentId)
        findNavController().navigate(action)
    }

    override fun fragmentTransferNote(id: Int, name: String, desc: String?, parent: String) {
        val action = NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(
            id,
            name,
            desc,
            parent
        )
        findNavController().navigate(action)
    }

    private fun btnFloatingAddClicked() {
        noteViewModel.addFloatingButtonOnClick()
    }

    private fun btnFloatingAddNoteClicked(parentId: Int, parent: String) {
        val action =
            parentId.let { it1 ->
                NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(
                    it1, null, null, parent
                )
            }
        action.let { it1 -> findNavController().navigate(it1) }
    }

    private fun btnFloatingAddFileClicked(parentId: Int, parent: String) {
        NewFileCustomDialog(parent.toString(), parentId){note->noteViewModel.insert(note)}.show(
            requireActivity().supportFragmentManager,
            "MyCustomFragment"
        )
    }

    private fun toolbarOptionBlubIconClicked(parentId: Int) {
        val popup2: PopupWindow? = context?.showPopUpUpdateAndDelete()
        popup2?.contentView?.apply {

            findViewById<LinearLayout>(R.id.linear_delete).setOnClickListener {
                noteViewModel.deleteNote(parentId)
                activity?.onBackPressed()
                popup2.dismiss()
            }

            findViewById<LinearLayout>(R.id.linear_update).setOnClickListener {
                UpdateFileCustomDialog(parentId, binding.toolbarTitleTxt){id: Int?, name: String -> noteViewModel.updateFile(id,name) }.show(
                    requireActivity().supportFragmentManager,
                    "myUpadteDialog"
                )
                popup2.dismiss()
            }
        }

        popup2?.isOutsideTouchable = true
        popup2?.isFocusable = true
        popup2?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup2?.showAsDropDown(binding.toolbarOptionBlubIcon)
    }

    private fun updateToolbar(parent: String) {
        noteViewModel.updateToolbar(
            binding.toolbarTitleTxt,
            binding.icBackToolbar,
            binding.toolbarOptionBlubIcon,
            parent
        )
    }

    private fun registerObserver() {
        //submit recycler view list
        noteViewModel.listLiveData.observe(viewLifecycleOwner, Observer { notes ->
            notes?.let { recyclerAdapter.submitList(it.reversed()) }
        })
        //observe visibility of floating buttons
        noteViewModel.floatingButtonVisibilityLiveData.observe(viewLifecycleOwner) { isVisible ->
            binding.btnFloatingAddNote.isVisible = isVisible
            binding.btnFloatingAddFile.isVisible = isVisible
        }
    }
}