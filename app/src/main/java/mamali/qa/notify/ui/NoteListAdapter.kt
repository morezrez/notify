package mamali.qa.notify.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import mamali.qa.notify.R
import mamali.qa.notify.models.Kind
import mamali.qa.notify.utils.getRelativeTime
import mamali.qa.notify.utils.toPersianDigit
import mamali.qa.notify.models.RecyclerDataModel
import java.util.Date

class NoteListAdapter(private val adapterCommunicatorInterface: AdapterCommunicatorInterface) :
    ListAdapter<RecyclerDataModel, NoteListAdapter.ViewHolder>(NOTES_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = when (viewType) {
            TYPE_RECYCLER_VIEW_ITEM -> R.layout.recyclerview_item
            TYPE_RECYCLER_VIEW_ITEM_DEVIDER -> R.layout.recyclerview_item_devider
            else -> throw IllegalArgumentException("Invalid view type")
        }

        val view =
            LayoutInflater.from(parent.context)
                .inflate(layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is RecyclerDataModel.NoteItem -> TYPE_RECYCLER_VIEW_ITEM
            is RecyclerDataModel.Devider -> TYPE_RECYCLER_VIEW_ITEM_DEVIDER

        }

    }

    companion object {
        private val NOTES_COMPARATOR =
            object : DiffUtil.ItemCallback<RecyclerDataModel>() {
                override fun areItemsTheSame(
                    oldItem: RecyclerDataModel,
                    newItem: RecyclerDataModel
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: RecyclerDataModel,
                    newItem: RecyclerDataModel
                ): Boolean {
                    return oldItem == newItem
                }
            }

        private const val TYPE_RECYCLER_VIEW_ITEM = 0
        private const val TYPE_RECYCLER_VIEW_ITEM_DEVIDER = 1
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {


        fun bindNoteItem(item: RecyclerDataModel.NoteItem) {
            val txtFileTitle: TextView = itemView.findViewById(R.id.file_title_txt)
            val txtFileDetail: TextView = itemView.findViewById(R.id.file_detail_txt)
            val imgFileicon: ImageView = itemView.findViewById(R.id.file_icon_vector)
            val imgFileHighlight: ImageView = itemView.findViewById(R.id.file_icon_circle_highlight)
            val imgOptionBlubIcon: ImageView = itemView.findViewById(R.id.option_blub_icon)

            txtFileTitle.text = item.name.toPersianDigit()

            if (item.kind == Kind.Note) {

                imgFileHighlight.setImageResource(R.drawable.note_icon_circle_highlight)
                imgFileicon.setImageResource(R.drawable.note_icon_vector)
                val lastDate = Date(item.date ?: 0L).getRelativeTime()
                txtFileDetail.text = lastDate.toPersianDigit()

            } else {
                if (item.children != null) {
                    txtFileDetail.text =
                        "حاوی " + item.children.toString().toPersianDigit() + " فایل"
                } else {
                    txtFileDetail.text =
                        txtFileDetail.context.getString(R.string.file_empty_txt)
                }
            }

            txtFileTitle.setOnClickListener {
                if (item.kind == Kind.File) {
                    val parent = item.name
                    val parentId = item.id
                    adapterCommunicatorInterface.fragmentTransferFile(parent, parentId)
                } else {
                    val name = item.name
                    val desc = item.description
                    val id = item.id
                    val parent = item.parent
                    adapterCommunicatorInterface.fragmentTransferNote(id, name, desc, parent)
                }
            }

            imgOptionBlubIcon.setOnClickListener {
                adapterCommunicatorInterface.onDeleteIconClick(item.id, imgOptionBlubIcon)
            }
        }

        fun bindNoteItemDevider(item: RecyclerDataModel.Devider) {
            val devider: View = itemView.findViewById(R.id.devider)
            devider.setBackgroundColor(item.bgColor)
        }

        fun bind(dataModel: RecyclerDataModel) {
            when (dataModel) {
                is RecyclerDataModel.NoteItem -> bindNoteItem(dataModel)
                is RecyclerDataModel.Devider -> bindNoteItemDevider(dataModel)
            }
        }
    }
}

interface AdapterCommunicatorInterface {
    fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView)
    fun fragmentTransferFile(parent: String, parentId: Int)
    fun fragmentTransferNote(id: Int, name: String, desc: String?, parent: String)
}











