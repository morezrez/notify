package mamali.qa.notify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mamali.qa.notify.models.Kind
import mamali.qa.notify.utils.getRelativeTime
import mamali.qa.notify.utils.toPersianDigit
import mamali.qa.notify.models.NoteViewEntity
import java.util.Date

class NoteListAdapter(private val adapterCommunicatorInterface: AdapterCommunicatorInterface) :
    ListAdapter<NoteViewEntity, NoteListAdapter.ViewHolder>(NOTES_COMPARATOR) {

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
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

        if (current.kind == Kind.Note) {
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
                holder.txtFileDetail.text =
                    holder.txtFileDetail.context.getString(R.string.file_empty_txt)
            }
        }

        holder.txtFileTitle.setOnClickListener {
            if (current.kind == Kind.File) {
                val parent = current.name
                val parentId = current.id
                adapterCommunicatorInterface.fragmentTransferFile(parent, parentId)
            } else {
                val name = current.name
                val desc = current.description
                val id = current.id
                val parent = current.parent
                adapterCommunicatorInterface.fragmentTransferNote(id,name,desc,parent)
            }
        }

        holder.imgOptionBlubIcon.setOnClickListener {
            adapterCommunicatorInterface.onDeleteIconClick(current.id, holder.imgOptionBlubIcon)
        }
    }

    companion object {
        private val NOTES_COMPARATOR =
            object : DiffUtil.ItemCallback<NoteViewEntity>() {
                override fun areItemsTheSame(
                    oldItem: NoteViewEntity,
                    newItem: NoteViewEntity
                ): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(
                    oldItem: NoteViewEntity,
                    newItem: NoteViewEntity
                ): Boolean {
                    return oldItem.name == newItem.name
                }
            }
    }

}

interface AdapterCommunicatorInterface {
    fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView)
    fun fragmentTransferFile(parent: String, parentId: Int)
    fun fragmentTransferNote(id: Int, name: String, desc: String?, parent: String)
}