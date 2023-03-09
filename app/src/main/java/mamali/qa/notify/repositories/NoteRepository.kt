package mamali.qa.notify.repositories

import mamali.qa.notify.database.NoteDao
import mamali.qa.notify.database.NoteEntity

class NoteRepository(private val noteDao: NoteDao) {

    fun getNotes(parentId: Int?) = noteDao.getNotes(parentId)
    fun getSelectedNote(id: Int) = noteDao.getSelectedNotes(id)
    fun deleteNote(id: Int?) = noteDao.deleteNote(id)
    suspend fun insert(noteEntity: NoteEntity) = noteDao.insert(noteEntity)
    fun updateNote(name: String, desc: String, id: Int, date: Long) =
        noteDao.updateNote(name, desc, id, date)

    suspend fun updateFile(id: Int?, name: String) = noteDao.updateFile(id, name)
}