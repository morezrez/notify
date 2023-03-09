package mamali.qa.notify

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import mamali.qa.notify.databinding.DialogAddFileBinding

class UpdateFileCustomDialog(id : Int?,txtToolbar : TextView) : DialogFragment() {

   val id=id
    val txtToolbar=txtToolbar

    private val noteViewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NotesApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getDialog()!!.getWindow()?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        val binding = DialogAddFileBinding.inflate(inflater, container, false)

        binding.dialogTitle.text="تغییر عنوان"
        binding.dialogSubtitle.text="عنوان جدید را وارد کنید"
        binding.btnCreate.text="تغییر عنوان"
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