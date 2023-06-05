package mamali.qa.notify.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.NoteViewModel
import mamali.qa.notify.NoteViewModelFactory
import mamali.qa.notify.NotesApplication
import mamali.qa.notify.R
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.databinding.DialogAddFileBinding
import mamali.qa.notify.models.Kind

//dialog for update and add new file handel here

class NewFileCustomDialog(val parent: String, val parentId: Int?) : DialogFragment() {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }

    private lateinit var binding: DialogAddFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        binding = DialogAddFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            addFileOnClick(binding)
        }
        binding.txtAddFileCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    private fun addFileOnClick(binding: DialogAddFileBinding) {
        val name = binding.edtFileTitle.text.toString()
        val kind = Kind.File
        val parent = parent
        val parentId = parentId
        val file = NoteEntity(name, null, kind, parent, parentId)
        noteViewModel.insert(file)
        dialog?.dismiss()
    }
}