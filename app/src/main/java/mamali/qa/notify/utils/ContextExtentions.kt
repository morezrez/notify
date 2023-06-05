package mamali.qa.notify.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.cardview.widget.CardView
import mamali.qa.notify.R

fun Context.showPopUpDelete(onDeleteClick: () -> Unit): PopupWindow {
    val inflater =getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
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

fun Context.showPopUpUpdateAndDelete(): PopupWindow {
    val inflater =getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view = inflater.inflate(R.layout.update_delete_menu, null)

    return PopupWindow(
        view,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,

        )
}