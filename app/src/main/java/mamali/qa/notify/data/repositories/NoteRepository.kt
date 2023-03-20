package mamali.qa.notify.data.repositories

import mamali.qa.notify.data.datasource.NoteLocalDataSource
import mamali.qa.notify.models.NoteEntity

class NoteRepository(private val noteLocalDataSource: NoteLocalDataSource) {

    fun getNotes(parentId: Int?) = noteLocalDataSource.getNotes(parentId)
    fun deleteNote(id: Int?) = noteLocalDataSource.deleteNote(id)
    suspend fun insert(noteEntity: NoteEntity) = noteLocalDataSource.insert(noteEntity)
    fun updateNote(name: String, desc: String, id: Int, date: Long) =
        noteLocalDataSource.updateNote(name, desc, id, date)

    suspend fun updateFile(id: Int?, name: String) = noteLocalDataSource.updateFile(id, name)
}