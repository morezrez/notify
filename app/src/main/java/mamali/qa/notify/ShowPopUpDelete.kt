package mamali.qa.notify

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.cardview.widget.CardView

fun showPopUpDelete(context: Context, onDeleteClick: () -> Unit): PopupWindow {
    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.delete_menu, null)
    view.findViewById<CardView>(R.id.crdDelete).setOnClickListener {
        onDeleteClick()
    }

    return PopupWindow(
        view,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
