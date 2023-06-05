package mamali.qa.notify

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.utils.getFormatted
import mamali.qa.notify.utils.toPersianDigit
import mamali.qa.notify.databinding.FragmentNoteDetailsBinding
import mamali.qa.notify.models.Kind
import mamali.qa.notify.utils.showPopUpDelete
import mamali.qa.notify.viewModel.NoteDetailViewModel
import mamali.qa.notify.viewModel.NoteDetailViewModelFactory
import java.util.Date

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
    val dateUTC: Long = System.currentTimeMillis()
    val date: Date = Date(dateUTC)
    private val shamsi: String = date.getFormatted()
    var popup: PopupWindow? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get arguments
        val args = getArgs()
        name = args?.name.toString()
        desc = args?.desc.toString()

        //handel difference between new note and some old note that have desc and title
        if (name != null && desc != null && name != "null" && desc != "null") {
            binding.edtNoteTitle.setText(name)
            binding.edtNoteDetail.setText(desc)
        }

        //date
        binding.txtNoteDate.text = shamsi.toPersianDigit()

        binding.icBackToolbarNoteDetail.setOnClickListener {
            activity?.onBackPressed()
        }

        //delete and update popup menu
        binding.optionBlubIconNoteDetail.setOnClickListener {
            if (name != null && desc != null && args?.parentId != null && name != "null" && desc != "null") {
                popup = context?.showPopUpDelete {
                    noteDetailViewModel.deleteNote(args.parentId)
                    popup?.dismiss()
                    activity?.onBackPressed()
                }
                popup?.isOutsideTouchable = true
                popup?.isFocusable = true
                popup?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                popup?.showAsDropDown(binding.optionBlubIconNoteDetail)
            } else {
                Toast.makeText(
                    context,
                    getString(R.string.insert_empty_note_toast),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    //when user click on back button
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                title = binding.edtNoteTitle.text.toString()
                desc2 = binding.edtNoteDetail.text.toString()
                val kind = Kind.Note
                val args = getArgs()
                val parent = args?.parent
                val parentId = args?.parentId
                //for notes that already exist
                if (name != "null" && desc != "null") {
                    updateNote(parentId)
                }
                //for new note
                else if (title.isNotEmpty()) {
                    insertNote(parent!!, parentId!!, kind)
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

    //function for get arguments
    fun getArgs(): NoteDetailsFragmentArgs? {
        val bundle = arguments
        val args = bundle?.let { NoteDetailsFragmentArgs.fromBundle(it) }
        return args
    }

    fun updateNote(parentId: Int?) {
        if (name != title || desc != desc2) {
            if (title.isEmpty()) {
                Toast.makeText(context, getString(R.string.updateEmptyTitle), Toast.LENGTH_SHORT).show()
            } else {
                noteDetailViewModel.updateNote(title, desc2, parentId!!, dateUTC)
            }
        } else {
            Toast.makeText(context, getString(R.string.updateWithoutChange), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun insertNote(parent: String, parentId: Int, kind: Kind) {
        parent.let {
            parentId.let { it1 ->
                noteDetailViewModel.getInput(
                    title, desc2, kind, it,
                    it1, dateUTC
                )
            }
        }
    }
}