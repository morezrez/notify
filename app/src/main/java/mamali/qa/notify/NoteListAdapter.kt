package mamali.qa.notify

import android.content.ClipData
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mamali.qa.notify.database.NoteEntity


class NoteListAdapter(
    val fragment: NotesFragment,
    val noteClickDeleteInterface: NoteClickDeleteInterface
) :
    ListAdapter<NoteEntity, NoteListAdapter.ViewHolder>(NOTES_COMPARATOR) {


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
        holder.txtFileTitle.text = current.name
        if (current.kind == "Note") {
            holder.apply {
                imgFileHighlight.setImageResource(R.drawable.note_icon_circle_highlight)
                imgFileicon.setImageResource(R.drawable.note_icon_vector)
            }
        }

        holder.txtFileTitle.setOnClickListener {
            if (current.kind == "file") {
                val parent = current.name
                val parentId = current.id
                val action = NotesFragmentDirections.actionNotesFragmentSelf(parent,parentId)
                fragment.findNavController().navigate(action)
            } else {
                Toast.makeText(fragment.context, "it is a note!", Toast.LENGTH_LONG).show()
            }
        }


        holder.imgOptionBlubIcon.setOnClickListener {
            val popup = PopupMenu(fragment.context, holder.imgOptionBlubIcon)
            popup.inflate(R.menu.delete_memu)
            popup.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
                when (item!!.itemId) {
                    R.id.menu1 -> {
                        noteClickDeleteInterface.onDeleteIconClick(current.id)

                    }
                }
                true
            })
            popup.show()
        }

    }


    companion object {
        private val NOTES_COMPARATOR = object : DiffUtil.ItemCallback<NoteEntity>() {
            override fun areItemsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: NoteEntity, newItem: NoteEntity): Boolean {
                return oldItem.name == newItem.name
            }
        }
    }


}

interface NoteClickDeleteInterface {
    fun onDeleteIconClick(id: Int)
}
