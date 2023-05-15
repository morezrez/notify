package mamali.qa.notify.models

import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

sealed class RecyclerDataModel {
    data class Devider(
        @ColorInt val bgColor: Int,
    ) : RecyclerDataModel()

    data class NoteItem(

        val name: String="",
        val description: String?="",
        val kind: Kind=Kind.NOTHING,
        val parent: String="",
        val parent_id: Int=0,
        val date: Long?=null,
        val children: Int?=0,
        val id: Int=0

    ) : RecyclerDataModel()
}
