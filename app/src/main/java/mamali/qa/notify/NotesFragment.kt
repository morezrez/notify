package mamali.qa.notify

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import mamali.qa.notify.databinding.FragmentNotesBinding
import mamali.qa.notify.dialogs.NewFileCustomDialog
import mamali.qa.notify.dialogs.UpdateFileCustomDialog
import mamali.qa.notify.utils.showPopUpDelete
import mamali.qa.notify.utils.showPopUpUpdateAndDelete


class NotesFragment : Fragment(), NoteClickDeleteInterface {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }
    var popup: PopupWindow? = null
    private lateinit var binding: FragmentNotesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerAdapter = NoteListAdapter(this, this)
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
        //submit recycler view list
        noteViewModel.listLiveData.observe(requireActivity(), Observer { notes ->
            notes?.let { recyclerAdapter.submitList(it.reversed()) }
        })
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

    private fun btnFloatingAddClicked() {
        noteViewModel.addFloatingButtonOnClick(
            binding.btnFloatingAddFile,
            binding.btnFloatingAddNote
        )
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
        NewFileCustomDialog(parent.toString(), parentId).show(
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
                UpdateFileCustomDialog(parentId, binding.toolbarTitleTxt).show(
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
}