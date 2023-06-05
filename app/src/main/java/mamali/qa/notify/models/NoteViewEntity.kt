package mamali.qa.notify.models

import androidx.room.DatabaseView
import androidx.room.Entity

@DatabaseView(viewName = "note_view_table",
    value = "select * from note_table left join (SELECT parent_id, count(*) as children FROM note_table  group by parent_id) as subquery on note_table.id == subquery.parent_id")

@Entity(tableName = "note_view_table")
data class NoteViewEntity(
    val name: String="",
    val description: String?="",
    val kind: Kind=Kind.NOTHING,
    val parent: String="",
    val parent_id: Int=0,
    val date: Long?=null,
    val children: Int?=0,
    val id: Int=0
)
