package mamali.qa.notify

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import mamali.qa.notify.databinding.FragmentNotesBinding


class NotesFragment : Fragment(), NoteClickDeleteInterface {


    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }
    var popup: PopupWindow? = null

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
        //submit recycler view list
        noteViewModel.listLiveData.observe(requireActivity(), Observer { notes ->
            notes?.let { recyclerAdapter.submitList(it.reversed()) }
        })
        //making difference in toolbar for root and inside folders
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
                        it1, null, null, parent
                    )
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

            val popup2: PopupWindow = showPopUpUpdateAndDelete(this.requireContext())
            popup2.contentView.apply {

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

            popup2.isOutsideTouchable = true
            popup2.isFocusable = true
            popup2.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popup2.showAsDropDown(binding.toolbarOptionBlubIcon)
        }

        return binding.root
    }

    override fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView) {
        popup?.dismiss()
        popup = showPopUpDelete(this.requireContext()) {
            noteViewModel.deleteNote(id)
            popup?.dismiss()
        }
        popup?.isOutsideTouchable = true
        popup?.isFocusable
        popup?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        popup?.showAsDropDown(imgOptionBlub)
    }


}