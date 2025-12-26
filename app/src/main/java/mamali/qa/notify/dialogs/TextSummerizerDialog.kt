package mamali.qa.notify.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import mamali.qa.notify.R
import mamali.qa.notify.databinding.DialogSummerizeNoteBinding

class TextSummerizerDialog(
    // Callback to save the FINAL note (called after summary is done)
    val updateFile: (name: String, desc: String, id: Int, date: Long) -> Unit,
    // Callback to trigger the AI summary (returns initial "Processing..." string)
    val sammerizeFile: (text: String, rate: Float) -> String,
    val originalText: String,
    val name: String,
    val myId: Int,
    val date: Long
) : DialogFragment() {

    // Use nullable binding to safely handle view lifecycle
    private var _binding: DialogSummerizeNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog!!.window?.setBackgroundDrawableResource(R.drawable.dialog_frame)
        _binding = DialogSummerizeNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreate.setOnClickListener {
            val selectedOption: Int = binding.radioGroup1.checkedRadioButtonId

            if (selectedOption == -1) {
                Toast.makeText(context, "ضریب خلاصه سازی را انتخاب کنید", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val radioButton = view.findViewById<RadioButton>(selectedOption)
            val rate = radioButton.text.toString().toFloatOrNull() ?: 0.5f

            // 1. Disable button so user doesn't click twice
            binding.btnCreate.isEnabled = false
            binding.btnCreate.text = "در حال پردازش"

            // 2. Trigger the AI summary in Fragment
            // This returns "Processing..." immediately, actual result comes later via updateSummaryText
            val initialMessage = sammerizeFile(originalText, rate)

            // Optional: Show a toast or update UI with initial message
            // Toast.makeText(context, initialMessage, Toast.LENGTH_SHORT).show()
        }

        binding.txtAddFileCancel.setOnClickListener {
            dismiss()
        }
    }

    /**
     * This function is called by the Fragment when the AI finishes calculating.
     */
    fun updateSummaryText(summary: String) {
        // Check if binding is still valid (dialog is open)
        if (_binding != null) {
            // 1. Save the new summary to the database
            updateFile(name, summary, myId, date)

            // 2. Show success message
            Toast.makeText(context, "خلاصه سازی شما با موفقیت انجام شد!", Toast.LENGTH_LONG).show()

            // 3. Close the dialog and go back
            dismiss()
            activity?.onBackPressed()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}