package mamali.qa.notify.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import mamali.qa.notify.viewModel.NoteViewModel
import mamali.qa.notify.R
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.databinding.DialogAddFileBinding
import mamali.qa.notify.models.Kind

//dialog for update and add new file handel here
// TODO(SHAYAN): Problem, Code style issue, put all property in one line instead of random enter,
@AndroidEntryPoint
class NewFileCustomDialog(
    val parent: String?,
    val parentId: Int?,
    val insertFile: (note: NoteEntity) -> Unit
) : DialogFragment() {

    // TODO(SHAYAN): Problem, This cause memory leak, you must set it to null.
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

    fun addFileOnClick(binding: DialogAddFileBinding) {
        val name = binding.edtFileTitle.text.toString()
        val kind = Kind.File
        val parent = parent
        val parentId = parentId
        val file = NoteEntity(name, null, kind, parent, parentId)
        insertFile(file)
        dialog?.dismiss()
    }


}