package mamali.qa.notify.data.datasource

import androidx.lifecycle.LiveData
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity

interface DataSource {
    fun getNotes(parentId: Int?) : LiveData<List<NoteViewEntity>>
    suspend fun deleteNote(id: Int?)
    suspend fun insert(noteEntity: NoteEntity)
    suspend fun updateNote(name: String, desc: String, id: Int, date: Long)
    suspend fun updateFile(id: Int?, name: String)
}