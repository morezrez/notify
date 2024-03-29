package mamali.qa.notify.data.repositories

import androidx.lifecycle.LiveData
import mamali.qa.notify.data.datasource.NoteLocalDataSource
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteLocalDataSource: NoteLocalDataSource) {
    fun getNotes(parentId: Int?) : LiveData<List<NoteViewEntity>> = noteLocalDataSource.getNotes(parentId)
    suspend fun deleteNote(id: Int?) = noteLocalDataSource.deleteNote(id)
    suspend fun insert(noteEntity: NoteEntity) = noteLocalDataSource.insert(noteEntity)
    suspend fun updateNote(name: String, desc: String, id: Int, date: Long) =
        noteLocalDataSource.updateNote(name, desc, id, date)

    suspend fun updateFile(id: Int?, name: String) = noteLocalDataSource.updateFile(id, name)
}