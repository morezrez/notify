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
import mamali.qa.notify.models.RecyclerDataModel
import mamali.qa.notify.utils.showPopUpDelete
import mamali.qa.notify.utils.showPopUpUpdateAndDelete
import mamali.qa.notify.viewModel.NoteViewModel

@AndroidEntryPoint
class NotesFragment : Fragment(), AdapterCommunicatorInterface {

    private val noteViewModel: NoteViewModel by viewModels()
    var popup: PopupWindow? = null
    private lateinit var binding: FragmentNotesBinding
    private lateinit var recyclerAdapter: NoteListAdapter

    val parentId by lazy{
        val args = arguments?.let { NotesFragmentArgs.fromBundle(it) }
        args?.parentId!!
    }

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
        // TODO(SHAYAN): Problem, Read about SavedStateHandle
        val bundle = arguments
        val args = bundle?.let { NotesFragmentArgs.fromBundle(it) }
        val parent = args?.parent.toString()
        val parentId = args?.parentId!!

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
        // TODO(SHAYAN): codeStyle, use apply
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
        NewFileCustomDialog(
            parent.toString(),
            parentId
        ) { note -> noteViewModel.insert(note) }.show(
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
                UpdateFileCustomDialog(
                    parentId,
                    binding.toolbarTitleTxt
                ) { id: Int?, name: String -> noteViewModel.updateFile(id, name) }.show(
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
        // TODO(SHAYAN): Problem, You should not pass view to viewModel, it's redundant method
        noteViewModel.updateToolbar(
            binding.toolbarTitleTxt,
            binding.icBackToolbar,
            binding.toolbarOptionBlubIcon,
            parent
        )
    }

    private fun registerObserver() {
        //submit recycler view list
        noteViewModel.getNotes(parentId).observe(viewLifecycleOwner, Observer { notes ->
            // TODO(SHAYAN): Problem, this is viewModel logic
            val noteItemsList: List<RecyclerDataModel> = notes.map {
                with(it) {
                    RecyclerDataModel.NoteItem(
                        name,
                        description,
                        kind,
                        parent,
                        parent_id,
                        date,
                        children,
                        id
                    )
                }
            }
            val recyclerList: MutableList<RecyclerDataModel> = mutableListOf()
            for (item in noteItemsList) {
                recyclerList.add(item)
                recyclerList.add(RecyclerDataModel.Devider(resources.getColor(R.color.gray_300)))
            }

            recyclerList?.let { recyclerAdapter.submitList(it.reversed()) }
            if (recyclerList.isEmpty()){binding.txtEmptyState.isVisible=true}
        })

        //observe visibility of floating buttons
        noteViewModel.floatingButtonVisibilityLiveData.observe(viewLifecycleOwner) { isVisible ->
            binding.btnFloatingAddNote.isVisible = isVisible
            binding.btnFloatingAddFile.isVisible = isVisible
        }
    }


    private fun getData(): List<RecyclerDataModel> = listOf()
}