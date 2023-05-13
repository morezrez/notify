package mamali.qa.notify.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mamali.qa.notify.data.database.NoteDao
import mamali.qa.notify.models.NoteEntity
import javax.inject.Inject

class NoteLocalDataSource @Inject constructor(private val noteDao: NoteDao) : DataSource {

    override fun getNotes(parentId: Int?) =
        noteDao.getNotes(parentId)

    override suspend fun deleteNote(id: Int?) {
        withContext(Dispatchers.IO) {
            noteDao.deleteNote(id)
        }
    }

    override suspend fun insert(noteEntity: NoteEntity) {
        withContext(Dispatchers.IO) {
            noteDao.insert(noteEntity)
        }
    }

    override suspend fun updateNote(name: String, desc: String, id: Int, date: Long) {
        withContext(Dispatchers.IO) {
            noteDao.updateNote(name, desc, id, date)
        }
    }

    override suspend fun updateFile(id: Int?, name: String) {
        withContext(Dispatchers.IO){
            noteDao.updateFile(id, name)
        }
    }
}