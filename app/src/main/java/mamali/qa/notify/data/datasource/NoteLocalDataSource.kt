package mamali.qa.notify.data.datasource

import androidx.lifecycle.LiveData
import mamali.qa.notify.data.database.NoteDao
import mamali.qa.notify.models.NoteEntity
import mamali.qa.notify.models.NoteViewEntity

class NoteLocalDataSource(private val noteDao: NoteDao) : DataSource{

    override fun getNotes(parentId: Int?)  =
        noteDao.getNotes(parentId)

    override fun deleteNote(id: Int?) {
        noteDao.deleteNote(id)    }

    override suspend fun insert(noteEntity: NoteEntity) {
        noteDao.insert(noteEntity)    }

    override fun updateNote(name: String, desc: String, id: Int, date: Long) {
        noteDao.updateNote(name, desc, id, date)    }

    override suspend fun updateFile(id: Int?, name: String) {
        noteDao.updateFile(id, name)    }
}