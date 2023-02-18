package mamali.qa.notify.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NoteDao {
    @Query("SELECT * FROM note_table WHERE parent_id= :parentId")
    fun getNotes(parentId: Int?): LiveData<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id= :id")
    fun getSelectedNotes(id: Int): LiveData<List<NoteEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(note: NoteEntity)

    @Query("DELETE FROM note_table WHERE id= :id OR parent_id=:id")
    fun deleteNote(id: Int?)

    @Query("UPDATE note_table SET name= (CASE WHEN id=:id THEN :name ELSE name END), parent=(CASE WHEN parent_id=:id THEN :name ELSE parent END)")
    fun updateFile(id: Int?, name: String)
}