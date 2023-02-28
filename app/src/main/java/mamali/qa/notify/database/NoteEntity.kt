package mamali.qa.notify.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note_table")
data class NoteEntity(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String?=null,
    @ColumnInfo(name = "kind") val kind: String,
    @ColumnInfo(name = "parent") val parent: String?=null,
    @ColumnInfo(name = "parent_id") val parentId: Int?=null,
    @ColumnInfo(name = "date") val date: Long?=null,
    @PrimaryKey(autoGenerate = true) val id: Int=0
)
