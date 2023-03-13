package mamali.qa.notify.database

import androidx.lifecycle.LiveData
import androidx.room.*
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_view_table WHERE parent_id=:parentId")
    fun getNotes(parentId: Int?): LiveData<List<NoteViewEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id= :id OR parent_id=:id")
    fun deleteNote(id: Int?)

    @Query("UPDATE note_table SET name= (CASE WHEN id=:id THEN :name ELSE name END), parent=(CASE WHEN parent_id=:id THEN :name ELSE parent END)")
    fun updateFile(id: Int?, name: String)

    @Query("UPDATE note_table SET name=:name,description=:desc,date=:date WHERE id=:id")
    fun updateNote(name: String, desc: String, id: Int, date:Long)
}