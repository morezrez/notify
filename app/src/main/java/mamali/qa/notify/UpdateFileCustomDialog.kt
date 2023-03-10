package mamali.qa.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.databinding.DialogAddFileBinding

class UpdateFileCustomDialog(val id : Int?, private val txtToolbar : TextView) : DialogFragment() {

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        val binding = DialogAddFileBinding.inflate(inflater, container, false)

        binding.dialogTitle.text= getString(R.string.dialig_update_title)
        binding.dialogSubtitle.text=getString(R.string.dialog_update_subtitle)
        binding.btnCreate.text=getString(R.string.dialog_update_btn_txt)
        binding.btnCreate.setOnClickListener {
           val name =  binding.edtFileTitle.text
            noteViewModel.updateFile(id,name.toString())
            txtToolbar.text=name
            dialog?.dismiss()
        }

        binding.txtAddFileCancel.setOnClickListener {
            dialog?.dismiss()
        }
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.40).toInt()
        dialog!!.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

    }
}