//package mamali.qa.notify.ui
//
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ImageView
//import android.widget.TextView
//import androidx.recyclerview.widget.DiffUtil
//import androidx.recyclerview.widget.ListAdapter
//import androidx.recyclerview.widget.RecyclerView
//import androidx.recyclerview.widget.RecyclerView.ViewHolder
//import mamali.qa.notify.R
//import mamali.qa.notify.models.Kind
//import mamali.qa.notify.utils.getRelativeTime
//import mamali.qa.notify.utils.toPersianDigit
//import mamali.qa.notify.models.RecyclerDataModel
//import java.util.Date
//
//class NoteListAdapter(private val adapterCommunicatorInterface: AdapterCommunicatorInterface) :
//    ListAdapter<RecyclerDataModel, BaseViewHolder>(NOTES_COMPARATOR) {
//
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
//        return when (viewType) {
//            TYPE_RECYCLER_VIEW_ITEM -> ItemViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.recyclerview_item, parent, false)
//            )
//            TYPE_RECYCLER_VIEW_ITEM_DEVIDER -> DivideViewHolder(
//                LayoutInflater.from(parent.context)
//                    .inflate(R.layout.recyclerview_item_devider, parent, false)
//            )
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }
//
//    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
//        holder.bind(getItem(position))
//    }
//
//
//    override fun getItemViewType(position: Int): Int {
//        return when (getItem(position)) {
//            is RecyclerDataModel.NoteItem -> TYPE_RECYCLER_VIEW_ITEM
//            is RecyclerDataModel.Devider -> TYPE_RECYCLER_VIEW_ITEM_DEVIDER
//
//        }
//
//    }
//
//    companion object {
//        private val NOTES_COMPARATOR =
//            object : DiffUtil.ItemCallback<RecyclerDataModel>() {
//                override fun areItemsTheSame(
//                    oldItem: RecyclerDataModel,
//                    newItem: RecyclerDataModel
//                ): Boolean {
//                    return oldItem == newItem
//                }
//
//                override fun areContentsTheSame(
//                    oldItem: RecyclerDataModel,
//                    newItem: RecyclerDataModel
//                ): Boolean {
//                    return oldItem == newItem
//                }
//            }
//
//        private const val TYPE_RECYCLER_VIEW_ITEM = 0
//        private const val TYPE_RECYCLER_VIEW_ITEM_DEVIDER = 1
//    }
//
//    // TODO(SHAYAN): Problem, inner class can cause memory leak
//    // TODO(SHAYAN): Problem, What happened if you had 20 view type? like messenger app
//    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
//
//        private fun bindNoteItem(item: RecyclerDataModel.NoteItem) {
//             val txtFileTitle: TextView = itemView.findViewById(R.id.file_title_txt)
//             val txtFileDetail: TextView = itemView.findViewById(R.id.file_detail_txt)
//             val imgFileicon: ImageView = itemView.findViewById(R.id.file_icon_vector)
//             val imgFileHighlight: ImageView = itemView.findViewById(R.id.file_icon_circle_highlight)
//             val imgOptionBlubIcon: ImageView = itemView.findViewById(R.id.option_blub_icon)
//
//            txtFileTitle.text = item.name.toPersianDigit()
//
//            if (item.kind == Kind.Note) {
//
//                imgFileHighlight.setImageResource(R.drawable.note_icon_circle_highlight)
//                imgFileicon.setImageResource(R.drawable.note_icon_vector)
//                val lastDate = Date(item.date ?: 0L).getRelativeTime()
//                txtFileDetail.text = lastDate.toPersianDigit()
//
//            } else {
//                if (item.children != null) {
//                    txtFileDetail.text =
//                        // TODO(SHAYAN): Problem, hard code
//                        "حاوی " + item.children.toString().toPersianDigit() + " فایل"
//                } else {
//                    txtFileDetail.text =
//                        txtFileDetail.context.getString(R.string.file_empty_txt)
//                }
//            }
//
//            txtFileTitle.setOnClickListener {
//                // TODO(SHAYAN): Problem, This is not viewHolder logic
//                if (item.kind == Kind.File) {
//                    val parent = item.name
//                    val parentId = item.id
//                    adapterCommunicatorInterface.fragmentTransferFile(parent, parentId)
//                } else {
//                    val name = item.name
//                    val desc = item.description
//                    val id = item.id
//                    val parent = item.parent
//                    adapterCommunicatorInterface.fragmentTransferNote(id, name, desc, parent)
//                }
//            }
//
//            imgOptionBlubIcon.setOnClickListener {
//                adapterCommunicatorInterface.onDeleteIconClick(item.id, imgOptionBlubIcon)
//            }
//        }
//
//        fun bindNoteItemDevider(item: RecyclerDataModel.Devider) {
//
//        }
//
//        fun bind(dataModel: RecyclerDataModel) {
//            when (dataModel) {
//                is RecyclerDataModel.NoteItem -> bindNoteItem(dataModel)
//                is RecyclerDataModel.Devider -> bindNoteItemDevider(dataModel)
//            }
//        }
//    }
//}
//
//abstract class BaseViewHolder(view: View): ViewHolder(view) {
//    abstract fun bind(data: RecyclerDataModel)
//}
//
//class DivideViewHolder(view: View): BaseViewHolder(view) {
//    override fun bind(data: RecyclerDataModel) {
//        val devider: View = itemView.findViewById(R.id.devider)
//        devider.setBackgroundColor((data as RecyclerDataModel.Devider).bgColor)
//    }
//
//}
//
//class ItemViewHolder(view: View): BaseViewHolder(view) {
//    override fun bind(data: RecyclerDataModel) {
//
//    }
//}
//
//interface AdapterCommunicatorInterface {
//    fun onDeleteIconClick(id: Int, imgOptionBlub: ImageView)
//    fun fragmentTransferFile(parent: String, parentId: Int)
//    fun fragmentTransferNote(id: Int, name: String, desc: String?, parent: String)
//}