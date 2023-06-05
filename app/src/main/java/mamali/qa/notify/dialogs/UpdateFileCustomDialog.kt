package mamali.qa.notify.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.NoteViewModel
import mamali.qa.notify.NotesApplication
import mamali.qa.notify.R
import mamali.qa.notify.databinding.DialogAddFileBinding
import mamali.qa.notify.models.NoteEntity

class UpdateFileCustomDialog(
    val id: Int?,
    private val txtToolbar: TextView,
    val updateFile: (id: Int?, name: String) -> Unit
) : DialogFragment() {

    private val noteViewModel: NoteViewModel by viewModels()

    private lateinit var binding: DialogAddFileBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        binding = DialogAddFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.dialogTitle.text = getString(R.string.dialig_update_title)
        binding.dialogSubtitle.text = getString(R.string.dialog_update_subtitle)
        binding.btnCreate.text = getString(R.string.dialog_update_btn_txt)
        binding.btnCreate.setOnClickListener {
            val name = binding.edtFileTitle.text
            updateFile(id, name.toString())
            txtToolbar.text = name
            dialog?.dismiss()
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
}