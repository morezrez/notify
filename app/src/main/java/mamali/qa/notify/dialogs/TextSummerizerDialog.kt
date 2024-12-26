package mamali.qa.notify.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import mamali.qa.notify.R
import mamali.qa.notify.databinding.DialogAddFileBinding
import mamali.qa.notify.databinding.DialogSummerizeNoteBinding

class TextSummerizerDialog(
    val updateFile: (name: String, desc: String, id: Int, date: Long) -> Unit,
    val sammerizeFile: (text: String, rate: Float) -> String,
    val originalText: String,
    val name: String,
    val myId: Int,
    val date: Long
) : DialogFragment() {

    private lateinit var binding: DialogSummerizeNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_frame);
        binding = DialogSummerizeNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnCreate.setOnClickListener {
            val selectedOption: Int = binding.radioGroup1!!.checkedRadioButtonId
            // Assigning id of the checked radio button
            val radioButton = view.findViewById<RadioButton>(selectedOption)
            val summerizedText = sammerizeFile(originalText, radioButton.text.toString().toFloat())
            updateFile(name, summerizedText, myId, date)
            dialog?.dismiss()
            activity?.onBackPressed()
            Toast.makeText(context,"خلاصه سازی با ضریب" + radioButton.text + "انجام شد، مجدد فایل را باز کنید.",Toast.LENGTH_LONG).show()
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
