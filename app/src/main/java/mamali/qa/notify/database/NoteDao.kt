package mamali.qa.notify.database

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface NoteDao {
    @DatabaseView("select * from (select * from note_table left join (SELECT parent_id, count(*) as children FROM note_table  group by parent_id) as subquery on note_table.id == subquery.parent_id) as note_table")
    data class NoteEntityWithCount(
        val name: String,
        val description: String?,
        val kind: String,
        val parent: String,
        val parent_id: Int,
        val children: Int?,
        val id: Int
    )

    @Query("SELECT * FROM noteentitywithcount WHERE parent_id=:parentId")
    fun getNotes(parentId: Int?): LiveData<List<NoteEntityWithCount>>

    @Query("SELECT * FROM note_table WHERE id= :id")
    fun getSelectedNotes(id: Int): LiveData<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id= :id OR parent_id=:id")
    fun deleteNote(id: Int?)

    @Query("UPDATE note_table SET name= (CASE WHEN id=:id THEN :name ELSE name END), parent=(CASE WHEN parent_id=:id THEN :name ELSE parent END)")
    fun updateFile(id: Int?, name: String)

    @Query("UPDATE note_table SET name=:name,description=:desc WHERE id=:id")
    fun updateNote(name: String, desc: String, id: Int)
}