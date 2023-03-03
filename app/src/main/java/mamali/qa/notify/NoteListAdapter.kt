package mamali.qa.notify

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mamali.qa.notify.Utils.getRelativeTime
import mamali.qa.notify.Utils.toPersianDigit
import mamali.qa.notify.database.NoteDao
import java.util.Date


class NoteListAdapter(
    val fragment: NotesFragment,
    val noteClickDeleteInterface: NoteClickDeleteInterface
) :
    ListAdapter<NoteDao.NoteEntityWithCount, NoteListAdapter.ViewHolder>(NOTES_COMPARATOR) {


    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val txtFileTitle: TextView = ItemView.findViewById(R.id.file_title_txt)
        val txtFileDetail: TextView = ItemView.findViewById(R.id.file_detail_txt)
        val imgFileicon: ImageView = ItemView.findViewById(R.id.file_icon_vector)
        val imgFileHighlight: ImageView = ItemView.findViewById(R.id.file_icon_circle_highlight)
        val imgOptionBlubIcon: ImageView = ItemView.findViewById(R.id.option_blub_icon)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val current = getItem(position)

        holder.txtFileTitle.text = current.name.toPersianDigit()

        if (current.kind == "Note") {
            holder.apply {
                imgFileHighlight.setImageResource(R.drawable.note_icon_circle_highlight)
                imgFileicon.setImageResource(R.drawable.note_icon_vector)
                val lastDate = Date(current.date ?: 0L).getRelativeTime()
                txtFileDetail.text = lastDate.toPersianDigit()
            }
        } else {
            if (current.children != null) {
                holder.txtFileDetail.text =
                    "حاوی " + current.children.toString().toPersianDigit() + " فایل"
            } else {
                holder.txtFileDetail.text = "خالی"
            }
        }



        holder.txtFileTitle.setOnClickListener {
            if (current.kind == "file") {
                val parent = current.name
                val parentId = current.id
                val action = NotesFragmentDirections.actionNotesFragmentSelf(parent, parentId)
                fragment.findNavController().navigate(action)
            } else {

                val name = current.name
                val desc = current.description
                val id = current.id
                val parent = current.parent
                val action = NotesFragmentDirections.actionNotesFragmentToNoteDetailsFragment(
                    id,
                    name,
                    desc,
                    parent
                )
                fragment.findNavController().navigate(action)
            }
        }


        holder.imgOptionBlubIcon.setOnClickListener {
            noteClickDeleteInterface.onDeleteIconClick(current.id, holder.imgOptionBlubIcon)
        }
    }


    companion object {
        private val NOTES_COMPARATOR =
            object : DiffUtil.ItemCallback<NoteDao.NoteEntityWithCount>() {
                override fun areItemsTheSame(
                    oldItem: NoteDao.NoteEntityWithCount,
                    newItem: NoteDao.NoteEntityWithCount
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: NoteDao.NoteEntityWithCount,
                    newItem: NoteDao.NoteEntityWithCount
                ): Boolean {
                    return oldItem.name == newItem.name
                }
            }
    }


}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView)
}




